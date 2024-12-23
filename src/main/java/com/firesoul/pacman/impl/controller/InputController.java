package com.firesoul.pacman.impl.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.firesoul.pacman.api.controller.Event;
import com.firesoul.pacman.api.controller.EventController;

public class InputController implements EventController {

    private final Set<Integer> keysPressed = new HashSet<>();
    private final Map<String, Event> events = new HashMap<>();
    private final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(final KeyEvent e) {
        }
    
        @Override
        public void keyPressed(final KeyEvent e) {
            keysPressed.add(e.getKeyCode());
        }
     
        @Override
        public void keyReleased(final KeyEvent e) {
            keysPressed.remove(e.getKeyCode());
        }
    };

    /**
     * Create a controller for key pressed.
     */
    public InputController() {
        this.addEvent("MoveUp", this::moveUp);
        this.addEvent("MoveDown", this::moveDown);
        this.addEvent("MoveLeft", this::moveLeft);
        this.addEvent("MoveRight", this::moveRight);
        this.addEvent("PauseGame", this::pauseGame);
    }
    
    private boolean moveUp() {
        return this.isKeyPressed(KeyEvent.VK_W) || this.isKeyPressed(KeyEvent.VK_UP);
    }
    
    private boolean moveDown() {
        return this.isKeyPressed(KeyEvent.VK_S) || this.isKeyPressed(KeyEvent.VK_DOWN);
    }
    
    private boolean moveLeft() {
        return this.isKeyPressed(KeyEvent.VK_A) || this.isKeyPressed(KeyEvent.VK_LEFT);
    }
    
    private boolean moveRight() {
        return this.isKeyPressed(KeyEvent.VK_D) || this.isKeyPressed(KeyEvent.VK_RIGHT);
    }

    private boolean pauseGame() {
        return this.isKeyPressedOnce(KeyEvent.VK_ESCAPE);
    }

    @Override
    public void addEvent(final String name, final Event e) {
        this.events.put(name, e);
    }

    @Override
    public boolean getEvent(final String name) {
        return this.events.get(name).check();
    }
    
    /**
     * Check if a key is pressed.
     * @param keyCode the key code
     * @return true if the key is pressed
     */
    public boolean isKeyPressed(final int keyCode) {
        return this.keysPressed.contains(keyCode);
    }

    /**
     * Check if a key is pressed once.
     * @param keyCode the key code
     * @return true if the key is pressed once
     */
    public boolean isKeyPressedOnce(final int keyCode) {
        final boolean pressed = this.keysPressed.contains(keyCode);
        if (pressed) {
            this.keysPressed.remove(keyCode);
        }
        return pressed;
    }

    /**
     * @return key controller the game is listening to
     */
    public KeyListener getKeyListener() {
        return this.keyListener;
    }
}
