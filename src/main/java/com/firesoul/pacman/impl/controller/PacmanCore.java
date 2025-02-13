package com.firesoul.pacman.impl.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintStream;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.api.view.Renderer.UIType;
import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.view.Window;

public class PacmanCore implements Game {

    private enum GameState {
        RUNNING,
        PAUSED,
        GAME_OVER
    }

    private static final String MAP_IMAGE_PATH = "src/main/resources/sprites/map/map.png";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 224;
    private static final int HEIGHT = 288;
    private static final PrintStream LOGGER = System.out;

    private final Thread logicThread = new Thread(this, "Game");
    private final InputController inputController = new InputController();
    private final Renderer renderer;
    private final Pacman pacman;
    private GameState state;

    public PacmanCore() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final double scaleX = screenSize.getWidth() / 10000 * 9;
        final double scaleY = screenSize.getHeight() / 10000 * 16;
        this.pacman = new Pacman(this);
        this.renderer = new Window(TITLE, WIDTH, HEIGHT, scaleX, scaleY);
        this.renderer.addInputController(this.inputController);
    }

    @Override
    public void init() {
        this.renderer.init(MAP_IMAGE_PATH);
        this.pacman.init();
        this.render();
        this.logicThread.start();
        this.start();
    }

    /**
     * Game loop logic, runs at MAX_UPDATES per seconds and handles the rendering and let the game work on multiple platform at the same speed.
     * It stops whenever the game state is not RUNNING.
     */
    @Override
    public void run() {
        final Timer timer = new TimerImpl(Timer.secondsToMillis(1));
        final double ns = 1.0E9 / this.getMaxUpdates();
        double deltaTime = 0.0;
        int updates = 0;
        int frames = 0;
        long lastTime = System.nanoTime();
        timer.start();
        while (!this.isOver()) {
            try {
                Thread.sleep(frames - (long)(frames - 100 / 60));
            } catch (InterruptedException e) {}
            final long now = System.nanoTime();
            deltaTime = deltaTime + ((now - lastTime) / ns);
            lastTime = now;
            while (deltaTime >= 1.0) {
                if (this.isRunning()) {
                    this.update(deltaTime);
                } else {
                    this.onPause();
                }
                updates++;
                deltaTime--;
            }
            this.render();
            frames++;
            timer.update();
            if (timer.isExpired()) {
                this.renderer.setTitle(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
                timer.startAgain();
            }
        }
    }

    @Override
    public void start() {
        this.state = GameState.RUNNING;
        this.pacman.wakeAll();
    }

    @Override
    public void pause() {
        this.state = GameState.PAUSED;
        this.pacman.pauseAll();
    }

    @Override
    public void gameOver() {
        this.state = GameState.GAME_OVER;
    }

    @Override
    public void update(final double deltaTime) {
        this.pacman.update(deltaTime);
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
        this.renderer.startDraw();
        this.renderer.draw(this.pacman.getGameObjects());
        this.renderer.drawText(UIType.LEVEL, "Level: " + Integer.toString(this.pacman.getLevel()));
        this.renderer.drawText(UIType.LIVES, "Lives: " + Integer.toString(this.pacman.getLives()));
        this.renderer.drawText(UIType.SCORE, "Score: " + Integer.toString(this.pacman.getScore()));
        this.renderer.endDraw();
    }

    @Override
    public boolean isRunning() {
        return this.state == GameState.RUNNING;
    }

    @Override
    public boolean isPaused() {
        return this.state == GameState.PAUSED;
    }

    @Override
    public boolean isOver() {
        return this.state == GameState.GAME_OVER;
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
        PacmanCore.LOGGER.println(message);
    }
}
