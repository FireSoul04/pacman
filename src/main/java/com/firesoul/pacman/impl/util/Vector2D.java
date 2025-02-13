package com.firesoul.pacman.impl.util;

import java.util.Objects;

import com.firesoul.pacman.api.util.Vector;

public class Vector2D implements Vector {

    private double x;
    private double y;

    /**
     * Default vector constructor
     * @param x coordinate
     * @param y coordinate
     */
    public Vector2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a vector with both coordinates at 0
     */
    public Vector2D() {
        this(0, 0);
    }

    /**
     * @return Vector's x coordinate
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return Vector's y coordinate
     */
    public double getY() {
        return this.y;
    }

    /**
     * @param x the new x coordinate
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * @param y the new y coordinate
     */
    public void setY(final double y) {
        this.y = y;
    }

    @Override
    public Vector2D add(final Vector2D v) {
        return Vector2D.add(this, v);
    }
    
    @Override
    public Vector2D sub(final Vector2D v) {
        return Vector2D.sub(this, v);
    }
    
    @Override
    public Vector2D invert() {
        return Vector2D.invert(this);
    }

    @Override
    public Vector2D dot(final double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    @Override
    public boolean intersect(final Vector2D v) {
        return this.x <= v.x + Vector.EPSILON && this.x >= v.x - Vector.EPSILON &&
               this.y <= v.y + Vector.EPSILON && this.y >= v.y - Vector.EPSILON;
    }
    
    @Override
    public Vector2D clamp(final Vector2D min, final Vector2D max) {
        return new Vector2D(
            Math.min(Math.max(this.x, min.x), max.x),
            Math.min(Math.max(this.y, min.y), max.y)
        );
    }

    @Override
    public Vector2D wrap(final Vector2D min, final Vector2D max) {
        Vector2D wrapped = new Vector2D(this.x, this.y);
        if (wrapped.getX() > max.getX()) {
            wrapped.setX(min.getX());
        } else if (wrapped.getX() < min.getX()) {
            wrapped.setX(max.getX());
        }
        if (wrapped.getY() > max.getY()) {
            wrapped.setY(min.getY());
        } else if (wrapped.getY() < min.getY()) {
            wrapped.setY(max.getY());
        }
        return wrapped;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Vector2D)) {
            return false;
        }
        Vector2D v = (Vector2D) o;
        return this.x == v.x && this.y == v.y;
    }

    @Override
    public String toString() {
        return "[x: " + this.x + ", y:" + this.y + "]";
    }

    /**
     * Add two vectors
     * @param v1
     * @param v2
     * @return The sum of the two vectors
     */
    public static Vector2D add(final Vector2D v1, final Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * Subtract two vectors
     * @param v1
     * @param v2
     * @return The subtraction of the two vectors
     */
    public static Vector2D sub(final Vector2D v1, final Vector2D v2) {
        return add(v1, v2.invert());
    }

    /**
     * Invert the sign of the vector's coordinates
     * @param v the vector to invert
     * @return The inverted vector
     */
    public static Vector2D invert(final Vector2D v) {
        return new Vector2D(-v.x, -v.y);
    }

    /**
     * @return A vector with zero as coordinates
     */
    public static Vector2D zero() {
        return new Vector2D();
    }

    /**
     * @return A vector directed up.
     */
    public static Vector2D up() {
        return new Vector2D(0, -1);
    }

    /**
     * @return A vector directed down.
     */
    public static Vector2D down() {
        return new Vector2D(0, 1);
    }

    /**
     * @return A vector directed left.
     */
    public static Vector2D left() {
        return new Vector2D(-1, 0);
    }

    /**
     * @return A vector directed right.
     */
    public static Vector2D right() {
        return new Vector2D(1, 0);
    }
}
