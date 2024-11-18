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
    void start();

    /**
     * Main game loop
     * @param deltaTime
     */
    void loop(final double deltaTime);

    /**
     * Set game over flag
     */
    void gameOver();
}
