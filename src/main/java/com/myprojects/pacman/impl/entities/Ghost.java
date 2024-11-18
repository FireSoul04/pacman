package com.myprojects.pacman.impl.entities;

import com.myprojects.pacman.api.entities.Collidable;
import com.myprojects.pacman.api.entities.Movable;
import com.myprojects.pacman.impl.util.Vector2;

public abstract class Ghost extends Entity implements Movable, Collidable {

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
}