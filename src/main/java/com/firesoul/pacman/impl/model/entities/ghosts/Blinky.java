package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

import java.util.List;

import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Blinky extends Ghost {

    private List<Directions> allDirections = List.of(
        Directions.UP,
        Directions.DOWN,
        Directions.LEFT,
        Directions.RIGHT
    );

    /**
     * Creates Blinky, the red ghost.
     */
    public Blinky(final Vector2D position) {
        super(position, "blinky", 0, Vector2D.right());
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
