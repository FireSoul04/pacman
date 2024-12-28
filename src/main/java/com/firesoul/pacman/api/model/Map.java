package com.firesoul.pacman.api.model;

import java.util.List;

import com.firesoul.editor.gui.Pair;
import com.firesoul.pacman.api.util.Vector;
import com.firesoul.pacman.impl.util.Vector2D;

public interface Map {

    /**
     * @return all game objects in the map
     */
    List<GameObject> getGameObjects();

    /**
     * @return the dimensions of the map
     */
    Vector getDimensions();

    /**
     * @return the graph representing all the nodes in the map
     */
    List<Pair<Vector2D, List<Vector2D>>> getMapNodes();
}
