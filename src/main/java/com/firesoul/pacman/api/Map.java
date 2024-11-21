package com.firesoul.pacman.api;

import java.io.IOException;
import java.util.List;

import com.firesoul.pacman.api.entities.Entity;
import com.firesoul.pacman.api.entities.GameObject;

public interface Map {

    /**
     * Read a file that contains the informations of the entities in map
     * @return Map's entities and their positions
     */
    List<Entity> getEntityMap() throws IOException, ClassNotFoundException;

    /**
     * Read a file that contains the informations of all blocks of the map
     * @return Map of blocks
     */
    List<Block> getBlockMap() throws IOException, ClassNotFoundException;

    /**
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    List<GameObject> getGameObjects() throws IOException, ClassNotFoundException;
}
