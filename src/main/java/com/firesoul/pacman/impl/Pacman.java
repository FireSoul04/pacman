package com.firesoul.pacman.impl;

import java.io.PrintStream;

import com.firesoul.pacman.api.Game;
import com.firesoul.pacman.api.Renderer;

public class Pacman implements Game {

    private static final String MAP_PATH = "/map/";
    private static final String ENTITY_MAP_PATH = MAP_PATH + "entities.map";
    private static final String BLOCK_MAP_PATH = MAP_PATH + "blocks.map";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 400;
    private static final int HEIGHT = WIDTH * 3 / 4;
    private static final int SCALE = 3;
    
    private PrintStream logger;
    private Renderer renderer;
    private State state;
    private int level;

    public Pacman() {
        this.logger = System.out;
        this.renderer = new Window("Pacman", WIDTH, HEIGHT, SCALE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.level = 1;
        this.state = State.RUNNING;
        this.renderer.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        // TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        this.renderer.draw();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
    }

    /**
     * 
     */
    public Renderer getWindow() {
        return this.renderer;
    }
}
