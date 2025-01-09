package com.firesoul.pacman.impl.model.entities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

public abstract class Ghost extends SolidObject2D implements Movable {

    private static final long VULNERABILITY_START_BLINKING_TIME = Timer.secondsToMillis(3);
    private static final long VULNERABILITY_TIME = Timer.secondsToMillis(10);
    private static final long ANIMATION_SPEED = Timer.secondsToMillis(0.2);
    private static final double NORMAL_SPEED = 0.5;
    private static final double DEAD_SPEED = 3;
    private static final Vector2D SPRITE_SIZE = new Vector2D(16, 16);
    private static final Vector2D SIZE = SPRITE_SIZE.dot(0.5);

    private final DirectionalAnimation2D movementAnimations;
    private final DirectionalAnimation2D deadAnimation = new DirectionalAnimation2D("dead_ghost", 0);
    private final Animation2D vulnerableAnimation = new Animation2D("vulnerable", ANIMATION_SPEED);
    private final Animation2D vulnerableAnimationBlinking = new Animation2D("vulnerable_blinking", ANIMATION_SPEED);
    private final Timer vulnerabiltyTimer = new TimerImpl(VULNERABILITY_TIME);
    private final Timer insideCageTimer;
    private final Vector2D startPosition;
    private final Vector2D startDirection;
    private final List<Vector2D> path = new LinkedList<>();
    private Vector2D currentDirection;
    private Vector2D nextDirection;
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
            this.exitFromCage();
        } else if (this.dead) {
            this.goToCage();
        } else {
            this.move();
            this.goToDirection(this.nextDirection);
        }
        final Vector2D newPosition = this.getPosition()
            .add(this.currentDirection.dot(this.speed)
            .dot(deltaTime))
            .wrap(SPRITE_SIZE.invert(), this.getScene().getDimensions().add(SPRITE_SIZE));
        this.setPosition(newPosition);
        this.updateTimers();
        this.moveColliders();
        this.animate(this.currentDirection);
    }

    private void goToCage() {
        this.followForcePath();
        if (this.path.isEmpty()) {
            this.revive();
        }
    }

    private void exitFromCage() {
        if (this.roam) {
            final Vector2D distanceToExit = this.getPosition().sub(this.pacman.getCageExit().sub(new Vector2D(0, 8)));
            final double roundedX = Math.round(distanceToExit.getX());
            if (roundedX < 0) {
                this.goToDirection(Vector2D.right());
            } else if (roundedX > 0) {
                this.goToDirection(Vector2D.left());
            } else {
                this.currentDirection = Vector2D.up(); // Exit from cage
                this.speed = 0.5;
            }
        } else {
            this.goToDirectionOrElse(this.nextDirection, this.nextDirection.invert()); // Stay inside of the cage
        }
    }

    protected List<Vector2D> getPath() {
        return Collections.unmodifiableList(this.path);
    }

    protected void changePath(final Vector2D position) {
        if (this.path.isEmpty()) {
            this.path.addAll(this.pacman.findPathTo(this.getPosition(), position));
        }
    }

    protected void followForcePath() {
        if (!this.path.isEmpty()) {
            final Vector2D node = this.path.getFirst().add(new Vector2D(-8, 8));
            final Vector2D rounded = this.rounded(this.getPosition());
            double distance = Pacman.distance(rounded, node);
            System.out.println("Current Position: " + this.getPosition() + ", Node: " + node + ", Distance: " + distance);
            if (Pacman.distance(rounded, node) > this.speed) {
                this.currentDirection = new Vector2D(
                    Math.abs(node.getX() - rounded.getX()) < this.speed ? 0 : Math.signum(node.getX() - rounded.getX()),
                    Math.abs(node.getY() - rounded.getY()) < this.speed ? 0 : Math.signum(node.getY() - rounded.getY())
                );
            } else {
                this.path.removeFirst();
            }
        }
    }

    protected void followPath() {
        if (!this.path.isEmpty()) {
            final Vector2D node = this.path.getFirst().add(new Vector2D(-8, 8));
            final Vector2D rounded = this.rounded(this.getPosition());
            if (Pacman.distance(rounded, node) > 4) {
                this.nextDirection = new Vector2D(
                    Math.abs(node.getX() - rounded.getX()) < 4 ? 0 : Math.signum(node.getX() - rounded.getX()),
                    Math.abs(node.getY() - rounded.getY()) < 4 ? 0 : Math.signum(node.getY() - rounded.getY())
                );
                if (this.nextDirection.getX() != 0 && this.nextDirection.getY() != 0) {
                    this.nextDirection.setX(0);
                }
            } else {
                this.path.removeFirst();
            }
        }
    }

    private Vector2D rounded(final Vector2D v) {
        return new Vector2D(
            Math.round(v.getX()),
            Math.round(v.getY())
        );
    }

    protected void goToDirection(final Vector2D direction) {
        if (this.canMove(this.getDirectionFromVector(direction))) {
            this.currentDirection = direction;
        }
    }

    protected void goToDirectionOrElse(final Vector2D direction, final Vector2D alternativeDirection) {
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
            if (!this.isDead()) {
                if (this.isVulnerable()) {
                    this.speed = DEAD_SPEED;
                    this.die();
                } else if (!player.isDead()) {
                    player.die();
                }
            }
        } else if (gameObject instanceof Wall) {
            this.checkMove();
        } else if (gameObject instanceof CageExit && this.bodyIsCollidingWith(collider)) {
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

    private void revive() {
        this.dead = false;
        this.vulnerable = false;
        this.roam = false;
        this.insideCage = true;
        this.speed = NORMAL_SPEED;
        this.currentDirection = this.startDirection;
        this.nextDirection = this.startDirection;
        this.setPosition(this.pacman.getCageEnter());
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
        this.insideCageTimer.startAgain();
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
        this.vulnerabiltyTimer.startAgain();
        this.vulnerable = true;
    }

    /**
     * The ghost dies.
     */
    public void die() {
        this.dead = true;
        this.path.clear();
        this.pacman.ghostEaten();
        this.changePath(this.pacman.getCageEnter());
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
    protected Animation2D getAnimation(final DirectionalAnimation2D animation, final Directions direction) {
        return animation.getAnimation(direction);
    }

    /**
     * Animate based on the direction where the ghost is going.
     * @param direction where the ghost is going.
     */
    protected void animate(final Vector2D direction) {
        this.changeVariant(direction);
        final Animation2D animation = (Animation2D) this.getDrawable();
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

    protected Vector2D getPlayerPosition() {
        return this.pacman.getPlayerPosition();
    }

    private void changeVariant(final Vector2D direction) {
        if (this.dead) {
            this.changeBasedOnDirection(this.deadAnimation, direction);
        } else if (this.isVulnerable()) {
            if (this.vulnerabiltyTimer.getRemainingTime() < VULNERABILITY_START_BLINKING_TIME) {
                this.setDrawable(vulnerableAnimationBlinking);
            } else {
                this.setDrawable(vulnerableAnimation);
            }
        } else {
            this.changeBasedOnDirection(this.movementAnimations, direction);
        }
    }

    private void changeBasedOnDirection(final DirectionalAnimation2D animation, final Vector2D direction) {
        if (direction.equals(Vector2D.up())) {
            this.setDrawable(this.getAnimation(animation, Directions.UP));
        } else if (direction.equals(Vector2D.down())) {
            this.setDrawable(this.getAnimation(animation, Directions.DOWN));
        } else if (direction.equals(Vector2D.left())) {
            this.setDrawable(this.getAnimation(animation, Directions.LEFT));
        } else if (direction.equals(Vector2D.right())) {
            this.setDrawable(this.getAnimation(animation, Directions.RIGHT));
        }
    }

    /**
     * Move based on specific ghost.
     */
    protected abstract void move();
}