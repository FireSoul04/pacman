package com.firesoul.pacman.impl;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.firesoul.pacman.api.Renderer;

public class Window extends Canvas implements Renderer {
    
    private final JFrame frame;
    private int width;
    private int height;
    private int scale;

    public Window(
        final String title,
        final int width, 
        final int height, 
        final int scale
    ) {
        this.frame = new JFrame(title);
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.frame.setSize(new Dimension(this.width * this.scale, this.height * this.scale));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
        this.frame.add(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
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
        return this.width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return this.height;
    }
}
