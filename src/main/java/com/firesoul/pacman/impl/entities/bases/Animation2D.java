package com.firesoul.pacman.impl.entities.bases;

import java.awt.Image;
import java.util.List;

import com.firesoul.pacman.api.entities.Drawable;
import com.firesoul.pacman.api.util.Timer;
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
        this.animationTimer.stopAtTimerEnd();
        if (this.animationTimer.isStopped()) {
            this.animationFrame = (this.animationFrame + 1) % this.frames.size();
            this.animationTimer.restart();
        }
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
