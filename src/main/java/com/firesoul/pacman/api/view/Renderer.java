package com.firesoul.pacman.api.view;

import java.awt.Color;
import java.util.List;

import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.impl.controller.InputController;

public interface Renderer {

    /**
     * Setup the game screen.
     */
    void init();

    /**
     * Draw the game to the screen.
     */
    void draw();

    /**
     * Clears game's screen.
     */
    void clear();

    /**
     * Load game objects to render them to the screen.
     * @param gameObjects
     */
    void load(List<GameObject> gameObjects);

    /**
     * Set render color.
     * @param color
     */
    void setColor(Color color);

    /**
     * Resize the window.
     * @param width
     * @param height
     */
    void resize(int width, int height);

    /**
     * Set new scale factor.
     * @param scale
     */
    void setScale(int scale);

    /**
     * Add input controller to the game.
     * @param inputController Input controller.
     */
    void addInputController(InputController inputController);

    /**
     * @return Screen width.
     */
    int getWidth();

    /**
     * @return Screen height.
     */
    int getHeight();
}
