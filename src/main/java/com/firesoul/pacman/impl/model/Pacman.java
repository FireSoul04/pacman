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

    private static final String MAP_PATH = "src/main/resources/map/map.txt";

    private final GameCore game;
    private final Timer nextLevelTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final List<Ghost> ghosts = new ArrayList<>();
    private Player player;
    private Scene2D scene;
    
    private int level;

    public Pacman(final GameCore game) {
        this.game = game;
        this.scene = new Scene2D(MAP_PATH);
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
            GameCore.log("Player is dead, lives remaining: " + this.player.getLives());
            this.reset();
            this.liveLostTimer.restart();
        }
    }

    private void checkGameOver() {
        if (this.player.getLives() <= 0) {
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

    private void reset() {
        Player oldPlayer = this.loadGameObjects(p -> {
            this.player.addInput(this.game.getInputController());
            this.player.reset();
        });
        this.scene.removeGameObject(oldPlayer);
        this.scene.addGameObject(this.player);
    }

    private void createScene() {
        this.loadGameObjects(p -> {
            p.addInput(this.game.getInputController());
            this.player = p;
        });
        if (this.player == null) {
            throw new IllegalStateException("Cannot find player in this scene");
        } else if (this.ghosts.size() != 4) {
            throw new IllegalStateException("One or more ghosts are missing in this scene");
        }
    }

    private Player loadGameObjects(java.util.function.Consumer<Player> fun) {
        Player player = null;
        this.scene = new Scene2D(MAP_PATH);
        for (final GameObject g : this.getGameObjects()) {
            if (g instanceof Player p) {
                player = p;
                fun.accept(p);
            } else if (g instanceof Ghost gh) {
                this.ghosts.add(gh);
            } else if (g instanceof PowerPill pl) {
                pl.connectToGameLogic(this);
            }
        }
        return player;
    }
}
