package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2;

public class Player extends Entity2D implements Movable, Collidable {

    private enum State {
        IDLE,
        MOVING,
        DEAD
    }

    private static final long MAX_EATING_TIME = 5000;
    private static final int MAX_LIVES = 3;

    private State state;
    private Timer eatTimer;
    private int lives;
    private boolean canEat;

    public Player(final Vector2 position, final Vector2 speed) {
        super(position, speed);
        state = State.IDLE;
        eatTimer = new TimerImpl(MAX_EATING_TIME);
        lives = MAX_LIVES;
        canEat = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isColliding(final Collidable other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isColliding'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onCollide'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move(final double deltaTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    /**
     * @return If pacman can eat ghosts
     */
    public boolean canEat() {
        return this.canEat;
    }

    /**
     * Start the timer for pacman to eat ghosts
     */
    public void startEating() {
        this.eatTimer.start();
        this.canEat = true;
    }
}
