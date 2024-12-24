package com.firesoul.pacman.impl.model.entities;

import java.util.Collections;
import java.util.List;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class Fruit extends GameObject2D implements Collidable {
    
    private final int points;

    public Fruit(final Vector2D position, final Scene2D scene, final int points) {
        super(position, Vector2D.zero(), scene);
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onCollide'");
    }

    @Override
    public List<Collider> getColliders() {
        return Collections.unmodifiableList(List.of(/*this.collider*/));
    }
}
