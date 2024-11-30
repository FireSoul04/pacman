package com.firesoul.pacman.impl.controller;

import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.List;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.model.Room2D;
import com.firesoul.pacman.impl.model.entities.*;
import com.firesoul.pacman.impl.model.entities.ghosts.*;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Window;

public class Pacman implements Game {

    private static final String TITLE = "Pacman";
    private static final String MAP_PATH = "src/main/resources/map/map.txt";
    private static final int WIDTH = 400;
    private static final int HEIGHT = WIDTH * 3 / 4;
    private static final int SCALE = 3;
    
    private static final PrintStream logger = System.out;
    private static InputController inputController = new InputController();
    private static Room2D room;
    private static Vector2D dimensions;

    // Meanwhile we don't have a proper way to load the ghosts from a file
    private final List<Ghost> ghosts = List.of(
        new Blinky(new Vector2D(0, 16), new Vector2D(1, 1)),
        new Inky(new Vector2D(0, 32), new Vector2D(1, 1)),
        new Pinky(new Vector2D(0, 48), new Vector2D(1, 1)),
        new Clyde(new Vector2D(0, 64), new Vector2D(1, 1))
    );

    private final Thread displayThread = new Thread(this, "Display");
    private final Timer nextLevelTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Player player = new Player(Vector2D.zero(), new Vector2D(1, 1));
    private final Renderer renderer = new Window(TITLE, WIDTH, HEIGHT, SCALE);
    private State state;
    private int level;

    public Pacman() {
        this.renderer.addInputController(Pacman.inputController);
        this.resetRoom();
        this.nextLevelTimer.start();
    }

    @Override
    public synchronized void init() {
        this.level = 1;
        this.start();
        this.renderer.init();
        this.displayThread.start();
    }

    @Override
    public void start() {
        this.state = State.RUNNING;
        Pacman.room.wakeAll();
    }

    @Override
    public void pause() {
        this.state = State.PAUSED;
        Pacman.room.pauseAll();
    }

    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
    }

    @Override
    public void update(final double deltaTime) {
        if (!this.isWaitingNextLevel() && !this.isPlayerWaitingRespawn()) {
            this.pauseOnKeyPressed(KeyEvent.VK_ESCAPE);
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkDeadGhosts();
            this.checkGameOver();
            Pacman.room.updateAll(deltaTime);
        }
        this.nextLevelTimer.update();
        this.liveLostTimer.update();
        if (this.isOver()) {
            System.out.println("Game Over!");
            try {
                Thread.sleep(Timer.secondsToMillis(1));
            } catch (final InterruptedException e) {
            }
            System.exit(0);
        }
    }

    private boolean isWaitingNextLevel() {
        return this.nextLevelTimer.isRunning();
    }

    private boolean isPlayerWaitingRespawn() {
        return this.liveLostTimer.isRunning();
    }

    private void pauseOnKeyPressed(final int key) {
        if (Pacman.inputController.isKeyPressedOnce(key)) {
            this.pause();
        }
    }

    private void checkNextLevel() {
        if (this.isLevelCompleted()) {
            Pacman.logger.println("Level " + this.level + " completed!");
            this.level++;
            this.resetRoom();
            this.nextLevelTimer.restart();
        }
    }

    private void checkPlayerDeath() {
        if (this.player.isDead()) {
            Pacman.logger.println("Player is dead, lives remaining: " + this.player.getLives());
            this.resetRoom();
            this.liveLostTimer.restart();
        }
    }

    private void checkGameOver() {
        if (this.player.getLives() <= 0) {
            this.gameOver();
        }
    }

    private boolean isLevelCompleted() {
        return this.howManyInstancesOf(Pill.class) == 0;
    }

    private long howManyInstancesOf(final Class<?> clazz) {
        int counter = 0;
        for (final GameObject g : Pacman.room.getGameObjects()) {
            if (g.getClass().equals(clazz)) {
                counter++;
            }
        }
        return counter;
    }

    private void checkDeadGhosts() {
        for (final GameObject g : Pacman.room.getGameObjects()) {
            if (g instanceof Ghost && ((Ghost) g).isDead()) {
                g.disable();
            }
        }
    }

    private void resetRoom() {
        Pacman.room = new Room2D(Pacman.MAP_PATH);
        this.renderer.resize(Pacman.room.getDimensions());
        this.player.reset();
        Pacman.room.addGameObject(this.player);
        for (final Ghost ghost : this.ghosts) {
            ghost.reset();
            Pacman.room.addGameObject(ghost);
        }
        Pacman.room.addGameObject(new PowerPill(new Vector2D(60, 0)));
        for (int i = 0; i < 10; i++) {
            Pacman.room.addGameObject(new Pill(new Vector2D(100 + i * 16, 0)));
        }
        //
        Pacman.dimensions = Pacman.room.getDimensions();
    }

    @Override
    public void onPause() {
        if (Pacman.inputController.isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            this.start();
        }
    }

    @Override
    public void render() {
        this.renderer.draw(Pacman.room.getGameObjects());
    }

    @Override
    public boolean isRunning() {
        return this.state == State.RUNNING;
    }

    @Override
    public boolean isPaused() {
        return this.state == State.PAUSED;
    }

    @Override
    public boolean isOver() {
        return this.state == State.GAME_OVER;
    }

    @Override
    public Renderer getRenderer() {
        return this.renderer;
    }

    /**
     * @return the current game level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Set all the ghosts vulnerable
     */
    public static void setGhostVulnerable() {
        for (final GameObject g : Pacman.room.getGameObjects()) {
            if (g instanceof Ghost) {
                ((Ghost) g).setVulnerable();
            }
        }
    }

    /**
     * @return the current game level room dimensions.
     */
    public static Vector2D getRoomDimensions() {
        return Pacman.dimensions;
    }

    /**
     * @return the key controller.
     */
    public static boolean isKeyPressed(final int key) {
        return Pacman.inputController.isKeyPressed(key);
    }

    /**
     * @return the key controller.
     */
    public static boolean isKeyPressedOnce(final int key) {
        return Pacman.inputController.isKeyPressedOnce(key);
    }

    /**
     * Log a message to standard output.
     * @param log the message to log.
     */
    public static void log(final String log) {
        Pacman.logger.println(log);
    }
}
