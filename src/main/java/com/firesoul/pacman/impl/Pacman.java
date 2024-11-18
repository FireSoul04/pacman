package com.firesoul.pacman.impl;

import java.io.PrintStream;

import com.firesoul.pacman.api.Game;
import com.firesoul.pacman.api.Renderer;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.util.TimerImpl;

public class Pacman implements Game {

    private static final String MAP_PATH = "/map/";
    private static final String ENTITY_MAP_PATH = MAP_PATH + "entities.map";
    private static final String BLOCK_MAP_PATH = MAP_PATH + "blocks.map";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 400;
    private static final int HEIGHT = WIDTH * 3 / 4;
    private static final int SCALE = 3;
    
    private static double MAX_UPDATES = 60.0;
    
    private PrintStream logger;
    private Renderer renderer;
    private State state;
    private int level;

    public Pacman() {
        this.logger = System.out;
        this.renderer = new Window("Pacman", WIDTH, HEIGHT, SCALE);
        this.renderer.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final Timer timer = new TimerImpl(Timer.secondsToMillis(1));
        final double ns = 1.0E9 / Pacman.MAX_UPDATES;
        double deltaTime = 0.0;
        int updates = 0;
        int frames = 0;
        this.init();
        timer.start();
        while (this.state == State.RUNNING) {
            final long now = System.nanoTime();
            deltaTime += (now - lastTime) / ns;
            lastTime = now;
            while (deltaTime >= 1) {
                this.update(deltaTime);
                updates++;
                deltaTime--;
            }
            this.render();
            frames++;
            timer.stopAtTimerEnd();
            if (timer.isStopped()) {
                timer.restart();
                logger.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.level = 1;
        this.state = State.RUNNING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        // TODO
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
    public void gameOver() {
        this.state = State.GAME_OVER;
    }

    /**
     * 
     */
    public Renderer getWindow() {
        return this.renderer;
    }
}
