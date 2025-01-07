package com.firesoul.pacman.impl.model.entities;

import java.util.Collections;
import java.util.List;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Invisible2D;

public class CageExit extends GameObject2D implements Collidable {

    private final Collider collider;
    private static final Vector2D SIZE = new Vector2D(16, 16); 

    public CageExit(final Vector2D position) {
        super(position, new Invisible2D(SIZE));
        this.collider = new BoxCollider2D(this, new Vector2D(8, 1), false);
        this.setVisible(false);
    }
    
    @Override
    public void onCollide(final Collider collider, final Collider other) {
    }

    @Override
    public List<Collider> getColliders() {
        return Collections.unmodifiableList(List.of(this.collider));
    }
}
