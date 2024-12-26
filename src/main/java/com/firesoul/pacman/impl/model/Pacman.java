package com.firesoul.pacman.impl.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.GameCore;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.util.TimerImpl;

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
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final List<Ghost> ghosts = new ArrayList<>();
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
        this.reset();
    }

    public void update(final double deltaTime) {
        if (!this.isWaitingNextLevel() && !this.isPlayerWaitingRespawn()) {
            this.game.pauseOnKeyPressed(KeyEvent.VK_ESCAPE);
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkDeadGhosts();
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
            this.reset();
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

    private long howManyInstancesOf(final Class<?> clazz) {
        int counter = 0;
        for (final GameObject g : this.getGameObjects()) {
            if (g.getClass().equals(clazz)) {
                counter++;
            }
        }
        return counter;
    }

    private void checkDeadGhosts() {
        for (final GameObject g : this.getGameObjects()) {
            if (g instanceof Ghost gh && gh.isDead()) {
                g.disable();
            }
        }
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

    private void reset() {
        this.scene = new Scene2D(MAP_PATH);
        this.ghosts.clear();
        for (final GameObject g : this.getGameObjects()) {
            if (g instanceof Player p) {
                p.addInput(this.game.getInputController());
                this.player = p;
            } else if (g instanceof Ghost gh) {
                this.ghosts.add(gh);
            } else if (g instanceof PowerPill pl) {
                pl.connectToGameLogic(this);
            }
        }
        if (this.player == null) {
            throw new IllegalStateException("Cannot find player in this scene");
        } else if (this.ghosts.size() != 4) {
            throw new IllegalStateException("One or more ghosts are missing in this scene");
        }
    }
}
