package com.firesoul.pacman.impl.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import com.firesoul.pacman.api.model.Graph;

public class GraphImpl<T> implements Graph<T> {

    private final Map<T, Node<T>> nodes = new LinkedHashMap<>();

    @Override
    public void addNode(final T node) {
        this.nodes.put(node, Graph.node(node));
    }

    @Override
    public void removeNode(T node) {
        this.nodes.get(node).removeEdges();
        this.nodes.remove(node);
    }

    @Override
    public void addEdge(final T source, final T destination, final double weight) {
        if (!this.nodes.containsKey(source)) {
            throw new IllegalStateException("Cannot add an edge to a non existant node");
        }
        this.nodes.get(source).addEdge(Graph.node(destination), weight);
        this.nodes.get(destination).addEdge(Graph.node(source), weight);
    }

    @Override
    public List<T> nodes() {
        return Collections.unmodifiableList(this.nodes.values().stream().map(t -> t.node()).toList());
    }

    @Override
    public Map<T, List<T>> edges() {
        return Collections.unmodifiableMap(this.nodes.values().stream().collect(Collectors.toMap(Node::node, t -> t.edges().keySet().stream().map(Node::node).toList())));
    }

    @Override
    public List<T> edges(final T source) {
        return Collections.unmodifiableList(this.nodes.values().stream().filter(t -> t.node().equals(source)).flatMap(t -> t.edges().entrySet().stream()).map(t -> t.getKey().node()).toList());
    }

    @Override
    public void clear() {
        this.nodes.clear();
    }

    @Override
    public List<T> findShortestPath(final T source, final T destination) {
        final List<T> path = new LinkedList<>();
        final Queue<Node<T>> queue = new LinkedList<>();
        this.nodes.get(source).setWeight(0);
        queue.add(Graph.node(source));
        while (!queue.isEmpty()) {
            final Node<T> src = queue.stream().min((a, b) -> Double.compare(a.getWeight(), b.getWeight())).get();
            path.add(src.node());
            for (final var edge : src.edges().entrySet()) {
                final Node<T> dst = edge.getKey();
                final double weight = edge.getValue();
                this.relax(src, dst, weight);
            }
        }
        return path;
    }

    private void relax(final Node<T> source, final Node<T> destination, final double weight) {
        final double newWeight = destination.getWeight() + weight;
        if (source.getWeight() > newWeight) {
            source.setWeight(newWeight);
        }
    }
}
