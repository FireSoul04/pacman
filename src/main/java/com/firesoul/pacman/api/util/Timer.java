package com.firesoul.pacman.api.util;

public interface Timer {

    public static long CONVERT_SECONDS_MILLISECONDS = 1000;

    /**
     * Start the time count
     */
    void start();

    /**
     * Restart the time count
     */
    void restart();

    /**
     * Stop when the time count reach the end
     */
    void update();

    /**
     * Stop the timer manually
     */
    void stop();

    /**
     * Pause the time count
     */
    void pause();

    /**
     * @return The timer's end time
     */
    long getEndTime();

    /**
     * @return The timer's current time
     */
    long getCurrentTime();

    /**
     * @return If the timer is counting
     */
    boolean isRunning();

    /**
     * @return If the timer can count or it's stopped
     */
    boolean isExpired();

    /**
     * Convert from millis to seconds
     * @param millis
     * @return millis in seconds
     */
    public static long millisToSeconds(final long millis) {
        return millis / CONVERT_SECONDS_MILLISECONDS;
    }

    /**
     * Convert from seconds to millis
     * @param seconds
     * @return seconds in millis
     */
    public static long secondsToMillis(final long seconds) {
        return seconds * CONVERT_SECONDS_MILLISECONDS;
    }

    /**
     * Convert from seconds to millis
     * @param seconds
     * @return seconds in millis
     */
    public static long secondsToMillis(final double seconds) {
        return (long)(seconds * CONVERT_SECONDS_MILLISECONDS);
    }

    /**
     * @param t
     * @return The time in milliseconds
     */
    public static long getTime(final long t) {
        return System.currentTimeMillis() - t;
    }

    /**
     * Get the difference between two times expressed in milliseconds, the order don't change the result
     * @param t1
     * @param t2
     * @return Difference between two times in milliseconds
     */
    public static long differenceTime(final long t1, final long t2) {
        final long time1 = System.currentTimeMillis() - Math.max(t1, t2);
        final long time2 = System.currentTimeMillis() - Math.min(t1, t2);
        return time1 - time2;
    }
}
