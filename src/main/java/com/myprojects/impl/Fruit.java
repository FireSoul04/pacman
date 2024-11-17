package com.myprojects.impl;

import com.myprojects.api.Collidable;

public class Fruit extends Entity implements Collidable {
    
    private final int points;

    public Fruit(final int points) {
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
