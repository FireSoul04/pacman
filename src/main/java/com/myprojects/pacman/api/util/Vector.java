package com.myprojects.pacman.api.util;

import com.myprojects.pacman.impl.util.Vector2;

public interface Vector {

    /**
     * Add this vector to another
     * @param v the vector to sum
     * @return The sum of the two vectors
     */
    Vector2 add(final Vector2 v);

    /**
     * Subtract this vector to another
     * @param v the vector to subtract
     * @return The subtraction of the two vectors
     */
    Vector2 sub(final Vector2 v);

    /**
     * Invert the sign of the vector's coordinates
     * @return The inverted vector
     */
    Vector2 invert();
}
