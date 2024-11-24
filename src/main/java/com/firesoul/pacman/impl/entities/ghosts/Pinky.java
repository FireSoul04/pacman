package com.firesoul.pacman.impl.entities.ghosts;

import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.entities.Ghost;

public class Pinky extends Ghost {

    /**
     * Creates Pinky, the pink ghost.
     * @param position
     * @param speed
     */
    public Pinky(final Vector2D position, final Vector2D speed) {
        super(position, speed, "pinky");
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        ((Animation2D)this.getDrawable()).reset();
        this.setPosition(new Vector2D(0, 48));
    }
}
