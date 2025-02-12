package com.firesoul.pacman.testClasses;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class EntityTest extends GameObject2D implements Movable, Collidable {

    private static final Vector2D HITBOX_SIZE = new Vector2D(8, 8);

    private BiConsumer<EntityTest, Double> test;
    private final Collider collider;
    private final Vector2D speed;
    
    public EntityTest(final Vector2D position, final Vector2D speed) {
        super(position);
        this.speed = speed;
        this.collider = new BoxCollider2D(this, HITBOX_SIZE, true);
    }

    public void setTestFunction(final BiConsumer<EntityTest, Double> test) {
        this.test = test;
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
        System.out.println("Collided with " + other);
    }

    @Override
    public List<Collider> getColliders() {
        return Collections.unmodifiableList(List.of(this.collider));
    }

    @Override
    public void update(final double deltaTime) {
        this.test.accept(this, deltaTime);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" +
            "pos: " + this.getPosition() + ", " +
            "speed: " + this.speed + "]";
    }

    public Vector2D getSpeed() {
        return this.speed;
    }

    public void setTestPosition(final Vector2D position) {
        this.setPosition(position);
    }
}