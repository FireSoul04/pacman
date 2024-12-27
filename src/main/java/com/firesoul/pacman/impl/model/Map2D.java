package com.firesoul.pacman.impl.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.firesoul.editor.gui.Pair;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Map;
import com.firesoul.pacman.impl.model.entities.Wall;
import com.firesoul.pacman.impl.util.Vector2D;

public class Map2D implements Map {

    private Vector2D bounds;

    private final String mapPath;

    /**
     * Create a 2 dimensional map with no entities or blocks.
     */
    public Map2D(final int width, final int height) {
        this("");
        this.bounds = new Vector2D(width, height);
    }

    /**
     * Create a 2 dimensional map reading entites and blocks from the given paths.
     */
    public Map2D(final String mapPath) {
        this.mapPath = mapPath;
    }
    
    @Override
    public List<GameObject> getGameObjects() {
        return this.getMap(this.mapPath);
    }

    @Override
    public Vector2D getDimensions() {
        return this.bounds;
    }
    
    @SuppressWarnings("unchecked")
    private List<GameObject> getMap(final String mapPath) {
        final List<GameObject> gameObjects = new ArrayList<>();
        try (
            final ObjectInputStream reader = new ObjectInputStream(
                new FileInputStream(this.mapPath)
            )
        ) {
            this.bounds = (Vector2D) reader.readObject();
            
            final var file = (java.util.Map<Pair<Vector2D, Vector2D>, String>) reader.readObject();
            for (var entry : file.entrySet()) {
                gameObjects.add(this.readGameObject(entry.getKey(), entry.getValue()));
            }
        } catch (final IOException | ClassNotFoundException e) {
            System.out.println("Cannot read file: " + e);
            System.exit(1);
        }
        return gameObjects;
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
