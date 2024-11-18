package com.myprojects.pacman.api;

import java.util.List;
import java.util.NoSuchElementException;

import com.myprojects.pacman.impl.Entity;

public interface Room {

    List<Block> getBlocks();

    List<Entity> getEntities();

    void addEntity(final Entity entity) throws IllegalStateException, IllegalArgumentException;

    void removeEntity(final Entity entity) throws IllegalArgumentException, NoSuchElementException;
}
