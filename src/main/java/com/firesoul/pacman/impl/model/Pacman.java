package com.firesoul.pacman.impl.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.api.model.GraphOperators;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.GameCore;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;

public class Pacman {

    public static enum Directions {
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    private static final int MAX_LIVES = 3;
    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    
    private final GameCore game;
    private final Timer nextLevelTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(3));
    private final Timer eatenTimer = new TimerImpl(Timer.secondsToMillis(0.5));
    private final Set<Timer> timers = Set.of(
        this.nextLevelTimer,
        this.liveLostTimer,
        this.eatenTimer
    );
    private final List<Ghost> ghosts = new ArrayList<>();
    private Graph<Vector2D> mapNodes = new GraphImpl<>();
    private int lives = MAX_LIVES;
    private Player player;
    private Scene2D scene;
    private Vector2D cageEnter;
    private Vector2D cageExit;
    private int level;

    public Pacman(final GameCore game) {
        this.game = game;
    }

    public void init() {
        this.level = 1;
        this.nextLevelTimer.start();
        this.createScene();
    }

    public void update(final double deltaTime) {
        this.game.pauseOnKeyPressed(KeyEvent.VK_ESCAPE);
        this.timers.forEach(Timer::update);
        if (this.eatenTimer.isRunning() || this.liveLostTimer.isRunning()) {
            this.updateAll(0);
        } else if (this.player.isDead() && this.liveLostTimer.isExpired()) {
            this.reset();
            this.nextLevelTimer.restart();
            this.liveLostTimer.reset();
        } else if (this.timers.stream().noneMatch(Timer::isRunning)) {
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkGameOver();
            this.updateAll(deltaTime);
        }
        if (this.game.isOver()) {
            GameCore.log("Game Over!");
            try {
                Thread.sleep(Timer.secondsToMillis(1));
            } catch (InterruptedException e) {
            }
            System.exit(0);
        }
    }

    private void checkNextLevel() {
        if (this.isLevelCompleted()) {
            GameCore.log("Level " + this.level + " completed!");
            this.level++;
            this.createScene();
            this.nextLevelTimer.restart();
        }
    }

    private void checkPlayerDeath() {
        if (this.player.isDead()) {
            GameCore.log("Player is dead, lives remaining: " + this.lives);
            this.playerDie();
        }
    }

    private void checkGameOver() {
        if (this.lives <= 0) {
            this.game.gameOver();
        }
    }

    private boolean isLevelCompleted() {
        return this.howManyInstancesOf(Pill.class) == 0;
    }

    private int howManyInstancesOf(final Class<?> clazz) {
        int counter = 0;
        for (final GameObject g : this.getGameObjects()) {
            if (g.getClass().equals(clazz)) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Search for the best path starting from the ghost position to the cage, considering the two closest nodes.
     * @param position of the ghost
     * @return path from position of the ghost to the inside of the cage
     */
    public List<Vector2D> findPathToCage(final Vector2D position) {
        this.eatenTimer.restart();
        final Vector2D movedPosition = position.sub(new Vector2D(-8, 8));
        final List<Vector2D> closestNodes = this.mapNodes.nodes().stream()
            .filter(t -> Math.abs(t.getX() - movedPosition.getX()) < 2 || Math.abs(t.getY() - movedPosition.getY()) < 2)
            .filter(t -> Pacman.distance(t, movedPosition) > 0)
            .collect(Collectors.toMap(t -> t, t -> Pacman.distance(t, movedPosition)))
            .entrySet()
            .stream()
            .sorted((a, b) -> Double.compare(a.getValue(), b.getValue()))
            .map(Map.Entry::getKey)
            .limit(2)
            .toList();
        final Vector2D src1 = closestNodes.get(0);
        final Vector2D src2 = closestNodes.size() > 1 ? closestNodes.get(1) : null;
        final Vector2D dst = this.cageEnter.sub(new Vector2D(-8, 8));
        final List<Vector2D> path1 = GraphOperators.findShortestPath(this.mapNodes, src1, dst);
        final List<Vector2D> path2 = src2 == null ? path1 : GraphOperators.findShortestPath(this.mapNodes, src2, dst);
        return path1.size() < path2.size() ? path1 : path2;
    }

    /**
     * @return the position of the inside of the cage
     */
    public Vector2D getCageEnter() {
        return this.cageEnter;
    }
    
    /**
     * @return the position of the cage's exit
     */
    public Vector2D getCageExit() {
        return this.cageExit;
    }

    /**
     * Set all the ghosts vulnerable.
     */
    public void setGhostVulnerable() {
        for (final Ghost g : this.ghosts) {
            g.setVulnerable();
        }
    }

    /**
     * Get all the gameObjects of the scene.
     */
    public List<GameObject> getGameObjects() {
        return this.scene.getGameObjects();
    }

    /**
     * Add a gameObject to the scene.
     * @param gameObject
     */
    public void addGameObject(final GameObject gameObject) {
        this.scene.addGameObject(gameObject);
    }

    /**
     * Update all the gameObjects of the scene.
     * @param deltaTime
     */
    public void updateAll(final double deltaTime) {
        this.scene.updateAll(deltaTime);
    }

    /**
     * Wake all the gameObjects of the scene.
     */
    public void wakeAll() {
        this.scene.wakeAll();
    }

    /**
     * Pause all the gameObjects of the scene.
     */
    public void pauseAll() {
        this.scene.pauseAll();
    }

    /**
     * @return the current game level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Decreases the player lives.
     */
    public void playerDie() {
        this.lives--;
        this.liveLostTimer.restart();
    }

    private void createScene() {
        this.scene = new Scene2D(MAP_PATH);
        this.mapNodes = this.scene.getMapNodes();
        this.ghosts.clear();
        for (final GameObject g : this.getGameObjects()) {
            if (g instanceof Player p) {
                p.addInput(this.game.getInputController());
                this.player = p;
            } else if (g instanceof Ghost gh) {
                gh.connectToGameLogic(this);
                this.ghosts.add(gh);
            } else if (g instanceof PowerPill pl) {
                pl.connectToGameLogic(this);
            } else if (g instanceof CageEnter) {
                this.cageEnter = g.getPosition();
            } else if (g instanceof CageExit) {
                this.cageExit = g.getPosition();
            }
        }
        if (this.player == null) {
            throw new IllegalStateException("Cannot find player in this scene");
        } else if (this.ghosts.size() != 4) {
            throw new IllegalStateException("One or more ghosts are missing in this scene");
        }
    }

    private void reset() {
        for (final GameObject g : this.getGameObjects()) {
            g.reset();
        }
    }

    /**
     * Caclulate the euclidean distance between two points.
     * @param v1
     * @param v2
     * @return distance from v1 to v2
     */
    public static double distance(final Vector2D v1, final Vector2D v2) {
        return Math.sqrt((v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY()));
    }
}
