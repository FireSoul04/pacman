package com.firesoul.pacman.impl.util;

import com.firesoul.pacman.api.util.Timer;

public class TimerImpl implements Timer {

    private static final int INFINITE_TIMER = -1;

    private final long endTime;
    private long startTime;
    private long pauseTime;
    private boolean isCounting;
    private boolean isStopped;

    /**
     * Create a timer
     * @param endTime Time when the timer stops
     */
    public TimerImpl(final long endTime) {
        this.endTime = endTime;
        this.startTime = 0;
        this.pauseTime = 0;
        this.isCounting = false;
        this.isStopped = false;
    }

    /**
     * Create a timer that ends when stop is called
     */
    public TimerImpl() {
        this(INFINITE_TIMER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (this.pauseTime == 0) {
            this.pauseTime = System.currentTimeMillis();
        }
        if (!this.isCounting && !this.isStopped) {
            this.startTime = System.currentTimeMillis() - Timer.getTime(this.pauseTime);
            this.isCounting = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restart() {
        this.pauseTime = 0;
        this.isStopped = false;
        this.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (this.getRemainingTime() <= 0) {
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
    @Override
    public long getEndTime() {
        return this.endTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCurrentTime() {
        return this.isCounting
            ? Timer.getTime(this.startTime)
            : Timer.differenceTime(this.startTime, this.pauseTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getRemainingTime() {
        return this.endTime - this.startTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return this.isCounting;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpired() {
        return this.isStopped;
    }
}
