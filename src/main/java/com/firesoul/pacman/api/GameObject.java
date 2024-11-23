package com.firesoul.pacman.api;

import java.io.Serializable;

import com.firesoul.pacman.api.entities.Drawable;
import com.firesoul.pacman.impl.util.Vector2D;

public interface GameObject extends Serializable {

    /**
     * @return Game object's current position
     */
    Vector2D getPosition();

    /**
     * @return Game object's speed
     */
    Vector2D getSpeed();

    /**
     * @return Game object's drawable representation.
     */
    Drawable getDrawable();

    /**
     * @return Game object's visibility.
     */
    boolean isVisible();
}
