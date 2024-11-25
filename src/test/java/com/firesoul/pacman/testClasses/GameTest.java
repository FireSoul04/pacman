package com.firesoul.pacman.testClasses;

import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.model.Room2D;

public class GameTest implements Game {
    
    private Runnable consumer;
    private Room2D room = new Room2D();
    private State state;

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

    @Override
    public void start() {
        this.state = State.RUNNING;
        System.out.println("Game started");
    }

    @Override
    public void pause() {
        this.state = State.PAUSED;
        System.out.println("Game paused");
    }

    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
        System.out.println("Game over");
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