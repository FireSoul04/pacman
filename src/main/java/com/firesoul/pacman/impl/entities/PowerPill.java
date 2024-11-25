package com.firesoul.pacman.impl.entities;

import com.firesoul.pacman.api.entities.Collidable;
import com.firesoul.pacman.api.entities.Collider;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.entities.bases.GameObject2D;
import com.firesoul.pacman.impl.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Sprite2D;

public class PowerPill extends GameObject2D implements Collidable {

    private final Collider collider;

    /**
     * Create a pill that if eaten by pacman ghosts are vulnerable
     * @param position
     */
    public PowerPill(final Vector2D position) {
        super(position, Vector2D.zero(), new Sprite2D("powerpill"));
        this.collider = new BoxCollider2D(this, new Vector2D(8, 8));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollide(final Collidable other) {
        if (other instanceof Player) {
            this.disable();
            Pacman.setGhostVulnerable();
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
