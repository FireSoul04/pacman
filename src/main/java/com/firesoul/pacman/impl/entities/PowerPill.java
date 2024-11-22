package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.impl.util.Vector2D;

public class PowerPill extends Entity2D implements Collidable {

    /**
     * Create a pill that if eaten by pacman ghosts are vulnerable
     * @param position
     */
    public PowerPill(final Vector2D position) {
        super(position, Vector2D.zero());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide(final Collidable other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onCollide'");
    }

    @Override
    public Collider getCollider() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCollider'");
    }

}
