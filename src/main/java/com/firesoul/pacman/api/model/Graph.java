package com.firesoul.pacman.api.model;

public interface Graph<T> {

    void addNode(T node);

    void addEdge(T source, T destination, double weight);

    void clear();
}
