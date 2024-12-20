package com.firesoul.pacman.impl.model.entities;

import java.awt.event.KeyEvent;
import java.util.*;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.InputController;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;

public class Player extends GameObject2D implements Movable, Collidable {

    private static final int MAX_LIVES = 3;
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.1);
    private static final Vector2D SIZE = new Vector2D(16, 16);
    private static final Vector2D SIZE_HORIZONTAL = new Vector2D(SIZE.getX(), 1);
    private static final Vector2D SIZE_VERTICAL = new Vector2D(1, SIZE.getY());
    private static final Vector2D PACMAN_START_POSITION = new Vector2D(4, 4).add(SIZE.dot(0.5));

    private final Map<Directions, Boolean> move = new HashMap<>(Map.of(
        Directions.UP, true,
        Directions.DOWN, true,
        Directions.LEFT, true,
        Directions.RIGHT, true
    ));

    // this.colliders.get(this.getDirectionFromVector(Vector2D.up())).setPosition(playerPosition
    //     .add(Vector2D.up())
    //     .sub(SIZE.dot(0.5))

    // this.colliders.get(this.getDirectionFromVector(Vector2D.left())).setPosition(playerPosition
    //     .add(Vector2D.left())
    //     .sub(SIZE.dot(0.5))

    // this.colliders.get(this.getDirectionFromVector(Vector2D.down())).setPosition(playerPosition
    //     .add(Vector2D.down())
    //     .sub(SIZE.dot(0.5))
    //     .add(new Vector2D(0, SIZE.getY() - 1))

    // this.colliders.get(this.getDirectionFromVector(Vector2D.right())).setPosition(playerPosition
    //     .add(Vector2D.right())
    //     .sub(SIZE.dot(0.5))
    //     .add(new Vector2D(SIZE.getX() - 1, 0))
    private final Map<Directions, Collider> colliders = new HashMap<>(Map.of(
        Directions.UP, new BoxCollider2D(this, SIZE_HORIZONTAL),
        Directions.DOWN, new BoxCollider2D(this, SIZE_HORIZONTAL),
        Directions.LEFT, new BoxCollider2D(this, SIZE_VERTICAL),
        Directions.RIGHT, new BoxCollider2D(this, SIZE_VERTICAL)
    ));
    private final Scene2D scene;
    private final InputController input;
    private final DirectionalAnimation2D animations;
    private Vector2D lastDirection;
    private boolean dead;
    private int lives;

    public Player(final Vector2D position, final Vector2D speed, final Scene2D scene, final InputController input) {
        super(position, speed);
        this.lives = MAX_LIVES;
        this.dead = false;
        this.scene = scene;
        this.input = input;
        this.lastDirection = Vector2D.right();
        this.animations = new DirectionalAnimation2D("pacman", ANIMATION_SPEED);
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.moveColliders();
    }

    @Override
    public void onCollide(final Collidable other) {
        if (other instanceof Wall) {
            for (final var collider : this.colliders.entrySet()) {
                for (final Collider c2 : other.getColliders()) {
                    final Collider c1 = collider.getValue();
                    final Directions direction = collider.getKey();
                    this.move.put(direction, c1.isOvelapping(c2));
                }
            }
        } 
    }

    private Vector2D roundedPosition(final Vector2D v) {
        return new Vector2D(Math.round(v.getX()), Math.round(v.getY()));
    }

    @Override
    public void update(final double deltaTime) {
        final Vector2D direction = this.readInput();
        final Vector2D imageSize = this.getDrawable().getImageSize();
        final Vector2D newPosition = this.getPosition()
            .add(new Vector2D(
                direction.getX() * this.getSpeed().getX(),
                direction.getY() * this.getSpeed().getY()
            )
            .dot(deltaTime));
        final Vector2D roundedPosition = this.roundedPosition(newPosition.wrap(imageSize.invert(), this.scene.getDimensions()));
        this.setPosition(roundedPosition);
        this.moveColliders();
        this.animate(direction);
    }

    private void moveColliders() {
        for (final Collider c : this.getColliders()) {
            c.update();
        }
    }

    private Vector2D readInput() {
        Vector2D direction = this.lastDirection;
        if (!this.move.get(this.getDirectionFromVector(direction))) {
            direction = Vector2D.zero();
        }
        if (this.input.isKeyPressed(KeyEvent.VK_W) && this.move.get(this.getDirectionFromVector(Vector2D.up()))) {
            direction = Vector2D.up();
        }
        if (this.input.isKeyPressed(KeyEvent.VK_S) && this.move.get(this.getDirectionFromVector(Vector2D.down()))) {
            direction = Vector2D.down();
        }
        if (this.input.isKeyPressed(KeyEvent.VK_A) && this.move.get(this.getDirectionFromVector(Vector2D.left()))) {
            direction = Vector2D.left();
        }
        if (this.input.isKeyPressed(KeyEvent.VK_D) && this.move.get(this.getDirectionFromVector(Vector2D.right()))) {
            direction = Vector2D.right();
        }
        this.lastDirection = direction;
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
        this.setDrawable(this.getAnimation(this.getDirectionFromVector(direction)));
    }

    private Directions getDirectionFromVector(final Vector2D v) {
        Directions d = Directions.RIGHT;
        if (v.equals(Vector2D.up())) {
            d = Directions.UP;
        } else if (v.equals(Vector2D.down())) {
            d = Directions.DOWN;
        } else if (v.equals(Vector2D.left())) {
            d = Directions.LEFT;
        }
        return d;
    }
    
    private Animation2D getAnimation(final Directions direction) {
        return this.animations.getAnimation(direction);
    }

    @Override
    public List<Collider> getColliders() {
        return Collections.unmodifiableList(this.colliders
            .values()
            .stream()
            .toList());
    }

    @Override
    public void pause() {
    }

    @Override
    public void wake() {
    }

    /**
     * Reset the player.
     */
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(Player.PACMAN_START_POSITION);
        this.move.replaceAll((k, v) -> true);
        this.dead = false;
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
