package com.firesoul.pacman.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.firesoul.pacman.api.Block;
import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.Map;
import com.firesoul.pacman.api.entities.Entity;

public class Map2D implements Map {

    private final String entityMapPath;
    private final String blockMapPath;

    /**
     * Create a 2 dimensional map
     */
    public Map2D(final String entityMapPath, final String blockMapPath) {
        this.entityMapPath = entityMapPath;
        this.blockMapPath = blockMapPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entity> getEntityMap() throws IOException, ClassNotFoundException {
        return this.getMap(this.entityMapPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Block> getBlockMap() throws IOException, ClassNotFoundException {
        return this.getMap(this.blockMapPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameObject> getGameObjects() throws IOException, ClassNotFoundException {
        final List<GameObject> ret = new ArrayList<>(this.getBlockMap());
        ret.addAll(this.getEntityMap());
        return ret;
    }
    
    private <T> List<T> getMap(final String mapPath) throws IOException, ClassNotFoundException {
        final List<T> map = new ArrayList<>();
        try (
            final ObjectInputStream reader = new ObjectInputStream(
                new BufferedInputStream(
                    ClassLoader.getSystemResourceAsStream(mapPath)
                )
            )
        ) {
            System.out.println(reader.readObject());
        }
        return map;
    }
}
