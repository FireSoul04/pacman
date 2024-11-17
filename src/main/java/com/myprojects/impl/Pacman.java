package com.myprojects.impl;

import com.myprojects.api.Game;

public class Pacman implements Game {

    private State state;
    private int level;

    public Pacman() {
        this.level = 1;
        this.state = State.RUNNING;
    }

    public void start() {

    }

    public void loop(final double deltaTime) {

    }

    public void gameOver() {
        this.state = State.GAME_OVER;
    }
}
