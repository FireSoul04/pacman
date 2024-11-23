package com.firesoul.pacman.impl.entities;

import java.util.List;
import java.util.ArrayList;

import com.firesoul.pacman.api.Block;
import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.Room;
import com.firesoul.pacman.api.entities.Movable;
import com.firesoul.pacman.impl.Map2D;
import com.firesoul.pacman.impl.util.Vector2D;

public class Room2D implements Room {

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 300;

    private final Map2D map;
    private final List<GameObject> gameObjects;

    /**
     * Default constructor for a room with no entities or blocks.
     */
    public Room2D() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Room2D(final int width, final int height) {
        this.map = new Map2D(width, height);
        this.gameObjects = new ArrayList<GameObject>();
    }

    /**
     * Constructor for a room with a given entity and block map.
     * 
     * @param entityMapPath the path to the entity map
     * @param blockMapPath the path to the block map
     */
    public Room2D(final String entityMapPath, final String blockMapPath) {
        this.map = new Map2D(entityMapPath, blockMapPath);
        this.gameObjects = new ArrayList<GameObject>();
        this.gameObjects.addAll(this.map.getGameObjects());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAll(final double deltaTime) {
        for (final GameObject gameObject : this.gameObjects) {
            if (gameObject instanceof Movable) {
                ((Movable) gameObject).update(deltaTime);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGameObject(final GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeGameObject(final GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameObject> getGameObjects() {
        return List.copyOf(this.gameObjects);
    }

    /**
     * @return the blocks in the room.
     */
    public List<Block> getBlocks() {
        return filterGameObject(Block.class);
    }

    /**
     * @return the entities in the room.
     */
    public List<Entity2D> getEntities() {
        return filterGameObject(Entity2D.class);
    }

    /**
     * @return the dimensions of the room.
     */
    public Vector2D getDimensions() {
        return this.map.getDimensions();
    }

    private <T> List<T> filterGameObject(final Class<T> clazz) {
        final List<T> filtered = new ArrayList<T>();
        for (final GameObject gameObject : this.gameObjects) {
            if (clazz.isInstance(gameObject)) {
                filtered.add(clazz.cast(gameObject));
            }
        }
        return filtered;
    }
}
