package com.firesoul.editor.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.impl.model.entities.Wall;
import com.firesoul.pacman.impl.util.Vector2D;

public class Gui extends JFrame implements MouseListener {

    private static final int LEFT_MARGIN = 48;
    private static final int WIDTH = 224;
    private static final int HEIGHT = 248;
    private static final int SCALE = 3;
    private static final String MAP_PATH = "src/main/resources/map/map.txt";

    private final List<Rectangle> rects;
    private final JPanel buttonFrame;
    private final Canvas canvas;
    private Point startPos;

    @SuppressWarnings("unchecked")
    public Gui() {
        super("Level editor");
        this.rects = new LinkedList<>();
        this.buttonFrame = new JPanel(new BorderLayout());
        this.canvas = new Canvas();
        this.canvas.addMouseListener(this);
        this.setSize(WIDTH * SCALE + 100, HEIGHT * SCALE + 80);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(this.canvas);

        final JButton reset = new JButton("Reset");
        final JButton goBack = new JButton("Go back to last modify");
        final JButton save = new JButton("Save");
        reset.addActionListener(e -> reset());
        goBack.addActionListener(e -> goBack());
        save.addActionListener(e -> save());

        try (
            final ObjectInputStream reader = new ObjectInputStream(
                new FileInputStream(MAP_PATH)
            )
        ) {
            if (Files.size(Path.of(MAP_PATH)) > 0) {
                reader.readObject();
                ((List<GameObject>) reader.readObject()).stream()
                    .filter(t -> t instanceof Collidable)
                    .forEach(t -> {
                        this.rects.add(
                            new Rectangle(
                                (int)((Collidable) t).getColliders().get(0).getPosition().getX() + (LEFT_MARGIN / SCALE),
                                (int)((Collidable) t).getColliders().get(0).getPosition().getY(),
                                (int)((Collidable) t).getColliders().get(0).getDimensions().getX(),
                                (int)((Collidable) t).getColliders().get(0).getDimensions().getY()
                            )
                        );
                    }
                );
            }
        } catch (final IOException | ClassNotFoundException e) {
            System.out.println("Cannot read file: " + e);
            this.rects.clear();
        }

        this.buttonFrame.add(reset, BorderLayout.WEST);
        this.buttonFrame.add(goBack, BorderLayout.CENTER);
        this.buttonFrame.add(save, BorderLayout.EAST);
        this.add(this.buttonFrame, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void run() {
        final Graphics g = this.canvas.getGraphics();
        try {
            while (true) {
                g.setColor(Color.BLUE);
                g.drawImage(this.readImage(), LEFT_MARGIN, 0, WIDTH * SCALE, HEIGHT * SCALE, this.canvas);
                this.rects.forEach(t -> g.fillRect(t.x * SCALE, t.y * SCALE, t.width * SCALE, t.height * SCALE));
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

    private synchronized void reset() {
        final Graphics g = this.canvas.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH * SCALE + LEFT_MARGIN, HEIGHT * SCALE);
        this.canvas.update(g);
        this.rects.clear();
    }

    private synchronized void goBack() {
        if (this.rects.size() > 0) {
            this.rects.removeLast();
        }
        final Graphics g = this.canvas.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH * SCALE + LEFT_MARGIN, HEIGHT * SCALE);
        this.canvas.update(g);
    }

    private synchronized void save() {
        try (ObjectOutputStream os = new ObjectOutputStream(
            new FileOutputStream(MAP_PATH)
        )) {
            os.writeObject(new Vector2D(WIDTH, HEIGHT));

            final List<GameObject> gameObjects = new ArrayList<>();
            this.rects.forEach(t -> gameObjects.add(new Wall(new Vector2D(t.x - (LEFT_MARGIN / SCALE), t.y), new Vector2D(t.width, t.height))));

            os.writeObject(gameObjects);
        } catch (IOException e) {
            System.out.println("Cannot open file: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Image readImage() throws IOException {
        return ImageIO.read(new File("src/main/resources/sprites/map/map_hitboxes.png"));
    }

    @Override
    public synchronized void mouseClicked(final MouseEvent e) {
    }

    @Override
    public synchronized void mousePressed(final MouseEvent e) {
        this.startPos = e.getPoint();
    }

    @Override
    public synchronized void mouseReleased(final MouseEvent e) {
        final Rectangle rect = this.getRectangle(this.startPos, e.getPoint());
        if (rect.width > 0 && rect.height > 0) {
            this.rects.add(rect);
        }
    }

    @Override
    public synchronized void mouseEntered(final MouseEvent e) {
    }

    @Override
    public synchronized void mouseExited(final MouseEvent e) {
    }
}
