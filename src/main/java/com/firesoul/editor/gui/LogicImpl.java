package com.firesoul.editor.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.impl.model.*;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.model.entities.ghosts.*;
import com.firesoul.pacman.impl.util.Vector2D;

public class LogicImpl implements Logic {
    
    private static final int WIDTH = 224;
    private static final int HEIGHT = 248;
    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    private static final List<GameObjects> LIMITED_GAME_OBJECTS = List.of(
        GameObjects.PLAYER,
        GameObjects.BLINKY,
        GameObjects.PINKY,
        GameObjects.INKY,
        GameObjects.CLYDE
    );

    public enum GameObjects {
        PLAYER,
        BLINKY,
        PINKY,
        INKY,
        CLYDE,
        PILL,
        POWERPILL,
        WALL,
        MAPNODE,
        CAGEENTER,
        CAGEEXIT,
        LINKNODES,
        ERASER;
    };

    private final ConcurrentLinkedQueue<GameObject> gameObjects = new ConcurrentLinkedQueue<>();
    private final FileParser parser = new FileParser(WIDTH, HEIGHT, MAP_PATH);
    private final List<MapNode> selectedNodes = new LinkedList<>();
    private final Graph<MapNode> mapNodes = new GraphImpl<>();
    
    @Override
    public void save() {
        if (this.areAllLimitedObjectsPresent()) {
            this.parser.save(this.gameObjects.stream()
                .collect(Collectors.toMap(
                    t -> new Pair<>(t.getPosition(), t instanceof Wall
                        ? ((Wall) t).getColliders().getFirst().getDimensions()
                        : new Vector2D(t.getDrawable().getImageSize().getY(), t.getDrawable().getImageSize().getY())),
                    t -> t.getClass().getName())
                ), this.mapNodes.edges()
                    .entrySet()
                    .stream()
                    .map(t -> new Pair<>(t.getKey().getPosition(), t.getValue().keySet().stream().map(a -> a.getPosition()).toList()))
                    .toList()
            );
        } else {
            System.out.println("Cannot save, some mandatory game objects are missing");
        }
    }

    private boolean areAllLimitedObjectsPresent() {
        return LIMITED_GAME_OBJECTS.stream().filter(a -> 
                this.gameObjects.stream().anyMatch(t ->
                    a.name().equals(t.getClass().getSimpleName().toUpperCase())
                )
            ).toList()
            .equals(LIMITED_GAME_OBJECTS);
    }

    @Override
    public void load() {
        try {
            this.loadGameObjects();
            this.loadMapNodes();
        } catch (IllegalStateException e) {
            System.out.println("Cannot load from file");
        }
    }

    @Override
    public void reset() {
        this.gameObjects.clear();
        this.mapNodes.clear();
    }

    private void loadGameObjects() {
        this.gameObjects.addAll(this.parser.load()
            .entrySet()
            .stream()
            .map(this::createGameObject).toList()
        );
    }

    private void loadMapNodes() {
        this.mapNodes.clear();
        for (final var node : this.parser.getMapNodes()) {
            final var x = new MapNode(node.x());
            this.mapNodes.addNode(x);
            for (final var elem : node.y()) {
                final var y = new MapNode(elem);
                this.mapNodes.addNode(y);
                this.mapNodes.addEdge(x, y, Pacman.distance(node.x(), elem));
            }
        }
    }

