package com.firesoul.editor.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.firesoul.pacman.impl.util.Pair;
import com.firesoul.pacman.impl.util.Vector2D;

public class FileParser {

    private final int width;
    private final int height;
    private final String mapPath;
    private List<Pair<Vector2D, List<Vector2D>>> mapNodes = new LinkedList<>();

    public FileParser(final int width, final int height, final String mapPath) {
        this.width = width;
        this.height = height;
        this.mapPath = mapPath;
    }

    public synchronized void save(
        final Map<Pair<Vector2D, Vector2D>, String> gameObjects,
        final List<Pair<Vector2D, List<Vector2D>>> mapNodes
    ) {
        try (ObjectOutputStream os = new ObjectOutputStream(
            new FileOutputStream(this.mapPath)
        )) {
            os.writeObject(new Vector2D(this.width, this.height));
            os.writeObject(gameObjects);
            os.writeObject(mapNodes);
        } catch (IOException e) {
            System.out.println("Cannot open file: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized Map<Pair<Vector2D, Vector2D>, String> load() {
        final Map<Pair<Vector2D, Vector2D>, String> gameObjects = new HashMap<>();
        try (
            final ObjectInputStream reader = new ObjectInputStream(
                new FileInputStream(this.mapPath)
            )
        ) {
            if (Files.size(Path.of(this.mapPath)) > 0) {
                reader.readObject();
                gameObjects.putAll((Map<Pair<Vector2D, Vector2D>, String>) reader.readObject());
                this.mapNodes = (List<Pair<Vector2D, List<Vector2D>>>) reader.readObject();
            }
        } catch (final IOException | ClassNotFoundException e) {
            System.out.println("Cannot read file: " + e);
            gameObjects.clear();
        }
        return gameObjects;
    }

    public List<Pair<Vector2D, List<Vector2D>>> getMapNodes() {
        return Collections.unmodifiableList(this.mapNodes);
    }
}
