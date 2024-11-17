package com.myprojects.api;

public interface Game {

    static enum State {
        RUNNING,
        STOPPED,
        GAME_OVER
    }

    void start();

    void loop(final double deltaTime);

    void gameOver();
}
