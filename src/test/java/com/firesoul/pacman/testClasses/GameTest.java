package com.firesoul.pacman.testClasses;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.controller.GameCore;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.util.TimerImpl;

public class GameTest implements Game {
    
    private enum GameState {
        RUNNING,
        PAUSED,
        GAME_OVER
    }

    private Runnable consumer;
    private Scene2D room = new Scene2D();
    private GameState state;

    public GameTest() {
        this.consumer = () -> {};
    }

    public GameTest(final Runnable consumer) {
        this.consumer = consumer;
    }

    @Override
    public void init() {
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
                GameCore.log(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
                timer.restart();
            }
        }
    }

    @Override
    public void start() {
        this.state = GameState.RUNNING;
        System.out.println("Game started");
    }

    @Override
    public void pause() {
        this.state = GameState.PAUSED;
        System.out.println("Game paused");
    }

    @Override
    public void gameOver() {
        this.state = GameState.GAME_OVER;
        System.out.println("Game over");
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
    public void update(final double deltaTime) {
        room.getGameObjects().forEach(g -> {
            if (g instanceof Movable) {
                ((Movable)g).update(deltaTime);
            }
        });
        consumer.run();
    }

    @Override
    public void onPause() {
        consumer.run();
    }

    @Override
    public void render() {
    
    }

    @Override
    public Renderer getRenderer() {
        return null;
    }

    public void addGameObject(final GameObject g) {
        room.addGameObject(g);
    }
};