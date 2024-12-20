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
            final Collidable thisGameObject = (Collidable) this.getAttachedGameObject();
            final Collidable otherGameObject = (Collidable) other.getAttachedGameObject();
            thisGameObject.onCollide(otherGameObject);
            return true;
        }
        return false;
    }

    /**
     * Check if two colliders are overlapping.
     * @param other collider to check with
     * @return if they are overlapping
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
     * @return the game object attached to this collider
     */
    GameObject2D getAttachedGameObject();

    /**
     * Update the position based on the position of this game object.
     */
    void update();
}
