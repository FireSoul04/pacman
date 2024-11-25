package com.firesoul.pacman.impl.controller;

import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.List;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Renderer;
import com.firesoul.pacman.impl.entities.Ghost;
import com.firesoul.pacman.impl.entities.Pill;
import com.firesoul.pacman.impl.entities.Player;
import com.firesoul.pacman.impl.entities.ghosts.Blinky;
import com.firesoul.pacman.impl.entities.ghosts.Clyde;
import com.firesoul.pacman.impl.entities.ghosts.Inky;
import com.firesoul.pacman.impl.entities.ghosts.Pinky;
import com.firesoul.pacman.impl.model.Room2D;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Window;

public class Pacman implements Game {

    // private static final String MAP_PATH = "/map/";
    // private static final String ENTITY_MAP_PATH = MAP_PATH + "entities.map";
    // private static final String BLOCK_MAP_PATH = MAP_PATH + "blocks.map";
    private static final String TITLE = "Pacman";
    private static final int WIDTH = 400;
    private static final int HEIGHT = WIDTH * 3 / 4;
    private static final int SCALE = 3;
    
    private static final PrintStream logger = System.out;
    private static InputController inputController = new InputController();
    private static Vector2D dimensions;

    // Meanwhile we don't have a proper way to load the ghosts from a file
    private final List<Ghost> ghosts = List.of(
        new Blinky(new Vector2D(0, 16), new Vector2D(1, 1)),
        new Inky(new Vector2D(0, 32), new Vector2D(1, 1)),
        new Pinky(new Vector2D(0, 48), new Vector2D(1, 1)),
        new Clyde(new Vector2D(0, 64), new Vector2D(1, 1))
    );

    private final Timer nextLevelTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Timer liveLostTimer = new TimerImpl(Timer.secondsToMillis(2));
    private final Player player = new Player(Vector2D.zero(), new Vector2D(1, 1));
    private final Renderer renderer = new Window(TITLE, WIDTH, HEIGHT, SCALE);
    private Room2D room;
    private State state;
    private int level;

    public Pacman() {
        this.renderer.addInputController(Pacman.inputController);
        this.resetRoom();
        this.nextLevelTimer.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        this.level = 1;
        this.start();
        this.renderer.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.state = State.RUNNING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        this.state = State.PAUSED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver() {
        this.state = State.GAME_OVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaTime) {
        if (!this.isWaitingNextLevel() && !this.isPlayerWaitingRespawn()) {
            this.pauseOnKeyPressed(KeyEvent.VK_ESCAPE);
            this.checkNextLevel();
            this.checkPlayerDeath();
            this.checkGameOver();
            this.room.updateAll(deltaTime);
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
        return this.room.getGameObjects().stream().filter(gameObject -> gameObject.getClass().equals(clazz)).count();
    }

    private void resetRoom() {
        // When we have a proper way to load a level from a file
        //this.room = new Room2D(ENTITY_MAP_PATH, BLOCK_MAP_PATH);
        this.room = new Room2D(WIDTH, HEIGHT);
        this.player.reset();
        this.room.addGameObject(this.player);
        for (final Ghost ghost : this.ghosts) {
            ghost.reset();
            this.room.addGameObject(ghost);
        }
        for (int i = 0; i < 10; i++) {
            this.room.addGameObject(new Pill(new Vector2D(100 + i * 16, 0)));
        }
        //
        Pacman.dimensions = this.room.getDimensions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        if (Pacman.inputController.isKeyPressedOnce(KeyEvent.VK_ESCAPE)) {
            this.start();
        }
        this.room.pauseAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        this.renderer.draw(this.room.getGameObjects());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return this.state == State.RUNNING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPaused() {
        return this.state == State.PAUSED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOver() {
        return this.state == State.GAME_OVER;
    }

    /**
     * {@inheritDoc}
     */
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
