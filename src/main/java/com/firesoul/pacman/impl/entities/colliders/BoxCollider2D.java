package com.firesoul.pacman.impl.entities.colliders;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.impl.entities.bases.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class BoxCollider2D implements Collider {

    private GameObject2D entity;
    private Vector2D dimensions;

    /**
     * Creates a new BoxCollider2D with the given dimensions.
     * @param dimensions The dimensions of the collider.
     */
    public BoxCollider2D(final GameObject2D entity, final Vector2D dimensions) {
        this.entity = entity;
        this.dimensions = dimensions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isColliding(final Collider other) {
        if (this.isOvelapping(other)) {
            final Collidable thisEntity = (Collidable)this.entity;
            final Collidable otherEntity = (Collidable)other.getAttachedEntity();
            thisEntity.onCollide(otherEntity);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getDimensions() {
        return this.dimensions;
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

    private boolean isOvelapping(final Collider other) {
        return this.getPosition().getX() < other.getPosition().getX() + other.getDimensions().getX()
            && this.getPosition().getX() + this.dimensions.getX() > other.getPosition().getX()
            && this.getPosition().getY() < other.getPosition().getY() + other.getDimensions().getY()
            && this.getPosition().getY() + this.dimensions.getY() > other.getPosition().getY();
    }
}
