package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Entity;
import com.firesoul.pacman.impl.util.Vector2D;

public abstract class Entity2D extends GameObject2D implements Entity {

    /**
     * Create a new Entity2D
     * @param position
     * @param speed
     */
    public Entity2D(final Vector2D position, final Vector2D speed) {
        super(position, speed);
    }
}
