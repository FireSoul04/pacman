package com.firesoul.pacman.impl.model;

import java.util.*;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.Pacman.Directions;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.model.entities.colliders.ColliderCenterLayout;
import com.firesoul.pacman.impl.util.Vector2D;

public class SolidObject2D extends GameObject2D implements Collidable {

    /**
     * Colliders for directions are placed in every edge of the sprite of thi object
     */
    private final Set<Directions> move = new HashSet<>();
    private final Map<Directions, Collider> colliders;

    public SolidObject2D(final Vector2D position, final Vector2D speed, final Scene2D scene, final Vector2D spriteSize, final Vector2D objectSize) {
        super(position, speed, scene);
        final Vector2D sizeHorizontal = new Vector2D(spriteSize.getX() - 1, 1);
        final Vector2D sizeVertical = new Vector2D(1, spriteSize.getY() - 1);
        this.colliders = new HashMap<>(Map.of(
            Directions.NONE, new BoxCollider2D(this, objectSize, new ColliderCenterLayout()),
            Directions.UP, new BoxCollider2D(this, sizeHorizontal, 
                (g, s) -> g.getPosition().add(Vector2D.up()).sub(objectSize).add(new Vector2D(0.5, 0.5))),
            Directions.DOWN, new BoxCollider2D(this, sizeHorizontal, 
                (g, s) -> g.getPosition().add(Vector2D.down()).add(sizeVertical.sub(new Vector2D(0.5, 0.5))).sub(objectSize)),
            Directions.LEFT, new BoxCollider2D(this, sizeVertical, 
                (g, s) -> g.getPosition().add(Vector2D.left()).sub(objectSize).add(new Vector2D(0.5, 0.5))),
            Directions.RIGHT, new BoxCollider2D(this, sizeVertical, 
                (g, s) -> g.getPosition().add(Vector2D.right()).add(sizeHorizontal.sub(new Vector2D(0.5, 0.5))).sub(objectSize))
        ));
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
    }

    @Override
    public List<Collider> getColliders() {
        return Collections.unmodifiableList(this.colliders
            .values()
            .stream()
            .toList());
    }

    /**
     * @param direction
     * @return if the object can move in the given direction
     */
    public boolean canMove(final Directions direction) {
        return !this.move.contains(direction);
    }

    /**
     * Check if the solid object is not colliding an obstacle.
     */
    public void checkMove() {
        this.colliders.forEach((k, v) -> {
            if (!k.equals(Directions.NONE)) {
                if (v.hasCollidedLastFrame()) {
                    this.move.add(k);
                }
            }
        });
    }

    /**
     * @param other
     * @return the directions of the collier
     */
    public Directions getDirectionFromCollider(final Collider other) {
        Directions d = Directions.NONE;
        for (final var entry : this.colliders.entrySet()) {
            if (entry.getValue().equals(other)) {
                d = entry.getKey();
            }
        }
        return d;
    }

    /**
     * Move the colliders based on object's direction and colliders' layout.
     */
    protected void moveColliders() {
        for (final Collider c : this.getColliders()) {
            c.update();
            this.move.clear();
        }
    }
}
