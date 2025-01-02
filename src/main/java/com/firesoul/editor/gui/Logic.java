package com.firesoul.editor.gui;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.firesoul.editor.gui.LogicImpl.GameObjects;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.impl.model.MapNode;
import com.firesoul.pacman.impl.util.Vector2D;

public interface Logic {

    void save();

    void load();

    void reset();

    ConcurrentLinkedQueue<GameObject> getGameObjects();

    void addGameObject(GameObjects selected, Vector2D position, Vector2D size);

    void removeGameObject(Vector2D position);

    void removeWall(Vector2D position);

    void click(GameObjects selected, Vector2D position);

    void linkNodes();

    void removeNode(Vector2D p);

    long howManyInstanciesOf(GameObjects g);

    boolean isLimited(GameObjects g);

    Graph<MapNode> getMap();
}