package com.firesoul.pacman.api.model;

import java.util.List;
import java.util.Map;

public interface Graph<T> {

    void addNode(T node);

    void removeNode(T node);

    void addEdge(T source, T destination, double weight);

    void removeEdge(T source, T destination);

    void removeEdges(T source);

    List<T> nodes();

    Map<T, Map<T, Double>> edges();

    Map<T, Double> edgesOf(T source);

    void clear();
}
