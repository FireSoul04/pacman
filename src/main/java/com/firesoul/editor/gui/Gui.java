package com.firesoul.editor.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.firesoul.editor.gui.LogicImpl.GameObjects;
import com.firesoul.pacman.api.model.*;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.util.Vector2D;

public class Gui extends JFrame implements MouseListener {

    private static final int SCALE = 3;
    private static final int LEFT_MARGIN = 48;
    private static final int SIZE = 16;
    private static final int GRID_SIZE = 8;
    private static final int WIDTH = 224;
    private static final int HEIGHT = 248;

    private final Logic logic = new LogicImpl();
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

    private void addHandlerButtons() {
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

    private void addEditorButtons() {
        Arrays.asList(GameObjects.values()).stream().map(GameObjects::name).map(String::toLowerCase).forEach(t -> this.editorFrame.add(new JButton(t)));
        List.of(this.editorFrame.getComponents())
            .stream()
            .filter(t -> t instanceof JButton)
            .map(t -> (JButton) t)
            .forEach(t -> t.addActionListener(e -> this.selected = GameObjects.valueOf(t.getText().toUpperCase())));

        final JButton link = new JButton("link");
        link.addActionListener(t -> this.logic.linkNodes());
        this.editorFrame.add(link);
        this.editorFrame.add(this.labelSelected);
    }

    private void saveToFile() {
        this.logic.save();
    }

    private void loadFromFile() {
        this.logic.load();
    }

    public void run() {
        while (true) {
            this.draw();
            this.labelSelected.setText(this.selected.name().toLowerCase());
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

    private void draw() {
        final Graphics g = this.canvas.getGraphics();
        this.drawBackground(g);
        for (final GameObject gameObject : this.logic.getGameObjects()) {
            final Vector2D position = gameObject.getPosition();
            final Vector2D size = gameObject instanceof Wall w
                ? w.getColliders().getFirst().getDimensions()
                : gameObject.getDrawable().getImageSize();
            final Rectangle rect = new Rectangle((int) position.getX(), (int) position.getY(), (int) size.getX(), (int) size.getY());
            if (gameObject instanceof Wall) {
                this.drawWall(g, rect);
            } else {
                this.drawImage(g, gameObject, rect);
            }
        }
        this.drawGraph(g);
    }

    private void drawBackground(final Graphics g) {
        Image image = null;
        try {
            image = this.readImage();
        } catch (final IOException e) {
            System.out.println("Cant found image");
        }
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getContentPane().getWidth() * SCALE, this.getContentPane().getHeight() * SCALE);
        if (image != null) {
            g.drawImage(image, LEFT_MARGIN, 0, WIDTH * SCALE, HEIGHT * SCALE, this.canvas);
        }
    }

    private Image readImage() throws IOException {
        return ImageIO.read(new File("src/main/resources/sprites/map/map_hitboxes.png"));
    }

    private void drawWall(final Graphics g, final Rectangle t) {
        g.setColor(Color.BLUE);
        g.fillRect(t.x * SCALE, t.y * SCALE, t.width * SCALE, t.height * SCALE);
    }

    private void drawImage(final Graphics g, final GameObject gameObject, final Rectangle t) {
        final Image image = gameObject.getDrawable().getImage();
        if (image != null) {
            g.drawImage(image, t.x * SCALE, t.y * SCALE, t.width * SCALE, t.height * SCALE, this.canvas);
        }
    }

    private void drawGraph(final Graphics g) {
        g.setColor(Color.RED);
        try {
            for (final var node : this.logic.getMap().edges().entrySet()) {
                final int x1 = (int) node.getKey().getPosition().dot(SCALE).getX() + (SIZE / 2) * SCALE;
                final int y1 = (int) node.getKey().getPosition().dot(SCALE).getY() + (SIZE / 2) * SCALE;
                final Rectangle t = new Rectangle((int) node.getKey().getPosition().dot(SCALE).getX(), (int) node.getKey().getPosition().dot(SCALE).getY(), SIZE, SIZE);
                g.drawRect(t.x, t.y, t.width * SCALE, t.height * SCALE);
                for (final var edge : node.getValue().keySet()) {
                    final int x2 = (int) edge.getPosition().dot(SCALE).getX() + (SIZE / 2) * SCALE;
                    final int y2 = (int) edge.getPosition().dot(SCALE).getY() + (SIZE / 2) * SCALE;
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        } catch (ConcurrentModificationException e) {
        }
    }

    private Rectangle getRectangle(final Point p1, final Point p2) {
        final int w = (int) Math.abs(p2.getX() - p1.getX());
        final int h = (int) Math.abs(p2.getY() - p1.getY());
        return new Rectangle(approximate((int)p1.x / SCALE), approximate((int)p1.y / SCALE), approximate(w / SCALE), approximate(h / SCALE));
    }

    private int approximate(final int x) {
        return x - (x % 4);
    }

    private int approximateGrid(final int x) {
        return x - (x % (this.logic.isLimited(this.selected) ? GRID_SIZE / 2 : GRID_SIZE)) - 4;
    }

    private void reset() {
        final Graphics g = this.canvas.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH * SCALE + LEFT_MARGIN, HEIGHT * SCALE);
        this.canvas.update(g);
        this.logic.reset();
    }

    private void goBack() {
        // unimplemented
        // if (this.logic.getGameObjects().size() > 0) {
        //     this.logic.getGameObjects().remove(this.logic.getGameObjects().toArray()[this.logic.getGameObjects().size() - 1]);
        // }
        // final Graphics g = this.canvas.getGraphics();
        // g.setColor(Color.WHITE);
        // g.fillRect(0, 0, WIDTH * SCALE + LEFT_MARGIN, HEIGHT * SCALE);
        // this.canvas.update(g);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        if (this.selected.equals(GameObjects.WALL)) {
            this.startPos = e.getPoint();
        } else {
            final Vector2D p = this.selected.equals(GameObjects.ERASER)
                ? new Vector2D(e.getX() / SCALE, e.getY() / SCALE)
                : new Vector2D(approximateGrid(e.getX() / SCALE), approximateGrid(e.getY() / SCALE));
            this.logic.click(this.selected, p);
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (this.selected.equals(GameObjects.WALL)) {
            final Rectangle rect = this.getRectangle(this.startPos, e.getPoint());
            this.addGameObject(e.getPoint(), rect);
        }
    }

    private void addGameObject(final Point p, final Rectangle rect) {
        final Vector2D position = this.selected.equals(GameObjects.WALL)
            ? new Vector2D(approximate((int) this.startPos.getX() / SCALE), approximate((int) (this.startPos.getY() / SCALE)))
            : new Vector2D(approximateGrid((int) p.getX() / SCALE), approximateGrid((int) p.getY() / SCALE));
        this.logic.addGameObject(this.selected, position, new Vector2D(rect.getWidth(), rect.getHeight()));
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }
}
