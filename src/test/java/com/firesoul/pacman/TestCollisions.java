package com.firesoul.pacman;

import java.util.function.Function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.testClasses.EntityTest;
import com.firesoul.pacman.testClasses.GameTest;

public class TestCollisions {

    private static final int BOUNDS_X = 400;
    private static final int BOUNDS_Y = 300;

    Timer timer;
    Vector2D bounds;
    GameTest game;
    EntityTest entity;

    @BeforeEach
    void setup() {
        timer = new TimerImpl(Timer.secondsToMillis(0.2));
        game = new GameTest();
        bounds = new Vector2D(BOUNDS_X, BOUNDS_Y);
    }

    @AfterEach
    void start() {
        timer.start();
        game.run();
    }

    @Test
    void testOutOfBoundsX() {
        outOfBounds(newPos -> newPos.getX() < 0 || newPos.getX() > bounds.getX());
    }

    @Test
    void testOutOfBoundsY() {
        outOfBounds(newPos -> newPos.getY() < 0 || newPos.getY() > bounds.getY());
    }

    @Test
    void testOutOfBoundsXAndY() {
        outOfBounds(newPos -> 
            newPos.getX() < 0 || newPos.getX() > bounds.getX() ||
            newPos.getY() < 0 || newPos.getY() > bounds.getY()
        );
    }

    void outOfBounds(final Function<Vector2D, Boolean> outOfBoundsCondition) {
        entity = new EntityTest(new Vector2D(BOUNDS_X / 2, BOUNDS_Y / 2), new Vector2D(20, -30));
        game.addGameObject(entity);
        entity.setTestFunction((e, dt) -> {
            Vector2D newPos = move(e, dt);
            if (outOfBoundsCondition.apply(newPos)) {
                Assertions.fail("Entity's y coordinate is out of bounds");
            }
            timer.update();
            if (timer.isExpired()) {
                game.gameOver();
            }
        });
    }

    Vector2D move(final EntityTest e, final double dt) {
        Vector2D newPos = e.getPosition()
            .add(e.getSpeed().dot(dt))
            .wrap(Vector2D.zero(), bounds);
        e.setTestPosition(newPos);
        return newPos;
    }

    @Test
    void testCollisionsX() {
        EntityTest collidingEntity = new EntityTest(
            new Vector2D(BOUNDS_X / 2, 0), Vector2D.zero());
        setupCollisions(collidingEntity, new Vector2D(20, 0));
    }

    @Test
    void testCollisionsY() {
        EntityTest collidingEntity = new EntityTest(
            new Vector2D(0, BOUNDS_Y / 2), Vector2D.zero());
        setupCollisions(collidingEntity, new Vector2D(0, 15));
    }

    @Test
    void testCollisionsXAndY() {
        EntityTest collidingEntity = new EntityTest(
            new Vector2D(BOUNDS_X / 2, BOUNDS_Y / 2), Vector2D.zero());
        setupCollisions(collidingEntity, new Vector2D(20, 15));
    }

    void setupCollisions(final EntityTest collidingEntity, final Vector2D speed) {
        entity = new EntityTest(Vector2D.zero(), speed);
        entity.setTestFunction(this::checkCollisions);
        collidingEntity.setTestFunction((e, dt) -> {});
        game.addGameObject(collidingEntity);
        game.addGameObject(entity);
    }

    void checkCollisions(final EntityTest e, final double dt) {
        if (entity.getCollider().isColliding(e.getCollider())) {
            game.gameOver();
        }
        move(e, dt);
        timer.update();
        if (timer.isExpired()) {
            Assertions.fail("Entity did not collide with other entity");
        }
    }
}
