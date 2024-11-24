package com.firesoul.pacman.impl.entities.ghosts;

import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.impl.entities.Ghost;

public class Clyde extends Ghost {

    /**
     * Creates Clyde, the orange ghost.
     * @param position
     * @param speed
     */
    public Clyde(final Vector2D position, final Vector2D speed) {
        super(position, speed, "clyde");
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        ((Animation2D)this.getDrawable()).reset();
        this.setPosition(new Vector2D(0, 64));
    }
}
