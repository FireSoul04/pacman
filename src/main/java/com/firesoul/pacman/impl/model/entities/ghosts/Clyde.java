package com.firesoul.pacman.impl.model.entities.ghosts;

import java.util.List;

import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.model.Pacman.Directions;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

public class Clyde extends Ghost {

    private List<Directions> allDirections = List.of(
        Directions.UP,
        Directions.DOWN,
        Directions.LEFT,
        Directions.RIGHT
    );

    /**
     * Creates Clyde, the orange ghost.
     */
    public Clyde(final Vector2D position) {
        super(position, "clyde", 8, Vector2D.up());
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
            if (Pacman.distance(this.getPosition(), this.getPlayerPosition()) > 48) {
                this.changePath(this.getRandomNode());
            } else {
                this.changePath(this.getPlayerPosition());
            }
        }
    }
}
