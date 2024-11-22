package com.firesoul.pacman.impl.util;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D add(final Vector2D v) {
        return Vector2D.add(this, v);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D sub(final Vector2D v) {
        return Vector2D.sub(this, v);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D invert() {
        return Vector2D.invert(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D dot(final double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean intersect(final Vector2D v) {
        return this.x <= v.x + Vector.EPSILON && this.x >= v.x - Vector.EPSILON &&
               this.y <= v.y + Vector.EPSILON && this.y >= v.y - Vector.EPSILON;
    }

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
}
