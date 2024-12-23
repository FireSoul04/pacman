package com.firesoul.pacman.impl.model.entities.colliders;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.entities.ColliderLayout;
import com.firesoul.pacman.impl.util.Vector2D;

public class ColliderSurfaceLayout implements ColliderLayout {

    /**
     * Just place the collider based on game object position.
     */
    @Override
    public Vector2D positionRelativeTo(final GameObject gameObject, final Vector2D size) {
        return gameObject.getPosition();
    }
}
