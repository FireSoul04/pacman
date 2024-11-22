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
     * Setup for the game
     */
    void init();

    /**
     * Start the game
     */
    void start();

    /**
     * Pause the game
     */
    void pause();

    /**
     * Set game over flag
     */
    void gameOver();

    /**
     * Main game loop
     * @param deltaTime
     */
    void update(double deltaTime);

    /**
     * What to do on render
     */
    void render();

    /**
     * 
     * @return
     */
    State getState();

    /**
     * 
     */
    default void log(final String log) {
        System.out.println(log);
    }

    /**
     * 
     * @return
     */
    default double getMaxUpdates() {
        return 60.0;
    }

    /**
     * Setup for the game
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
