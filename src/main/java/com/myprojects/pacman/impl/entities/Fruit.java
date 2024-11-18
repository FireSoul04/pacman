package com.myprojects.pacman.impl.entities;

import com.myprojects.pacman.api.entities.Collidable;
import com.myprojects.pacman.impl.util.Vector2;

public class Fruit extends Entity implements Collidable {
    
    private final int points;

    public Fruit(final Vector2 position, final Vector2 speed, final int points) {
        super(position, speed);
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

    @Override
    public boolean isColliding(Collidable other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isColliding'");
    }

    @Override
    public void onCollide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onCollide'");
    }
}
