package com.firesoul.pacman.api.controller;

public interface EventController {

    void addEvent(String name, Event e);

    boolean getEvent(String name);
}
