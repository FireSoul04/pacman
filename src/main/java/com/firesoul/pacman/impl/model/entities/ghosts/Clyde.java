package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;

public class Clyde extends Ghost {

    /**
     * Creates Clyde, the orange ghost.
     * @param position
     * @param speed
     */
    public Clyde(final Vector2D position, final Vector2D speed, final Scene2D scene) {
        super(position, speed, "clyde", scene);
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(new Vector2D(12, 80));
    }
}
