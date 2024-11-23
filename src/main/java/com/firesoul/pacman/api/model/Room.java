package com.firesoul.pacman.api.model;

import java.util.List;

import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.impl.controller.InputController;

public interface Room {

    /**
     * Update all game objects in the room.
     */
    void updateAll(InputController inputController, double deltaTime);

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
