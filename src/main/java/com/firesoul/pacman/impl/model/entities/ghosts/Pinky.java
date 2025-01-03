package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

public class Pinky extends Ghost {

    /**
     * Creates Pinky, the pink ghost.
     */
    public Pinky(final Vector2D position) {
        super(position, "pinky", 4, Vector2D.down());
    }

    @Override
    protected void move() {
        if (!this.canMove(this.getDirectionFromVector(this.getNextDirection()))) {
            this.setNextDirection(this.getCurrentDirection().invert());
        }
    }
}
