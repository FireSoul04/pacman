package com.firesoul.pacman.impl.model.entities;

import java.util.*;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.model.entities.colliders.ColliderCenterLayout;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.model.Pacman.Directions;

public abstract class Ghost extends GameObject2D implements Movable, Collidable {

    private static final long VULNERABILITY_START_BLINKING_TIME = Timer.secondsToMillis(3);
    private static final long VULNERABILITY_TIME = Timer.secondsToMillis(5);
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.2);
    private static final Vector2D SPRITE_SIZE = new Vector2D(16, 16);
    private static final Vector2D SIZE = SPRITE_SIZE.dot(0.5);
    private static final Vector2D SPEED = new Vector2D(1, 1);

    private final Set<Directions> move = new HashSet<>();
    private final Map<Directions, Collider> colliders;
    private final Scene2D scene;
    private final DirectionalAnimation2D movementAnimations;
    private final Animation2D vulnerableAnimation = new Animation2D("vulnerable", ANIMATION_SPEED);
    private final Animation2D vulnerableAnimationBlinking = new Animation2D("vulnerable_blinking", ANIMATION_SPEED);
    private final Timer vulnerabiltyTimer = new TimerImpl(VULNERABILITY_TIME);
    private boolean dead = false;
    private boolean vulnerable = false;

    /**
     * Create a ghost.
     * @param position
     * @param speed
     */
    public Ghost(final Vector2D position, final String name, final Scene2D scene) {
        super(position, SPEED);
        this.scene = scene;
        this.movementAnimations = new DirectionalAnimation2D(name, ANIMATION_SPEED);
        this.setDrawable(this.movementAnimations.getAnimation(Directions.RIGHT));

        final Vector2D sizeHorizontal = new Vector2D(SPRITE_SIZE.getX() - 1, 1);
        final Vector2D sizeVertical = new Vector2D(1, SPRITE_SIZE.getY() - 1);
        this.colliders = new HashMap<>(Map.of(
            Directions.NONE, new BoxCollider2D(this, SIZE, new ColliderCenterLayout()),
            Directions.UP, new BoxCollider2D(this, sizeHorizontal, 
                (g, s) -> g.getPosition().add(Vector2D.up()).sub(SIZE).add(new Vector2D(0.5, 0.5))),
            Directions.DOWN, new BoxCollider2D(this, sizeHorizontal, 
                (g, s) -> g.getPosition().add(Vector2D.down()).add(sizeVertical.sub(new Vector2D(0.5, 0.5))).sub(SIZE)),
            Directions.LEFT, new BoxCollider2D(this, sizeVertical, 
                (g, s) -> g.getPosition().add(Vector2D.left()).sub(SIZE).add(new Vector2D(0.5, 0.5))),
            Directions.RIGHT, new BoxCollider2D(this, sizeVertical, 
                (g, s) -> g.getPosition().add(Vector2D.right()).add(sizeHorizontal.sub(new Vector2D(0.5, 0.5))).sub(SIZE))
        ));
    }

    @Override
    public void update(final double deltaTime) {
        this.move();

        // TODO
        final Vector2D direction = Vector2D.left();
        final Vector2D imageSize = this.getDrawable().getImageSize();
        final Vector2D newPosition = this.getPosition()
            .add(new Vector2D(
                direction.getX() * this.getSpeed().getX(),
                direction.getY() * this.getSpeed().getY()
            )
            .dot(deltaTime))
            .wrap(imageSize.invert(), this.scene.getDimensions());
        this.setPosition(newPosition);
        //

        this.vulnerabiltyTimer.update();
        if (this.vulnerabiltyTimer.isExpired()) {
            this.vulnerable = false;
        }
        this.moveColliders();
        this.animate(direction);
    }

    private void moveColliders() {
        for (final Collider c : this.getColliders()) {
            c.update();
            this.move.clear();
        }
    }

    /**
     * If the ghost is colliding with the player, based on the vulnerability of the ghost, the player or the ghost dies.
     */
    @Override
    public void onCollide(final Collider collider, final Collider other) {
        final Collidable gameObject = (Collidable) other.getAttachedGameObject();
        if (gameObject instanceof Player player) {
            final Directions playerDirection = player.getDirectionFromCollider(other);
            final Directions ghostDirection = this.getDirectionFromCollider(collider);
            if (playerDirection.equals(Directions.NONE) && ghostDirection.equals(Directions.NONE)) {
                if (this.isVulnerable()) {
                    this.die();
                } else if (!player.isDead()) {
                    player.die();
                }
            }
        }
    }

    @Override
    public List<Collider> getColliders() {
        return Collections.unmodifiableList(this.colliders
            .values()
            .stream()
            .toList());
    }

    /**
     * Stop the vulnerability timer on pause, if it's started.
     */
    @Override
    public void pause() {
        if (this.isVulnerable()) {
            this.vulnerabiltyTimer.pause();
        }
    }

    /**
     * Restart the vulnerability timer on wake, if it's started.
     */
    @Override
    public void wake() {
        if (this.isVulnerable()) {
            this.vulnerabiltyTimer.start();
        }
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
     * @return If pacman can eat ghosts.
     */
    public boolean isVulnerable() {
        return this.vulnerable;
    }

    /**
     * Start the timer for the ghost to be vulnerable to pacman.
     */
    public void setVulnerable() {
        this.vulnerabiltyTimer.restart();
        this.vulnerable = true;
    }

    /**
     * The ghost dies.
     */
    public void die() {
        this.dead = true;
    }

    /**
     * @return if the ghost is dead.
     */
    public boolean isDead() {
        return this.dead;
    }

    /**
     * Get the animation based on the direction given.
     * @param direction where the animation is directed.
     * @return the animation in that direction.
     */
    protected Animation2D getAnimation(final Directions direction) {
        return this.movementAnimations.getAnimation(direction);
    }

    /**
     * Animate based on the direction where the ghost is going.
     * @param direction where the ghost is going.
     */
    protected void animate(final Vector2D direction) {
        final Animation2D animation = (Animation2D) this.getDrawable();
        this.changeVariant(direction);
        animation.start();
        animation.update();
    }

    private void changeVariant(final Vector2D direction) {
        if (this.isVulnerable()) {
            if (this.vulnerabiltyTimer.getRemainingTime() < VULNERABILITY_START_BLINKING_TIME) {
                this.setDrawable(vulnerableAnimationBlinking);
            } else {
                this.setDrawable(vulnerableAnimation);
            }
        } else {
            this.changeBasedOnDirection(direction);
        }
    }

    private void changeBasedOnDirection(final Vector2D direction) {
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

    /**
     * Reset the ghost position.
     */
    public abstract void reset();

    /**
     * Move based on specific ghost.
     */
    protected abstract void move();
}