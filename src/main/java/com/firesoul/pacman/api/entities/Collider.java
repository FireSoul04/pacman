package com.firesoul.pacman.api.entities;

import com.firesoul.pacman.impl.util.Vector2D;

public interface Collider {

    /**
     * @return the bounds of this collider
     */
    Vector2D getBounds();
}
