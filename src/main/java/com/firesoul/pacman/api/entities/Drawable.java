package com.firesoul.pacman.api.entities;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.firesoul.pacman.impl.util.Vector2D;

public interface Drawable {
    
    static final String PATH_TO_SPRITES = "src/main/resources/sprites/";

    /**
     * Get the image of the drawable object.
     * @return The image of the drawable object.
     */
    Image getImage();

    /**
     * Get the size of the image.
     * @return The size of the image.
     */
    default Vector2D getImageSize() {
        final Image image = this.getImage();
        return new Vector2D(
            image.getWidth(null), 
            image.getHeight(null)
        );
    }

    /**
     * Load the image of the drawable object.
     * @param name The name of the image.
     * @return The image of the drawable object.
     */
    static Image loadImage(final String name) {
        Image image = null;
        try {
            image = ImageIO.read(new File(PATH_TO_SPRITES + name + "/" + name + ".png"));
        } catch (IOException e) {
            System.err.println("Error loading " + name + " sprite");
            System.exit(1);
        }
        return image;
    }

    /**
     * Load the images of the drawable object.
     * @param name The name of the images.
     * @return The images of the drawable object.
     */
    static List<Image> loadImages(final String name) {
        final List<Image> images = new ArrayList<>();
        final File dir = new File(PATH_TO_SPRITES + name);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Error loading " + name + " sprites");
            System.exit(1);
        }
        try {
            for (final File file : dir.listFiles()) {
                images.add(ImageIO.read(file));
            }
        } catch (IOException e) {
            System.err.println("Error loading " + name + " sprites");
            System.exit(1);
        }
        return images;
    }
}
