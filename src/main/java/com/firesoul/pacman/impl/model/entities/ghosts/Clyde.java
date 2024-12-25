package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Clyde extends Ghost {

    private final Vector2D startPosition;

    /**
     * Creates Clyde, the orange ghost.
     */
    public Clyde(final Vector2D position) {
        super(position, "clyde");
        this.startPosition = position;
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(this.startPosition);
    }
}
