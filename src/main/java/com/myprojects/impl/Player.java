package com.myprojects.impl;

import com.myprojects.api.Collidable;
import com.myprojects.api.Movable;

public class Player extends Entity implements Movable, Collidable {

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

    @Override
    public void move(final double deltaTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

}
