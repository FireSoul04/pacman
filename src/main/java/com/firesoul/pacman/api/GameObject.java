package com.firesoul.pacman.api;

import java.io.Serializable;

import com.firesoul.pacman.api.view.Drawable;
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
     * Pause all the timers attached to this game object.
     */
    void pause();

    /**
     * Wake up all the timers attached to this game object.
     */
    void wake();

    /**
     * Disable the game object.
     */
    void disable();

    /**
     * Enable the game object.
     */
    void enable();

    /**
     * @return Game object's activity.
     */
    boolean isActive();

    /**
     * @return Game object's visibility.
     */
    boolean isVisible();
}
