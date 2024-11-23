package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.impl.entities.bases.Entity2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class Pill extends Entity2D implements Collidable {

    /**
     * Create a little pill that is required to conclude the current level
     * @param position
     */
    public Pill(final Vector2D position) {
        super(position, Vector2D.zero());
    }

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
