package com.firesoul.pacman.impl.model;

import java.util.List;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class MapNode extends GameObject2D implements Collidable {

    private static final Vector2D SIZE = new Vector2D(8, 8);

    private Collider collider;

    public MapNode(final Vector2D position) {
        super(position);
        this.collider = new BoxCollider2D(this, SIZE);
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(this.collider);
    }
}
