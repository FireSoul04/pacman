package com.firesoul.pacman.impl.model.entities.colliders;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.entities.ColliderLayout;
import com.firesoul.pacman.impl.util.Vector2D;

public class ColliderCenterLayout implements ColliderLayout {

    /**
     * Calculate the position relative to the center of the game object
     */
    @Override
    public Vector2D positionRelativeTo(final GameObject gameObject, final Vector2D size) {
        return gameObject
            .getPosition()
            .sub(size.dot(0.5));
    }
    
}
