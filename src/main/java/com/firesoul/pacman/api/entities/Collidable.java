package com.firesoul.pacman.api.entities;

import com.firesoul.pacman.impl.util.Vector2D;

public interface Collidable {

    /**
     * The action perfomed when this entity is colliding
     * @param other entity whom is colliding
     */
    void onCollide(Collidable other);

    /**
     * @return the collider of this entity
     */
    Collider getCollider();

    /**
     * @return the bounds of this entity
     */
    default Vector2D getBounds() {
        return this.getCollider().getBounds();
    }

    /**
     * Check if this entity is colliding with the other
     * @param other
     * @return if the two entity are colliding
     */
    default boolean isColliding(final Collidable other) {
        return this.getBounds().intersect(other.getBounds());
    }
}
