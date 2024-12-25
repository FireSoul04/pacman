package com.firesoul.editor.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.impl.util.Vector2D;

public class FileParser {

    private final int width;
    private final int height;
    private final String mapPath;

    public FileParser(final int width, final int height, final String mapPath) {
        this.width = width;
        this.height = height;
        this.mapPath = mapPath;
    }

    public synchronized void save(final Map<Entry<Vector2D, Vector2D>, Class<? extends GameObject>> gameObjects) {
        try (ObjectOutputStream os = new ObjectOutputStream(
            new FileOutputStream(mapPath)
        )) {
            os.writeObject(new Vector2D(width, height));


            System.out.println(gameObjects);

            //os.writeObject(gameObjects);
        } catch (IOException e) {
            System.out.println("Cannot open file: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized Map<Entry<Vector2D, Vector2D>, Class<? extends GameObject>> load() {
        // try (
        //     final ObjectInputStream reader = new ObjectInputStream(
        //         new FileInputStream(MAP_PATH)
        //     )
        // ) {
        //     if (Files.size(Path.of(MAP_PATH)) > 0) {
        //         reader.readObject();
        //         ((List<GameObject>) reader.readObject()).stream()
        //             .filter(t -> t instanceof Collidable)
        //             .forEach(t -> {
        //                 this.rects.put(
        //                     new Rectangle(
        //                         (int)((Collidable) t).getColliders().get(0).getPosition().getX() + (LEFT_MARGIN / SCALE),
        //                         (int)((Collidable) t).getColliders().get(0).getPosition().getY(),
        //                         (int)((Collidable) t).getColliders().get(0).getDimensions().getX(),
        //                         (int)((Collidable) t).getColliders().get(0).getDimensions().getY()
        //                     )
        //                 );
        //             }
        //         );
        //     }
        // } catch (final IOException | ClassNotFoundException e) {
        //     System.out.println("Cannot read file: " + e);
        //     this.rects.clear();
        // }
        return Map.of();
    }
}
