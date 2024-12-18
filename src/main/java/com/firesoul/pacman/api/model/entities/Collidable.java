package com.firesoul.pacman.api.model.entities;

import java.util.List;

public interface Collidable {

    /**
     * The action perfomed when this game object is colliding
     * @param other game object whom is colliding
     */
    void onCollide(Collidable other);

    /**
     * @return the collider of this game object
     */
    List<Collider> getColliders();

    /**
     * Check if this game object is colliding with the other
     * @param other game object to check collision with
     * @return if the two game object are colliding
     */
    default boolean isColliding(final Collidable other) {
        boolean colliding = false;
        for (final Collider c1 : this.getColliders()) {
            for (final Collider c2 : other.getColliders()) {
                if (c1.isColliding(c2)) {
                    colliding = true;
                }
            }
        }
        return colliding;
    }
}
