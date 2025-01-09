package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

import java.util.Map;

import com.firesoul.pacman.impl.model.Pacman.Directions;

public class Blinky extends Ghost {

    private Map<Vector2D, Directions> allDirections = Map.of(
        Vector2D.up(), Directions.UP,
        Vector2D.down(), Directions.DOWN,
        Vector2D.left(), Directions.LEFT,
        Vector2D.right(), Directions.RIGHT
    );

    /**
     * Creates Blinky, the red ghost.
     */
    public Blinky(final Vector2D position) {
        super(position, "blinky", 0, Vector2D.right());
    }

    @Override
    protected void move() {
        if (this.allDirections.values().stream().filter(this::canMove).count() > 2 || this.getPath().isEmpty()) {
            this.changePath((this.getPlayerPosition()));
        }
        this.followPath();
    }
}
