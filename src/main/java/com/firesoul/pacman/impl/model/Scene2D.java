package com.firesoul.pacman.impl.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.model.Room;
import com.firesoul.pacman.api.model.entities.Collidable;
import com.firesoul.pacman.api.model.entities.Movable;
import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.model.entities.Wall;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.impl.view.Invisible2D;

public class Scene2D implements Room {

    private static final int DEFAULT_WIDTH = 224;
    private static final int DEFAULT_HEIGHT = 288;

    private final Timer collisionTimer = new TimerImpl(Timer.secondsToMillis(1 / 60));
    private final List<GameObject> gameObjects = new ArrayList<GameObject>();
    private final List<Collidable> cachedCollidables = new ArrayList<Collidable>();
    private final Map2D map;

    /**
     * Default constructor for a room with no entities or blocks.
     */
    public Scene2D() {
        this(Scene2D.DEFAULT_WIDTH, Scene2D.DEFAULT_HEIGHT);
    }

    public Scene2D(final int width, final int height) {
        this.map = new Map2D(width, height);
        this.collisionTimer.start();
    }

    /**
     * Constructor for a room with a given entity and block map.
     * 
     * @param entityMapPath the path to the entity map
     * @param blockMapPath the path to the block map
     */
    public Scene2D(final String mapPath) {
        this.map = new Map2D(mapPath);
        this.collisionTimer.start();
        
        for (final GameObject g : this.map.getGameObjects()) {
            if (g instanceof Wall) {
                ((Wall)g).setDrawable(new Invisible2D(((Wall)g).getColliders().get(0).getDimensions()));
            }
            this.addGameObject(g);
        }
    }

    @Override
    public void updateAll(final double deltaTime) {
        for (final GameObject gameObject : this.gameObjects) {
            if (gameObject instanceof Movable) {
                ((Movable) gameObject).update(deltaTime);
            }
        }
        this.removeInactiveGameObjects();
        this.checkCollisions();
    }

    @Override
    public void pauseAll() {
        for (final GameObject gameObject : this.gameObjects) {
            gameObject.pause();
        }
    }

    @Override
    public void wakeAll() {
        for (final GameObject gameObject : this.gameObjects) {
            gameObject.wake();
        }
    }

    @Override
    public void addGameObject(final GameObject gameObject) {
        this.gameObjects.add(gameObject);
        if (gameObject instanceof Collidable) {
            this.cachedCollidables.add((Collidable) gameObject);
        }
    }

    @Override
    public void removeGameObject(final GameObject gameObject) {
        this.gameObjects.remove(gameObject);
        if (gameObject instanceof Collidable) {
            this.cachedCollidables.remove((Collidable) gameObject);
        }
    }

    @Override
    public List<GameObject> getGameObjects() {
        return List.copyOf(this.gameObjects);
    }

    /**
     * @return the dimensions of the room.
     */
    public Vector2D getDimensions() {
        return this.map.getDimensions();
    }

    /**
     * Remove all inactive game objects from the room.
     */
    public void removeInactiveGameObjects() {
        final Iterator<GameObject> it = this.gameObjects.iterator();
        while (it.hasNext()) {
            final GameObject gameObject = it.next();
            if (!gameObject.isActive()) {
                it.remove();
                if (gameObject instanceof Collidable) {
                    this.cachedCollidables.remove((Collidable) gameObject);
                }
            }
        }
    }

    private void checkCollisions() {
        this.collisionTimer.update();
        if (this.collisionTimer.isExpired()) {
            for (final Collidable c1 : this.cachedCollidables) {
                for (final Collidable c2 : this.cachedCollidables) {
                    if (c1 != c2 && c1.isColliding(c2)) {
                        c1.onCollide(c2);
                        c2.onCollide(c1);
                    }
                }
            }
            this.collisionTimer.restart();
        }
    }
}
