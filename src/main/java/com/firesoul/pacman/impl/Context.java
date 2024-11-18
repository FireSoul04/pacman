package com.firesoul.pacman.impl;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.firesoul.pacman.api.Renderer;

public class Context extends Canvas {

    public void draw(final Renderer w) {
        final BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        final Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w.getWidth(), w.getHeight());
        g.dispose();
        bs.show();
    }
}
