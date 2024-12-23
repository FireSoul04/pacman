package com.firesoul.pacman.impl.model.entities;

import java.util.*;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.InputController;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.model.entities.colliders.ColliderCenterLayout;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Player extends GameObject2D implements Movable, Collidable {

    private static final int MAX_LIVES = 3;
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.1);
    private static final Vector2D SPRITE_SIZE = new Vector2D(16, 16);
    private static final Vector2D SIZE = SPRITE_SIZE.dot(0.5);
    private static final Vector2D START_POSITION = new Vector2D(4, 4).add(SIZE);
    private static final Vector2D SPEED = new Vector2D(1, 1);

    /**
     * Colliders for directions are placed in every edge of the sprite of pacman
     */
    private final Map<Directions, Boolean> move = new HashMap<>(Map.of(
        Directions.UP, true,
        Directions.DOWN, true,
        Directions.LEFT, true,
        Directions.RIGHT, true
    ));
    private final Map<Directions, Collider> colliders;
    private final Scene2D scene;
    private final InputController input;
    private final DirectionalAnimation2D animations = new DirectionalAnimation2D("pacman", ANIMATION_SPEED);
    private Vector2D lastDirection = Vector2D.right();
    private boolean dead = false;
    private int lives = MAX_LIVES;

    public Player(final Scene2D scene, final InputController input) {
        super(START_POSITION, SPEED);
        this.scene = scene;
        this.input = input;
        this.setDrawable(this.getAnimation(Directions.RIGHT));

        final Vector2D sizeHorizontal = new Vector2D(SPRITE_SIZE.getX(), 1);
        final Vector2D sizeVertical = new Vector2D(1, SPRITE_SIZE.getY());
        this.colliders = new HashMap<>(Map.of(
            Directions.NONE, new BoxCollider2D(this, SIZE, new ColliderCenterLayout()),
            Directions.UP, new BoxCollider2D(this, sizeHorizontal, 
                (g, s) -> g.getPosition().add(Vector2D.up()).sub(SIZE)),
            Directions.DOWN, new BoxCollider2D(this, sizeHorizontal, 
                (g, s) -> g.getPosition().add(Vector2D.down()).add(sizeVertical.sub(new Vector2D(1, 1))).sub(SIZE)),
            Directions.LEFT, new BoxCollider2D(this, sizeVertical, 
                (g, s) -> g.getPosition().add(Vector2D.left()).sub(SIZE)),
            Directions.RIGHT, new BoxCollider2D(this, sizeVertical, 
                (g, s) -> g.getPosition().add(Vector2D.right()).add(sizeHorizontal.sub(new Vector2D(1, 1))).sub(SIZE))
        ));
    }

    @Override
    public void onCollide(final Collider other) {
        final GameObject2D gameObject = other.getAttachedGameObject();
        if (gameObject instanceof Wall) {
            this.colliders.forEach((k, v) -> {
                if (v.hasCollidedLastFrame()) {
                    //System.out.println(k);
                }
            });
        }
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
            .dot(deltaTime))
            .wrap(imageSize.invert(), this.scene.getDimensions());
        this.setPosition(newPosition);
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
        if (!this.move.values().stream().anyMatch(t -> true)) {
            direction = Vector2D.zero();
        }
        if (this.input.getEvent("MoveUp") && this.move.get(this.getDirectionFromVector(Vector2D.up()))) {
            direction = Vector2D.up();
        }
        if (this.input.getEvent("MoveDown") && this.move.get(this.getDirectionFromVector(Vector2D.down()))) {
            direction = Vector2D.down();
        }
        if (this.input.getEvent("MoveLeft") && this.move.get(this.getDirectionFromVector(Vector2D.left()))) {
            direction = Vector2D.left();
        }
        if (this.input.getEvent("MoveRight") && this.move.get(this.getDirectionFromVector(Vector2D.right()))) {
            direction = Vector2D.right();
        }
        this.lastDirection = direction;
        return direction;
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
     * @param other
     * @return the directions of the collier
     */
    public Directions getDirectionFromCollider(final Collider other) {
        Directions d = Directions.NONE;
        for (final var entry : this.colliders.entrySet()) {
            if (entry.getValue().equals(other)) {
                d = entry.getKey();
            }
        }
        return d;
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
