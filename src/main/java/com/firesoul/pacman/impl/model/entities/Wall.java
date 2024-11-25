package com.firesoul.pacman.impl.model.entities;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Sprite2D;

public class Wall extends GameObject2D implements Collidable {

    private final Collider collider;

    public Wall(final Vector2D position) {
        super(position, Vector2D.zero(), new Sprite2D("wall"));
        this.collider = new BoxCollider2D(this, position);
    }

    @Override
    public void onCollide(final Collidable other) {
        
    }

    @Override
    public Collider getCollider() {
        return this.collider;
    }
    
}
