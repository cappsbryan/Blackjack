package com.bryancapps.blackjack.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class HandTest {
    private DealerHand testHand;

    @Before
    public void setUp() {
        testHand = new DealerHand();
    }

    @After
    public void tearDown() {
        testHand.clear();
    }

    @Test
    public void testDrawUpToSeventeen() {
        for (int i = 0; i < 1000; i++) {
            testHand.drawUpToSeventeen(new Deck(new Random()));
            int minScore = 17;
            int maxScore = 26;
            assertTrue(testHand.score() <= maxScore && testHand.score() >= minScore);
            testHand = new DealerHand();
        }
    }

}