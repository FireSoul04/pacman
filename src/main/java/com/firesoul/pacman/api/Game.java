package com.firesoul.pacman.api;

import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.util.TimerImpl;

public interface Game {

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
     * Main game loop.
     * @param deltaTime Time between each frame.
     */
    void update(double deltaTime);

    /**
     * What to do on render.
     */
    void render();

    /**
     * @return Get current game state, that can be RUNNING, PAUSED or GAME_OVER.
     */
    State getState();

    /**
     * Show a log message in standard output.
     * @param log Message to show.
     */
    default void log(final String log) {
        System.out.println(log);
    }

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
        long lastTime = System.nanoTime();
        double deltaTime = 0.0;
        int updates = 0;
        int frames = 0;
        this.init();
        timer.start();
        while (this.getState() == State.RUNNING) {
            final long now = System.nanoTime();
            deltaTime += (now - lastTime) / ns;
            lastTime = now;
            while (deltaTime >= 1.0) {
                this.update(deltaTime);
                updates++;
                deltaTime--;
            }
            this.render();
            frames++;
            if (timer.isStopped()) {
                timer.restart();
                this.log(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            timer.stopAtTimerEnd();
        }
    }
}
