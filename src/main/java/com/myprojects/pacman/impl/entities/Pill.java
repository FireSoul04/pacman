package com.myprojects.pacman.impl.entities;

import com.myprojects.pacman.api.entities.Collidable;
import com.myprojects.pacman.impl.util.Vector2;

public class Pill extends Entity implements Collidable {

    /**
     * Create a little pill that is required to conclude the current level
     * @param position
     * @param speed
     */
    public Pill(final Vector2 position, final Vector2 speed) {
        super(position, speed);
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
