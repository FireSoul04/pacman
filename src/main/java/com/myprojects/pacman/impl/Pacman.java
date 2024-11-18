package com.myprojects.pacman.impl;

import com.myprojects.pacman.api.Game;

public class Pacman implements Game {

    private State state;
    private int level;

    public Pacman() {
        this.level = 1;
        this.state = State.RUNNING;
    }

    public void start() {
        // TODO
    }

    public void loop(final double deltaTime) {
        // TODO
    }

    public void gameOver() {
        this.state = State.GAME_OVER;
    }
}
