package com.firesoul.pacman.impl.entities;

import java.awt.event.KeyEvent;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.entities.bases.GameObject2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;

public class Player extends GameObject2D implements Movable, Collidable {

    private static final int MAX_LIVES = 3;
    private static final Vector2D SIZE = new Vector2D(8, 8);
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.1);

    private final DirectionalAnimation2D animations;
    private final Collider collider;
    private boolean dead;
    private int lives;

    public Player(final Vector2D position, final Vector2D speed) {
        super(position, speed);
        this.lives = MAX_LIVES;
        this.dead = false;
        this.collider = new BoxCollider2D(this, Player.SIZE);
        this.animations = new DirectionalAnimation2D("pacman", ANIMATION_SPEED);
        this.setDrawable(this.getAnimation(Directions.RIGHT));
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
            this.setDrawable(this.getAnimation(Directions.UP));
        } else if (direction.equals(Vector2D.down())) {
            this.setDrawable(this.getAnimation(Directions.DOWN));
        } else if (direction.equals(Vector2D.left())) {
            this.setDrawable(this.getAnimation(Directions.LEFT));
        } else if (direction.equals(Vector2D.right())) {
            this.setDrawable(this.getAnimation(Directions.RIGHT));
        }
    }
    
    private Animation2D getAnimation(final Directions direction) {
        return this.animations.getAnimation(direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collider getCollider() {
        return this.collider;
    }

    /**
     * Reset the player.
     */
    public void reset() {
        ((Animation2D)this.getDrawable()).reset();
        this.setPosition(Vector2D.zero());
    }

    /**
     * The player dies.
     */
    public void die() {
        this.dead = true;
        this.lives--;
    }

    /**
     * @return if the player is dead.
     */
    public boolean isDead() {
        return this.dead;
    }

    /**
     * @return player's lives.
     */
    public int getLives() {
        return this.lives;
    }
}
