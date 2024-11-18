package com.myprojects.pacman.impl.entities;

import com.myprojects.pacman.impl.util.Vector2;

public abstract class Entity {

    private Vector2 position;
    private Vector2 speed;

    /**
     * Create an entity
     * @param position
     * @param speed
     */
    public Entity(final Vector2 position, final Vector2 speed) {
        this.position = position;
        this.speed = speed;
    }

    /**
     * @return Entity's current position
     */
    protected Vector2 getPosition() {
        return this.position;
    }

    /**
     * @return Entity's speed
     */
    protected Vector2 getSpeed() {
        return this.speed;
    }

    /**
     * Set the position of entity
     * @param position
     */
    protected void setPosition(final Vector2 position) {
        this.position = position;
    }

    /**
     * Set the speed of entity
     * @param speed
     */
    protected void setSpeed(final Vector2 speed) {
        this.speed = speed;
    }
}
