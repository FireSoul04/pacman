package com.firesoul.pacman;

import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.model.Room2D;

class GameTest implements Game {
        
    private Room2D room = new Room2D();
    private State state;

    @Override
    public void init() {
        this.start();
    }

    @Override
    public void start() {
        this.state = State.RUNNING;
    }

    @Override
    public void pause() {
        this.state = State.PAUSED;
    }

    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
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
        room.getEntities().forEach(g -> {
            if (g instanceof Movable) {
                ((Movable)g).update(deltaTime);
            }
        });
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