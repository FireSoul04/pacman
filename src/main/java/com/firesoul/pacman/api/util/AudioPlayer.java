package com.firesoul.pacman.api.util;

public interface AudioPlayer {
    
    static final String PATH_TO_AUDIO = "src/main/resources/audio/";

    void playOnce();

    void stop();

    void pause();

    void resume();

    void play();

    void reset();

    boolean alreadyPlaying();
}
