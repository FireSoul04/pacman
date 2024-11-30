package com.firesoul.pacman.impl.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class InputController implements KeyListener {

    private Map<Integer, Boolean> keys = new HashMap<>();

    /**
     * Create a controller for key pressed.
     */
    public InputController() {
        keys.put(KeyEvent.VK_W, false);
        keys.put(KeyEvent.VK_A, false);
        keys.put(KeyEvent.VK_S, false);
        keys.put(KeyEvent.VK_D, false);
        keys.put(KeyEvent.VK_ESCAPE, false);
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        keys.put(e.getKeyCode(), true);
    }
 
    @Override
    public void keyReleased(final KeyEvent e) {
        keys.put(e.getKeyCode(), false);
    }
    
    /**
     * Check if a key is pressed.
     * @param keyCode the key code.
     * @return true if the key is pressed.
     */
    public boolean isKeyPressed(final int keyCode) {
        return keys.getOrDefault(keyCode, false);
    }

    /**
     * Check if a key is pressed once.
     * @param keyCode the key code.
     * @return true if the key is pressed once.
     */
    public boolean isKeyPressedOnce(final int keyCode) {
        if (this.isKeyPressed(keyCode)) {
            keys.put(keyCode, false);
            return true;
        }
        return false;
    }
}