    private GameObject createGameObject(final Map.Entry<Pair<Vector2D, Vector2D>, String> t) {{
        try {
            return t.getValue().equals(Wall.class.getName())
                ? (GameObject) Class.forName(t.getValue())
                    .getConstructor(Vector2D.class, Vector2D.class)
                    .newInstance(t.getKey().x(), t.getKey().y())
                : (GameObject) Class.forName(t.getValue())
                    .getConstructor(Vector2D.class)
                    .newInstance(t.getKey().x());
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }}

    @Override
    public ConcurrentLinkedQueue<GameObject> getGameObjects() {
        return this.gameObjects.stream().collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    @Override
    public void addGameObject(final GameObjects selected, final Vector2D position, final Vector2D size) {
        final GameObject gameObject = switch (selected) {
            case GameObjects.PLAYER -> new Player(position);
            case GameObjects.BLINKY -> new Blinky(position);
            case GameObjects.PINKY -> new Pinky(position);
            case GameObjects.INKY -> new Inky(position);
            case GameObjects.CLYDE -> new Clyde(position);
            case GameObjects.PILL -> new Pill(position);
            case GameObjects.POWERPILL -> new PowerPill(position);
            case GameObjects.WALL -> new Wall(position, size);
            default -> throw new IllegalStateException("Invalid game object: " + selected);
        };
        if (size.getX() > 0 && size.getY() > 0) {
            if (!LIMITED_GAME_OBJECTS.contains(selected) ||
                this.howManyInstanciesOf(selected) < 1
            ) {
                this.gameObjects.add(gameObject);
            } else if (gameObject instanceof MapNode node) {
                this.mapNodes.addNode(node);
            }
        }
    }

    @Override
    public void removeGameObject(final Vector2D position) {
        final var gameObject = this.gameObjects.stream().filter(t -> this.inRangeOf(t.getPosition(), position, 8)).findFirst();
        if (gameObject.isPresent()) {
            this.gameObjects.remove(gameObject.get());
        }
    }

    private boolean inRangeOf(final Vector2D p1, final Vector2D p2, final double delta) {
        return Math.abs(p1.getX() - p2.getX()) < delta
            && Math.abs(p1.getY() - p2.getY()) < delta;
    }

    @Override
    public void removeWall(final Vector2D position) {
        final var wall = this.gameObjects.stream()
            .filter(t -> t instanceof Wall)
            .map(t -> (Wall) t)
            .filter(
                t -> position.getX() >= t.getPosition().getX()
                    && position.getX() <= t.getPosition().getX() + t.getColliders().getFirst().getDimensions().getX()
                    && position.getY() >= t.getPosition().getY()
                    && position.getY() <= t.getPosition().getY() + t.getColliders().getFirst().getDimensions().getY()
        ).findFirst();
        if (wall.isPresent()) {
            this.gameObjects.remove(wall.get());
        }
    }

    @Override
    public void click(final GameObjects selected, final Vector2D position) {
        if (selected.equals(GameObjects.ERASER)) {
            this.removeGameObject(position);
            this.removeWall(position);
            this.removeNode(position);
        } else if (selected.equals(GameObjects.LINKNODES) &&
                this.mapNodes.nodes().stream()
                .map(GameObject::getPosition)
                .anyMatch(t -> t.equals(position)) &&
            this.selectedNodes.size() < 2
        ) {
            final MapNode node = this.mapNodes.nodes().stream()
                .filter(t -> t.getPosition().equals(position))
                .findFirst()
                .get();
            this.selectedNodes.add(node);
        } else if (selected.equals(GameObjects.MAPNODE) &&
            this.mapNodes.nodes().stream().noneMatch(t -> t.getPosition().equals(position))
        ) {
            this.mapNodes.addNode(new MapNode(position));
        } else if (this.gameObjects.stream().filter(t -> t.getPosition().equals(position)).count() < 1) {
            this.addGameObject(selected, position, new Vector2D(16, 16));
        }
    }

    @Override
    public void linkNodes() {
        if (this.selectedNodes.size() == 2) {
            final MapNode src = this.selectedNodes.removeFirst();
            final MapNode dst = this.selectedNodes.removeFirst();
            this.mapNodes.addEdge(src, dst, Pacman.distance(src.getPosition(), dst.getPosition()));
        }
    }

    @Override
    public void removeNode(final Vector2D position) {
        final var node = this.mapNodes.nodes().stream().filter(t -> this.inRangeOf(t.getPosition(), position, 8)).findFirst();
        if (node.isPresent()) {
            this.mapNodes.removeNode(node.get());
        }
    }

    @Override
    public long howManyInstanciesOf(final GameObjects g) {
        return this.gameObjects.stream()
            .filter(t -> t.getClass().getSimpleName().toUpperCase().equals(g.name()))
            .count();
    }

    @Override
    public boolean isLimited(final GameObjects g) {
        return LIMITED_GAME_OBJECTS.stream().anyMatch(t -> t.equals(g));
    }

    @Override
    public Graph<MapNode> getMap() {
        return this.mapNodes;
    }
}
