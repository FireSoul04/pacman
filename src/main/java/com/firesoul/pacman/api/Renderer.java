package com.firesoul.pacman.api;

import java.awt.Color;

public interface Renderer {

    /**
     * Setup the game screen
     */
    void init();

    /**
     * Draw the game to the screen
     */
    void draw();

    /**
     * Clears game's screen
     */
    void clear();

    /**
     * Set render color
     * @param color
     */
    void setColor(Color color);

    /**
     * Resize the window
     * @param width
     * @param height
     */
    void resize(int width, int height);

    /**
     * Set new scale factor
     * @param scale
     */
    void setScale(int scale);

    /**
     * @return Screen width
     */
    int getWidth();

    /**
     * @return Screen height
     */
    int getHeight();
}
