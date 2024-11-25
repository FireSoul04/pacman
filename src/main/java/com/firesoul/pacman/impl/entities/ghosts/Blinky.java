package com.firesoul.pacman.impl.entities.ghosts;

import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;
import com.firesoul.pacman.impl.entities.Ghost;

public class Blinky extends Ghost {

    /**
     * Creates Blinky, the red ghost.
     * @param position
     * @param speed
     */
    public Blinky(final Vector2D position, final Vector2D speed) {
        super(position, speed, "blinky");
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(new Vector2D(0, 16));
    }
}
