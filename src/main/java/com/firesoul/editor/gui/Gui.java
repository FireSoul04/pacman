package com.firesoul.editor.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.impl.model.entities.Pill;
import com.firesoul.pacman.impl.model.entities.Player;
import com.firesoul.pacman.impl.model.entities.PowerPill;
import com.firesoul.pacman.impl.model.entities.Wall;
import com.firesoul.pacman.impl.model.entities.ghosts.Blinky;
import com.firesoul.pacman.impl.model.entities.ghosts.Clyde;
import com.firesoul.pacman.impl.model.entities.ghosts.Inky;
import com.firesoul.pacman.impl.model.entities.ghosts.Pinky;
import com.firesoul.pacman.impl.util.Vector2D;

public class Gui extends JFrame implements MouseListener {

    private static final int SIZE = 16;
    private static final int GRID_SIZE = 8;
    private static final int LEFT_MARGIN = 48;
    private static final int WIDTH = 224;
    private static final int HEIGHT = 248;
    private static final int SCALE = 3;
    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    private static final List<GameObjects> LIMITED_GAME_OBJECTS = List.of(
        GameObjects.PLAYER,
        GameObjects.BLINKY,
        GameObjects.PINKY,
        GameObjects.INKY,
        GameObjects.CLYDE
    );

    private enum GameObjects {
        PLAYER(Color.YELLOW),
        BLINKY(Color.RED),
        PINKY(Color.PINK),
        INKY(Color.CYAN),
        CLYDE(Color.ORANGE),
        PILL(Color.GRAY),
        POWERPILL(Color.WHITE),
        WALL(Color.BLUE);

        private final Color color;

        private GameObjects(final Color color) {
            this.color = color;
        }

        public Color getColor() {
            return this.color;
        }

        public static List<String> getAll() {
            return List.of(
                "Player",
                "Blinky",
                "Pinky",
                "Inky",
                "Clyde",
                "Pill",
                "Powerpill",
                "Wall"
            );
        }
    };

    private final ConcurrentLinkedQueue<Entry<GameObject, Rectangle>> gameObjects = new ConcurrentLinkedQueue<>();
    private final FileParser parser = new FileParser(WIDTH, HEIGHT, MAP_PATH);
    private final JPanel buttonFrame;
    private final JPanel editorFrame;
    private final JLabel labelSelected;
    private final Canvas canvas;
    private GameObjects selected = GameObjects.WALL;
    private Point startPos;

    public Gui() {
        super("Level editor");
        this.buttonFrame = new JPanel(new BorderLayout());
        this.editorFrame = new JPanel(new GridLayout(5, 5, 20, 20));
        this.canvas = new Canvas();
        this.canvas.addMouseListener(this);
        this.setSize((int)(WIDTH * SCALE * 1.8), (HEIGHT + 30) * SCALE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(this.canvas);
        this.labelSelected = new JLabel(this.selected.name().toLowerCase());

        this.loadFromFile();
        this.addHandlerButtons();
        this.addEditorButtons();
        this.add(this.buttonFrame, BorderLayout.SOUTH);
        this.add(this.editorFrame, BorderLayout.EAST);
        this.setVisible(true);
    }

    private synchronized void addHandlerButtons() {
        final JButton reset = new JButton("Reset");
        final JButton goBack = new JButton("Go back to last modify");
        final JButton save = new JButton("Save");
        reset.addActionListener(e -> reset());
        goBack.addActionListener(e -> goBack());
        save.addActionListener(e -> this.saveToFile());

        this.buttonFrame.add(reset, BorderLayout.WEST);
        this.buttonFrame.add(goBack, BorderLayout.CENTER);
        this.buttonFrame.add(save, BorderLayout.EAST);
    }

    private synchronized void addEditorButtons() {
        GameObjects.getAll().forEach(t -> this.editorFrame.add(new JButton(t)));
        List.of(this.editorFrame.getComponents())
            .stream()
            .filter(t -> t instanceof JButton)
            .map(t -> (JButton) t)
            .forEach(t -> t.addActionListener(e -> this.selected = GameObjects.valueOf(t.getText().toUpperCase())));
        this.editorFrame.add(this.labelSelected);
    }

    private synchronized void saveToFile() {
        this.parser.save(this.gameObjects.stream()
            .map(Entry::getKey)
            .collect(Collectors.toMap(
                t -> Map.entry(t.getPosition(), t.getDrawable().getImageSize()),
                GameObject::getClass)
            )
        );
    }

    private synchronized void loadFromFile() {
        try {
            this.gameObjects.addAll(this.parser.load()
                .entrySet()
                .stream()
                .map(t -> {
                    try {
                        final GameObject g = t.getValue()
                            .getConstructor(Vector2D.class)
                            .newInstance(t.getKey().getKey());
                        return Map.entry(
                            g,
                            new Rectangle((int) g.getPosition().getX(), (int) g.getPosition().getY(), (int) t.getKey().getValue().getX(), (int) t.getKey().getValue().getY())
                        );
                    } catch (Exception e) {
                        throw new IllegalStateException();
                    }
                }).toList()
            );
        } catch (IllegalStateException e) {
            System.out.println("Cannot load from file");
        }
    }

    public void run() {
        final Graphics g = this.canvas.getGraphics();
        try {
            while (true) {
                g.setColor(this.selected.getColor());
                g.drawImage(this.readImage(), LEFT_MARGIN, 0, WIDTH * SCALE, HEIGHT * SCALE, this.canvas);
                for (final var entry : this.gameObjects) {
                    if (entry.getKey() instanceof Wall) {
                        final Rectangle t = entry.getValue();
                        g.fillRect(t.x * SCALE, t.y * SCALE, t.width * SCALE, t.height * SCALE);
                    } else {
                        final GameObject gameObject = entry.getKey();
                        final Image image = gameObject.getDrawable().getImage();
                        final Rectangle t = entry.getValue();
                        if (image != null) {
                            g.drawImage(image, t.x * SCALE, t.y * SCALE, t.width * SCALE, t.height * SCALE, null);
                        }
                    }
                }
                this.labelSelected.setText(this.selected.name().toLowerCase());
            }
        } catch (IOException e) {}
    }

    private synchronized Rectangle getRectangle(final Point p1, final Point p2) {
        final int w = (int) Math.abs(p2.getX() - p1.getX());
        final int h = (int) Math.abs(p2.getY() - p1.getY());
        return new Rectangle(approximate((int)p1.x / SCALE), approximate((int)p1.y / SCALE), approximate(w / SCALE), approximate(h / SCALE));
    }

    private synchronized int approximate(final int x) {
        return x - (x % 4);
    }

    private synchronized int approximateGrid(final int x) {
        return 4 + x - (x % GRID_SIZE);
    }

    private synchronized void reset() {
        final Graphics g = this.canvas.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH * SCALE + LEFT_MARGIN, HEIGHT * SCALE);
        this.canvas.update(g);
        this.gameObjects.clear();
    }

