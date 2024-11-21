package com.firesoul.pacman.api;

import java.util.List;
import java.util.NoSuchElementException;

import com.firesoul.pacman.impl.entities.Entity2D;

public interface Room {

    /**
     * @return Get all blocks in the room
     */
    List<Block> getBlocks();

    /**
     * @return Get all entities in the room
     */
    List<Entity2D> getEntities();

    /**
     * Instantiate an entity in the room
     * @param entity to add
     * @throws IllegalArgumentException when entity is null
     * @throws IllegalStateException if the list of entites is full
     */
    void addEntity(Entity2D entity) throws IllegalArgumentException, IllegalStateException;

    /**
     * Remove an entity from the room
     * @param entity to remove
     * @throws IllegalArgumentException when entity is null
     * @throws NoSuchElementException if it's not present inside the list
     */
    void removeEntity(Entity2D entity) throws IllegalArgumentException, NoSuchElementException;
}
