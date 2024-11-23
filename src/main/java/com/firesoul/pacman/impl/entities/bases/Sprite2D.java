package com.firesoul.pacman.impl.entities.bases;

import java.awt.Image;

import com.firesoul.pacman.api.entities.Drawable;

public class Sprite2D implements Drawable {

    private final Image frame;

    public Sprite2D(final String name) {
        this.frame = Drawable.loadImage(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getImage() {
        return this.frame;
    }
}
