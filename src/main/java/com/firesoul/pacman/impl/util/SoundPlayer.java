package com.firesoul.pacman.impl.util;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.firesoul.pacman.api.util.AudioPlayer;
import com.firesoul.pacman.impl.controller.GameCore;

public class SoundPlayer implements AudioPlayer {

    private final int loop;
    private final String filePath;
    private long currentFrame = 0;
    private AudioInputStream audioInputStream;
    private Clip clip;

    public SoundPlayer(final String name, final int loop) {
        this.filePath = AudioPlayer.PATH_TO_AUDIO + name + ".wav";
        this.loop = loop;
        this.resetAudio();
    }
    
    public SoundPlayer(final String name) {
        this(name, 0);
    }

    private void resetAudio() {
        try {
            this.audioInputStream = AudioSystem.getAudioInputStream(new File(this.filePath).getAbsoluteFile());
            this.clip = AudioSystem.getClip();
            this.clip.open(this.audioInputStream);
            FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
        } catch (Exception e) {
            GameCore.log("Cannot load audio file " + this.filePath);
            System.exit(0);
        }
    }

    @Override
    public void playOnce() {
        this.clip.loop(this.loop);
    }

    @Override
    public void stop() {
        this.currentFrame = 0;
        this.clip.stop();
        this.clip.close();
    }

    @Override
    public void pause() {
        this.currentFrame = this.clip.getMicrosecondPosition();
        this.clip.stop();
    }

    @Override
    public void resume() {
        this.clip.close();
        this.reset();
        this.clip.setMicrosecondPosition(this.currentFrame);
        this.playOnce();
    }

    @Override
    public void play() {
        this.reset();
        this.playOnce();
    }

    @Override
    public void reset() {
        this.stop();
        this.clip.setMicrosecondPosition(0);
        this.resetAudio();
    }

    @Override
    public boolean alreadyPlaying() {
        return this.currentFrame > 0;
    }
}
