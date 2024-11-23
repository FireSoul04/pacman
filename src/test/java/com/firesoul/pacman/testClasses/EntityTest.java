package com.firesoul.pacman.testClasses;

import java.util.function.BiConsumer;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.impl.entities.bases.Entity2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class EntityTest extends Entity2D implements Movable, Collidable {

    private static final Vector2D HITBOX_SIZE = new Vector2D(8, 8);

    private BiConsumer<EntityTest, Double> test;
    private final Collider collider;
    
    public EntityTest(final Vector2D position, final Vector2D speed) {
        super(position, speed);
        this.collider = new BoxCollider2D(this, HITBOX_SIZE);
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