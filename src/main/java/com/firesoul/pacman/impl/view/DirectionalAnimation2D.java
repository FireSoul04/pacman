package com.firesoul.pacman.impl.view;

import java.util.Map;

import com.firesoul.pacman.impl.model.Pacman.Directions;

public class DirectionalAnimation2D {

    private final Map<Directions, Animation2D> animations;

    /**
     * Bidimensional animation for a sprite that is based on more directions.
     * @param name of the entity to get from.
     * @param animationSpeed frequency of the animation.
     */
    public DirectionalAnimation2D(final String name, final long animationSpeed) {
        this.animations = Map.of(
            Directions.UP, new Animation2D(name, name + "_up", animationSpeed),
            Directions.DOWN, new Animation2D(name, name + "_down", animationSpeed),
            Directions.LEFT, new Animation2D(name, name + "_left", animationSpeed),
            Directions.RIGHT, new Animation2D(name, name + "_right", animationSpeed)
        );
    }

    /**
     * Get the animation based on the direction given.
     * @param direction where the animation is directed.
     * @return the animation in that direction.
     */
    public Animation2D getAnimation(final Directions direction) {
        return this.animations.get(direction);
    }
}