    private synchronized void goBack() {
        if (this.gameObjects.size() > 0) {
            this.gameObjects.remove(this.gameObjects.toArray()[this.gameObjects.size() - 1]);
        }
        final Graphics g = this.canvas.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH * SCALE + LEFT_MARGIN, HEIGHT * SCALE);
        this.canvas.update(g);
    }

    public synchronized Image readImage() throws IOException {
        return ImageIO.read(new File("src/main/resources/sprites/map/map_hitboxes.png"));
    }

    @Override
    public synchronized void mouseClicked(final MouseEvent e) {
    }

    @Override
    public synchronized void mousePressed(final MouseEvent e) {
        if (this.selected.equals(GameObjects.WALL)) {
            this.startPos = e.getPoint();
        } else {
            final Point p = new Point(approximateGrid(e.getX() / SCALE), approximateGrid(e.getY() / SCALE));
            if (this.gameObjects.stream().map(t -> t.getValue()).filter(t -> t.x == p.x && t.y == p.y).count() < 1) {
                final Rectangle rect = new Rectangle(p.x, p.y, SIZE, SIZE);
                this.addGameObject(e.getPoint(), rect);
            }
        }
    }

    @Override
    public synchronized void mouseReleased(final MouseEvent e) {
        if (selected.equals(GameObjects.WALL)) {
            final Rectangle rect = this.getRectangle(this.startPos, e.getPoint());
            this.addGameObject(e.getPoint(), rect);
        }
    }

    private synchronized void addGameObject(final Point p, final Rectangle rect) {
        final Vector2D position = new Vector2D((int) (p.getX() / SCALE), (int) (p.getY() / SCALE));
        final GameObject gameObject = this.getGameObject(position);
        if (rect.width > 0 && rect.height > 0) {
            if (!LIMITED_GAME_OBJECTS.contains(this.selected) ||
                this.howManyInstanciesOfSelected() < 1
            ) {
                this.gameObjects.add(Map.entry(gameObject, rect));
            }
        }
    }

    private GameObject getGameObject(final Vector2D position) {
        return switch (this.selected) {
            case GameObjects.PLAYER -> new Player(position);
            case GameObjects.BLINKY -> new Blinky(position);
            case GameObjects.PINKY -> new Pinky(position);
            case GameObjects.INKY -> new Inky(position);
            case GameObjects.CLYDE -> new Clyde(position);
            case GameObjects.PILL -> new Pill(position);
            case GameObjects.POWERPILL -> new PowerPill(position);
            default -> throw new IllegalStateException("Invalid game object: " + this.selected);
        };
    }

    private long howManyInstanciesOfSelected() {
        return this.gameObjects.stream()
            .filter(t -> t.getKey().getClass().getSimpleName().toUpperCase().equals(this.selected.name()))
            .count();
    }

    @Override
    public synchronized void mouseEntered(final MouseEvent e) {
    }

    @Override
    public synchronized void mouseExited(final MouseEvent e) {
    }
}
