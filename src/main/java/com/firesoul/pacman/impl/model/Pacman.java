package com.firesoul.pacman.impl.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sound.sampled.Clip;

import com.firesoul.editor.gui.Pair;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.api.model.GraphOperators;
import com.firesoul.pacman.api.util.AudioPlayer;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.PacmanCore;
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
    private static final int GHOST_EATEN_SCORE = 400;
    private static final int MAX_LIVES = 3;
    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    
    private final PacmanCore game;
    private final Timer startTimer = new TimerImpl(Timer.secondsToMillis(4.5));
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
    private int ghostsEaten = 0;
    private int lives = MAX_LIVES;
    private int level = 1;
    private int score = 0;
    private Player player;
    private Scene2D scene;
    private Vector2D cageEnter;
    private Vector2D cageExit;

    public Pacman(final PacmanCore game) {
        this.game = game;
    }

    public void init() {
        this.createScene();
        this.mapNodes.nodes().stream().forEach(s ->this.mapNodes.nodes().stream().forEach(d -> this.paths.put(new Pair<>(s, d), GraphOperators.findShortestPath(this.mapNodes, s, d))));
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
            this.updateScore();
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkGameOver();
            this.updateAll(deltaTime);
        }
        if (this.ghosts.stream().noneMatch(Ghost::isVulnerable)) {
            this.ghostVulnerableSound.stop();
        }
        if (this.game.isOver()) {
            PacmanCore.log("Game Over!");
            try {
                Thread.sleep(Timer.secondsToMillis(1));
            } catch (InterruptedException e) {
            }
            System.exit(0);
        }
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
        this.ghostsEaten = 0;
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
        this.ghostsEaten++;
        this.addScore(GHOST_EATEN_SCORE * this.ghostsEaten);
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
     * Decreases the player lives.
     */
    public void playerDie() {
        this.lives--;
        this.liveLostTimer.startAgain();
        this.ghostVulnerableSound.stop();
    }

    /**
     * Adding an amount to the score.
     * @param amount 
     */
    public void addScore(final int amount) {
        this.score = this.score + amount;
    }

    /**
     * Get all the gameObjects of the scene.
     */
    public List<GameObject> getGameObjects() {
        return this.scene.getGameObjects();
    }

    /**
     * @return the current game level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * @return the player's remaining lives
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * @return the player's current score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * @return player's position
     */
    public Vector2D getPlayerPosition() {
        return this.player.getPosition();
    }

    /**
     * @return player's position
     */
    public Vector2D getPlayerDirection() {
        return this.player.getDirection();
    }

    /**
     * @return most far position from player
     */
    public Vector2D getMostFarPositionFromPlayer() {
        return new Vector2D(
            Math.abs(this.scene.getDimensions().getX() - this.getPlayerPosition().getX()),
            this.getPlayerPosition().getY()
        );
    }

    /**
     * @return get the position of a random node in the graph
     */
    public Vector2D getRandomNode() {
        return this.mapNodes.nodes().get(new Random().nextInt(this.mapNodes.nodes().size())).add(new Vector2D(-8, 8));
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
            .orElse(null);
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
            } else if (g instanceof Pill pl) {
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

    private void updateScore() {

    }

    private void checkNextLevel() {
        if (this.isLevelCompleted()) {
            PacmanCore.log("Level " + this.level + " completed!");
            this.level++;
            this.createScene();
            this.nextLevelTimer.startAgain();
            this.ghostVulnerableSound.stop();
        }
    }

    private void checkPlayerDeath() {
        if (this.player.isDead()) {
            this.playerDie();
            PacmanCore.log("Player is dead, lives remaining: " + this.lives);
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
}
