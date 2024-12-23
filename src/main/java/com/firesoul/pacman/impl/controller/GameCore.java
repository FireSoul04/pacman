package com.firesoul.pacman.impl.controller;

import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.List;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.view.Window;

public class GameCore implements Game {

    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    private static final String MAP_IMAGE_PATH = "src/main/resources/sprites/map/map.png";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 224;
    private static final int HEIGHT = 288;
    private static final double SCALE = 3;
    private static final PrintStream logger = System.out;

    private final Thread displayThread = new Thread(this, "Display");
    private final Renderer renderer = new Window(TITLE, WIDTH, HEIGHT, SCALE, SCALE);
    private final InputController inputController = new InputController();
    private final Pacman pacman;
    private Scene2D scene;
    private State state;

    public GameCore() {
        this.renderer.addInputController(this.inputController);
        this.pacman = new Pacman(this);
    }

    @Override
    public synchronized void init() {
        this.scene = new Scene2D(MAP_PATH);
        //this.scene = new Scene2D(WIDTH, HEIGHT);
        this.pacman.init();
        this.renderer.init(MAP_IMAGE_PATH);
        this.displayThread.start();
        this.start();
    }

    @Override
    public void start() {
        this.state = State.RUNNING;
        this.scene.wakeAll();
    }

    @Override
    public void pause() {
        this.state = State.PAUSED;
        this.scene.pauseAll();
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
        if (this.inputController.isKeyPressedOnce(key)) {
            this.pause();
        }
    }

    /**
     * Resets the current scene objects and load the new scene.
     */
    public void resetScene() {
        this.scene = new Scene2D(MAP_PATH);
        this.pacman.reset();
    }

    @Override
    public void onPause() {
        if (this.inputController.isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            this.start();
        }
    }

    @Override
    public void render() {
        this.renderer.draw(this.scene.getGameObjects());
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
     * @return All the gameObjects in the current scene.
     */
    public List<GameObject> getGameObjects() {
        return this.scene.getGameObjects();
    }

    /**
     * Add a gameObject to the scene.
     * @param gameObject
     */
    public void addGameObject(final GameObject gameObject) {
        this.scene.addGameObject(gameObject);
    }

    /**
     * Update all the gameObjects of the scene.
     * @param deltaTime
     */
    public void updateAll(final double deltaTime) {
        this.scene.updateAll(deltaTime);
    }

    /**
     * @return the current scene.
     */
    public Scene2D getScene() {
        return this.scene;
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
