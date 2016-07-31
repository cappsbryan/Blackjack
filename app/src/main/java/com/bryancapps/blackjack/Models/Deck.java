package com.bryancapps.blackjack.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a deck of playing cards
 * <p/>
 * Created by bryancapps on 8/4/15.
 */
public class Deck implements Serializable {
    private List<Card> drawnCards;

    public Deck() {
        drawnCards = new ArrayList<>();
        reset();
    }

    /**
     * Returns a random card from the deck and removes that card from the deck
     *
     * @return An int representing a playing card
     */
    public Card deal() {
        Card card;
        do {
            Card.Rank rank = Card.Rank.randomRank();
            Card.Suit suit = Card.Suit.randomSuit();
            card = new Card(rank, suit);
        } while (drawnCards.contains(card));

        drawnCards.add(card);
        return card;
    }

    public void reset() {
        drawnCards.clear();
    }
}
