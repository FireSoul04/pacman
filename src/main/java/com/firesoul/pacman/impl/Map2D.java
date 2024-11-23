package com.firesoul.pacman.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.firesoul.pacman.api.Block;
import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.Map;
import com.firesoul.pacman.api.entities.Entity;
import com.firesoul.pacman.impl.util.Vector2D;

public class Map2D implements Map {

    private Vector2D bounds;

    private final String entityMapPath;
    private final String blockMapPath;

    /**
     * Create a 2 dimensional map with no entities or blocks.
     */
    public Map2D(final int width, final int height) {
        this("", "");
        this.bounds = new Vector2D(width, height);
    }

    /**
     * Create a 2 dimensional map reading entites and blocks from the given paths.
     */
    public Map2D(final String entityMapPath, final String blockMapPath) {
        this.entityMapPath = entityMapPath;
        this.blockMapPath = blockMapPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entity> getEntityMap() {
        return this.getMap(this.entityMapPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Block> getBlockMap() {
        return this.getMap(this.blockMapPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameObject> getGameObjects() {
        final List<GameObject> ret = new ArrayList<>(this.getBlockMap());
        ret.addAll(this.getEntityMap());
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getDimensions() {
        return this.bounds;
    }
    
    @SuppressWarnings("unchecked")
    private <T> List<T> getMap(final String mapPath) {
        final List<T> map = Collections.emptyList();
        try (
            final ObjectInputStream reader = new ObjectInputStream(
                new BufferedInputStream(
                    ClassLoader.getSystemResourceAsStream(mapPath)
                )
            )
        ) {
            this.bounds = (Vector2D) reader.readObject();
            map.addAll((List<T>) reader.readObject());
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return map;
    }
}
