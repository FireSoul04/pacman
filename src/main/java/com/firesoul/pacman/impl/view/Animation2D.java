package com.firesoul.pacman.impl.view;

import java.awt.Image;
import java.util.List;

import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.api.view.Drawable;
import com.firesoul.pacman.impl.util.TimerImpl;

public class Animation2D implements Drawable {
    
    private final boolean oneShot;
    private final Timer animationTimer;
    private final List<Image> frames;
    private int animationFrame = 0;

    /**
     * Creates a 2D animation that once finished it stops.
     * @param name The name of the animation
     * @param variant The variant of the animation
     * @param animationSpeed The speed of the animation
     * @param oneShot If the animation won't loop
     */
    public Animation2D(final String name, final String variant, final long animationSpeed, final boolean oneShot) {
        this.frames = Drawable.loadImages(name, variant);
        this.animationTimer = new TimerImpl(animationSpeed);
        this.animationTimer.start();
        this.oneShot = oneShot;
    }

    /**
     * Creates a 2D animation that loops itself.
     * @param name The name of the animation
     * @param variant The variant of the animation
     * @param animationSpeed The speed of the animation
     */
    public Animation2D(final String name, final String variant, final long animationSpeed) {
        this(name, variant, animationSpeed, false);
    }

    /**
     * Creates a 2D animation that loops itself.
     * @param name The name of the animation
     * @param animationSpeed The speed of the animation
     * @param oneShot If the animation won't loop
     */
    public Animation2D(final String name, final long animationSpeed, final boolean oneShot) {
        this(name, name, animationSpeed, oneShot);
    }

    /**
     * Creates a 2D animation that loops itself.
     * @param name The name of the animation
     * @param animationSpeed The speed of the animation
     */
    public Animation2D(final String name, final long animationSpeed) {
        this(name, name, animationSpeed);
    }

    @Override
    public Image getImage() {
        return this.frames.get(this.animationFrame);
    }

    /**
     * Update the animation.
     */
    public void update() {
        if (this.animationFrame == this.frames.size() - 1 && this.oneShot) {
            return;
        }
        this.animationTimer.update();
        if (this.animationTimer.isExpired()) {
            this.animationFrame++;
            if (this.animationFrame >= this.frames.size()) {
                this.animationFrame = 0;
            }
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
     * @return The current frame
     */
    public int getCurrentFrame() {
        return this.animationFrame;
    }

    /**
     * Add a frame to the animation.
     * @param image The frame to add
     */
    public void addFrame(final Image image) {
        this.frames.add(image);
    }

    /**
     * Set the variant of the animation.
     * @param variant The variant of the animation
     */
    public void setVariant(final String variant) {
        this.reset();
        this.frames.clear();
        this.frames.addAll(Drawable.loadImages(variant, variant));
    }
}
