package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

public class Inky extends Ghost {
    /**
     * Creates Inky, the cyan ghost.
     */
    public Inky(final Vector2D position) {
        super(position, "inky", 4);
    }

    @Override
    protected void move() {
        if (!this.canMove(this.getDirectionFromVector(this.getNextDirection()))) {
            this.setNextDirection(this.getCurrentDirection().invert());
        }
    }
}
