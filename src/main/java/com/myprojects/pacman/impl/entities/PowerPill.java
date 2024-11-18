package com.myprojects.pacman.impl.entities;

import com.myprojects.pacman.api.entities.Collidable;
import com.myprojects.pacman.impl.util.Vector2;

public class PowerPill extends Entity implements Collidable {

    /**
     * Create a pill that if eaten by pacman ghosts are vulnerable
     * @param position
     * @param speed
     */
    public PowerPill(final Vector2 position, final Vector2 speed) {
        super(position, speed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isColliding(Collidable other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isColliding'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onCollide'");
    }

}
