package com.firesoul.pacman.impl.controller;

import java.awt.event.KeyEvent;
import java.io.PrintStream;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.entities.Pill;
import com.firesoul.pacman.impl.entities.Player;
import com.firesoul.pacman.impl.model.Room2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Window;

public class Pacman implements Game {

    // private static final String MAP_PATH = "/map/";
    // private static final String ENTITY_MAP_PATH = MAP_PATH + "entities.map";
    // private static final String BLOCK_MAP_PATH = MAP_PATH + "blocks.map";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 400;
    private static final int HEIGHT = WIDTH * 3 / 4;
    private static final int SCALE = 3;
    
    private static final PrintStream logger = System.out;
    private static InputController inputController = new InputController();
    private static Vector2D dimensions;

    private final Renderer renderer;
    private Room2D room;
    private State state;
    private int level;

    public Pacman() {
        this.renderer = new Window(TITLE, WIDTH, HEIGHT, SCALE);
        this.renderer.addInputController(Pacman.inputController);
        //Pacman.room = new Room2D(ENTITY_MAP_PATH, BLOCK_MAP_PATH);
        this.room = new Room2D(WIDTH, HEIGHT);
        this.room.addGameObject(new Player(Vector2D.zero(), new Vector2D(1, 1)));
        for (int i = 0; i < 10; i++) {
            this.room.addGameObject(new Pill(new Vector2D(100 + i * 16, 0)));
        }
        Pacman.dimensions = this.room.getDimensions();
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
        if (Pacman.inputController.isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            this.pause();
        }
        this.room.updateAll(deltaTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        if (Pacman.inputController.isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            this.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        this.renderer.draw(this.room.getGameObjects());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return this.state == State.RUNNING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPaused() {
        return this.state == State.PAUSED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOver() {
        return this.state == State.GAME_OVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Renderer getRenderer() {
        return this.renderer;
    }

    /**
     * @return the current game level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * @return the current game level room dimensions.
     */
    public static Vector2D getRoomDimensions() {
        return Pacman.dimensions;
    }

    /**
     * @return the key controller.
     */
    public static InputController getInputController() {
        return Pacman.inputController;
    }

    /**
     * Log a message to standard output.
     * @param log the message to log.
     */
    public static void log(final String log) {
        Pacman.logger.println(log);
    }
}
