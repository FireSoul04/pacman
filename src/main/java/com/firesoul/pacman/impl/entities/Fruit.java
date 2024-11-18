package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.impl.util.Vector2;

public class Fruit extends Entity2D implements Collidable {
    
    private final int points;

    public Fruit(final Vector2 position, final int points) {
        super(position, Vector2.zero());
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

    @Override
    public boolean isColliding(final Collidable other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isColliding'");
    }

    @Override
    public void onCollide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onCollide'");
    }
}
