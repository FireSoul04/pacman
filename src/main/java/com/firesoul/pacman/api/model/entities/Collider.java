package com.firesoul.pacman.api.model.entities;

import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;

public interface Collider {

    /**
     * Checks if this collider is colliding with the other collider.
     * @param other the other collider to check for collision
     * @return true if this collider is colliding with the other collider
     */
    default boolean isColliding(final Collider other) {
        if (this.isOvelapping(other)) {
            final Collidable thisEntity = (Collidable) this.getAttachedEntity();
            final Collidable otherEntity = (Collidable) other.getAttachedEntity();
            thisEntity.onCollide(otherEntity);
            return true;
        }
        return false;
    }

    /**
     * Check if two colliders are overlapping with each other.
     * @param other collider to check
     * @return if the two colliders are overlapping
     */
    boolean isOvelapping(Collider other);

    /**
     * @return the position of this collider
     */
    Vector2D getPosition();

    /**
     * @return the dimensions of this collider
     */
    Vector2D getDimensions();

    /**
     * @return the entity attached to this collider
     */
    GameObject2D getAttachedEntity();
}
