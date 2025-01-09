package com.firesoul.pacman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.util.TimerImpl;

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
        assertFalse(timer.isRunning());
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
        assertTrue(timer.isRunning());

        long infiniteLoopCount = System.currentTimeMillis();
        while (timer.isRunning()) {
            timer.update();

            if (System.currentTimeMillis() - infiniteLoopCount >= Timer.secondsToMillis(TIMER_TIME * 5)) {
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
        Thread.sleep(WAIT_TIME);
        timer.pause();
        final long startTime = timer.getCurrentTime();
        Thread.sleep(WAIT_TIME);
        assertEquals(startTime, timer.getCurrentTime());
        timer.start();
        final long x = timer.getCurrentTime();
        assertTrue(x <= WAIT_TIME + 30 && x >= WAIT_TIME - 30);
        Thread.sleep(WAIT_TIME);
        final long y = timer.getCurrentTime();
        assertTrue(y <= WAIT_TIME * 2 + 30 && y >= WAIT_TIME * 2 - 30);
    }

    @Test
    void testStop() throws InterruptedException {
        timer.start();
        assertTrue(timer.isRunning());
        timer.stop();
        assertFalse(timer.isRunning());
        final long stopTime = timer.getCurrentTime();
        timer.start();
        Thread.sleep(WAIT_TIME);
        assertEquals(stopTime, timer.getCurrentTime());
    }

    @Test
    void testRestart() throws InterruptedException {
        timer.start();
        final long startTime = timer.getCurrentTime();
        while (timer.isRunning()) {
            timer.update();
        }
        timer.stop();
        assertTrue(timer.isExpired());
        Thread.sleep(WAIT_TIME);
        timer.startAgain();
        assertFalse(timer.isExpired());
        assertEquals(startTime, timer.getCurrentTime());
        assertTrue(timer.isRunning());
        while (timer.isRunning()) {
            timer.update();
        }
        assertTrue(timer.isExpired());
    }
}
