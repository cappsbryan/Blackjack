package com.bryancapps.blackjack.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardTest {

    @Test
    public void testCreate() {
        Card testCard;
        for (Card.Rank rank : Card.Rank.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                testCard = Card.create(rank, suit);
                assertEquals(rank, testCard.rank());
                assertEquals(suit, testCard.suit());
            }
        }
    }

    @Test
    public void testRank() throws Exception {
        Card testCard;
        for (Card.Rank rank : Card.Rank.values()) {
            testCard = Card.create(rank, Card.Suit.CLUBS);
            assertEquals(rank, testCard.rank());
        }
    }

    @Test
    public void testSuit() throws Exception {
        Card testCard;
        for (Card.Suit suit : Card.Suit.values()) {
            testCard = Card.create(Card.Rank.ACE, suit);
            assertEquals(suit, testCard.suit());
        }
    }

    @Test
    public void testValue() throws Exception {
        Card testA = Card.create(Card.Rank.ACE, Card.Suit.CLUBS);
        Card test2 = Card.create(Card.Rank.TWO, Card.Suit.CLUBS);
        Card test3 = Card.create(Card.Rank.THREE, Card.Suit.CLUBS);
        Card test4 = Card.create(Card.Rank.FOUR, Card.Suit.CLUBS);
        Card test5 = Card.create(Card.Rank.FIVE, Card.Suit.CLUBS);
        Card test6 = Card.create(Card.Rank.SIX, Card.Suit.CLUBS);
        Card test7 = Card.create(Card.Rank.SEVEN, Card.Suit.CLUBS);
        Card test8 = Card.create(Card.Rank.EIGHT, Card.Suit.CLUBS);
        Card test9 = Card.create(Card.Rank.NINE, Card.Suit.CLUBS);
        Card test10 = Card.create(Card.Rank.TEN, Card.Suit.CLUBS);
        Card testJ = Card.create(Card.Rank.JACK, Card.Suit.CLUBS);
        Card testQ = Card.create(Card.Rank.QUEEN, Card.Suit.CLUBS);
        Card testK = Card.create(Card.Rank.KING, Card.Suit.CLUBS);

        assertEquals(1, testA.value());
        assertEquals(2, test2.value());
        assertEquals(3, test3.value());
        assertEquals(4, test4.value());
        assertEquals(5, test5.value());
        assertEquals(6, test6.value());
        assertEquals(7, test7.value());
        assertEquals(8, test8.value());
        assertEquals(9, test9.value());
        assertEquals(10, test10.value());
        assertEquals(10, testJ.value());
        assertEquals(10, testQ.value());
        assertEquals(10, testK.value());
    }

}