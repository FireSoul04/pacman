package com.myprojects.pacman.api;

public interface Collidable {

    boolean isColliding(final Collidable other);

    void onCollide();
}
