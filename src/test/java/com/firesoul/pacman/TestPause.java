package com.firesoul.pacman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.firesoul.pacman.api.util.Timer;
import com.firesoul.pacman.impl.util.TimerImpl;
import com.firesoul.pacman.impl.util.Vector2D;
import com.firesoul.pacman.testClasses.EntityTest;
import com.firesoul.pacman.testClasses.GameTest;

public class TestPause {

    Timer count;
    Timer interval;
    GameTest game;
    EntityTest entity;
    Vector2D expected;

    @BeforeEach
    void setup() {
        count = new TimerImpl(Timer.secondsToMillis(5));
        interval = new TimerImpl(Timer.secondsToMillis(1));
        entity = new EntityTest(Vector2D.zero(), new Vector2D(2, 0));
        entity.setTestFunction(this::move);
        expected = Vector2D.zero();
    }

    @Test
    void testMove() {
        game = new GameTest(this::onPause);
        game.addGameObject(entity);
        game.run();
    }

    void move(final EntityTest e, final double dt) {
        Vector2D newPos = e.getPosition()
            .add(e.getSpeed().dot(dt));
        e.setTestPosition(newPos);
    }

    void onPause() {
        count.start();
        interval.start();
        count.stopAtTimerEnd();
        if (count.isStopped()) {
            game.gameOver();
        }
        interval.stopAtTimerEnd();
        if (interval.isStopped()) {
            interval.restart();
            if (game.isPaused()) {
                Assertions.assertEquals(expected, entity.getPosition());
                game.start();
            } else {
                expected = entity.getPosition();
                game.pause();
            }
        }
    }
}
