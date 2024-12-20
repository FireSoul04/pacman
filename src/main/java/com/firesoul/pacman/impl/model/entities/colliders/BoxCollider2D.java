package com.firesoul.pacman.impl.model.entities.colliders;

import java.io.Serializable;

import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.ColliderLayout;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class BoxCollider2D implements Collider, Serializable {

    private GameObject2D gameObject;
    private ColliderLayout layout;
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
        this.position = this.layout.positionRelativeTo(gameObject, dimensions);
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
    public void update() {
        this.position = this.layout.positionRelativeTo(this.gameObject, this.dimensions);
    }

    @Override
    public GameObject2D getAttachedGameObject() {
        return this.gameObject;
    }

    /**
     * Check if this collider is overlapping with the other collider.
     * 
     * @param other The other collider.
     * @return True if the colliders are overlapping, false otherwise.
     */
    public boolean isOvelapping(final Collider other) {
        final Vector2D d1 = this.getDimensions();
        final Vector2D d2 = other.getDimensions();
        final Vector2D c1 = this.getPosition();
        final Vector2D c2 = other.getPosition();
        return c1.getX() < c2.getX() + d2.getX()
            && c1.getX() + d1.getX() > c2.getX()
            && c1.getY() < c2.getY() + d2.getY()
            && c1.getY() + d1.getY() > c2.getY();
    }
}
