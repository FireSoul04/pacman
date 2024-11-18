package com.myprojects.pacman.impl;

import com.myprojects.pacman.api.Collidable;
import com.myprojects.pacman.api.Movable;

public class Player extends Entity implements Movable, Collidable {

    private enum State {
        IDLE,
        MOVING,
        DEAD
    }

    private static final long MAX_EATING_TIME = 5000;

    private State state;
    private int lives;
    private boolean canEat;
    private long eatTime;

    @Override
    public boolean isColliding(final Collidable other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isColliding'");
    }

    @Override
    public void onCollide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onCollide'");
    }

    @Override
    public void move(final double deltaTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    public boolean canEat() {
        return this.canEat;
    }

    public void startEating() {
        this.canEat = true;
        this.eatTime = System.currentTimeMillis();
    }

    // To implement on Timer
    // private void stopEatingAtTimerEnd() {
    //     if (System.currentTimeMillis() - this.eatTime >= Player.MAX_EATING_TIME) {
    //         this.canEat = false;
    //     }
    // }

}
