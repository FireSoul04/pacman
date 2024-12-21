package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Blinky extends Ghost {
    
    private static final Vector2D START_POSITION = new Vector2D(12, 32);
    
    /**
     * Creates Blinky, the red ghost.
     * @param scene where it belongs
     */
    public Blinky(final Scene2D scene) {
        super(START_POSITION, "blinky", scene);
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(START_POSITION);
    }
}
