package com.firesoul.pacman.impl.model.entities;

import java.util.List;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.model.entities.colliders.ColliderSurfaceLayout;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Invisible2D;

public class Wall extends GameObject2D implements Collidable {

    private final Collider collider;

    public Wall(final Vector2D position, final Vector2D size) {
        super(position, new Invisible2D(size));
        this.collider = new BoxCollider2D(this, size, new ColliderSurfaceLayout(), true);
        this.setVisible(false);
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(this.collider);
    }
}
