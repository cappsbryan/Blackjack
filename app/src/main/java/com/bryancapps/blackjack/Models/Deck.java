package com.bryancapps.blackjack.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representation of a deck of playing cards
 * <p/>
 * Created by bryancapps on 8/4/15.
 */
public class Deck implements Serializable {
    private final List<Card> drawnCards;
    private final Card.Rank[] ranks;
    private final Card.Suit[] suits;
    private Random random;

    public Deck() {
        drawnCards = new ArrayList<>();
        ranks = Card.Rank.values();
        suits = Card.Suit.values();
        random = new Random();
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
            Card.Rank rank = ranks[random.nextInt(ranks.length - 1)];
            Card.Suit suit = suits[random.nextInt(suits.length - 2)];
            card = new Card(rank, suit);
        } while (drawnCards.contains(card));

        drawnCards.add(card);
        return card;
    }

    public void reset() {
        drawnCards.clear();
        random = new Random();
    }
}
