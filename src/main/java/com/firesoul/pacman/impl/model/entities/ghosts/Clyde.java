package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Clyde extends Ghost {
    
    private static final Vector2D START_POSITION = new Vector2D(12, 80);

    /**
     * Creates Clyde, the orange ghost.
     * @param scene where it belongs
     */
    public Clyde(final Scene2D scene) {
        super(START_POSITION, "clyde", scene);
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(START_POSITION);
    }
}
