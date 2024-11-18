package com.firesoul.pacman.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.firesoul.pacman.api.Block;
import com.firesoul.pacman.api.Entity;
import com.firesoul.pacman.api.Map;

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
    @SuppressWarnings("unchecked")
    public List<Entity> getEntityMap() throws IOException, ClassNotFoundException {
        return (List<Entity>)this.getMap(this.entityMapPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Block> getBlockMap() throws IOException, ClassNotFoundException {
        return (List<Block>)this.getMap(this.blockMapPath);
    }
    
    private List<? extends Object> getMap(final String mapPath) throws IOException, ClassNotFoundException {
        final List<? extends Object> map = new ArrayList<>();
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
