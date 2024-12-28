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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.impl.model.GraphImpl;
import com.firesoul.pacman.impl.model.MapNode;
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
        PLAYER,
        BLINKY,
        PINKY,
        INKY,
        CLYDE,
        PILL,
        POWERPILL,
        WALL,
        MAPNODE,
        LINKNODES,
        ERASER;

        public static List<String> getAll() {
            return List.of(
                "Player",
                "Blinky",
                "Pinky",
                "Inky",
                "Clyde",
                "Pill",
                "Powerpill",
                "Wall",
                "MapNode",
                "LinkNodes",
                "Eraser"
            );
        }
    };

    private final ConcurrentLinkedQueue<Pair<GameObject, Rectangle>> gameObjects = new ConcurrentLinkedQueue<>();
    private final FileParser parser = new FileParser(WIDTH, HEIGHT, MAP_PATH);
    private final List<MapNode> selectedNodes = new LinkedList<>();
    private final Graph<MapNode> mapNodes = new GraphImpl<>();
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

        final JButton link = new JButton("Link");
        link.addActionListener(t -> this.linkNodes());
        this.editorFrame.add(link);
        this.editorFrame.add(this.labelSelected);
    }

    private synchronized void saveToFile() {
        if (this.areAllLimitedObjectsPresent()) {
            this.parser.save(this.gameObjects.stream()
                .collect(Collectors.toMap(
                    t -> new Pair<>(t.x().getPosition(), new Vector2D(t.y().getWidth(), t.y().getHeight())),
                    t -> t.x().getClass().getName())
                ), this.mapNodes.edges()
                    .entrySet()
                    .stream()
                    .map(t -> new Pair<>(t.getKey().getPosition(), t.getValue().stream().map(a -> a.getPosition()).toList()))
                    .toList()
            );
        } else {
            System.out.println("Cannot save, some mandatory game objects are missing");
        }
    }

    private synchronized boolean areAllLimitedObjectsPresent() {
        return LIMITED_GAME_OBJECTS.stream().filter(a -> 
                this.gameObjects.stream().anyMatch(t ->
                    a.name().equals(t.x().getClass().getSimpleName().toUpperCase())
                )
            ).toList()
            .equals(LIMITED_GAME_OBJECTS);
    }

    private synchronized void loadFromFile() {
        try {
            this.gameObjects.addAll(this.parser.load()
                .entrySet()
                .stream()
                .map(t -> {
                    try {
                        GameObject g = null;
                        if (t.getValue().equals(Wall.class.getName())) {
                            g = (GameObject) Class.forName(t.getValue())
                                .getConstructor(Vector2D.class, Vector2D.class)
                                .newInstance(t.getKey().x(), t.getKey().y());
                        } else {
                            g = (GameObject) Class.forName(t.getValue())
                                .getConstructor(Vector2D.class)
                                .newInstance(t.getKey().x());
                        }
                        return new Pair<>(
                            g,
                            new Rectangle((int) g.getPosition().getX(), (int) g.getPosition().getY(), (int) t.getKey().y().getX(), (int) t.getKey().y().getY())
                        );
                    } catch (Exception e) {
                        throw new IllegalStateException();
                    }
                }).toList()
            );
            for (final var node : this.parser.getMapNodes()) {
                final var x = new MapNode(node.x());
                this.mapNodes.addNode(x);
                for (final var elem : node.y()) {
                    final var y = new MapNode(elem);
                    this.mapNodes.addNode(y);
                    this.mapNodes.addEdge(x, y, this.distance(node.x(), elem));
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Cannot load from file");
        }
    }

    public void run() {
        final Graphics g = this.canvas.getGraphics();
        try {
            while (true) {
                g.drawImage(this.readImage(), LEFT_MARGIN, 0, WIDTH * SCALE, HEIGHT * SCALE, this.canvas);
                for (final var entry : this.gameObjects) {
                    if (entry.x() instanceof Wall) {
                        g.setColor(Color.BLUE);
                        final Rectangle t = entry.y();
                        g.fillRect(t.x * SCALE, t.y * SCALE, t.width * SCALE, t.height * SCALE);
                    } else {
                        final GameObject gameObject = entry.x();
                        final Image image = gameObject.getDrawable().getImage();
                        final Rectangle t = entry.y();
                        if (image != null) {
                            g.drawImage(image, t.x * SCALE, t.y * SCALE, t.width * SCALE, t.height * SCALE, this.canvas);
                        }
                    }
                }
                g.setColor(Color.RED);
                for (final var node : this.mapNodes.edges().entrySet()) {
                    final int x1 = (int) node.getKey().getPosition().dot(SCALE).getX() + (SIZE / 2) * SCALE;
                    final int y1 = (int) node.getKey().getPosition().dot(SCALE).getY() + (SIZE / 2) * SCALE;
                    final Rectangle t = new Rectangle((int) node.getKey().getPosition().dot(SCALE).getX(), (int) node.getKey().getPosition().dot(SCALE).getY(), SIZE, SIZE);
                    g.drawRect(t.x, t.y, t.width * SCALE, t.height * SCALE);
                    for (final var edge : node.getValue()) {
                        final int x2 = (int) edge.getPosition().dot(SCALE).getX() + (SIZE / 2) * SCALE;
                        final int y2 = (int) edge.getPosition().dot(SCALE).getY() + (SIZE / 2) * SCALE;
                        g.drawLine(x1, y1, x2, y2);
                    }
                }
                this.labelSelected.setText(this.selected.name().toLowerCase());
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }
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
        return x - (x % (LIMITED_GAME_OBJECTS.stream().anyMatch(t -> t.equals(this.selected)) ? GRID_SIZE / 2 : GRID_SIZE)) - 4;
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
        if (this.selected.equals(GameObjects.ERASER)) {
            final Point p = new Point(e.getX() / SCALE, e.getY() / SCALE);
            this.removeGameObject(p);
            this.removeWall(p);
            this.removeNode(p);
        } else if (this.selected.equals(GameObjects.WALL)) {
            this.startPos = e.getPoint();
        } else if (this.selected.equals(GameObjects.LINKNODES)) {
            final Vector2D p = new Vector2D(approximateGrid(e.getX() / SCALE), approximateGrid(e.getY() / SCALE));
            if (this.mapNodes.nodes().stream()
                .map(GameObject::getPosition)
                .anyMatch(t -> t.equals(p))
                && this.selectedNodes.size() < 2
            ) {
                final MapNode node = this.mapNodes.nodes().stream()
                    .filter(t -> t.getPosition().equals(p))
                    .findFirst()
                    .get();
                this.selectedNodes.add(node);
            }
        } else if (this.selected.equals(GameObjects.MAPNODE)) {
            final Vector2D p = new Vector2D(approximateGrid(e.getX() / SCALE), approximateGrid(e.getY() / SCALE));
            if (this.mapNodes.nodes().stream().noneMatch(t -> t.getPosition().equals(p))) {
                this.mapNodes.addNode(new MapNode(p));
            }
        } else {
            final Point p = new Point(approximateGrid(e.getX() / SCALE), approximateGrid(e.getY() / SCALE));
            if (this.selected.equals(GameObjects.MAPNODE) || this.gameObjects.stream().map(t -> t.y()).filter(t -> t.x == p.x && t.y == p.y).count() < 1) {
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

    private synchronized void removeGameObject(final Point p) {
        final Vector2D position = new Vector2D(approximateGrid((int) p.getX()), approximateGrid((int) p.getY()));
        final var gameObject = this.gameObjects.stream().filter(t -> t.x().getPosition().equals(position)).findFirst();
        if (gameObject.isPresent()) {
            gameObjects.remove(gameObject.get());
        }
    }

    private synchronized void removeWall(final Point p) {
        final var wall = this.gameObjects.stream().filter(
            t -> p.getX() >= t.x().getPosition().getX()
                && p.getX() <= t.x().getPosition().getX() + t.y().getWidth()
                && p.getY() >= t.x().getPosition().getY()
                && p.getY() <= t.x().getPosition().getY() + t.y().getHeight()
        ).findFirst();
        if (wall.isPresent()) {
            gameObjects.remove(wall.get());
        }
    }

    private synchronized void addGameObject(final Point p, final Rectangle rect) {
        final Vector2D position = new Vector2D(approximateGrid((int) p.getX() / SCALE), approximateGrid((int) p.getY() / SCALE));
        final GameObject gameObject = this.getGameObject(position, new Vector2D(rect.getWidth(), rect.getHeight()));
        if (rect.width > 0 && rect.height > 0) {
            if (!LIMITED_GAME_OBJECTS.contains(this.selected) ||
                this.howManyInstanciesOfSelected() < 1
            ) {
                this.gameObjects.add(new Pair<>(gameObject, rect));
            } else if (gameObject instanceof MapNode node) {
                this.mapNodes.addNode(node);
            }
        }
    }

    private GameObject getGameObject(final Vector2D position, final Vector2D size) {
        return switch (this.selected) {
            case GameObjects.PLAYER -> new Player(position);
            case GameObjects.BLINKY -> new Blinky(position);
            case GameObjects.PINKY -> new Pinky(position);
            case GameObjects.INKY -> new Inky(position);
            case GameObjects.CLYDE -> new Clyde(position);
            case GameObjects.PILL -> new Pill(position);
            case GameObjects.POWERPILL -> new PowerPill(position);
            case GameObjects.WALL -> new Wall(new Vector2D(approximate((int) this.startPos.getX() / SCALE), approximate((int) (this.startPos.getY() / SCALE))), size);
            default -> throw new IllegalStateException("Invalid game object: " + this.selected);
        };
    }

    private void linkNodes() {
        if (this.selectedNodes.size() == 2) {
            final MapNode src = this.selectedNodes.removeFirst();
            final MapNode dst = this.selectedNodes.removeFirst();
            this.mapNodes.addEdge(src, dst, this.distance(src.getPosition(), dst.getPosition()));
        }
    }

    private void removeNode(final Point p) {
        final var node = this.mapNodes.nodes().stream().filter(t -> t.getPosition().equals(new Vector2D(approximateGrid((int) p.getX()), approximateGrid((int) p.getY())))).findFirst();
        if (node.isPresent()) {
            this.mapNodes.removeNode(node.get());
        }
    }

    private double distance(final Vector2D v1, final Vector2D v2) {
        return Math.sqrt((v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY()));
    }

    private long howManyInstanciesOf(final GameObjects g) {
        return this.gameObjects.stream()
            .filter(t -> t.x().getClass().getSimpleName().toUpperCase().equals(g.name()))
            .count();
    }

    private long howManyInstanciesOfSelected() {
        return howManyInstanciesOf(this.selected);
    }

    @Override
    public synchronized void mouseEntered(final MouseEvent e) {
    }

    @Override
    public synchronized void mouseExited(final MouseEvent e) {
    }
}
