package com.firesoul.pacman.impl.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.firesoul.pacman.api.model.Graph;

public class GraphImpl<T> implements Graph<T> {

    private final Map<T, Map<T, Double>> nodes = new LinkedHashMap<>();

    @Override
    public void addNode(final T node) {
        this.nodes.putIfAbsent(node, new LinkedHashMap<>());
    }

    @Override
    public void removeNode(final T node) {
        this.removeEdges(node);
        this.nodes.remove(node);
    }

    @Override
    public void addEdge(final T source, final T destination, final double weight) {
        if (!this.nodes.containsKey(source) || !this.nodes.containsKey(destination)) {
            throw new IllegalStateException("Cannot add an edge to a non existant node");
        }
        this.nodes.get(source).put(destination, weight);
        this.nodes.get(destination).put(source, weight);
    }

    @Override
    public void removeEdge(final T source, final T destination) {
        if (!this.nodes.containsKey(source) || !this.nodes.containsKey(destination)) {
            throw new IllegalStateException("Cannot add an edge to a non existant node");
        }
        this.nodes.get(source).remove(destination);
        this.nodes.get(destination).remove(source);
    }

    @Override
    public void removeEdges(final T source) {
        this.nodes.get(source).clear();
        this.nodes.forEach((k, v) -> v.remove(source));
    }

    @Override
    public List<T> nodes() {
        return Collections.unmodifiableList(List.copyOf(this.nodes.keySet()));
    }

    @Override
    public Map<T, Map<T, Double>> edges() {
        return Collections.unmodifiableMap(this.nodes);
    }

    @Override
    public Map<T, Double> edgesOf(final T source) {
        return Collections.unmodifiableMap(this.nodes.get(source));
    }

    @Override
    public void clear() {
        this.nodes.clear();
    }

    @Override
    public int hashCode() {
        return this.nodes.hashCode();
    }
}
