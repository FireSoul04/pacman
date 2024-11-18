package com.myprojects.pacman.api;

import java.util.List;
import java.util.NoSuchElementException;

import com.myprojects.pacman.impl.entities.Entity;

public interface Room {

    /**
     * @return Get all blocks in the room
     */
    List<Block> getBlocks();

    /**
     * @return Get all entities in the room
     */
    List<Entity> getEntities();

    /**
     * Instantiate an entity in the room
     * @param entity to add
     * @throws IllegalArgumentException when entity is null
     * @throws IllegalStateException if the list of entites is full
     */
    void addEntity(final Entity entity) throws IllegalArgumentException, IllegalStateException;

    /**
     * Remove an entity from the room
     * @param entity to remove
     * @throws IllegalArgumentException when entity is null
     * @throws NoSuchElementException if it's not present inside the list
     */
    void removeEntity(final Entity entity) throws IllegalArgumentException, NoSuchElementException;
}
