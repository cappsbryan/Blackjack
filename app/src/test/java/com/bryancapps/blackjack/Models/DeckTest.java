package com.bryancapps.blackjack.Models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bryancapps on 7/30/16.
 */
public class DeckTest {
    Deck testDeck;

    @Before
    public void setUp() {
        testDeck = new Deck();
    }

    @After
    public void tearDown() throws Exception {
        testDeck.reset();
    }

    @Test
    public void testDeal() throws Exception {
        testDeck.reset();
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            Card dealtCard = testDeck.deal();
            assertFalse(cards.contains(dealtCard));
            cards.add(dealtCard);
        }
        assertTrue(cards.size() == 52);
    }
}