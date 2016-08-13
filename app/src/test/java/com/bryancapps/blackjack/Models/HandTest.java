package com.bryancapps.blackjack.Models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HandTest {
    private Hand testHand;

    @Before
    public void setUp() {
        testHand = new Hand(new Deck());
    }

    @After
    public void tearDown() {
        testHand.clear();
    }

    @Test
    public void testDrawUpToSeventeen() {
        for (int i = 0; i < 1000; i++) {
            testHand.drawUpToSeventeen();
            int minScore = 17;
            int maxScore = 26;
            assertTrue(testHand.getScore(true) <= maxScore && testHand.getScore(true) >= minScore);
            testHand = new Hand(new Deck());
        }
    }

}