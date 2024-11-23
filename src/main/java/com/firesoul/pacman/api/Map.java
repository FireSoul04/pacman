package com.firesoul.pacman.api;

import java.util.List;

import com.firesoul.pacman.api.entities.Entity;
import com.firesoul.pacman.api.util.Vector;

public interface Map {

    /**
     * Read a file that contains the informations of the entities in map.
     * @return Map's entities and their positions.
     */
    List<Entity> getEntityMap();

    /**
     * Read a file that contains the informations of all blocks of the map.
     * @return Map of blocks.
     */
    List<Block> getBlockMap();

    /**
     * @return Get all game objects in the map.
     */
    List<GameObject> getGameObjects();

    /**
     * @return Get the dimensions of the map.
     */
    Vector getDimensions();
}
