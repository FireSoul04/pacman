package com.firesoul.pacman.impl.entities;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Drawable;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.entities.bases.Entity2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;

public class Player extends Entity2D implements Movable, Collidable, Drawable {

    private enum State {
        IDLE,
        MOVING,
        DEAD
    }

    private static final String PATH_TO_SPRITES = "src/main/resources/sprites/pacman/";
    private static final long MAX_EATING_TIME = Timer.secondsToMillis(5);
    private static final int MAX_LIVES = 3;

    private final Collider collider;
    private final List<Image> frames;
    private State state;
    private Timer eatTimer;
    private int animationFrame;
    private int lives;
    private boolean canEat;

    public Player(final Vector2D position, final Vector2D speed) {
        super(position, speed);
        this.state = State.IDLE;
        this.lives = MAX_LIVES;
        this.canEat = false;
        this.animationFrame = 0;
        this.eatTimer = new TimerImpl(MAX_EATING_TIME);
        this.collider = new BoxCollider2D(this, new Vector2D(8, 8)); // For debugging purposes 8 pxs
        this.frames = new ArrayList<>();
        try {
            this.frames.add(ImageIO.read(new File(PATH_TO_SPRITES + "pacman_0.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide(final Collidable other) {
        System.out.println("Entity is colliding with " + other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        // TODO

        //TESTING PURPOSE
        final Vector2D imageSize = new Vector2D(
            this.getImage().getWidth(null), 
            this.getImage().getHeight(null)
        );
        this.setPosition(this.getPosition()
            .add(this.getSpeed().dot(deltaTime))
            .wrap(Vector2D.zero().sub(imageSize), Pacman.getRoomDimensions()));
        //
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collider getCollider() {
        return this.collider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getImage() {
        return this.frames.get(this.animationFrame);
    }

    /**
     * @return If pacman can eat ghosts
     */
    public boolean canEat() {
        return this.canEat;
    }

    /**
     * Start the timer for pacman to eat ghosts
     */
    public void startEating() {
        this.eatTimer.start();
        this.canEat = true;
    }
}
