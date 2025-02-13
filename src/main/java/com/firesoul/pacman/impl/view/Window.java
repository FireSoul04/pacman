package com.firesoul.pacman.impl.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.controller.InputController;
import com.firesoul.pacman.impl.util.Vector2D;

public class Window extends JFrame implements Renderer {
    
    private static final int MAP_WIDTH = 224;
    private static final int MAP_HEIGHT = 248;

    private final Map<UIType, JLabel> labels = new HashMap<>();
    private final JPanel textPanel = new JPanel();
    private final Canvas canvas = new Canvas();
    private final int startWidth;
    private final int startHeight;
    private InputController inputController;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;
    private Image backgroundImage;
    private int width;
    private int height;
    private double scaleX;
    private double scaleY;

    public Window(
        final String title,
        final int width,
        final int height,
        final double scaleX,
        final double scaleY
    ) {
        super(title);
        this.startWidth = width;
        this.startHeight = height;
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public void init() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.canvas.setSize(new Dimension((int) (MAP_WIDTH * this.scaleX), (int) (MAP_HEIGHT * this.scaleY)));
        this.getContentPane().add(this.canvas, BorderLayout.NORTH);
        this.getContentPane().add(this.textPanel);
        this.getContentPane().setPreferredSize(new Dimension((int) (this.width * this.scaleX), (int) (this.height * this.scaleY)));
        this.setVisible(true);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.canvas.addKeyListener(this.inputController.getKeyListener());
        this.canvas.createBufferStrategy(2); // Create double buffering
        this.backgroundImage = null;
        this.bufferStrategy = this.canvas.getBufferStrategy();
        this.labels.put(UIType.LEVEL, new JLabel(""));
        this.labels.put(UIType.LIVES, new JLabel(""));
        this.labels.put(UIType.SCORE, new JLabel(""));
        this.labels.forEach((k, v) -> this.textPanel.add(v));
        this.labels.forEach((k, v) -> v.setForeground(Color.WHITE));
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(final ComponentEvent componentEvent) {
                final Window w = Window.this;
                final Dimension d = ((JFrame) componentEvent.getComponent()).getContentPane().getSize();
                w.setScaleX(d.getWidth() / w.startWidth);
                w.setScaleY(d.getHeight() / w.startHeight);
            }
        });
    }

    @Override
    public void init(final String backgroundImagePath) {
        this.init();
        try {
            this.backgroundImage = ImageIO.read(new File(backgroundImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void draw(final List<GameObject> gameObjects) {
        for (final GameObject gameObject : gameObjects) {
            if (gameObject.isVisible() && gameObject.getDrawable() != null) {
                final Image image = gameObject.getDrawable().getImage();
                final int w = image.getWidth(this.canvas);
                final int h = image.getHeight(this.canvas);
                final Vector2D start = gameObject.getPosition().sub(new Vector2D(w, h).dot(0.5));
                final int x = (int) start.getX();
                final int y = (int) start.getY();
                this.graphics.drawImage(
                    image,
                    (int) (x * this.scaleX), 
                    (int) (y * this.scaleY),
                    (int) (w * this.scaleX),
                    (int) (h * this.scaleY),
                    this.canvas
                );
            }
        }
        // gameObjects.forEach(this::drawColliders);
    }

    // DEBUG PURPOSE
    // final Color color = new Color(1, 0, 0, 0.5f);
    // private void drawColliders(final GameObject g) {
    //     this.graphics.setColor(color);
    //     if (g instanceof Collidable collidable) {
    //         for (final Collider c : collidable.getColliders()) {
    //             int cx = (int) c.getPosition().dot(this.scaleX).getX();
    //             int cy = (int) c.getPosition().dot(this.scaleY).getY();
    //             int cw = (int) c.getDimensions().dot(this.scaleX).getX();
    //             int ch = (int) c.getDimensions().dot(this.scaleY).getY();
    //             this.graphics.fillRect(cx, cy, cw, ch);
    //         }
    //     }
    // }

    @Override
    public void drawText(final UIType type, final String text) {
        this.labels.get(type).setText(text);
    }

    @Override
    public void startDraw() {
        this.setGraphics();
        this.clear();
    }

    @Override
    public void endDraw() {
        this.graphics.dispose();
        this.bufferStrategy.show();
    }

    @Override
    public void setColor(final Color color) {
        this.graphics.setColor(color);
    }

    @Override
    public void clear() {
        this.setColor(Color.BLACK);
        this.graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        this.textPanel.setBackground(Color.BLACK);
        if (this.backgroundImage != null) {
            this.graphics.drawImage(this.backgroundImage, 0, 0, (int) (MAP_WIDTH * this.scaleX), (int) (MAP_HEIGHT * this.scaleY), this.canvas);
        }
    }

    @Override
    public void resize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void resize(final Vector2D dimensions) {
        this.width = (int) dimensions.getX();
        this.height = (int) dimensions.getY();
    }

    @Override
    public void setScaleX(final double scaleX) {
        this.scaleX = scaleX;
    }

    @Override
    public void setScaleY(final double scaleY) {
        this.scaleY = scaleY;
    }

    @Override
    public int getWidth() {
        return (int) (this.width * this.scaleX);
    }

    @Override
    public int getHeight() {
        return (int) (this.height * this.scaleY);
    }

    @Override
    public void addInputController(final InputController inputController) {
        this.inputController = inputController;
    }

    /**
     * Get the input controller.
     * @return the input controller.
     */
    public InputController getInputController() {
        return this.inputController;
    }

    private void setGraphics() {
        this.graphics = this.bufferStrategy.getDrawGraphics();
    }
}
