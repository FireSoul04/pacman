package com.firesoul.editor.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
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
        GameObjects.CLYDE,
        GameObjects.CAGEENTER,
        GameObjects.CAGEEXIT
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
    private boolean editGraphMode = false;

    @Override
    public void save() {
        if (this.areAllLimitedObjectsPresent()) {
            this.parser.save(this.parseGameObjects(), this.parseGraph());
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

    private Map<Pair<Vector2D, Vector2D>, String> parseGameObjects() {
        return this.gameObjects.stream()
            .collect(Collectors.toMap(
                t -> new Pair<>(t.getPosition(), t instanceof Wall
                    ? ((Wall) t).getColliders().getFirst().getDimensions()
                    : new Vector2D(t.getDrawable().getImageSize().getY(), t.getDrawable().getImageSize().getY())),
                t -> t.getClass().getName())
            );
    }

    private List<Pair<Vector2D, List<Vector2D>>> parseGraph() {
        return this.mapNodes.edges()
            .entrySet()
            .stream()
            .map(t -> new Pair<>(t.getKey().getPosition(), t.getValue().keySet().stream().map(a -> a.getPosition()).toList()))
            .distinct()
            .toList();
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

    private void loadGameObjects() {
        this.gameObjects.addAll(this.parser.load()
            .entrySet()
            .stream()
            .map(this::createGameObject).toList()
        );
    }

    private void loadMapNodes() {
        this.mapNodes.clear();
        final Map<MapNode, List<Vector2D>> nodes = this.parser.getMapNodes()
            .stream()
            .collect(Collectors.toMap(
                t -> new MapNode(t.x()),
                t -> t.y()
            ));
        nodes.keySet().forEach(t -> this.mapNodes.addNode(t));
        final Map<Vector2D, MapNode> nodePositions = this.mapNodes.nodes()
            .stream()
            .collect(Collectors.toMap(
                MapNode::getPosition,
                Function.identity()
            ));

        for (final var node : nodes.entrySet()) {
            final var src = node.getKey();
            for (final var position : nodes.get(src)) {
                final var dst = nodePositions.get(position);
                this.mapNodes.addEdge(src, dst, Pacman.distance(src.getPosition(), dst.getPosition()));
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
    public void reset() {
        if (this.editGraphMode) {
            this.selectedNodes.clear();
            this.mapNodes.clear();
        } else {
            this.gameObjects.clear();
        }
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
            case GameObjects.CAGEENTER -> new CageEnter(position);
            case GameObjects.CAGEEXIT -> new CageExit(position);
            case GameObjects.WALL -> new Wall(position, size);
            default -> throw new IllegalStateException("Invalid game object: " + selected);
        };
        if (size.getX() > 0 && size.getY() > 0) {
            if (!LIMITED_GAME_OBJECTS.contains(selected) ||
                this.howManyInstanciesOf(selected) < 1
            ) {
                this.gameObjects.add(gameObject);
            }
        }
    }

    @Override
    public void click(final GameObjects selected, final Vector2D position) {
        if (selected.equals(GameObjects.ERASER)) {
            this.erase(position);
        } else if (selected.equals(GameObjects.LINKNODES)) {
            this.link(position);
        } else if (selected.equals(GameObjects.MAPNODE)) {
            this.insertMapNode(position);
        } else if (selected.equals(GameObjects.CAGEENTER) || selected.equals(GameObjects.CAGEEXIT)) {
            if (this.editGraphMode) {
                this.addGameObject(selected, position, new Vector2D(16, 16));
                this.mapNodes.addNode(new MapNode(position));
            }
        } else if (this.gameObjects.stream().filter(t -> t.getPosition().equals(position)).count() < 1) {
            if (!this.editGraphMode) {
                this.addGameObject(selected, position, new Vector2D(16, 16));
            }
        }
    }

    private void erase(final Vector2D position) {
        if (this.editGraphMode) {
            this.removeGameObject(position);
            this.removeWall(position);
            this.removeNode(position);
        }
    }

    private void removeGameObject(final Vector2D position) {
        final Optional<GameObject> gameObject;
        if (!this.editGraphMode) {
            gameObject = this.gameObjects.stream()
                .filter(t -> this.inRangeOf(t.getPosition(), position, 8))
                .findFirst();
        } else {
            gameObject = this.gameObjects.stream()
                .filter(t -> t instanceof CageEnter || t instanceof CageExit)
                .filter(t -> this.inRangeOf(t.getPosition().add(new Vector2D(8, 8)), position, 8))
                .findFirst();
        }
        if (gameObject.isPresent()) {
            this.gameObjects.remove(gameObject.get());
        }
    }

    private void removeWall(final Vector2D position) {
        if (!this.editGraphMode) {
            final var wall = this.gameObjects.stream()
                .filter(t -> t instanceof Wall)
                .map(t -> (Wall) t)
                .filter(t -> isInBoundsOf(t, position))
                .findFirst();
            if (wall.isPresent()) {
                this.gameObjects.remove(wall.get());
            }
        }
    }

    private boolean isInBoundsOf(final Wall g, final Vector2D p) {
        return p.getX() >= g.getPosition().getX()
            && p.getX() <= g.getPosition().getX() + g.getColliders().getFirst().getDimensions().getX()
            && p.getY() >= g.getPosition().getY()
            && p.getY() <= g.getPosition().getY() + g.getColliders().getFirst().getDimensions().getY();
    }

    private void removeNode(final Vector2D position) {
        if (this.editGraphMode) {
            final var node = this.mapNodes.nodes().stream().filter(t -> this.inRangeOf(t.getPosition(), position.sub(new Vector2D(8, 8)), 8)).findFirst();
            if (node.isPresent()) {
                final GameObject g = this.gameObjects.stream().filter(t -> t.getPosition().equals(node.get().getPosition())).findFirst().orElse(null);
                if (g instanceof CageEnter || g instanceof CageExit) {
                    this.gameObjects.removeIf(t -> t.getPosition().equals(g.getPosition()));
                }
                this.mapNodes.removeNode(node.get());
            }
        }
    }

    private void link(final Vector2D position) {
        if (this.editGraphMode &&
            this.selectedNodes.size() < 2 &&
            this.mapNodes.nodes().stream().anyMatch(t -> this.inRangeOf(t.getPosition(), position, 8))
        ) {
            final MapNode node = this.mapNodes.nodes().stream()
                .filter(t -> this.inRangeOf(t.getPosition(), position, 8))
                .findFirst()
                .get();
            this.selectedNodes.add(node);
            this.linkNodes();
        }
    }

    private boolean inRangeOf(final Vector2D p1, final Vector2D p2, final double delta) {
        return Math.abs(p1.getX() - p2.getX()) < delta
            && Math.abs(p1.getY() - p2.getY()) < delta;
    }

    private void insertMapNode(final Vector2D position) {
        if (
            this.editGraphMode &&
            this.mapNodes.nodes().stream().noneMatch(t -> t.getPosition().equals(position))
        ) {
            this.mapNodes.addNode(new MapNode(position));
        }
    }

    @Override
    public void linkNodes() {
        if (this.editGraphMode && this.selectedNodes.size() == 2) {
            final MapNode src = this.selectedNodes.removeFirst();
            final MapNode dst = this.selectedNodes.removeFirst();
            if (src.equals(dst)) {
                return;
            }
            this.mapNodes.addEdge(src, dst, Pacman.distance(src.getPosition(), dst.getPosition()));
        }
    }

    private int howManyInstanciesOf(final GameObjects g) {
        return (int) this.gameObjects.stream()
            .filter(t -> t.getClass().getSimpleName().toUpperCase().equals(g.name()))
            .count();
    }

    @Override
    public ConcurrentLinkedQueue<GameObject> getGameObjects() {
        return this.gameObjects.stream().collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    @Override
    public boolean isLimited(final GameObjects g) {
        return LIMITED_GAME_OBJECTS.stream().anyMatch(t -> t.equals(g));
    }

    @Override
    public Graph<MapNode> getMap() {
        return this.mapNodes;
    }

    @Override
    public void setEditGraphMode(final boolean editGraphMode) {
        this.editGraphMode = editGraphMode;
    }

    @Override
    public boolean isInEditGraphMode() {
        return this.editGraphMode;
    }
}
