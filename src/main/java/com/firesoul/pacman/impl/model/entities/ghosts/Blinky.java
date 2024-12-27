package com.firesoul.pacman.impl.model.entities.ghosts;

import com.firesoul.pacman.impl.model.entities.Ghost;
import com.firesoul.pacman.impl.util.Vector2D;

import java.util.List;
import java.util.Random;

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
        if (this.allDirections.stream().filter(this::canMove).count() > 2) {
            this.setNextDirection(this.getNextRandomDirection());
        }
        while (!this.canMove(this.getDirectionFromVector(this.getNextDirection()))) {
            this.setNextDirection(this.getRandomDirection());
        }
    }

    private Vector2D getRandomDirection() {
        final Random rand = new Random();
        final int n = rand.nextInt(4);
        return switch (n) {
            case 0 -> Vector2D.up();
            case 1 -> Vector2D.down();
            case 2 -> Vector2D.left();
            default -> Vector2D.right();
        };
    }

    private Vector2D getNextRandomDirection() {
        final Random rand = new Random();
        final int n = rand.nextInt(5);
        if (this.getCurrentDirection().equals(Vector2D.up()) || this.getCurrentDirection().equals(Vector2D.down())) {
            return switch(n) {
                case 0 -> Vector2D.left();
                case 1 -> Vector2D.right();
                default -> this.getCurrentDirection();
            };
        } else {
            return switch(n) {
                case 0 -> Vector2D.up();
                case 1 -> Vector2D.down();
                default -> this.getCurrentDirection();
            };
        }
    }
}
