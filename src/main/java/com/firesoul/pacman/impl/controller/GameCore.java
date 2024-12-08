package com.firesoul.pacman.impl.controller;

import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.List;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.model.entities.ghosts.*;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Window;

public class GameCore implements Game {

    private static final String TITLE = "Pacman";
    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    private static final String MAP_IMAGE_PATH = "src/main/resources/sprites/map/map.png";
    private static final int WIDTH = 229;
    private static final int HEIGHT = 289;
    private static final int SCALE = 3;
    
    private static final PrintStream logger = System.out;
    private static InputController inputController = new InputController();
    private static Scene2D scene;
    private static Vector2D dimensions;

    private final Pacman pacman = new Pacman(this);
    private final Thread displayThread = new Thread(this, "Display");
    private final Renderer renderer = new Window(TITLE, WIDTH, HEIGHT, SCALE);
    private State state;

    public GameCore() {
        this.renderer.addInputController(GameCore.inputController);
        this.resetScene();
    }

    @Override
    public synchronized void init() {
        this.start();
        this.pacman.init();
        this.renderer.init(MAP_IMAGE_PATH);
        this.displayThread.start();
    }

    @Override
    public void start() {
        this.state = State.RUNNING;
        GameCore.scene.wakeAll();
    }

    @Override
    public void pause() {
        this.state = State.PAUSED;
        GameCore.scene.pauseAll();
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
        if (GameCore.inputController.isKeyPressedOnce(key)) {
            this.pause();
        }
    }

    /**
     * Resets the current scene objects and load the new scene.
     */
    public void resetScene() {
        GameCore.scene = new Scene2D(GameCore.MAP_PATH);
        this.pacman.reset();
        GameCore.dimensions = GameCore.scene.getDimensions();
    }

    @Override
    public void onPause() {
        if (GameCore.inputController.isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            this.start();
        }
    }

    @Override
    public void render() {
        this.renderer.draw(GameCore.scene.getGameObjects());
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
        return GameCore.scene.getGameObjects();
    }

    /**
     * Add a gameObject to the scene.
     * @param g
     */
    public void addGameObject(final GameObject g) {
        GameCore.scene.addGameObject(g);
    }

    /**
     * Update all the gameObjects of the scene.
     * @param deltaTime
     */
    public void updateAll(final double deltaTime) {
        GameCore.scene.updateAll(deltaTime);
    }

    /**
     * @return the current game level scene dimensions.
     */
    public static Vector2D getSceneDimensions() {
        return GameCore.dimensions;
    }

    /**
     * @return the key controller.
     */
    public static boolean isKeyPressed(final int key) {
        return GameCore.inputController.isKeyPressed(key);
    }

    /**
     * @return the key controller.
     */
    public static boolean isKeyPressedOnce(final int key) {
        return GameCore.inputController.isKeyPressedOnce(key);
    }

    /**
     * Log a message to standard output.
     * @param log the message to log.
     */
    public static void log(final String log) {
        GameCore.logger.println(log);
    }
}
