package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Entity;
import com.firesoul.pacman.impl.util.Vector2D;

public abstract class Entity2D extends GameObject2D implements Entity {

    private Vector2D position;
    private Vector2D speed;

    /**
     * Create an entity
     * @param position
     * @param speed
     */
    public Entity2D(final Vector2D position, final Vector2D speed) {
        this.position = position;
        this.speed = speed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getPosition() {
        return this.position;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getSpeed() {
        return this.speed;
    }

    /**
     * Set the position of entity
     * @param position
     */
    protected void setPosition(final Vector2D position) {
        this.position = position;
    }

    /**
     * Set the speed of entity
     * @param speed
     */
    protected void setSpeed(final Vector2D speed) {
        this.speed = speed;
    }
}
