package com.firesoul.pacman.impl.model.entities.colliders;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
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

    /**
     * Check if this collider is overlapping with the other collider.
     * The position of the colliders is the center of the attached entity.
     * 
     * @param other The other collider.
     * @return True if the colliders are overlapping, false otherwise.
     */
    private boolean isOvelapping(final Collider other) {
        final Vector2D d1 = this.getDimensions().dot(0.5);
        final Vector2D d2 = other.getDimensions().dot(0.5);
        final Vector2D i1 = this.getAttachedEntity().getDrawable().getImageSize().dot(0.5);
        final Vector2D i2 = other.getAttachedEntity().getDrawable().getImageSize().dot(0.5);
        final Vector2D c1 = this.getPosition().add(i1);
        final Vector2D c2 = other.getPosition().add(i2);
        return c1.getX() - d1.getX() < c2.getX() + d2.getX()
            && c1.getX() + d1.getX() > c2.getX() - d2.getX()
            && c1.getY() - d1.getY() < c2.getY() + d2.getY()
            && c1.getY() + d1.getY() > c2.getY() - d2.getY();
    }
}
