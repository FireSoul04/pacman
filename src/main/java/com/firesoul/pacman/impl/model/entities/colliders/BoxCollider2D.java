package com.firesoul.pacman.impl.model.entities.colliders;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.ColliderLayout;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class BoxCollider2D implements Collider, Serializable {

    private final GameObject2D gameObject;
    private final ColliderLayout layout;
    private final Vector2D dimensions;
    private final boolean solid;
    private Vector2D position;
    private boolean collided = false;
    private Set<Collider> collidersColliding = new HashSet<>();

    /**
     * Creates a new collider with a box form with the given dimensions and not solid. It position in the center of the image by default.
     * @param gameObject attached to
     * @param dimensions of the box
     */
    public BoxCollider2D(final GameObject2D gameObject, final Vector2D dimensions) {
        this(gameObject, dimensions, false);
    }

    /**
     * Creates a new collider with a box form with the given dimensions. It position in the center of the image by default.
     * @param gameObject attached to
     * @param dimensions of the box
     * @param solid if the object is solid
     */
    public BoxCollider2D(final GameObject2D gameObject, final Vector2D dimensions, final boolean solid) {
        this(gameObject, dimensions, new ColliderCenterLayout(), solid);
    }

    /**
     * Creates a new collider with a box form and not solid.
     * @param gameObject attached to
     * @param dimensions of the box
     * @param layout that decide how to follow the game object when it moves
     */
    public BoxCollider2D(final GameObject2D gameObject, final Vector2D dimensions, final ColliderLayout layout) {
        this(gameObject, dimensions, layout, false);
    }

    /**
     * Creates a new collider with a box form.
     * @param gameObject attached to
     * @param dimensions of the box
     * @param layout that decide how to follow the game object when it moves
     * @param solid if the object is solid
     */
    public BoxCollider2D(final GameObject2D gameObject, final Vector2D dimensions, final ColliderLayout layout, final boolean solid) {
        this.gameObject = gameObject;
        this.dimensions = dimensions;
        this.layout = layout;
        this.solid = solid;
        this.position = layout.positionRelativeTo(gameObject, dimensions);
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
        this.collidersColliding.clear();
    }

    @Override
    public GameObject2D getAttachedGameObject() {
        return this.gameObject;
    }

    @Override
    public boolean hasCollidedLastFrame() {
        return this.collided;
    }

    @Override
    public Set<Collider> collidedGameObjects() {
        return this.collidersColliding;
    }

    @Override
    public boolean isSolid() {
        return this.solid;
    }

    @Override
    public boolean equals(final Object o) {
        final BoxCollider2D b = (BoxCollider2D) o;
        return this == o || 
            (this.position.equals(b.getPosition())
            && this.dimensions.equals(b.getDimensions()));
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
        this.collided = c1.getX() < c2.getX() + d2.getX()
            && c1.getX() + d1.getX() > c2.getX()
            && c1.getY() < c2.getY() + d2.getY()
            && c1.getY() + d1.getY() > c2.getY();
        if (this.collided) {
            this.collidersColliding.add(other);
        }
        return this.collided;
    }
}
