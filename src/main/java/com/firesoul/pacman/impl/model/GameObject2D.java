package com.firesoul.pacman.impl.model;

import com.firesoul.pacman.api.model.GameObject;
import com.firesoul.pacman.api.view.Drawable;
import com.firesoul.pacman.impl.util.Vector2D;

public class GameObject2D implements GameObject {

    private static final long serialVersionUID = 1L;

    private Vector2D position;
    private Scene2D scene;
    private Drawable drawable;
    private boolean visible;
    private boolean active;

    /**
     * Create a new GameObject2D with a method to render it.
     * @param position
     * @param scene
     * @param drawable
     */
    public GameObject2D(final Vector2D position, final Scene2D scene, final Drawable drawable) {
        this.drawable = drawable;
        this.position = position;
        this.scene = scene;
        this.visible = true;
        this.active = true;
    }

    /**
     * Create a new GameObject2D without a method to render it.
     * @param position
     * @param scene
     */
    public GameObject2D(final Vector2D position, final Scene2D scene) {
        this(position, scene, null);
    }

    public GameObject2D(final Vector2D position, final Drawable drawable) {
        this(position, null, drawable);
    }

    public GameObject2D(final Vector2D position) {
        this(position, null, null);
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Scene2D getScene() {
        return this.scene;
    }

    @Override
    public Drawable getDrawable() {
        return this.drawable;
    }

    @Override
    public void setScene(final Scene2D scene) {
        this.scene = scene;
    }

    @Override
    public void disable() {
        this.active = false;
    }

    @Override
    public void enable() {
        this.active = true;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Default method doesn't do anything. Override to make use of this.
     */
    @Override
    public void pause() {
    }

    /**
     * Default method doesn't do anything. Override to make use of this.
     */
    @Override
    public void wake() {
    }

    /**
     * Set the position of entity.
     * @param position
     */
    protected void setPosition(final Vector2D position) {
        this.position = position;
    }

    /**
     * Set the drawable of entity.
     * @param drawable
     */
    protected void setDrawable(final Drawable drawable) {
        this.drawable = drawable;
    }

    /**
     * Set the visibility of entity.
     * @param visible
     */
    protected void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
