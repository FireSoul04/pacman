package com.myprojects.pacman.api;

public interface Game {

    static enum State {
        RUNNING,
        PAUSED,
        GAME_OVER
    }

    /**
     * Setup for the game
     */
    void run();

    /**
     * Setup for the game
     */
    void init();

    /**
     * Main game loop
     * @param deltaTime
     */
    void update(final double deltaTime);

    /**
     * What to do on render
     */
    void render();

    /**
     * Set game over flag
     */
    void gameOver();
}