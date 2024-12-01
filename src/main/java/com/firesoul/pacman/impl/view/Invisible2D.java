package com.firesoul.pacman.impl.view;

import com.firesoul.pacman.impl.util.Vector2D;

public class Invisible2D extends Sprite2D {

    private final Vector2D dimensions;

    public Invisible2D(final Vector2D dimensions) {
        super("invisible");
        this.dimensions = dimensions;
    }

    @Override
    public Vector2D getImageSize() {
        return this.dimensions;
    }
}
