package com.firesoul.pacman.api.controller;

import com.firesoul.pacman.api.view.Renderer;

public interface Game extends Runnable {

    /**
     * Setup for the game.
     */
    void init();

    /**
     * Start the game.
     */
    void start();

    /**
     * Pause the game.
     */
    void pause();

    /**
     * Set game over flag.
     */
    void gameOver();

    /**
     * @return if the game is running.
     */
    boolean isRunning();

    /**
     * @return if the game is paused.
     */
    boolean isPaused();

    /**
     * @return if the game is over.
     */
    boolean isOver();

    /**
     * Main game loop.
     * @param deltaTime Time between each frame
     */
    void update(double deltaTime);

    /**
     * What to do on pause.
     */
    void onPause();

    /**
     * What to do on render.
     */
    void render();

    /**
     * @return the window where the game is rendered.
     */
    Renderer getRenderer();

    /**
     * Game loop logic.
     */
    void run();

    /**
     * @return Get max updates per second
     */
    default double getMaxUpdates() {
        return 60.0;
    }
}
