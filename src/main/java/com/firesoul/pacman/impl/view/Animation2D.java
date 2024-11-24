package com.firesoul.pacman.impl.view;

import java.awt.Image;
import java.util.List;

import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Drawable;
import com.firesoul.pacman.impl.util.TimerImpl;

public class Animation2D implements Drawable {
    
    private final Timer animationTimer;
    private final List<Image> frames;
    private int animationFrame;

    public Animation2D(final String name, final long animationSpeed) {
        this.animationFrame = 0;
        this.frames = Drawable.loadImages(name);
        this.animationTimer = new TimerImpl(animationSpeed);
        this.animationTimer.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getImage() {
        return this.frames.get(this.animationFrame);
    }

    /**
     * Update the animation.
     */
    public void update() {
        this.animationTimer.update();
        if (this.animationTimer.isExpired()) {
            this.animationFrame = (this.animationFrame + 1) % this.frames.size();
            this.animationTimer.restart();
        }
    }

    /**
     * Start the animation.
     */
    public void start() {
        this.animationTimer.start();
    }

    /**
     * Stop the animation.
     */
    public void stop() {
        this.animationTimer.stop();
    }

    /**
     * Reset the animation.
     */
    public void reset() {
        this.animationFrame = 0;
    }

    /**
     * Get the current frame of the animation.
     * @return The current frame.
     */
    public int getCurrentFrame() {
        return this.animationFrame;
    }

    /**
     * Add a frame to the animation.
     * @param image The frame to add.
     */
    public void addFrame(final Image image) {
        this.frames.add(image);
    }
}
