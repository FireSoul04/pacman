package com.myprojects.pacman.impl;

import java.io.PrintStream;

import com.myprojects.pacman.api.Game;
import com.myprojects.pacman.api.util.Timer;
import com.myprojects.pacman.impl.util.TimerImpl;

public class Pacman implements Game {

    // private static int WIDTH = 300;
    // private static int HEIGHT = Pacman.WIDTH / 4 * 3;
    // private static int SCALE = 3;
    private static double MAX_UPDATES = 60.0;
    private PrintStream logger;
    private State state;
    private int level;

    public Pacman() {
        logger = System.out;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        Timer timer = new TimerImpl(1000);
        final double ns = 1.0E9 / Pacman.MAX_UPDATES;
        double deltaTime = 0.0;
        int updates = 0;
        int frames = 0;
        this.init();
        timer.start();
        while (this.state == State.RUNNING) {
            long now = System.nanoTime();
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
                timer = new TimerImpl(1000);
                timer.start();
                logger.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
    }

    @Override
    public void init() {
        this.level = 1;
        this.state = State.RUNNING;
    }

    @Override
    public void update(final double deltaTime) {
        // TODO
    }

    @Override
    public void render() {
        // TODO
    }

    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
    }
}
