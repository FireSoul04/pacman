package com.firesoul.pacman.impl.entities.bases;

import com.firesoul.pacman.api.GameObject;
import com.firesoul.pacman.api.entities.Drawable;
import com.firesoul.pacman.impl.util.Vector2D;

public class GameObject2D implements GameObject {

    private static final long serialVersionUID = 1L;

    private Vector2D position;
    private Vector2D speed;
    private Drawable drawable;
    private boolean visible;

    /**
     * Create a new GameObject2D with a method to render it.
     * @param position
     * @param speed
     * @param drawable
     */
    public GameObject2D(final Vector2D position, final Vector2D speed, final Drawable drawable) {
        this.drawable = drawable;
        this.position = position;
        this.speed = speed;
        this.visible = true;
    }

    /**
     * Create a new GameObject2D without a method to render it.
     * @param position
     * @param speed
     */
    public GameObject2D(final Vector2D position, final Vector2D speed) {
        this(position, speed, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2D getSpeed() {
        return this.speed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Drawable getDrawable() {
        return this.drawable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Set the position of entity.
     * @param position
     */
    protected void setPosition(final Vector2D position) {
        this.position = position;
    }

    /**
     * Set the speed of entity.
     * @param speed
     */
    protected void setSpeed(final Vector2D speed) {
        this.speed = speed;
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
