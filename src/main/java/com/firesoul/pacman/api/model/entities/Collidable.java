package com.firesoul.pacman.api.model.entities;

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
    default Vector2D getDimensions() {
        return this.getCollider().getDimensions();
    }

    /**
     * @return the position of this entity
     */
    default Vector2D getColliderPosition() {
        return this.getCollider().getPosition();
    }

    /**
     * Check if this entity is colliding with the other
     * @param other entity to check collision with
     * @return if the two entity are colliding
     */
    default boolean isColliding(final Collidable other) {
        return this.getCollider().isColliding(other.getCollider());
    }
}
