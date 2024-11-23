package com.firesoul.pacman.api;

import java.util.List;

import com.firesoul.pacman.impl.entities.Entity2D;

public interface Room {

    /**
     * Update all game objects in the room
     */
    void updateAll(double deltaTime);

    /**
     * Add a game object to the room
     * @param gameObject to add
     */
    void addGameObject(GameObject gameObject);

    /**
     * Remove a game object from the room
     * @param gameObject to remove
     */
    void removeGameObject(GameObject gameObject);

    /**
     * @return Get all game objects in the room
     */
    List<GameObject> getGameObjects();

    /**
     * @return Get all blocks in the room
     */
    List<Block> getBlocks();

    /**
     * @return Get all entities in the room
     */
    List<Entity2D> getEntities();
}
