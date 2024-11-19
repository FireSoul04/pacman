package com.firesoul.pacman.impl;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.firesoul.pacman.api.Renderer;
import com.firesoul.pacman.impl.entities.Player;

public class Window implements Renderer {
    
    private final JFrame frame;
    private final Canvas canvas;
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
    }

    //TEMP
    private int cont = 0;
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw() {
        final Graphics g = this.canvas.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        //TEMP
        g.setColor(Color.WHITE);
        g.fillRect(cont++, 0, 40, 40);
        if (cont >= this.getWidth()) {
            cont = 0;
        }
        try {
            Thread.sleep(1);
        } catch (Exception e) {

        }
        //
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
}
