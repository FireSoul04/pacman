package com.firesoul.pacman;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.impl.controller.PacmanCore;

public class Start {

    public static void main(final String[] args) {
        final Game pacman = new PacmanCore();
        pacman.init();
    }
}
