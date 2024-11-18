package com.firesoul.pacman.api.entities;

import java.io.Serializable;

import com.firesoul.pacman.api.util.Vector;

public interface Entity extends Serializable {

    /**
     * @return Entity's current position
     */
    Vector getPosition();

    /**
     * @return Entity's speed
     */
    Vector getSpeed();
}
