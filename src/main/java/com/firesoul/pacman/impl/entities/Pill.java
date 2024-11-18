package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.impl.util.Vector2;

public class Pill extends Entity2D implements Collidable {

    /**
     * Create a little pill that is required to conclude the current level
     * @param position
     */
    public Pill(final Vector2 position) {
        super(position, Vector2.zero());
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
