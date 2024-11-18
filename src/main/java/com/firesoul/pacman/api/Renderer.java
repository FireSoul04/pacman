package com.firesoul.pacman.api;

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
     * Resize the window
     * @param width
     * @param height
     */
    void resize(final int width, final int height);

    /**
     * Set new scale factor
     * @param scale
     */
    void setScale(final int scale);

    /**
     * @return Screen width
     */
    int getWidth();

    /**
     * @return Screen height
     */
    int getHeight();
}
