package com.firesoul.pacman.impl.model;

import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.api.model.Scene;
import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.impl.controller.MapFileParserImpl;
import com.firesoul.pacman.impl.model.entities.PowerPill;
import com.firesoul.pacman.impl.util.Vector2D;

public class Scene2D implements Scene {

    private static final int DEFAULT_WIDTH = 224;
    private static final int DEFAULT_HEIGHT = 288;

    private final List<GameObject> gameObjects = new LinkedList<GameObject>();
    private final Graph<Vector2D> mapNodes;
    private final Vector2D dimensions;
    private Pacman pacman;

    /**
     * Default constructor for a room with no entities or blocks.
     */
    public Scene2D() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Scene2D(final int width, final int height) {
        this.dimensions = new Vector2D(width, height);
        this.mapNodes = new GraphImpl<>();
    }

    /**
     * Constructor for a room with a given entity and block map.
     * 
     * @param entityMapPath the path to the entity map
     * @param blockMapPath the path to the block map
     */
    public Scene2D(final String mapPath) {
        final MapFileParserImpl map = new MapFileParserImpl(mapPath);
        this.dimensions = map.getDimensions();
        this.mapNodes = map.getMapNodes();
        map.getGameObjects().forEach(this::addGameObject);
    }

    public void connectToGameLogic(final Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void updateAll(final double deltaTime) {
        for (final GameObject gameObject : this.gameObjects) {
            if (gameObject instanceof Movable movable) {
                movable.update(deltaTime);
            }
        }
        this.removeInactiveGameObjects();
        this.checkCollisions();
    }

    @Override
    public void pauseAll() {
        this.gameObjects.forEach(GameObject::pause);
    }

    @Override
    public void wakeAll() {
        this.gameObjects.forEach(GameObject::wake);
    }

    @Override
    public void addGameObject(final GameObject gameObject) {
        gameObject.setScene(this);
        this.gameObjects.add(gameObject);
        if (gameObject instanceof PowerPill powerPill) {
            powerPill.connectToGameLogic(this.pacman);
        }
    }

    @Override
    public void removeGameObject(final GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    @Override
    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(this.gameObjects);
    }

    @Override
    public Graph<Vector2D> getMapNodes() {
        return this.mapNodes;
    }

    /**
     * @return the dimensions of the room.
     */
    public Vector2D getDimensions() {
        return this.dimensions;
    }

    /**
     * Remove all inactive game objects from the room.
     */
    public void removeInactiveGameObjects() {
        final Iterator<GameObject> it = this.gameObjects.iterator();
        while (it.hasNext()) {
            final GameObject gameObject = it.next();
            if (!gameObject.isActive()) {
                it.remove();
            }
        }
    }

    private void checkCollisions() {
        for (final GameObject g1 : this.gameObjects) {
            if (g1 instanceof Movable) {
                for (final GameObject g2 : this.gameObjects) {
                    if (g1 != g2 && g1 instanceof Collidable c1 && g2 instanceof Collidable c2) {
                        checkCollidersCollisions(c1, c2);
                    }
                }
            }
        }
    }

    private void checkCollidersCollisions(final Collidable g1, final Collidable g2) {
        for (final Collider c1 : g1.getColliders()) {
            for (final Collider c2 : g2.getColliders()) {
                if (c1.isColliding(c2)) {
                    g1.onCollide(c1, c2);
                    g2.onCollide(c2, c1);
                }
            }
        }
    }
}
