package com.firesoul.pacman.impl.model.entities;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.model.SolidObject2D;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D;
import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.model.Pacman.Directions;
import com.firesoul.pacman.impl.model.Pacman.OutsideCageNotifier;

public abstract class Ghost extends SolidObject2D implements Movable {

    private static final long VULNERABILITY_START_BLINKING_TIME = Timer.secondsToMillis(3);
    private static final long VULNERABILITY_TIME = Timer.secondsToMillis(20);
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.2);
    private static final double NORMAL_SPEED = 0.5;
    private static final double DEAD_SPEED = 2;
    private static final Vector2D SPRITE_SIZE = new Vector2D(16, 16);
    private static final Vector2D SIZE = SPRITE_SIZE.dot(0.5);
    // private static final Vector2D CAGE_CENTER_POSITION = new Vector2D(112, 112);

    private final DirectionalAnimation2D movementAnimations;
    private final Animation2D vulnerableAnimation = new Animation2D("vulnerable", ANIMATION_SPEED);
    private final Animation2D vulnerableAnimationBlinking = new Animation2D("vulnerable_blinking", ANIMATION_SPEED);
    // private final Animation2D deadAnimation = new Animation2D("vulnerable_blinking", ANIMATION_SPEED); // TODO
    private final Timer insideCageTimer;
    private final Timer vulnerabiltyTimer = new TimerImpl(VULNERABILITY_TIME);
    private final Vector2D startPosition;
    private final Vector2D startDirection;
    private Vector2D currentDirection;
    private Vector2D nextDirection;
    private OutsideCageNotifier outsideCageNotifier;
    private Pacman pacman;
    private double speed = NORMAL_SPEED;
    private boolean dead = false;
    private boolean vulnerable = false;
    private boolean roam = false;
    private boolean insideCage = true;

    /**
     * Create a ghost.
     * @param position
     * @param name
     */
    public Ghost(final Vector2D position, final String name, final double insideCageTime, final Vector2D startDirection) {
        super(position, SPRITE_SIZE, SIZE);
        this.startPosition = position;
        this.startDirection = startDirection;
        this.currentDirection = startDirection;
        this.nextDirection = startDirection;
        this.insideCageTimer = new TimerImpl(Timer.secondsToMillis(insideCageTime));
        this.movementAnimations = new DirectionalAnimation2D(name, ANIMATION_SPEED);
        this.setDrawable(this.movementAnimations.getAnimation(Directions.RIGHT));
        this.insideCageTimer.start();
    }

    @Override
    public void update(final double deltaTime) {
        if (this.insideCage) {
            this.exitCage();
        } else if (this.dead) {
            this.goToCage();
            this.currentDirection = Vector2D.zero();
        } else {
            this.move();
            this.goToDirection(this.nextDirection);
        }
        final Vector2D newPosition = this.getPosition()
            .add(this.currentDirection.dot(speed)
            .dot(deltaTime))
            .wrap(SPRITE_SIZE.invert(), this.getScene().getDimensions().add(SPRITE_SIZE));
        this.setPosition(newPosition);
        this.updateTimers();
        this.moveColliders();
        this.animate(this.currentDirection);
    }

    private void exitCage() {
        if (this.roam) {
            final Vector2D distanceToExit = this.getPosition().sub(this.outsideCageNotifier.getPosition());
            final double roundedX = Math.round(distanceToExit.getX());
            if (roundedX < 0) {
                this.goToDirection(Vector2D.right());
            } else if (roundedX > 0) {
                this.goToDirection(Vector2D.left());
            } else {
                this.currentDirection = Vector2D.up();
                this.speed = 0.5;
            }
        } else {
            this.goToDirectionOrElse(this.nextDirection, this.nextDirection.invert());
        }
    }

    private void goToCage() {
        this.pacman.findPathToCage(this.getPosition());
        // System.out.println();
    }

    public void goToDirection(final Vector2D direction) {
        if (this.canMove(this.getDirectionFromVector(direction))) {
            this.currentDirection = direction;
        }
    }

    public void goToDirectionOrElse(final Vector2D direction, final Vector2D alternativeDirection) {
        if (this.canMove(this.getDirectionFromVector(direction))) {
            this.currentDirection = direction;
        } else {
            this.nextDirection = alternativeDirection;
        }
    }

    private void updateTimers() {
        this.vulnerabiltyTimer.update();
        if (this.vulnerabiltyTimer.isExpired()) {
            this.vulnerable = false;
        }
        this.insideCageTimer.update();
        if (this.insideCageTimer.isExpired()) {
            this.roam = true;
        }
    }

    /**
     * If the ghost is colliding with the player, based on the vulnerability of the ghost, the player or the ghost dies.
     */
    @Override
    public void onCollide(final Collider collider, final Collider other) {
        final Collidable gameObject = (Collidable) other.getAttachedGameObject();
        if (gameObject instanceof Player player && this.bodyIsCollidingWithBodyOf(player, collider, other)) {
            if (this.isDead()) {
                this.goToCage();
            } else if (this.isVulnerable()) {
                this.speed = DEAD_SPEED;
                this.die();
            } else if (!player.isDead()) {
                player.die();
            }
        } else if (gameObject instanceof Wall) {
            this.checkMove();
        } else if (gameObject instanceof OutsideCageNotifier) {
            this.insideCage = false;
        }
    }

    /**
     * Stop the vulnerability timer on pause, if it's started.
     */
    @Override
    public void pause() {
        if (this.isVulnerable()) {
            this.vulnerabiltyTimer.pause();
        }
        if (this.isInsideCage()) {
            this.insideCageTimer.pause();
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
        if (this.isInsideCage()) {
            this.insideCageTimer.start();
        }
    }

    /**
     * Go to start position.
     */
    @Override
    public void reset() {
        this.dead = false;
        this.vulnerable = false;
        this.roam = false;
        this.insideCage = true;
        this.speed = NORMAL_SPEED;
        this.currentDirection = this.startDirection;
        this.nextDirection = this.startDirection;
        this.changeVariant(Vector2D.right());
        this.setPosition(this.startPosition);
        this.insideCageTimer.restart();
    }

    /**
     * @param outsideCageNotifier that notifies a ghost when it's outside of the cage
     */
    public void addOutsideCageNotifier(final OutsideCageNotifier outsideCageNotifier) {
        this.outsideCageNotifier = outsideCageNotifier;
    }

    /**
     * @param pacman game logic
     */
    public void connectToGameLogic(final Pacman pacman) {
        this.pacman = pacman;
    }

    /**
     * @return If pacman can eat ghosts
     */
    public boolean isVulnerable() {
        return this.vulnerable;
    }

    /**
     * @return If ghost is inside the starting cage
     */
    public boolean isInsideCage() {
        return this.insideCage;
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

    protected Vector2D getCurrentDirection() {
        return this.currentDirection;
    }

    protected Vector2D getNextDirection() {
        return this.nextDirection;
    }

    protected void setCurrentDirection(final Vector2D direction) {
        this.currentDirection = direction;
    }

    protected void setNextDirection(final Vector2D direction) {
        this.nextDirection = direction;
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
     * Move based on specific ghost.
     */
    protected abstract void move();
}