package com.firesoul.pacman.impl.entities.ghosts;

import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.entities.Ghost;

public class Pinky extends Ghost {

    /**
     * Creates Pinky, the pink ghost.
     * @param position
     * @param speed
     */
    public Pinky(final Vector2D position, final Vector2D speed) {
        super(position, speed, "pinky");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        // TODO
        final Vector2D direction = Vector2D.right();
        final Vector2D imageSize = this.getDrawable().getImageSize();
        final Vector2D newPosition = this.getPosition().add(direction.dot(deltaTime));
        this.setPosition(newPosition.wrap(imageSize.invert(), Pacman.getRoomDimensions()));
        this.animate(direction);
    }
}
