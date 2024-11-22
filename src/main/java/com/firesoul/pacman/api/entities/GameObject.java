package com.firesoul.pacman.api.entities;

import java.io.Serializable;

import com.firesoul.pacman.impl.util.Vector2D;

public interface GameObject extends Serializable {

    /**
     * @return Entity's current position
     */
    Vector2D getPosition();

    /**
     * @return Entity's speed
     */
    Vector2D getSpeed();
}
