package com.firesoul.pacman.api.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface Graph<T> {

    static <T> Node<T> node(T node) {
        return new Node<>(node);
    }

    public static class Node<T> implements Comparable<Node<T>> {
        private final Map<Node<T>, Double> edges = new LinkedHashMap<>();
        private final T node;
        private double weight;
        private Node<T> father = null;

        private Node(final T node, final double weight) {
            this.node = node;
            this.weight = weight;
        }

        private Node(final T node) {
            this(node, Double.POSITIVE_INFINITY);
        }
        
        public T node() {
            return node;
        }
        
        public Map<Node<T>, Double> edges() {
            return Collections.unmodifiableMap(this.edges);
        }

        public void addEdge(final Node<T> node, final double weight) {
            this.edges.put(node, weight);
        }

        public void removeEdge(final Node<T> node) {
            this.edges.remove(node);
        }

        public void removeEdges() {
            this.edges.clear();
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(final double weight) {
            this.weight = weight;
        }
        
        public Node<T> getFather() {
            return father;
        }

        public void setFather(final Node<T> father) {
            this.father = father;
        }

        @Override
        public int compareTo(final Node<T> o) {
            return Double.compare(this.getWeight(), o.getWeight());
        }
    }

    void addNode(T node);

    void removeNode(T node);

    void addEdge(T source, T destination, double weight);

    List<T> nodes();

    Map<T, List<T>> edges();

    List<T> edges(T source);

    void clear();

    List<T> findShortestPath(T source, T destination);
}
