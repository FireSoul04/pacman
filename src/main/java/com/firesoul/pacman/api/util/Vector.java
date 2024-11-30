package com.firesoul.pacman.api.util;

import java.io.Serializable;

import com.firesoul.pacman.impl.util.Vector2D;

public interface Vector extends Serializable {

    static double EPSILON = 0.1;

    /**
     * Add this vector to another
     * @param v the vector to sum
     * @return The sum of the two vectors
     */
    Vector2D add(Vector2D v);

    /**
     * Subtract this vector to another
     * @param v the vector to subtract
     * @return The subtraction of the two vectors
     */
    Vector2D sub(Vector2D v);

    /**
     * Invert the sign of the vector's coordinates
     * @return The inverted vector
     */
    Vector2D invert();

    /**
     * Multiply this vector by a scalar
     * @param scalar the scalar to multiply
     * @return The product of the vector and the scalar
     */
    Vector2D dot(double scalar);

    /**
     * Check if this vector intersects with another
     * @param v the vector to check
     * @return if the two vectors intersect
     */
    boolean intersect(Vector2D v);

    /**
     * Clamp the vector to a range
     * @param min the minimum value of the magnitude
     * @param max the maximum value of the magnitude
     * @return the magnitude of the vector
     */
    Vector2D clamp(Vector2D min, Vector2D max);

    /**
     * Wrap the vector to a range
     * @param min the minimum value of the magnitude
     * @param max the maximum value of the magnitude
     * @return the magnitude of the vector
     */
    Vector2D wrap(Vector2D min, Vector2D max);
}
