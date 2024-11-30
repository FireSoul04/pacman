package com.firesoul.pacman.impl.model.entities;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class Wall extends GameObject2D implements Collidable {

    private final Collider collider;

    public Wall(final Vector2D position, final Vector2D size) {
        super(position, Vector2D.zero());
        this.collider = new BoxCollider2D(this, size);
    }

    @Override
    public void onCollide(final Collidable other) {
        
    }

    @Override
    public Collider getCollider() {
        return this.collider;
    }
    
}
