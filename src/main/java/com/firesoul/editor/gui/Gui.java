package com.firesoul.editor.gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.firesoul.editor.gui.LogicImpl.GameObjects;
import com.firesoul.pacman.api.model.*;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Invisible2D;

public class Gui extends JFrame implements MouseListener {

    private static final int MARGIN = 16;
    private static final int SIZE = 16;
    private static final int GRID_SIZE = 8;
    private static final int MAP_WIDTH = 224;
    private static final int MAP_HEIGHT = 248;

    private final Logic logic = new LogicImpl();
    private final JPanel buttonFrame;
    private final JPanel editorFrame;
    private final JLabel labelSelected;
    private final Canvas canvas;
    private GameObjects selected = GameObjects.WALL;
    private Point startPos;
    private double scaleX;
    private double scaleY;

    public Gui() {
        super("Level editor");
        this.buttonFrame = new JPanel(new BorderLayout());
        this.editorFrame = new JPanel(new GridLayout(5, 6, 20, 20));
        this.canvas = new Canvas();
        this.canvas.addMouseListener(this);
        this.labelSelected = new JLabel(this.selected.name().toLowerCase());

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.scaleX = screenSize.getWidth() / 10000 * 9;
        this.scaleY = screenSize.getHeight() / 10000 * 16;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout());
        this.loadFromFile();
        this.addEditorButtons();
        this.addHandlerButtons();
        this.getContentPane().add(this.canvas);
        this.getContentPane().add(this.editorFrame);
        this.getContentPane().setPreferredSize(new Dimension((int) ((MAP_WIDTH + MARGIN * 2) * this.scaleX * 2), (int) ((MAP_HEIGHT) * this.scaleY)));
        this.setVisible(true);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(final ComponentEvent componentEvent) {
                final Gui w = Gui.this;
                final Dimension d = ((JFrame) componentEvent.getComponent()).getContentPane().getSize();
                w.setScaleX((d.getWidth() - w.editorFrame.getWidth()) / (Gui.MAP_WIDTH + MARGIN * 2));
                w.setScaleY((d.getHeight() - w.buttonFrame.getHeight()) / Gui.MAP_HEIGHT);
            }
        });
    }

    private void addHandlerButtons() {
        final JButton reset = new JButton("Reset");
        final JButton save = new JButton("Save");
        reset.addActionListener(e -> this.reset());
        save.addActionListener(e -> this.saveToFile());

        this.editorFrame.add(reset);
        this.editorFrame.add(save);
    }

    private void addEditorButtons() {
        Arrays.asList(GameObjects.values()).stream().map(GameObjects::name).map(String::toLowerCase).forEach(t -> this.editorFrame.add(new JButton(t)));
        List.of(this.editorFrame.getComponents())
            .stream()
            .filter(t -> t instanceof JButton)
            .map(t -> (JButton) t)
            .forEach(t -> t.addActionListener(e -> this.selected = GameObjects.valueOf(t.getText().toUpperCase())));

        final JButton switchToEditGraphMode = new JButton("graph mode");
        final JButton switchToInsertMode = new JButton("insert mode");
        switchToEditGraphMode.addActionListener(t -> this.logic.setEditGraphMode(true));
        switchToInsertMode.addActionListener(t -> this.logic.setEditGraphMode(false));
        this.editorFrame.add(switchToEditGraphMode);
        this.editorFrame.add(switchToInsertMode);
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
        BufferStrategy bs = this.canvas.getBufferStrategy();
        if (bs == null) {
            this.canvas.createBufferStrategy(2);
            bs = this.canvas.getBufferStrategy();
        }
        final Graphics g = bs.getDrawGraphics();
        this.drawBackground(g);
        for (final GameObject gameObject : this.logic.getGameObjects()) {
            final Vector2D position = gameObject.getPosition();
            final Vector2D size = gameObject instanceof Wall w
                ? w.getColliders().getFirst().getDimensions()
                : gameObject.getDrawable().getImageSize();
            final Rectangle rect = new Rectangle((int) position.getX(), (int) position.getY(), (int) size.getX(), (int) size.getY());
            if (gameObject instanceof Wall) {
                this.drawRect(g, rect, Color.BLUE);
            }
            if (this.logic.isInEditGraphMode()) {
                if (gameObject instanceof CageEnter) {
                    this.drawRect(g, rect, Color.ORANGE);
                } else if (gameObject instanceof CageExit) {
                    this.drawRect(g, rect, Color.GREEN);
                }
            } else {
                this.drawImage(g, gameObject, rect);
            }
        }
        if (this.logic.isInEditGraphMode()) {
            this.drawGraph(g);
        }
        g.dispose();
        bs.show();
    }

    private void drawBackground(final Graphics g) {
        Image image = null;
        try {
            image = this.readImage();
        } catch (final IOException e) {
            System.out.println("Cant find image");
        }
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int) ((MAP_WIDTH + MARGIN * 2) * this.scaleX), (int) (MAP_HEIGHT * this.scaleY));
        if (image != null) {
            g.drawImage(
                image,
                (int)(MARGIN * this.scaleX),
                0,
                (int)(MAP_WIDTH * this.scaleX),
                (int)(MAP_HEIGHT * this.scaleY),
                this.canvas
            );
        }
    }

    private Image readImage() throws IOException {
        return ImageIO.read(new File("src/main/resources/sprites/map/map_hitboxes.png"));
    }

    private void drawRect(final Graphics g, final Rectangle t, final Color color) {
        g.setColor(color);
        g.fillRect((int) (t.x * this.scaleX), (int) (t.y * this.scaleY), (int) (t.width * this.scaleX), (int) (t.height * this.scaleY));
    }

    private void drawImage(final Graphics g, final GameObject gameObject, final Rectangle t) {
        if (gameObject.getDrawable() != null && !(gameObject.getDrawable() instanceof Invisible2D)) {
            final Image image = gameObject.getDrawable().getImage();
            if (image != null) {
                g.drawImage(
                    image,
                    (int) (t.x * this.scaleX),
                    (int) (t.y * this.scaleY),
                    (int) (t.width * this.scaleX),
                    (int) (t.height * this.scaleY),
                    this.canvas
                );
            }
        }
    }

    private void drawGraph(final Graphics g) {
        g.setColor(Color.RED);
        try {
            this.drawNodes(g);
            this.drawEdges(g);
        } catch (Exception e) {
        }
    }

    private void drawNodes(final Graphics g) {
        for (final var src : this.logic.getMap().edges().entrySet()) {
            final Rectangle t = new Rectangle((int) src.getKey().dot(this.scaleX).getX(), (int) src.getKey().dot(this.scaleY).getY(), SIZE, SIZE);
            g.drawRect(t.x, t.y, (int) (t.width * this.scaleX), (int) (t.height * this.scaleY));
        }
    }

    private void drawEdges(final Graphics g) {
        for (final var src : this.logic.getMap().edges().entrySet()) {
            int x1 = (int) src.getKey().dot(this.scaleX).getX() + (int) ((SIZE / 2) * this.scaleX);
            int y1 = (int) src.getKey().dot(this.scaleY).getY() + (int) ((SIZE / 2) * this.scaleY);
            for (final var dst : src.getValue().keySet()) {
                int x2 = (int) dst.dot(this.scaleX).getX() + (int) ((SIZE / 2) * this.scaleX);
                int y2 = (int) dst.dot(this.scaleY).getY() + (int) ((SIZE / 2) * this.scaleY);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private Rectangle getRectangle(final Point p1, final Point p2) {
        final int w = (int) Math.abs(p2.getX() - p1.getX());
        final int h = (int) Math.abs(p2.getY() - p1.getY());
        return new Rectangle(approximate((int) (p1.x / this.scaleX)), approximate((int) (p1.y / this.scaleY)), approximate((int) (w / this.scaleX)), approximate((int) (h / this.scaleY)));
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
        g.fillRect(0, 0, (int) ((MAP_WIDTH + MARGIN) * this.scaleX), (int) (MAP_HEIGHT * this.scaleY));
        this.canvas.update(g);
        this.logic.reset();
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
                ? new Vector2D(e.getX() / this.scaleX, e.getY() / this.scaleY)
                : new Vector2D(approximateGrid((int) (e.getX() / this.scaleX)), approximateGrid((int) (e.getY() / this.scaleY)));
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

    public void setScaleX(final double scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(final double scaleY) {
        this.scaleY = scaleY;
    }
    
    private void addGameObject(final Point p, final Rectangle rect) {
        final Vector2D position = this.selected.equals(GameObjects.WALL)
            ? new Vector2D(approximate((int) (this.startPos.getX() / this.scaleX)), approximate((int) ((this.startPos.getY() / this.scaleY))))
            : new Vector2D(approximateGrid((int) (p.getX() / this.scaleX)), approximateGrid((int) (p.getY() / this.scaleY)));
        this.logic.addGameObject(this.selected, position, new Vector2D(rect.getWidth(), rect.getHeight()));
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }
}
