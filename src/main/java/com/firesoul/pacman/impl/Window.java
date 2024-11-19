package com.firesoul.pacman.impl;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.firesoul.pacman.api.Renderer;
import com.firesoul.pacman.impl.entities.Player;

public class Window implements Renderer {
    
    private final JFrame frame;
    private final Canvas canvas;
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
        this.canvas = new Canvas();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.frame.setSize(new Dimension(this.width * this.scale, this.height * this.scale));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.add(this.canvas);
        this.frame.setVisible(true);
        this.canvas.createBufferStrategy(2); // Create double buffering
        this.bufferStrategy = this.canvas.getBufferStrategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        this.setGraphics();
        this.clear();
        this.setColor(Color.WHITE);
        this.graphics.fillRect(0, 0, 50, 50);
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

    private void setGraphics() {
        this.graphics = this.bufferStrategy.getDrawGraphics();
    }
}
