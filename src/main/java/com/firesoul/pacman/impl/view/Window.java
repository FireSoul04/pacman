package com.firesoul.pacman.impl.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.controller.InputController;
import com.firesoul.pacman.impl.util.Vector2D;

public class Window extends Canvas implements Renderer {
    
    private static final int MAP_WIDTH = 224;
    private static final int MAP_HEIGHT = 240;

    private final JFrame frame;
    private final InputController inputController;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;
    private Image backgroundImage;
    private int width;
    private int height;
    private int scale;

    public Window(
        final String title,
        final int width, 
        final int height, 
        final int scale
    ) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.frame = new JFrame(title);
        this.inputController = new InputController();
    }

    @Override
    public synchronized void init() {
        this.frame.setSize(new Dimension(this.width * this.scale, this.height * this.scale));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.add(this);
        this.frame.setVisible(true);
        this.backgroundImage = null;
        this.addKeyListener(this.inputController);
        this.createBufferStrategy(2); // Create double buffering
        this.bufferStrategy = this.getBufferStrategy();
    }

    @Override
    public synchronized void init(final String backgroundImagePath) {
        this.init();
        try {
            this.backgroundImage = ImageIO.read(new File(backgroundImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public synchronized void draw(final List<GameObject> gameObjects) {
        this.setGraphics();
        this.clear();
        for (final GameObject gameObject : gameObjects) {
            if (gameObject.isVisible() && gameObject.getDrawable() != null) {
                final Image image = gameObject.getDrawable().getImage();
                final int w = image.getWidth(this.frame);
                final int h = image.getHeight(this.frame);
                final Vector2D start = gameObject.getPosition().sub(new Vector2D(w, h).dot(0.5));
                final int x = (int)start.getX();
                final int y = (int)start.getY();
                this.graphics.drawImage(
                    image,
                    x * this.scale, 
                    y * this.scale,
                    w * this.scale,
                    h * this.scale,
                    this.frame
                );
                this.drawColliders(gameObject);
            }
        }
        this.graphics.dispose();
        this.bufferStrategy.show();
    }

    private void drawColliders(final GameObject g) {
        if (g instanceof Collidable) {
            Collidable collidable = (Collidable) g;
            for (Collider c : collidable.getColliders()) {
                int cx = (int) c.getPosition().dot(this.scale).getX();
                int cy = (int) c.getPosition().dot(this.scale).getY();
                int cw = (int) c.getDimensions().dot(this.scale).getX();
                int ch = (int) c.getDimensions().dot(this.scale).getY();
                this.graphics.setColor(Color.RED);
                this.graphics.fillRect(cx, cy, cw, ch);
            }
        }
    }

    @Override
    public synchronized void setColor(final Color color) {
        this.graphics.setColor(color);
    }

    @Override
    public synchronized void clear() {
        if (backgroundImage == null) {
            this.setColor(Color.BLACK);
            this.graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        } else {
            this.graphics.drawImage(this.backgroundImage, 0, 0, MAP_WIDTH * this.scale, MAP_HEIGHT * this.scale, this.frame);
        }
    }

    @Override
    public synchronized void resize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public synchronized void resize(final Vector2D dimensions) {
        this.width = (int) dimensions.getX();
        this.height = (int) dimensions.getY();
    }

    @Override
    public synchronized void setScale(final int scale) {
        this.scale = scale;
    }

    @Override
    public int getWidth() {
        return this.width * this.scale;
    }

    @Override
    public int getHeight() {
        return this.height * this.scale;
    }

    @Override
    public synchronized void addInputController(final InputController inputController) {
        this.addKeyListener(inputController);
    }

    /**
     * Get the input controller.
     * @return the input controller.
     */
    public InputController getInputController() {
        return this.inputController;
    }

    private synchronized void setGraphics() {
        this.graphics = this.bufferStrategy.getDrawGraphics();
    }
}
