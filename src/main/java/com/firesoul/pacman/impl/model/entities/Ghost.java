package com.firesoul.pacman.impl.model.entities;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;

public abstract class Ghost extends GameObject2D implements Movable, Collidable {

    private static final long VULNERABILITY_START_BLINKING_TIME = Timer.secondsToMillis(3);
    private static final long VULNERABILITY_TIME = Timer.secondsToMillis(5);
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.2);
    private static final Vector2D SIZE = new Vector2D(8, 8);

    private final Scene2D scene;
    private final DirectionalAnimation2D movementAnimations;
    private final Animation2D vulnerableAnimation;
    private final Animation2D vulnerableAnimationBlinking;
    private final Timer vulnerabiltyTimer;
    private final Collider collider;
    private boolean dead;
    private boolean vulnerable;

    /**
     * Create a ghost.
     * @param position
     * @param speed
     */
    public Ghost(final Vector2D position, final Vector2D speed, final String name, final Scene2D scene) {
        super(position, speed);
        this.scene = scene;
        this.dead = false;
        this.vulnerable = false;
        this.collider = new BoxCollider2D(this, SIZE);
        this.vulnerabiltyTimer = new TimerImpl(VULNERABILITY_TIME);
        this.movementAnimations = new DirectionalAnimation2D(name, ANIMATION_SPEED);
        this.vulnerableAnimation = new Animation2D("vulnerable", ANIMATION_SPEED);
        this.vulnerableAnimationBlinking = new Animation2D("vulnerable_blinking", ANIMATION_SPEED);
        this.setDrawable(this.movementAnimations.getAnimation(Directions.RIGHT));
    }

    @Override
    public void update(final double deltaTime) {
        this.move();

        // TODO
        final Vector2D direction = Vector2D.down();
        final Vector2D imageSize = this.getDrawable().getImageSize();
        final Vector2D newPosition = this.getPosition().add(direction.dot(deltaTime));
        this.setPosition(newPosition.wrap(imageSize.invert(), this.scene.getDimensions()));
        //

        this.vulnerabiltyTimer.update();
        if (this.vulnerabiltyTimer.isExpired()) {
            this.vulnerable = false;
        }
        this.animate(direction);
    }

    /**
     * If the ghost is colliding with the player, based on the vulnerability of the ghost, the player or the ghost dies.
     */
    @Override
    public void onCollide(final Collidable other) {
        if (other instanceof Player) {
            final Player player = (Player) other;
            if (this.isVulnerable()) {
                this.die();
            } else if (!player.isDead()) {
                player.die();
            }
        }
    }

    @Override
    public Collider getCollider() {
        return this.collider;
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