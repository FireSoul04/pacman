package com.firesoul.pacman.impl.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Graph;
import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Collider;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.GameCore;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.model.entities.colliders.BoxCollider2D;
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

    public class OutsideCageNotifier extends GameObject2D implements Collidable {
        private final Collider collider;

        public OutsideCageNotifier(final Vector2D position) {
            super(position.add(Vector2D.up()));
            this.collider = new BoxCollider2D(this, new Vector2D(8, 1), false);
        }

        @Override
        public void onCollide(final Collider collider, final Collider other) {
        }

        @Override
        public List<Collider> getColliders() {
            return Collections.unmodifiableList(List.of(this.collider));
        }
    }

    private static final int MAX_LIVES = 3;
    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    private static final Vector2D EXIT_POSITION = new Vector2D(112, 84);
    private static final Vector2D CAGE_CENTER_POSITION = new Vector2D(112, 112);
    
    private final Ghost dummy = new Ghost(Vector2D.zero(), "blinky", 0, Vector2D.zero()) {
        @Override
        protected void move() {
        }
        @Override
        public void update(final double deltaTime) {
        }
    };
    private final GameCore game;
    private final Timer nextLevelTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final OutsideCageNotifier outsideCageNotifier = new OutsideCageNotifier(EXIT_POSITION);
    private final List<Ghost> ghosts = new ArrayList<>();
    private final Graph<MapNode> map = new GraphImpl<>();
    private int lives = MAX_LIVES;
    private Player player;
    private Scene2D scene;
    
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
        if (!this.isWaitingNextLevel() && !this.isPlayerWaitingRespawn()) {
            this.game.pauseOnKeyPressed(KeyEvent.VK_ESCAPE);
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkGameOver();
            this.updateAll(deltaTime);
        }
        this.nextLevelTimer.update();
        this.liveLostTimer.update();
        if (this.game.isOver()) {
            System.out.println("Game Over!");
            try {
                Thread.sleep(Timer.secondsToMillis(1));
            } catch (final InterruptedException e) {
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
            this.lives--;
            GameCore.log("Player is dead, lives remaining: " + this.lives);
            this.reset();
            this.liveLostTimer.restart();
        }
    }

    private void checkGameOver() {
        if (this.lives <= 0) {
            this.game.gameOver();
        }
    }

    private boolean isWaitingNextLevel() {
        return this.nextLevelTimer.isRunning();
    }

    private boolean isPlayerWaitingRespawn() {
        return this.liveLostTimer.isRunning();
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

    public List<Vector2D> findPathToCage(final Vector2D position) {
        final List<Vector2D> path = new LinkedList<>();
        double distanceToCage = 0;
        this.dummy.setPosition(new Vector2D(Math.round(position.getX()), Math.round(position.getY())));
        do {
            distanceToCage = this.distance(this.dummy.getPosition(), CAGE_CENTER_POSITION);
            Vector2D direction = Vector2D.zero();
            this.dummy.setPosition(this.dummy.getPosition().add(direction));
            // path.add(direction);
        } while (distanceToCage > 0);
        return path;
    }

    private double distance(final Vector2D v1, final Vector2D v2) {
        return Math.sqrt((v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY()));
    }

    /**
     * Set all the ghosts vulnerable.
     */
    public void setGhostVulnerable() {
        for (final GameObject g : this.getGameObjects()) {
            if (g instanceof Ghost gh) {
                gh.setVulnerable();
            }
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
     * Update all the gameObjects of the scene
     * @param deltaTime
     */
    public void updateAll(final double deltaTime) {
        this.scene.updateAll(deltaTime);
    }

    /**
     * Wake all the gameObjects of the scene
     */
    public void wakeAll() {
        this.scene.wakeAll();
    }

    /**
     * Pause all the gameObjects of the scene
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

    public void playerDie() {
        this.lives--;
    }

    private void createScene() {
        this.scene = new Scene2D(MAP_PATH);
        this.addGameObject(this.outsideCageNotifier);
        this.ghosts.clear();
        for (final GameObject g : this.getGameObjects()) {
            if (g instanceof Player p) {
                p.addInput(this.game.getInputController());
                this.player = p;
            } else if (g instanceof Ghost gh) {
                gh.addOutsideCageNotifier(this.outsideCageNotifier);
                gh.connectToGameLogic(this);
                this.ghosts.add(gh);
            } else if (g instanceof PowerPill pl) {
                pl.connectToGameLogic(this);
            } else if (g instanceof MapNode n) {
                map.addNode(n);
            }
        }
        if (this.player == null) {
            throw new IllegalStateException("Cannot find player in this scene");
        } else if (this.ghosts.size() != 4) {
            throw new IllegalStateException("One or more ghosts are missing in this scene");
        }
        this.dummy.setVisible(false);
        this.addGameObject(this.dummy);
    }

    private void reset() {
        for (final GameObject g : this.getGameObjects()) {
            g.reset();
        }
    }
}
