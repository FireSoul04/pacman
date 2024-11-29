package com.firesoul.pacman.api.controller;

import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.util.TimerImpl;

public interface Game extends Runnable {

    static enum State {
        RUNNING,
        PAUSED,
        GAME_OVER
    }

    /**
     * Setup for the game.
     */
    void init();

    /**
     * Start the game.
     */
    void start();

    /**
     * Pause the game.
     */
    void pause();

    /**
     * Set game over flag.
     */
    void gameOver();

    /**
     * @return if the game is running.
     */
    boolean isRunning();

    /**
     * @return if the game is paused.
     */
    boolean isPaused();

    /**
     * @return if the game is over.
     */
    boolean isOver();

    /**
     * Main game loop.
     * @param deltaTime Time between each frame.
     */
    void update(double deltaTime);

    /**
     * What to do on pause.
     */
    void onPause();

    /**
     * What to do on render.
     */
    void render();

    /**
     * @return the window where the game is rendered.
     */
    Renderer getRenderer();

    /**
     * @return Get max updates per second.
     */
    default double getMaxUpdates() {
        return 60.0;
    }

    /**
     * Default game loop, runs at MAX_UPDATES per seconds and handles the rendering and let the game work on multiple platform at the same speed.
     * It stops whenever the game state is not RUNNING.
     */
    default void run() {
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
                System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
                timer.restart();
            }
        }
    }
}
