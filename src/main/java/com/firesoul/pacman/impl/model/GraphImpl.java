package com.firesoul.pacman.impl.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.firesoul.pacman.api.model.Graph;

public class GraphImpl<T> implements Graph<T> {

    private final Map<T, Map<T, Double>> nodes;

    public GraphImpl() {
        this.nodes = new LinkedHashMap<>();
    }

    @Override
    public void addNode(final T node) {
        this.nodes.putIfAbsent(node, new LinkedHashMap<>());
    }

    @Override
    public void addEdge(final T source, final T destination, final double weight) {
        if (!this.nodes.containsKey(source)) {
            throw new IllegalStateException("Cannot add an edge to a non existant node");
        }
        this.nodes.get(source).put(destination, weight);
    }

    @Override
    public void clear() {
        this.nodes.clear();
    }
}
