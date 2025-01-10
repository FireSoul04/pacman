package com.firesoul.pacman.impl.model.entities;

import java.util.List;

import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Sprite2D;

public class PowerPill extends GameObject2D implements Collidable {

    private static final int EATEN_SCORE = 1000;

    private final Collider collider;
    private Pacman pacman;

    /**
     * Create a pill that if eaten by pacman ghosts are vulnerable
     * @param position
     */
    public PowerPill(final Vector2D position) {
        super(position, new Sprite2D("powerpill"));
        this.collider = new BoxCollider2D(this, new Vector2D(8, 8));
    }

    public void connectToGameLogic(final Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void onCollide(final Collider collider, final Collider other) {
        final Collidable gameObject = (Collidable) other.getAttachedGameObject();
        if (gameObject instanceof Player player && player.bodyIsCollidingWith(other)) {
            this.disable();
            this.pacman.setGhostVulnerable();
            this.pacman.addScore(EATEN_SCORE);
        }
    }

    @Override
    public List<Collider> getColliders() {
        return List.of(this.collider);
    }
}
