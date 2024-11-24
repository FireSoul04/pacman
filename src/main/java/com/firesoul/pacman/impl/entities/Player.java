package com.firesoul.pacman.impl.entities;

import java.awt.event.KeyEvent;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.entities.bases.GameObject2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
// import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;

public class Player extends GameObject2D implements Movable, Collidable {

    // private enum State {
    //     IDLE,
    //     MOVING,
    //     DEAD
    // }

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
        final Vector2D direction = readInput();
        final Vector2D imageSize = this.getDrawable().getImageSize();
        final Vector2D newPosition = this.getPosition().add(direction.dot(deltaTime));
        this.setPosition(newPosition.wrap(imageSize.invert(), Pacman.getRoomDimensions()));
        this.animate(direction);
    }

    private Vector2D readInput() {
        Vector2D direction = Vector2D.zero();
        if (Pacman.getInputController().isKeyPressed(KeyEvent.VK_W)) {
            direction = direction.add(new Vector2D(0, -1 * this.getSpeed().getY()));
        }
        if (Pacman.getInputController().isKeyPressed(KeyEvent.VK_S)) {
            direction = direction.add(new Vector2D(0, 1 * this.getSpeed().getY()));
        }
        if (Pacman.getInputController().isKeyPressed(KeyEvent.VK_A)) {
            direction = direction.add(new Vector2D(-1 * this.getSpeed().getX(), 0));
        }
        if (Pacman.getInputController().isKeyPressed(KeyEvent.VK_D)) {
            direction = direction.add(new Vector2D(1 * this.getSpeed().getX(), 0));
        }
        if (direction.getX() != 0 && direction.getY() != 0) {
            direction = new Vector2D(direction.getX(), 0);
        }
        return direction;
    }

    private void animate(final Vector2D direction) {
        final Animation2D animation = (Animation2D)this.getDrawable();
        if (direction.equals(Vector2D.zero())) {
            animation.stop();
            animation.reset();
        } else {
            animation.start();
            animation.update();
        }
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
