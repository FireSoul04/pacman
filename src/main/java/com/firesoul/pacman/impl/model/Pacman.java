package com.firesoul.pacman.impl.model;

import java.awt.event.KeyEvent;
import java.util.List;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.controller.GameCore;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.model.entities.ghosts.*;
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

    private final GameCore game;
    private final Timer nextLevelTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(2));
    // Meanwhile we don't have a proper way to load the ghosts from a file
    private List<Ghost> ghosts;
    private Player player;
    
    private int level;

    public Pacman(final GameCore game) {
        this.game = game;
    }

    public void init() {
        this.level = 1;
        this.nextLevelTimer.start();
        this.player = new Player(this.game.getScene(), this.game.getInputController());
        this.ghosts = List.of(
            new Blinky(this.game.getScene())//,
            // new Inky(this.game.getScene()),
            // new Pinky(this.game.getScene()),
            // new Clyde(this.game.getScene())
        );
        this.reset();
    }

    public void update(final double deltaTime) {
        if (!this.isWaitingNextLevel() && !this.isPlayerWaitingRespawn()) {
            this.game.pauseOnKeyPressed(KeyEvent.VK_ESCAPE);
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkDeadGhosts();
            this.checkGameOver();
            game.updateAll(deltaTime);
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
            this.game.resetScene();
            this.nextLevelTimer.restart();
        }
    }

    private void checkPlayerDeath() {
        if (this.player.isDead()) {
            GameCore.log("Player is dead, lives remaining: " + this.player.getLives());
            this.game.resetScene();
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
        for (final GameObject g : this.game.getGameObjects()) {
            if (g.getClass().equals(clazz)) {
                counter++;
            }
        }
        return counter;
    }

    private void checkDeadGhosts() {
        for (final GameObject g : this.game.getGameObjects()) {
            if (g instanceof Ghost && ((Ghost) g).isDead()) {
                g.disable();
            }
        }
    }

    /**
     * Set all the ghosts vulnerable
     */
    public void setGhostVulnerable() {
        for (final GameObject g : this.game.getGameObjects()) {
            if (g instanceof Ghost) {
                ((Ghost) g).setVulnerable();
            }
        }
    }

    /**
     * @return the current game level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Resets all the gameObjects of the scene.
     */
    public void reset() {
        this.player.reset();
        this.game.addGameObject(this.player);
        for (final Ghost ghost : this.ghosts) {
            ghost.reset();
            this.game.addGameObject(ghost);
        }
        this.game.addGameObject(new PowerPill(new Vector2D(60, 16), this));
        for (int i = 0; i < 10; i++) {
            this.game.addGameObject(new Pill(new Vector2D(100 + i * 16, 16)));
        }
    }
}
