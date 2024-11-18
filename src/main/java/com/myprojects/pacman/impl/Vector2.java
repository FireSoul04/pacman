package com.myprojects.pacman.impl;

import com.myprojects.pacman.api.Vector;

public class Vector2 implements Vector {

    private double x;
    private double y;

    /**
     * Default vector constructor
     * @param x coordinate
     * @param y coordinate
     */
    public Vector2(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a vector with both coordinates at 0
     */
    public Vector2() {
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
    public Vector2 add(final Vector2 v) {
        return Vector2.add(this, v);
    }
    
    /**
     * {@inheritDoc}
     */
    public Vector2 sub(final Vector2 v) {
        return Vector2.sub(this, v);
    }
    
    /**
     * {@inheritDoc}
     */
    public Vector2 invert() {
        return Vector2.invert(this);
    }

    /**
     * Add two vectors
     * @param v1
     * @param v2
     * @return The sum of the two vectors
     */
    public static Vector2 add(final Vector2 v1, final Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * Subtract two vectors
     * @param v1
     * @param v2
     * @return The subtraction of the two vectors
     */
    public static Vector2 sub(final Vector2 v1, final Vector2 v2) {
        return add(v1, v2.invert());
    }

    /**
     * Invert the sign of the vector's coordinates
     * @param v the vector to invert
     * @return The inverted vector
     */
    public static Vector2 invert(final Vector2 v) {
        return new Vector2(-v.x, -v.y);
    }

    /**
     * @return A vector with zero as coordinates
     */
    public static Vector2 zero() {
        return new Vector2();
    }
}
