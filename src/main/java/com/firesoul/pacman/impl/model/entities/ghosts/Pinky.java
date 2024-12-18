package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.Scene2D;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;

public class Pinky extends Ghost {

    /**
     * Creates Pinky, the pink ghost.
     * @param position
     * @param speed
     */
    public Pinky(final Vector2D position, final Vector2D speed, final Scene2D scene) {
        super(position, speed, "pinky", scene);
    }

    @Override
    protected void move() {
        
    }
    
    public void reset() {
        this.setDrawable(this.getAnimation(Directions.RIGHT));
        this.setPosition(new Vector2D(12, 64));
    }
}
