package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.entities.bases.GameObject2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;

public abstract class Ghost extends GameObject2D implements Movable, Collidable {

    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.2);
    private static final Vector2D SIZE = new Vector2D(8, 8);

    private final DirectionalAnimation2D animations;
    private Collider collider;
    private boolean dead;
    private boolean vulnerable;
    // private final Timer eatTimer;

    /**
     * Create a ghost
     * @param position
     * @param speed
     */
    public Ghost(final Vector2D position, final Vector2D speed, final String name) {
        super(position, speed);
        this.dead = false;
        this.vulnerable = false;
        // this.eatTimer = new TimerImpl(MAX_EATING_TIME);
        this.collider = new BoxCollider2D(this, Ghost.SIZE);
        this.animations = new DirectionalAnimation2D(name, ANIMATION_SPEED);
        this.setDrawable(this.animations.getAnimation(Directions.RIGHT));
    }

    /**
     * If the ghost is colliding with the player,
     */
    @Override
    public void onCollide(final Collidable other) {
        if (other instanceof Player) {
            Player player = (Player) other;
            if (this.isVulnerable()) {
                this.die();
            } else {
                player.die();
            }
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
     * Reset the ghost position.
     */
    public void reset() {
        ((Animation2D)this.getDrawable()).reset();
        this.setPosition(new Vector2D(0, 16));
    }

    /**
     * @return If pacman can eat ghosts
     */
    public boolean isVulnerable() {
        return this.vulnerable;
    }

    // /**
    //  * Start the timer for pacman to eat ghosts
    //  */
    // public void startEating() {
    //     this.eatTimer.start();
    //     this.canEat = true;
    // }

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
        return this.animations.getAnimation(direction);
    }

    protected void animate(final Vector2D direction) {
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
}