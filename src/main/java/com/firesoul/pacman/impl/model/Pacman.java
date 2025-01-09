package com.firesoul.pacman.impl.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sound.sampled.Clip;

import com.firesoul.editor.gui.Pair;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.api.model.GraphOperators;
import com.firesoul.pacman.api.util.AudioPlayer;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.GameCore;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.util.SoundPlayer;
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
    private final Timer startTimer = new TimerImpl(Timer.secondsToMillis(/*4.5*/0));
    private final Timer nextLevelTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(3));
    private final Timer eatenTimer = new TimerImpl(Timer.secondsToMillis(0.5));
    private final Set<Timer> timers = Set.of(
        this.startTimer,
        this.nextLevelTimer,
        this.liveLostTimer,
        this.eatenTimer
    );
    private final List<Ghost> ghosts = new ArrayList<>();
    private final AudioPlayer startSound = new SoundPlayer("start");
    private final AudioPlayer ghostVulnerableSound = new SoundPlayer("fright", Clip.LOOP_CONTINUOUSLY);
    private final AudioPlayer eatGhostSound = new SoundPlayer("eat_ghost");
    private Graph<Vector2D> mapNodes = new GraphImpl<>();
    private Map<Pair<Vector2D, Vector2D>, List<Vector2D>> paths = new HashMap<>();
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
        this.createScene();
        this.mapNodes.nodes().stream().forEach(s ->this.mapNodes.nodes().stream().forEach(d -> this.paths.put(new Pair<>(s, d), GraphOperators.findShortestPath(this.mapNodes, s, d))));
        this.level = 1;
        this.startTimer.start();
        this.startSound.playOnce();
    }

    public void update(final double deltaTime) {
        this.game.pauseOnKeyPressed(KeyEvent.VK_ESCAPE);
        this.timers.forEach(Timer::update);
        if (this.eatenTimer.isRunning() || this.liveLostTimer.isRunning()) {
            this.updateAll(0);
        } else if (this.player.isDead() && this.liveLostTimer.isExpired()) {
            this.reset();
            this.nextLevelTimer.startAgain();
            this.liveLostTimer.reset();
        } else if (this.timers.stream().noneMatch(Timer::isRunning)) {
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkGameOver();
            this.updateAll(deltaTime);
        }
        if (this.ghosts.stream().noneMatch(Ghost::isVulnerable)) {
            this.ghostVulnerableSound.stop();
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
            this.nextLevelTimer.startAgain();
            this.ghostVulnerableSound.stop();
        }
    }

    private void checkPlayerDeath() {
        if (this.player.isDead()) {
            this.playerDie();
            GameCore.log("Player is dead, lives remaining: " + this.lives);
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
        return (int) this.getGameObjects().stream().map(Object::getClass).filter(t -> t.equals(clazz)).count();
    }

    /**
     * Search for the best path starting from the ghost position to the cage, considering the two closest nodes.
     * @param position of the ghost
     * @return path from position to the inside of the cage
     */
    public List<Vector2D> findPathTo(final Vector2D source, final Vector2D destination) {
        final Vector2D src = this.closestNodeTo(source.sub(new Vector2D(-8, 8)));
        final Vector2D dst = this.closestNodeTo(destination.sub(new Vector2D(-8, 8)));
        return this.paths.get(new Pair<>(src, dst));
    }

    /**
     * Get the closest graph node from the position given.
     * @param position
     * @return closest note
     */
    public Vector2D closestNodeTo(final Vector2D position) {
        return this.mapNodes.nodes().stream()
            .filter(t -> Math.abs(t.getX() - position.getX()) < 2 || Math.abs(t.getY() - position.getY()) < 2)
            .filter(t -> Pacman.distance(t, position) >= 0)
            .collect(Collectors.toMap(t -> t, t -> Pacman.distance(t, position)))
            .entrySet()
            .stream()
            .sorted((a, b) -> Double.compare(a.getValue(), b.getValue()))
            .map(Map.Entry::getKey)
            .findFirst()
            .get();
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
        this.ghosts.forEach(Ghost::setVulnerable);
        this.ghostVulnerableSound.play();
    }

    /**
     * Start the timer when a ghost is eaten.
     */
    public void ghostEaten() {
        if (!this.eatenTimer.isRunning()) {
            this.eatenTimer.startAgain();
        }
        this.eatGhostSound.play();
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
        if (this.ghostVulnerableSound.alreadyPlaying()) {
            this.ghostVulnerableSound.resume();
        }
    }

    /**
     * Pause all the gameObjects of the scene.
     */
    public void pauseAll() {
        this.scene.pauseAll();
        this.ghostVulnerableSound.pause();
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
        this.liveLostTimer.startAgain();
        this.ghostVulnerableSound.stop();
    }

    /**
     * @return player's position
     */
    public Vector2D getPlayerPosition() {
        return this.player.getPosition();
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
        this.getGameObjects().forEach(t -> t.reset());
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
