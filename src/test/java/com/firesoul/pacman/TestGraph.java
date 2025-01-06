package com.firesoul.pacman;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.api.model.GraphOperators;
import com.firesoul.pacman.impl.model.GraphImpl;
import com.firesoul.pacman.impl.util.Vector2D;

public class TestGraph {

    private Map<String, Vector2D> nodes = Map.of(
        "node1", new Vector2D(0, 0),
        "node2", new Vector2D(1, 1),
        "node3", new Vector2D(2, 5),
        "node4", new Vector2D(2, 2),
        "node5", new Vector2D(3, 2),
        "node6", new Vector2D(5, 1),
        "node7", new Vector2D(6, 1),
        "node8", new Vector2D(4, 5)
    );
    private Map<Vector2D, Map<Vector2D, Double>> edges = Map.of(
        nodes.get("node1"), Map.of(
            nodes.get("node2"), 2.0,
            nodes.get("node3"), 8.0,
            nodes.get("node4"), 3.0,
            nodes.get("node6"), 6.0
        ),
        nodes.get("node2"), Map.of(
            nodes.get("node1"), 2.0,
            nodes.get("node3"), 5.0
        ),
        nodes.get("node3"), Map.of(
            nodes.get("node1"), 8.0,
            nodes.get("node2"), 5.0,
            nodes.get("node4"), 4.0,
            nodes.get("node5"), 4.0,
            nodes.get("node6"), 3.0
        ),
        nodes.get("node4"), Map.of(
            nodes.get("node1"), 3.0,
            nodes.get("node3"), 4.0,
            nodes.get("node6"), 2.0
        ),
        nodes.get("node5"), Map.of(
            nodes.get("node3"), 4.0,
            nodes.get("node6"), 1.0
        ),
        nodes.get("node6"), Map.of(
            nodes.get("node3"), 3.0,
            nodes.get("node4"), 2.0,
            nodes.get("node5"), 1.0
        ),
        nodes.get("node7"), Map.of(
            nodes.get("node8"), 3.0
        ),
        nodes.get("node8"), Map.of(
            nodes.get("node7"), 3.0
        )
    );
    private Graph<Vector2D> graph = new GraphImpl<>();

    @BeforeEach
    void fillGraph() {
        nodes.values().forEach(t -> graph.addNode(t));
        edges.forEach((k, v) -> v.forEach((n, w) -> graph.addEdge(k, n, w)));
    }

    @Test
    void dijkstra() {
        assertEquals(List.of(nodes.get("node1")), GraphOperators.findShortestPath(graph, nodes.get("node1"), nodes.get("node1")));
        assertEquals(List.of(nodes.get("node1"), nodes.get("node4"), nodes.get("node6")), GraphOperators.findShortestPath(graph, nodes.get("node1"), nodes.get("node6")));
        assertEquals(List.of(nodes.get("node2"), nodes.get("node1"), nodes.get("node4"), nodes.get("node6")), GraphOperators.findShortestPath(graph, nodes.get("node2"), nodes.get("node6")));
        assertEquals(List.of(), GraphOperators.findShortestPath(graph, nodes.get("node1"), nodes.get("node7")));
        assertEquals(List.of(nodes.get("node7"), nodes.get("node8")), GraphOperators.findShortestPath(graph, nodes.get("node7"), nodes.get("node8")));
    }
}
