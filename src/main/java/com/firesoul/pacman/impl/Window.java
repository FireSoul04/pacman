package com.firesoul.pacman.impl;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.firesoul.pacman.api.Renderer;

public class Window extends Canvas implements Renderer {
    
    private final JFrame frame;
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
        this.createBufferStrategy(2); // Create double buffering
        this.bufferStrategy = this.getBufferStrategy();
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
