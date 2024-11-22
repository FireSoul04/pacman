package com.firesoul.pacman;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.Game;
import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.GameObject;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.impl.entities.BoxCollider2D;
import com.firesoul.pacman.impl.entities.Entity2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class TestCollisions {

    GameTest game;
    GameObject entity;

    @BeforeEach
    void setup() {
        game = new GameTest();
        entity = new EntityTest(Vector2D.zero(), new Vector2D(2, 2));
        game.addGameObject(entity);
    }

    @Test
    void testMovement() {
        game.run();
    }

    class EntityTest extends Entity2D implements Movable, Collidable {

        private final Collider collider;
        
        public EntityTest(final Vector2D position, final Vector2D speed) {
            super(position, speed);
            this.collider = new BoxCollider2D(new Vector2D(8, 8));
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
            this.setPosition(this.getPosition().add(new Vector2D(this.getSpeed().getX() * deltaTime, 0)));
            if (this.getPosition().getX() > 200) {
                System.out.println("Out of bounds");
            }
        }

        public String toString() {
            return "GameObject [" +
                "pos: " + this.getPosition() + ", " +
                "speed: " + this.getSpeed() + "]]";
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
                gs.forEach((g) -> {
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
