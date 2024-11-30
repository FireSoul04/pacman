package com.firesoul.pacman.api.model;

import java.util.List;

import com.firesoul.pacman.api.util.Vector;

public interface Map {

    /**
     * @return Get all game objects in the map.
     */
    List<GameObject> getGameObjects();

    /**
     * @return Get the dimensions of the map.
     */
    Vector getDimensions();
}
