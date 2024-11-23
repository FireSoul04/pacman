package com.firesoul.pacman;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.entities.bases.Entity2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.model.Room2D;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;

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
            timer.stopAtTimerEnd();
            if (timer.isStopped()) {
                game.gameOver();
            }
        });
    }

    Vector2D move(final EntityTest e, final double deltaTime) {
        Vector2D newPos =  e.getPosition()
            .add(e.getSpeed().dot(deltaTime))
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
        entity.setTestFunction((e, dt) -> {
            if (entity.getCollider().isColliding(collidingEntity.getCollider())) {
                game.gameOver();
            }
            move(e, dt);
            timer.stopAtTimerEnd();
            if (timer.isStopped()) {
                Assertions.fail("Entity did not collide with other entity");
            }
        });
        collidingEntity.setTestFunction((e, dt) -> {});
        game.addGameObject(collidingEntity);
        game.addGameObject(entity);
    }

    class EntityTest extends Entity2D implements Movable, Collidable {

        private BiConsumer<EntityTest, Double> test;
        private final Collider collider;
        
        public EntityTest(final Vector2D position, final Vector2D speed) {
            super(position, speed);
            this.collider = new BoxCollider2D(this, new Vector2D(8, 8));
        }

        public void setTestFunction(final BiConsumer<EntityTest, Double> test) {
            this.test = test;
        }

        @Override
        public void onCollide(final Collidable other) {
            System.out.println("Collided with " + other);
        }

        @Override
        public Collider getCollider() {
            return this.collider;
        }

        @Override
        public void update(final double deltaTime) {
            this.test.accept(this, deltaTime);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " [" +
                "pos: " + this.getPosition() + ", " +
                "speed: " + this.getSpeed() + "]";
        }

        public void setTestPosition(final Vector2D position) {
            this.setPosition(position);
        }
    }

    class GameTest implements Game {
        
            private Room2D room = new Room2D();
            private State state;
        
            @Override
            public void init() {
                this.start();
            }

            @Override
            public void start() {
                this.state = State.RUNNING;
            }

            @Override
            public void pause() {
                this.state = State.PAUSED;
            }
        
            @Override
            public void gameOver() {
                this.state = State.GAME_OVER;
            }
            
            @Override
            public boolean isRunning() {
                return this.state == State.RUNNING;
            }
            
            @Override
            public boolean isPaused() {
                return this.state == State.PAUSED;
            }
            
            @Override
            public boolean isOver() {
                return this.state == State.GAME_OVER;
            }
        
            @Override
            public void update(final double deltaTime) {
                room.getEntities().forEach(g -> {
                    if (g instanceof Movable) {
                        ((Movable)g).update(deltaTime);
                    }
                });
            }
        
            @Override
            public void render() {
            
            }
        
            @Override
            public Renderer getRenderer() {
                return null;
            }
        
            public void addGameObject(final GameObject g) {
                room.addGameObject(g);
            }
    };
}
