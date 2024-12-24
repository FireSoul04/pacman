package com.firesoul.pacman.impl.model.entities;

import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.InputController;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.SolidObject2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Player extends SolidObject2D implements Movable {

    private static final int MAX_LIVES = 3;
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.1);
    private static final Vector2D SPRITE_SIZE = new Vector2D(16, 16);
    private static final Vector2D SIZE = SPRITE_SIZE.dot(0.5);
    private static final Vector2D START_POSITION = new Vector2D(4, 4).add(SIZE);
    private static final Vector2D SPEED = new Vector2D(1, 1);

    private final InputController input;
    private final DirectionalAnimation2D animations = new DirectionalAnimation2D("pacman", ANIMATION_SPEED);
    private Vector2D currentDirection = Vector2D.right();
    private Vector2D nextDirection = Vector2D.right();
    private boolean dead = false;
    private int lives = MAX_LIVES;

    public Player(final Scene2D scene, final InputController input) {
        super(START_POSITION, SPEED, scene, SPRITE_SIZE, SIZE);
        this.input = input;
        this.setDrawable(this.getAnimation(Directions.RIGHT));
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
        final GameObject2D gameObject = other.getAttachedGameObject();
        if (gameObject instanceof Wall) {
            this.checkMove();
        }
    }

    @Override
    public void update(final double deltaTime) {
        this.readInput();
        final Vector2D imageSize = this.getDrawable().getImageSize();
        final Vector2D newPosition = this.getPosition()
            .add(new Vector2D(
                this.currentDirection.getX() * this.getSpeed().getX(),
                this.currentDirection.getY() * this.getSpeed().getY()
            )
            .dot(deltaTime))
            .wrap(imageSize.invert(), this.getScene().getDimensions());
        this.setPosition(newPosition);
        this.moveColliders();
        this.animate(this.currentDirection);
    }

    private void readInput() {
        if (this.input.getEvent("MoveUp")) {
            this.nextDirection = Vector2D.up();
        }
        if (this.input.getEvent("MoveDown")) {
            this.nextDirection = Vector2D.down();
        }
        if (this.input.getEvent("MoveLeft")) {
            this.nextDirection = Vector2D.left();
        }
        if (this.input.getEvent("MoveRight")) {
            this.nextDirection = Vector2D.right();
        }
        if (!this.canMove(this.getDirectionFromVector(this.currentDirection))) {
            this.currentDirection = Vector2D.zero();
        }
        if (this.canMove(this.getDirectionFromVector(this.nextDirection))) {
            this.currentDirection = this.nextDirection;
        }
    }

    private void animate(final Vector2D direction) {
        final Animation2D animation = (Animation2D) this.getDrawable();
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
        this.setPosition(Player.START_POSITION);
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
