package com.firesoul.pacman.impl;

import java.io.PrintStream;

import com.firesoul.pacman.api.Game;
import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.Renderer;
import com.firesoul.pacman.impl.entities.Player;
import com.firesoul.pacman.impl.entities.Room2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class Pacman implements Game {

    // private static final String MAP_PATH = "/map/";
    // private static final String ENTITY_MAP_PATH = MAP_PATH + "entities.map";
    // private static final String BLOCK_MAP_PATH = MAP_PATH + "blocks.map";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 400;
    private static final int HEIGHT = WIDTH * 3 / 4;
    private static final int SCALE = 3;
    
    private static Room2D room;

    private PrintStream logger;
    private Renderer renderer;
    private State state;
    private int level;

    public Pacman() {
        this.logger = System.out;
        this.renderer = new Window(TITLE, WIDTH, HEIGHT, SCALE);
        //Pacman.room = new Room2D(ENTITY_MAP_PATH, BLOCK_MAP_PATH);
        Pacman.room = new Room2D(WIDTH, HEIGHT);
        Pacman.room.addGameObject(new Player(new Vector2D(WIDTH * 2 / 3, 0), new Vector2D(0.5, 0)));
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
        Pacman.room.updateAll(deltaTime);
        for (final GameObject g : Pacman.room.getGameObjects()) {
            this.renderer.load(g);
        }
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
     * @return the window where the game is rendered.
     */
    public Renderer getWindow() {
        return this.renderer;
    }

    /**
     * @return the current game level room.
     */
    public static Room2D getRoom() {
        return Pacman.room;
    }

    /**
     * @return the current game level room dimensions.
     */
    public static Vector2D getRoomDimensions() {
        return Pacman.room.getDimensions();
    }
}
