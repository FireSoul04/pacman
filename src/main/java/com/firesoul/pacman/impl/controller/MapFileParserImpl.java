package com.firesoul.pacman.impl.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.firesoul.editor.gui.Pair;
import com.firesoul.pacman.api.controller.MapFileParser;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.impl.model.GraphImpl;
import com.firesoul.pacman.impl.model.Pacman;
import com.firesoul.pacman.impl.model.entities.Wall;
import com.firesoul.pacman.impl.util.Vector2D;

public class MapFileParserImpl implements MapFileParser {

    private List<GameObject> gameObjects;
    private Graph<Vector2D> mapNodes = new GraphImpl<>();
    private Vector2D bounds;

    /**
     * Create a 2 dimensional map with no entities or blocks.
     */
    public MapFileParserImpl(final int width, final int height) {
        this("");
        this.bounds = new Vector2D(width, height);
    }

    /**
     * Create a 2 dimensional map reading entites and blocks from the given paths.
     */
    public MapFileParserImpl(final String mapPath) {
        this.getMap(mapPath);
    }
    
    @Override
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    @Override
    public Vector2D getDimensions() {
        return this.bounds;
    }

    @Override
    public Graph<Vector2D> getMapNodes() {
        return this.mapNodes;
    }
    
    @SuppressWarnings("unchecked")
    private void getMap(final String mapPath) {
        try (
            final ObjectInputStream reader = new ObjectInputStream(
                new FileInputStream(mapPath)
            )
        ) {
            this.bounds = (Vector2D) reader.readObject();
            
            final var file = (java.util.Map<Pair<String, Vector2D>, Vector2D>) reader.readObject();
            this.gameObjects = new ArrayList<>(file.entrySet().size());
            for (var entry : file.entrySet()) {
                this.gameObjects.add(this.readGameObject(new Pair<>(entry.getKey().y(), entry.getValue()), entry.getKey().x()));
            }
            final var nodeParsed = (List<Pair<Vector2D, List<Vector2D>>>) reader.readObject();
            this.loadGraph(nodeParsed);
        } catch (final IOException | ClassNotFoundException e) {
            PacmanCore.log("Cannot read file: " + e);
            System.exit(1);
        }
    }

    private void loadGraph(final List<Pair<Vector2D, List<Vector2D>>> nodeParsed) {
        final Map<Vector2D, List<Vector2D>> nodes = nodeParsed.stream()
            .collect(Collectors.toMap(
                t -> t.x(),
                t -> t.y()
            ));
        nodes.keySet().forEach(t -> this.mapNodes.addNode(t));
        for (final var node : nodes.entrySet()) {
            final var src = node.getKey();
            for (final var position : nodes.get(src)) {
                final var dst = position;
                this.mapNodes.addEdge(src, dst, Pacman.distance(src, dst));
            }
        }
    }

    private GameObject readGameObject(final Pair<Vector2D, Vector2D> details, final String gClass) {
        GameObject g = null;
        try {
            if (gClass.equals(Wall.class.getName())) {
                g = (GameObject) Class.forName(gClass)
                    .getConstructor(Vector2D.class, Vector2D.class)
                    .newInstance(details.x().sub(new Vector2D(16, 0)), details.y());
            } else {
                g = (GameObject) Class.forName(gClass)
                    .getConstructor(Vector2D.class)
                    .newInstance(details.x().add(new Vector2D(-8, 8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return g;
    }
}
