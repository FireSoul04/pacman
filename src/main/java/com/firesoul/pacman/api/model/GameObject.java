package com.firesoul.pacman.api.model;

import java.io.Serializable;

import com.firesoul.pacman.api.view.Drawable;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.util.Vector2D;

public interface GameObject extends Serializable {

    /**
     * @return Game object's current position
     */
    Vector2D getPosition();

    /**
     * @return Scene where the game object is
     */
    Scene2D getScene();

    /**
     * @return Game object's drawable representation
     */
    Drawable getDrawable();

    /**
     * @param scene where to move the game object
     */
    void setScene(Scene2D scene);

    /**
     * Pause all the timers attached to this game object.
     */
    void pause();

    /**
     * Wake up all the timers attached to this game object.
     */
    void wake();

    /**
     * Reset the game object
     */
    void reset();

    /**
     * Disable the game object.
     */
    void disable();

    /**
     * Enable the game object.
     */
    void enable();

    /**
     * @return Game object's activity
     */
    boolean isActive();

    /**
     * @return Game object's visibility
     */
    boolean isVisible();
}
