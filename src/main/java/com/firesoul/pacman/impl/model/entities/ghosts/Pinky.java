package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Pinky extends Ghost {
    
    private static final Vector2D START_POSITION = new Vector2D(12, 64);

    /**
     * Creates Pinky, the pink ghost.
     * @param scene where it belongs
     */
    public Pinky(final Scene2D scene) {
        super(START_POSITION, "pinky", scene);
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(START_POSITION);
    }
}
