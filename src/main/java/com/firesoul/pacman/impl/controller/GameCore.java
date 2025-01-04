package com.firesoul.pacman.impl.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintStream;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.view.Window;

public class GameCore implements Game {

    private static final String MAP_IMAGE_PATH = "src/main/resources/sprites/map/map.png";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 224;
    private static final int HEIGHT = 288;
    private static final PrintStream logger = System.out;

    private final Thread displayThread = new Thread(this, "Display");
    private final Renderer renderer;
    private final InputController inputController = new InputController();
    private final Pacman pacman;
    private State state;

    public GameCore() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final double scaleX = screenSize.getWidth() / 10000 * 9;
        final double scaleY = screenSize.getHeight() / 10000 * 16;
        this.renderer = new Window(TITLE, WIDTH, HEIGHT, scaleX, scaleY);
        this.renderer.addInputController(this.inputController);
        this.pacman = new Pacman(this);
    }

    @Override
    public synchronized void init() {
        this.pacman.init();
        this.renderer.init(MAP_IMAGE_PATH);
        this.displayThread.start();
        this.start();
    }

    @Override
    public void start() {
        this.state = State.RUNNING;
        this.pacman.wakeAll();
    }

    @Override
    public void pause() {
        this.state = State.PAUSED;
        this.pacman.pauseAll();
    }

    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
    }

    @Override
    public void update(final double deltaTime) {
        pacman.update(deltaTime);
    }

    /**
     * Pause when the specific key is pressed.
     * @param key
     */
    public void pauseOnKeyPressed(final int key) {
        if (this.inputController.getEvent("PauseGame")) {
            this.pause();
        }
    }

    @Override
    public void onPause() {
        if (this.inputController.getEvent("PauseGame")) {
            this.start();
        }
    }

    @Override
    public void render() {
        this.renderer.draw(this.pacman.getGameObjects());
    }

    @Override
    public boolean isRunning() {
        return this.state == State.RUNNING;
    }

    @Override
    public boolean isPaused() {
        return this.state == State.PAUSED;
    }

    @Override
    public boolean isOver() {
        return this.state == State.GAME_OVER;
    }

    @Override
    public Renderer getRenderer() {
        return this.renderer;
    }

    /**
     * @return the input handler.
     */
    public InputController getInputController() {
        return this.inputController;
    }

    /**
     * Log a message to standard output.
     * @param message
     */
    public static void log(final String message) {
        GameCore.logger.println(message);
    }
}
