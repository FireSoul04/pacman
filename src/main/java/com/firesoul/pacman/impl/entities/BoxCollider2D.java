package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.impl.util.Vector2D;

public class BoxCollider2D implements Collider {

    private GameObject2D entity;
    private Vector2D bounds;

    /**
     * Creates a new BoxCollider2D with the given bounds.
     * @param bounds The bounds of the collider.
     */
    public BoxCollider2D(final GameObject2D entity, final Vector2D bounds) {
        this.entity = entity;
        this.bounds = bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean collides(final Collider other) {
        if (this.isColliding(other)) {
            ((Collidable)this.entity).onCollide((Collidable)other.getAttachedEntity());
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getBounds() {
        return this.bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getPosition() {
        return this.entity.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObject2D getAttachedEntity() {
        return this.entity;
    }

    private boolean isColliding(final Collider other) {
        return this.getPosition().getX() < other.getPosition().getX() + other.getBounds().getX()
            && this.getPosition().getX() + this.bounds.getX() > other.getPosition().getX()
            && this.getPosition().getY() < other.getPosition().getY() + other.getBounds().getY()
            && this.getPosition().getY() + this.bounds.getY() > other.getPosition().getY();
    }
}
