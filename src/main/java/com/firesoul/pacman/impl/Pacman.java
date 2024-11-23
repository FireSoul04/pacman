package com.firesoul.pacman.impl;

import java.io.PrintStream;

import com.firesoul.pacman.api.Game;
import com.firesoul.pacman.api.Renderer;
import com.firesoul.pacman.api.Room;
import com.firesoul.pacman.impl.entities.Room2D;

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
    private Room room;
    private State state;
    private int level;

    public Pacman() {
        this.logger = System.out;
        this.renderer = new Window(TITLE, WIDTH, HEIGHT, SCALE);
        this.room = new Room2D(ENTITY_MAP_PATH, BLOCK_MAP_PATH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.level = 1;
        this.start();
        this.renderer.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.state = State.RUNNING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        this.state = State.PAUSED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        this.room.updateAll(deltaTime);
        // this.room.getGameObjects().forEach(e -> {
        //     e.get
        // });
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
     * 
     */
    public Renderer getWindow() {
        return this.renderer;
    }
}
