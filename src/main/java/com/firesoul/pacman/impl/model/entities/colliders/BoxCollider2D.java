package com.firesoul.pacman.impl.model.entities.colliders;

import java.io.Serializable;

import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class BoxCollider2D implements Collider, Serializable {

    private GameObject2D gameObject;
    private Vector2D position;
    private Vector2D dimensions;

    /**
     * Creates a new BoxCollider2D with the given dimensions.
     * @param dimensions The dimensions of the collider.
     */
    public BoxCollider2D(final GameObject2D gameObject, final Vector2D dimensions) {
        this(gameObject, gameObject.getPosition(), dimensions);
    }

    public BoxCollider2D(final GameObject2D gameObject, final Vector2D position, final Vector2D dimensions) {
        this.gameObject = gameObject;
        this.position = position;
        this.dimensions = dimensions;
    }

    @Override
    public Vector2D getDimensions() {
        return this.dimensions;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(final Vector2D position) {
        this.position = position;
    }

    @Override
    public GameObject2D getAttachedGameObject() {
        return this.gameObject;
    }

    /**
     * Check if this collider is overlapping with the other collider.
     * The position of the colliders is the center of the attached entity.
     * 
     * @param other The other collider.
     * @return True if the colliders are overlapping, false otherwise.
     */
    public boolean isOvelapping(final Collider other) {
        final Vector2D d1 = this.getDimensions().dot(0.5);
        final Vector2D d2 = other.getDimensions().dot(0.5);
        final Vector2D i1 = this.getAttachedGameObject().getDrawable().getImageSize().dot(0.5);
        final Vector2D i2 = other.getAttachedGameObject().getDrawable().getImageSize().dot(0.5);
        final Vector2D c1 = this.getPosition().add(i1);
        final Vector2D c2 = other.getPosition().add(i2);
        return c1.getX() - d1.getX() < c2.getX() + d2.getX()
            && c1.getX() + d1.getX() > c2.getX() - d2.getX()
            && c1.getY() - d1.getY() < c2.getY() + d2.getY()
            && c1.getY() + d1.getY() > c2.getY() - d2.getY();
    }
}
