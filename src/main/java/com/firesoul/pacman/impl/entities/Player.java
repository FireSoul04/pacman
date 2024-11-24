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

    private enum Animations {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        private final Animation2D animation;

        private Animations() {
            this.animation = new Animation2D("pacman", "pacman_" + this.name().toLowerCase(), Timer.secondsToMillis(ANIMATON_SPEED));
        }
    }

    // private static final long MAX_EATING_TIME = Timer.secondsToMillis(5);
    // private static final int MAX_LIVES = 3;
    private static final Vector2D SIZE = new Vector2D(16, 16);
    private static final double ANIMATON_SPEED = 0.1;

    private final Collider collider;
    // private final Timer eatTimer;
    // private State state;
    // private int lives;
    private boolean canEat;

    public Player(final Vector2D position, final Vector2D speed) {
        super(position, speed, Animations.RIGHT.animation);
        // this.state = State.IDLE;
        // this.lives = MAX_LIVES;
        this.canEat = false;
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
            direction = direction.add(Vector2D.up().dot(this.getSpeed().getY()));
        }
        if (Pacman.getInputController().isKeyPressed(KeyEvent.VK_S)) {
            direction = direction.add(Vector2D.down().dot(this.getSpeed().getY()));
        }
        if (Pacman.getInputController().isKeyPressed(KeyEvent.VK_A)) {
            direction = direction.add(Vector2D.left().dot(this.getSpeed().getX()));
        }
        if (Pacman.getInputController().isKeyPressed(KeyEvent.VK_D)) {
            direction = direction.add(Vector2D.right().dot(this.getSpeed().getX()));
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
            this.changeVariant(direction);
            animation.start();
            animation.update();
        }
    }
    
    private void changeVariant(final Vector2D direction) {
        if (direction.equals(Vector2D.up())) {
            this.setDrawable(Animations.UP.animation);
        } else if (direction.equals(Vector2D.down())) {
            this.setDrawable(Animations.DOWN.animation);
        } else if (direction.equals(Vector2D.left())) {
            this.setDrawable(Animations.LEFT.animation);
        } else if (direction.equals(Vector2D.right())) {
            this.setDrawable(Animations.RIGHT.animation);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collider getCollider() {
        return this.collider;
    }

    /**
     * @return If pacman can eat ghosts
     */
    public boolean canEat() {
        return this.canEat;
    }

    // /**
    //  * Start the timer for pacman to eat ghosts
    //  */
    // public void startEating() {
    //     this.eatTimer.start();
    //     this.canEat = true;
    // }
}
