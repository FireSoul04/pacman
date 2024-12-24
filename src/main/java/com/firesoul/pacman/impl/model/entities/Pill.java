package com.firesoul.pacman.impl.model.entities;

import java.util.Collections;
import java.util.List;

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
        super(position, new Sprite2D("pill"));
        this.collider = new BoxCollider2D(this, new Vector2D(2, 2));
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
        final Collidable gameObject = (Collidable) other.getAttachedGameObject();
        if (gameObject instanceof Player player && player.bodyIsCollidingWith(other)) {
            this.disable();
        }
    }

    @Override
    public List<Collider> getColliders() {
        return Collections.unmodifiableList(List.of(this.collider));
    }
}
