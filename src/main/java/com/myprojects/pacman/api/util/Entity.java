package com.myprojects.pacman.api.util;

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
