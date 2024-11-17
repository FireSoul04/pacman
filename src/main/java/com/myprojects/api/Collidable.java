package com.myprojects.api;

public interface Collidable {

    boolean isColliding(final Collidable other);

    void onCollide();
}
