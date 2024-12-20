package com.firesoul.pacman.api.model.entities;

import java.io.Serializable;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.impl.util.Vector2D;

public interface ColliderLayout extends Serializable {

    /**
     * Calculate the position of the layout relative to the game object position
     * @param gameObject
     * @param size of the collider
     * @return the new position
     */
    Vector2D positionRelativeTo(GameObject gameObject, Vector2D size);
}
