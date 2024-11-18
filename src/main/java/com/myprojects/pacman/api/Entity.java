package com.myprojects.pacman.api;

import com.myprojects.pacman.api.util.Vector;

public interface Entity {

    /**
     * @return Entity's current position
     */
    Vector getPosition();

    /**
     * @return Entity's speed
     */
    Vector getSpeed();
}
