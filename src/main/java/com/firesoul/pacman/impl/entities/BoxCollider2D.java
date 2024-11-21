package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.impl.util.Vector2D;

public class BoxCollider2D implements Collider {

    private Vector2D bounds;

    /**
     * Creates a new BoxCollider2D with the given bounds.
     * @param bounds The bounds of the collider.
     */
    public BoxCollider2D(final Vector2D bounds) {
        this.bounds = bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getBounds() {
        return this.bounds;
    }
}
