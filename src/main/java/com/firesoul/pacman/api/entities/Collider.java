package com.firesoul.pacman.api.entities;

import com.firesoul.pacman.impl.entities.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;

public interface Collider {

    /**
     * Checks if this collider collides with the other collider.
     * @param other the other collider to check for collision
     * @return true if this collider collides with the other collider
     */
    boolean collides(Collider other);

    /**
     * @return the position of this collider
     */
    Vector2D getPosition();

    /**
     * @return the bounds of this collider
     */
    Vector2D getBounds();

    /**
     * @return the entity attached to this collider
     */
    GameObject2D getAttachedEntity();
}
