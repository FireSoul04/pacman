package com.myprojects.pacman.impl;

import com.myprojects.pacman.api.Timer;

public class TimerImpl implements Timer {

    private final long endTime;
    private long currentTime;
    private boolean isCounting;
    private boolean isStopped;
    private long pauseTime;

    public TimerImpl(final long endTime) {
        this.endTime = endTime;
        this.currentTime = 0;
        this.isCounting = false;
        this.isStopped = false;
        this.pauseTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (!this.isCounting && !this.isStopped) {
            if (this.currentTime == 0) {
                this.currentTime = System.currentTimeMillis();
            }
            this.pauseTime = System.currentTimeMillis();
            this.isCounting = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopAtTimerEnd() {
        if (this.getCurrentTime() >= this.endTime) {
            this.isStopped = true;
            this.isCounting = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if (this.isCounting) {
            this.isStopped = true;
            this.isCounting = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        if (this.isCounting) {
            this.pauseTime = System.currentTimeMillis();
            this.isCounting = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getEndTime() {
        return this.endTime;
    }

    /**
     * {@inheritDoc}
     */
    public long getCurrentTime() {
        return this.isCounting ?
            Timer.getTime(this.pauseTime) :
            Timer.differenceTime(this.currentTime, this.pauseTime);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCounting() {
        return this.isCounting;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStopped() {
        return this.isStopped;
    }
}
