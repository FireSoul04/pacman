package com.firesoul.pacman.impl.model.entities.ghosts;

import java.util.List;

import com.firesoul.pacman.impl.model.Pacman.Directions;
import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

public class Pinky extends Ghost {

    private List<Directions> allDirections = List.of(
        Directions.UP,
        Directions.DOWN,
        Directions.LEFT,
        Directions.RIGHT
    );

    /**
     * Creates Pinky, the pink ghost.
     */
    public Pinky(final Vector2D position) {
        super(position, "pinky", 4, Vector2D.down());
    }

    @Override
    protected void move() {
        if (this.allDirections.stream().filter(this::canMove).count() > 2 || this.getPath().isEmpty()) {
            this.changePathOnSituation();
        }
        this.followPath();
    }

    private void changePathOnSituation() {
        if (this.isVulnerable()) {
            this.clearPath();
            this.changePath(this.getMostFarPositionFromPlayer());
        } else {
            // final Vector2D node1 = this.closestNodeTo(this.rounded(getPlayerPosition().add(this.getPlayerDirection().dot(48))));
            this.changePath(this.getPlayerPosition());
        }
    }
}
