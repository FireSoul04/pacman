package com.firesoul.pacman.impl.model.entities;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Sprite2D;

public class Pill extends GameObject2D implements Collidable {

    private final Collider collider;

    /**
     * Create a little pill that is required to conclude the current level
     * @param position
     */
    public Pill(final Vector2D position) {
        super(position, Vector2D.zero(), new Sprite2D("pill"));
        this.collider = new BoxCollider2D(this, new Vector2D(2, 2));
    }

    @Override
    public void onCollide(final Collidable other) {
        if (other instanceof Player) {
            this.disable();
        }
    }

    @Override
    public Collider getCollider() {
        return this.collider;
    }
}
