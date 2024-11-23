package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.entities.bases.Animation2D;
import com.firesoul.pacman.impl.entities.bases.GameObject2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
// import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;

public class Player extends GameObject2D implements Movable, Collidable {

    // private enum State {
    //     IDLE,
    //     MOVING,
    //     DEAD
    // }

    private static final long serialVersionUID = 1L;
    // private static final long MAX_EATING_TIME = Timer.secondsToMillis(5);
    // private static final int MAX_LIVES = 3;
    private static final Vector2D SIZE = new Vector2D(16, 16);

    private final Collider collider;
    // private final Timer eatTimer;
    // private State state;
    // private int lives;
    // private boolean canEat;

    public Player(final Vector2D position, final Vector2D speed) {
        super(position, speed, new Animation2D("pacman", Timer.secondsToMillis(0.2)));
        // this.state = State.IDLE;
        // this.lives = MAX_LIVES;
        // this.canEat = false;
        // this.eatTimer = new TimerImpl(MAX_EATING_TIME);
        this.collider = new BoxCollider2D(this, Player.SIZE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide(final Collidable other) {
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        // TODO

        //TESTING PURPOSE
        this.setPosition(this.getPosition()
            .add(this.getSpeed().dot(deltaTime))
            .wrap(this.getDrawable().getImageSize().invert(), Pacman.getRoomDimensions())
        );
        ((Animation2D)this.getDrawable()).update();
        //
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collider getCollider() {
        return this.collider;
    }

    // /**
    //  * @return If pacman can eat ghosts
    //  */
    // public boolean canEat() {
    //     return this.canEat;
    // }

    // /**
    //  * Start the timer for pacman to eat ghosts
    //  */
    // public void startEating() {
    //     this.eatTimer.start();
    //     this.canEat = true;
    // }
}
