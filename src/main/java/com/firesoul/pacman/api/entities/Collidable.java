package com.firesoul.pacman.api.entities;

public interface Collidable {

    /**
     * Check if this entity is colliding with the other
     * @param other
     * @return if the two entity are colliding
     */
    boolean isColliding(final Collidable other);

    /**
     * The action perfomed when this entity is colliding
     */
    void onCollide();
}
