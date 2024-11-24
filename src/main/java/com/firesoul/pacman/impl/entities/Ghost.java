package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.impl.entities.bases.GameObject2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;

public abstract class Ghost extends GameObject2D implements Movable, Collidable {

    private Collider collider;

    /**
     * Create a ghost
     * @param position
     * @param speed
     */
    public Ghost(final Vector2D position, final Vector2D speed) {
        super(position, speed);
        collider = new BoxCollider2D(this, new Vector2D(16, 16));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide(final Collidable other) {
        if (other instanceof Player) {
            Player player = (Player) other;
            if (player.canEat()) {
                this.disable();
            } else {
                player.disable();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collider getCollider() {
        return this.collider;
    }
}