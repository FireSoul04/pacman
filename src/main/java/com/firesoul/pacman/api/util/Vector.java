package com.firesoul.pacman.api.util;

import com.firesoul.pacman.impl.util.Vector2D;

public interface Vector {

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
     * Check if this vector intersects with another
     * @param v the vector to check
     * @return if the two vectors intersect
     */
    boolean intersect(Vector2D v);
}
