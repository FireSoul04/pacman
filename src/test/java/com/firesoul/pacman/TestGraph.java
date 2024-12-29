package com.firesoul.pacman;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.impl.model.GraphImpl;

public class TestGraph {

    private List<String> nodes = List.of(
        "node1",
        "node2",
        "node3",
        "node4",
        "node5",
        "node6"
    );
    private Map<String, Map<String, Double>> edges = Map.of(
        "node1", Map.of(
            "node2", 2.0,
            "node3", 8.0,
            "node4", 3.0,
            "node6", 6.0
        ),
        "node2", Map.of(
            "node1", 2.0,
            "node3", 5.0
        ),
        "node3", Map.of(
            "node1", 8.0,
            "node2", 5.0,
            "node4", 4.0,
            "node5", 4.0,
            "node6", 3.0
        ),
        "node4", Map.of(
            "node1", 3.0,
            "node3", 4.0,
            "node6", 2.0
        ),
        "node5", Map.of(
            "node3", 4.0,
            "node6", 1.0
        ),
        "node6", Map.of(
            "node3", 3.0,
            "node4", 2.0,
            "node5", 1.0
        )
    );
    private Graph<String> graph = new GraphImpl<>();

    @BeforeEach
    void fillGraph() {
        nodes.forEach(t -> graph.addNode(t));
        edges.forEach((k, v) -> v.forEach((n, w) -> graph.addEdge(k, n, w)));
    }

    @Test
    void dijkstra() {
        assertEquals(List.of("node1", "node4", "node6"), graph.findShortestPath("node1", "node6"));
    }
}
