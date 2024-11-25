package com.firesoul.pacman.api.model;

import java.util.List;

import com.firesoul.pacman.api.GameObject;

public interface Room {

    /**
     * Update all game objects in the room.
     */
    void updateAll(double deltaTime);

    /**
     * Pause all game objects in the room.
     */
    void pauseAll();

    /**
     * Wake up all game objects in the room.
     */
    void wakeAll();

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
