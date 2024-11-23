package com.firesoul.pacman.api;

import java.util.List;

public interface Room {

    /**
     * Update all game objects in the room.
     */
    void updateAll(double deltaTime);

    /**
     * Add a game object to the room.
     * @param gameObject to add.
     */
    void addGameObject(GameObject gameObject);

    /**
     * Remove a game object from the room.
     * @param gameObject to remove.
     */
    void removeGameObject(GameObject gameObject);

    /**
     * @return Get all game objects in the room.
     */
    List<GameObject> getGameObjects();
}
