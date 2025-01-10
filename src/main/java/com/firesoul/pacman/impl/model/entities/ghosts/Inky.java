package com.firesoul.pacman.impl.model.entities.ghosts;

import java.util.List;

import com.firesoul.pacman.impl.model.Pacman.Directions;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

public class Inky extends Ghost {

    private List<Directions> allDirections = List.of(
        Directions.UP,
        Directions.DOWN,
        Directions.LEFT,
        Directions.RIGHT
    );
    
    /**
     * Creates Inky, the cyan ghost.
     */
    public Inky(final Vector2D position) {
        super(position, "inky", 6, Vector2D.up());
    }

    @Override
    protected void move() {
        if (this.allDirections.stream().filter(this::canMove).count() > 2 || this.getPath().isEmpty()) {
            this.changePathOnSituation();
        }
        this.followPath();
    }

    private void changePathOnSituation() {
        this.clearPath();
        if (this.isVulnerable()) {
            this.changePath(this.getMostFarPositionFromPlayer());
        } else {
            this.changePath(this.getPlayerPosition());
        }
    }
}
