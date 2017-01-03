package com.bryancapps.blackjack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Representation of a deck of playing cards
 * <p/>
 * Created by Bryan Capps on 8/4/15.
 */
class Deck implements Serializable {
    private final List<Card> cards;
    private final Random random;
    private int index;

    Deck(Random random) {
        this.random = random;
        index = 0;
        cards = new ArrayList<>(52);
        for (Card.Rank rank : Card.Rank.values()) {
            if (rank == Card.Rank.BLANK) continue;
            for (Card.Suit suit : Card.Suit.values()) {
                if (suit == Card.Suit.DEALER || suit == Card.Suit.PLAYER) continue;
                cards.add(Card.create(rank, suit));
            }
        }
        shuffle();
    }

    Card draw() {
        if (index < cards.size()) {
            return cards.get(index++);
        } else {
            throw new IndexOutOfBoundsException("there are no more cards to draw from");
        }
    }

    void shuffle() {
        Collections.shuffle(cards, random);
        index = 0;
    }
}
