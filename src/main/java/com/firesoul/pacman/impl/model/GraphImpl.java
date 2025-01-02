package com.firesoul.pacman.impl.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
        this.nodes.get(source).addEdge(destination, weight);
        this.nodes.get(destination).addEdge(source, weight);
    }

    @Override
    public List<T> nodes() {
        return Collections.unmodifiableList(this.nodes.values().stream().map(t -> t.node()).toList());
    }

    @Override
    public Map<T, List<T>> edges() {
        return Collections.unmodifiableMap(this.nodes.values().stream().collect(Collectors.toMap(Node::node, t -> t.edges().keySet().stream().toList())));
    }

    @Override
    public List<T> edges(final T source) {
        return Collections.unmodifiableList(this.nodes.values().stream().filter(t -> t.node().equals(source)).flatMap(t -> t.edges().entrySet().stream()).map(t -> t.getKey()).toList());
    }

    @Override
    public void clear() {
        this.nodes.clear();
    }

    @Override
    public List<T> findShortestPath(final T source, final T destination) {
        this.nodes.get(source).setWeight(0);
        final Map<T, Node<T>> solutions = new LinkedHashMap<>();
        final PriorityQueue<Node<T>> queue = new PriorityQueue<>(this.nodes.values());
        while (!queue.isEmpty()) {
            final Node<T> src = queue.remove();
            solutions.put(src.node(), src);
            for (final var edge : src.edges().entrySet()) {
                final Node<T> dst = this.nodes.get(edge.getKey());
                final double weight = edge.getValue();
                this.relax(src, dst, weight);
            }
        }
        return computePath(solutions.get(source), solutions.get(destination), solutions).stream().map(t -> t.node()).toList();
    }

    private void relax(final Node<T> source, final Node<T> destination, final double weight) {
        final double newWeight = source.getWeight() + weight;
        if (destination.getWeight() > newWeight) {
            destination.setWeight(newWeight);
            destination.setFather(source.node());
        }
    }

    private List<Node<T>> computePath(final Node<T> source, final Node<T> destination, final Map<T, Node<T>> solutions) {
        final List<Node<T>> ret;
        if (destination == null) {
            ret = new LinkedList<>();
        } else if (source.equals(destination)) {
            ret = new LinkedList<>();
            ret.add(source);
        } else {
            final Node<T> father = solutions.get(destination.getFather());
            ret = computePath(source, father, solutions);
            if (father != null) {
                ret.add(destination);
            }
        }
        return ret;
    }
}
