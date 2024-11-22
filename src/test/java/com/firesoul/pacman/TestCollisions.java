package com.firesoul.pacman;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.Game;
import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.GameObject;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.entities.BoxCollider2D;
import com.firesoul.pacman.impl.entities.Entity2D;
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
        timer = new TimerImpl(Timer.secondsToMillis(5));
        game = new GameTest();
        bounds = new Vector2D(BOUNDS_X, BOUNDS_Y);
        entity = new EntityTest(new Vector2D(BOUNDS_X / 2, BOUNDS_Y / 2), new Vector2D(20, -30));
        game.addGameObject(entity);
    }

    @AfterEach
    void start() {
        timer.start();
        game.run();
    }

    Vector2D move(final EntityTest e, final double deltaTime) {
        Vector2D newPos =  e.getPosition()
            .add(e.getSpeed().dot(deltaTime))
            .wrap(Vector2D.zero(), bounds);
        e.setTestPosition(newPos);
        return newPos;
    }

    @Test
    void testOutOfBoundsX() {
        entity.setTestFunction((e, dt) -> {
            Vector2D newPos = move(e, dt);
            if (newPos.getX() < 0 || newPos.getX() > bounds.getX()) {
                Assertions.fail("Entity's x coordinate is out of bounds");
            }
            timer.stopAtTimerEnd();
            if (timer.isStopped()) {
                game.gameOver();
            }
        });
    }

    @Test
    void testOutOfBoundsY() {
        entity.setTestFunction((e, dt) -> {
            Vector2D newPos = move(e, dt);
            if (newPos.getY() < 0 || newPos.getY() > bounds.getY()) {
                Assertions.fail("Entity's y coordinate is out of bounds");
            }
            timer.stopAtTimerEnd();
            if (timer.isStopped()) {
                game.gameOver();
            }
        });
    }

    class EntityTest extends Entity2D implements Movable, Collidable {

        private BiConsumer<EntityTest, Double> test;
        private final Collider collider;
        
        public EntityTest(final Vector2D position, final Vector2D speed) {
            super(position, speed);
            this.collider = new BoxCollider2D(new Vector2D(8, 8));
        }

        public void setTestFunction(final BiConsumer<EntityTest, Double> test) {
            this.test = test;
        }

        @Override
        public void onCollide(final Collidable other) {
            // TODO
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
            return "GameObject [" +
                "pos: " + this.getPosition() + ", " +
                "speed: " + this.getSpeed() + "]]";
        }

        public void setTestPosition(final Vector2D position) {
            this.setPosition(position);
        }
    }

    class GameTest implements Game {
        
            private List<GameObject> gs = new ArrayList<>();
            private State state;
        
            @Override
            public void init() {
                this.state = State.RUNNING;
            }
        
            @Override
            public void update(final double deltaTime) {
                gs.forEach(g -> {
                    if (g instanceof Movable) {
                        ((Movable)g).update(deltaTime);
                    }
                });
                this.log(this.gs.toString());
            }
        
            @Override
            public void render() {
            
            }
        
            @Override
            public void gameOver() {
                this.state = State.GAME_OVER;
            }

            @Override
            public State getState() {
                return this.state;
            }
        
            public void addGameObject(final GameObject g) {
                gs.add(g);
            }
    };
}
