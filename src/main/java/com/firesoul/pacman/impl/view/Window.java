package com.firesoul.pacman.impl.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.List;

import javax.swing.JFrame;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.controller.InputController;

public class Window extends Canvas implements Renderer {
    
    private final JFrame frame;
    private final InputController inputController;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.frame.setSize(new Dimension(this.width * this.scale, this.height * this.scale));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.add(this);
        this.frame.setVisible(true);
        this.addKeyListener(inputController);
        this.createBufferStrategy(2); // Create double buffering
        this.bufferStrategy = this.getBufferStrategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(final List<GameObject> gameObjects) {
        this.setGraphics();
        this.clear();
        for (final GameObject gameObject : gameObjects) {
            if (gameObject.isVisible()) {
                final Image image = gameObject.getDrawable().getImage();
                this.graphics.drawImage(
                    image,
                    (int)gameObject.getPosition().getX() * this.scale, 
                    (int)gameObject.getPosition().getY() * this.scale,
                    image.getWidth(null) * this.scale,
                    image.getHeight(null) * this.scale,
                    null
                );
            }
        }
        this.graphics.dispose();
        this.bufferStrategy.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(final Color color) {
        this.graphics.setColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.setColor(Color.BLACK);
        this.graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScale(final int scale) {
        this.scale = scale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return this.width * this.scale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return this.height * this.scale;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addInputController(final InputController inputController) {
        this.addKeyListener(inputController);
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
