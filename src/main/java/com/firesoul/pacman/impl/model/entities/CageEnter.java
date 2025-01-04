package com.firesoul.pacman.impl.model.entities;

import com.firesoul.pacman.impl.model.GameObject2D;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Invisible2D;

public class CageEnter extends GameObject2D {

    private static final Vector2D SIZE = new Vector2D(16, 16); 

    public CageEnter(final Vector2D position) {
        super(position, new Invisible2D(SIZE));
    }
}
