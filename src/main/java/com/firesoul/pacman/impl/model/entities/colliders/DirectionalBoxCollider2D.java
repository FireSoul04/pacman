package com.firesoul.pacman.impl.model.entities.colliders;

import java.util.HashMap;
import java.util.Map;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.DirectionalAnimation2D.Directions;

public class DirectionalBoxCollider2D extends BoxCollider2D {

    private final Map<Directions, Boolean> collideDirections;

    public DirectionalBoxCollider2D(final GameObject2D gameObject, final Vector2D dimensions) {
        super(gameObject, dimensions);
        this.collideDirections = new HashMap<>(Map.of(
            Directions.UP, false,
            Directions.DOWN, false,
            Directions.LEFT, false,
            Directions.RIGHT, false
        ));
    }

    @Override
    public boolean isColliding(final Collider other) {
        if (this.isOvelapping(other)) {
            final Collidable thisEntity = (Collidable) this.getAttachedEntity();
            final Collidable otherEntity = (Collidable) other.getAttachedEntity();
            thisEntity.onCollide(otherEntity);
            return true;
        }
        return false;
    }

    /**
     * @return The directions where the colliders is colliding.
     */
    public Map<Directions, Boolean> getDirections() {
        return this.collideDirections;
    }

    /**
     * Check if this collider is overlapping with the other collider.
     * The position of the colliders is the center of the attached entity.
     * 
     * @param other The other collider.
     * @return True if the colliders are overlapping, false otherwise.
     */
    private boolean isOvelapping(final Collider other) {
        final Vector2D d1 = this.getDimensions().dot(0.5);
        final Vector2D d2 = other.getDimensions().dot(0.5);
        final Vector2D i1 = this.getAttachedEntity().getDrawable().getImageSize().dot(0.5);
        final Vector2D i2 = other.getAttachedEntity().getDrawable().getImageSize().dot(0.5);
        final Vector2D c1 = this.getPosition().add(i1);
        final Vector2D c2 = other.getPosition().add(i2);

        final boolean collidingRight = c1.getX() - d1.getX() < c2.getX() + d2.getX();
        final boolean collidingLeft = c1.getX() + d1.getX() > c2.getX() - d2.getX();
        final boolean collidingUp = c1.getY() - d1.getY() < c2.getY() + d2.getY();
        final boolean collidingDown = c1.getY() + d1.getY() > c2.getY() - d2.getY();

        // TODO
        // this.collideDirections.put(Directions.RIGHT, collidingRight);
        // this.collideDirections.put(Directions.LEFT, collidingLeft);
        // this.collideDirections.put(Directions.UP, collidingUp);
        // this.collideDirections.put(Directions.DOWN, collidingDown);

        return collidingRight && collidingLeft && collidingUp && collidingDown;
    }
}