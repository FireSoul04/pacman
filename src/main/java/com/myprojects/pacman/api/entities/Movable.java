package com.myprojects.pacman.api.entities;

public interface Movable {

    /**
     * Move the object attached
     * @param deltaTime
     */
    void move(final double deltaTime);
}
