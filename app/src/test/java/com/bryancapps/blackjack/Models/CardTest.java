package com.bryancapps.blackjack.Models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardTest {

    @Test
    public void testConstructor() {
        Card testCard;
        for (Card.Rank rank : Card.Rank.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                testCard = new Card(rank, suit);
                assertEquals(rank, testCard.rank());
                assertEquals(suit, testCard.suit());
            }
        }
    }

    @Test
    public void testRank() throws Exception {
        Card testCard;
        for (Card.Rank rank : Card.Rank.values()) {
            testCard = new Card(rank, Card.Suit.CLUBS);
            assertEquals(rank, testCard.rank());
        }
    }

    @Test
    public void testSuit() throws Exception {
        Card testCard;
        for (Card.Suit suit : Card.Suit.values()) {
            testCard = new Card(Card.Rank.ACE, suit);
            assertEquals(suit, testCard.suit());
        }
    }

    @Test
    public void testValue() throws Exception {
        Card testA = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
        Card test2 = new Card(Card.Rank.TWO, Card.Suit.CLUBS);
        Card test3 = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        Card test4 = new Card(Card.Rank.FOUR, Card.Suit.CLUBS);
        Card test5 = new Card(Card.Rank.FIVE, Card.Suit.CLUBS);
        Card test6 = new Card(Card.Rank.SIX, Card.Suit.CLUBS);
        Card test7 = new Card(Card.Rank.SEVEN, Card.Suit.CLUBS);
        Card test8 = new Card(Card.Rank.EIGHT, Card.Suit.CLUBS);
        Card test9 = new Card(Card.Rank.NINE, Card.Suit.CLUBS);
        Card test10 = new Card(Card.Rank.TEN, Card.Suit.CLUBS);
        Card testJ = new Card(Card.Rank.JACK, Card.Suit.CLUBS);
        Card testQ = new Card(Card.Rank.QUEEN, Card.Suit.CLUBS);
        Card testK = new Card(Card.Rank.KING, Card.Suit.CLUBS);

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