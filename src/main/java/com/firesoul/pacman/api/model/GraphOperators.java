package com.firesoul.pacman.api.model;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map;

public interface GraphOperators {

    static <T> Node<T> node(T node) {
        return new Node<>(node);
    }

    public static class Node<T> implements Comparable<Node<T>> {
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

    public static <T> List<T> findShortestPath(final Graph<T> g, final T source, final T destination) {
        final Map<T, Node<T>> nodes = g.nodes().stream().map(Node::new).collect(Collectors.toMap(Node::node, Function.identity()));
        nodes.get(source).setWeight(0);
        final PriorityQueue<Node<T>> queue = new PriorityQueue<>(nodes.values());
        while (!queue.isEmpty()) {
            final Node<T> src = queue.remove();
            for (final var edge : g.edgesOf(src.node()).entrySet()) {
                final Node<T> dst = nodes.get(edge.getKey());
                final double weight = edge.getValue();
                relax(src, dst, weight);
            }
        }
        return computePath(nodes.get(source), nodes.get(destination), nodes).stream().map(Node::node).toList();
    }

    private static <T> void relax(final Node<T> source, final Node<T> destination, final double weight) {
        final double newWeight = source.getWeight() + weight;
        if (destination.getWeight() > newWeight) {
            destination.setWeight(newWeight);
            destination.setFather(source);
        }
    }

    private static <T> List<Node<T>> computePath(final Node<T> source, final Node<T> destination, final Map<T, Node<T>> nodes) {
        final List<Node<T>> ret = new LinkedList<>();
        if (destination == null) {
            return ret;
        } else if (source.equals(destination)) {
            ret.add(source);
            return ret;
        } else {
            Node<T> current = destination;
            while (current != null && !current.equals(source)) {
                ret.addFirst(current);
                current = current.getFather();
            }
            if (current == null) {
                return new LinkedList<>();
            }
            ret.addFirst(source);
            return ret;
        }
    }
}
