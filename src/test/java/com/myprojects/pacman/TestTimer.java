package com.myprojects.pacman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myprojects.pacman.api.Timer;
import com.myprojects.pacman.impl.TimerImpl;

public class TestTimer {

    static double TIMER_TIME = 0.5;
    static long WAIT_TIME = 400;

    Timer timer;

    @BeforeEach
    void setupTimer() {
        timer = new TimerImpl(Timer.secondsToMillis(TIMER_TIME));
    }

    @Test
    void testSetup() {
        assertEquals(Timer.secondsToMillis(TIMER_TIME), timer.getEndTime());
        assertFalse(timer.isCounting());
    }

    @Test
    void testStartTwice() throws InterruptedException {
        timer.start();
        final long startTime = timer.getCurrentTime();
        Thread.sleep(WAIT_TIME);
        timer.start();
        assertNotEquals(startTime, timer.getCurrentTime());
    }

    @Test
    void testStopAtEnd() throws InterruptedException {
        timer.start();
        assertEquals(Timer.secondsToMillis(TIMER_TIME), timer.getEndTime());
        assertTrue(timer.isCounting());

        long infiniteLoopCount = System.currentTimeMillis();
        while (timer.isCounting()) {
            timer.stopAtTimerEnd();

            if (System.currentTimeMillis() - infiniteLoopCount >= Timer.secondsToMillis(TIMER_TIME * 2)) {
                Assertions.fail("Infinite loop detected");
            }
        }
        final long stopTime = timer.getCurrentTime();
        timer.start();
        Thread.sleep(WAIT_TIME);
        assertEquals(stopTime, timer.getCurrentTime());
    }

    @Test
    void testPause() throws InterruptedException {
        timer.start();
        timer.pause();
        final long startTime = timer.getCurrentTime();
        Thread.sleep(WAIT_TIME);
        assertEquals(startTime, timer.getCurrentTime());
        timer.start();
        assertEquals(startTime, timer.getCurrentTime());
        Thread.sleep(WAIT_TIME);
        final long y = timer.getCurrentTime();
        assertTrue(y <= WAIT_TIME + 5 && y >= WAIT_TIME - 5);
    }

    @Test
    void testStop() throws InterruptedException {
        timer.start();
        assertTrue(timer.isCounting());
        timer.stop();
        assertFalse(timer.isCounting());
        final long stopTime = timer.getCurrentTime();
        timer.start();
        Thread.sleep(WAIT_TIME);
        assertEquals(stopTime, timer.getCurrentTime());
    }
}
