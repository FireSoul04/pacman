package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.impl.util.Vector2;

public abstract class Ghost extends Entity2D implements Movable, Collidable {

    /**
     * Create a ghost
     * @param position
     * @param speed
     */
    public Ghost(final Vector2 position, final Vector2 speed) {
        super(position, speed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isColliding(final Collidable other) {
        // TODO
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide() {
        // TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        // TODO
    }
}