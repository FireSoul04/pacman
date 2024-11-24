package com.firesoul.pacman.impl.entities.ghosts;

import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Animation2D;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.entities.Ghost;

public class Blinky extends Ghost {

    private enum Animations {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        private final Animation2D animation;

        private Animations() {
            this.animation = new Animation2D("blinky", "blinky_" + this.name().toLowerCase(), Timer.secondsToMillis(ANIMATION_SPEED));
        }
    }

    private static final double ANIMATION_SPEED = 0.1;

    /**
     * Creates Blinky, the red ghost.
     * @param position
     * @param speed
     */
    public Blinky(final Vector2D position, final Vector2D speed) {
        super(position, speed);
        this.setDrawable(Animations.RIGHT.animation);
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

    private void animate(final Vector2D direction) {
        final Animation2D animation = (Animation2D)this.getDrawable();
        if (direction.equals(Vector2D.zero())) {
            animation.stop();
            animation.reset();
        } else {
            this.changeVariant(direction);
            animation.start();
            animation.update();
        }
    }

    private void changeVariant(final Vector2D direction) {
        if (direction.equals(Vector2D.up())) {
            this.setDrawable(Animations.UP.animation);
        } else if (direction.equals(Vector2D.down())) {
            this.setDrawable(Animations.DOWN.animation);
        } else if (direction.equals(Vector2D.left())) {
            this.setDrawable(Animations.LEFT.animation);
        } else if (direction.equals(Vector2D.right())) {
            this.setDrawable(Animations.RIGHT.animation);
        }
    }
}
