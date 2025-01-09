package com.firesoul.pacman.api.view;

import java.awt.Color;
import java.util.List;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.impl.controller.InputController;
import com.firesoul.pacman.impl.util.Vector2D;

public interface Renderer {

    /**
     * Setup the game screen.
     */
    void init();

    /**
     * Setup the game screen.
     * @param backgroundImagePath
     */
    void init(String backgroundImagePath);

    /**
     * Draw the game to the screen.
     * @param gameObjects List of game objects to draw.
     */
    void draw(List<GameObject> gameObjects);

    /**
     * Clears game's screen.
     */
    void clear();

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
     * Resize the window.
     * @param dimensions
     */
    void resize(Vector2D dimensions);

    /**
     * Set new scale factor for x axis.
     * @param scaleX
     */
    void setScaleX(double scaleX);

    /**
     * Set new scale factor for y axis.
     * @param scaleY
     */
    void setScaleY(double scaleY);

    /**
     * Set the title of the window.
     * @param title
     */
    void setTitle(String title);

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
