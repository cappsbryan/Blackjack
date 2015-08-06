package com.bryancapps.blackjack;

import java.util.ArrayList;

/**
 * Representation of a hand of playing cards
 * <p/>
 * Created by bryancapps on 6/19/15.
 */
public class CardHand {

    private ArrayList<Integer> cardsInHand;
    private CardDeck deck;

    public CardHand(CardDeck deck) {
        cardsInHand = new ArrayList<>();
        this.deck = deck;
    }

    public void add(int card) {
        cardsInHand.add(card);
    }

    public int size() {
        return cardsInHand.size();
    }

    public int draw() {
        int card = deck.deal();
        add(card);
        return card;
    }

    public int getScore(boolean countFirstCard) {
        int score = 0;
        boolean hasAce = false;

        int startingIndex;
        if (countFirstCard) {
            startingIndex = 0;
        } else {
            startingIndex = 1;
        }

        for (int i = startingIndex; i < cardsInHand.size(); i++) {
            int card = cardsInHand.get(i);

            if (card >= 0 && card < 4) {
                // card is an ace and should be counted as 11 or 1
                hasAce = true;
                score += 1;
            } else if (card >= 4 && card < 20) {
                // card is a king, queen, jack, or ten and should be counted as 10
                score += 10;
            } else {
                score += (((51 - card) / 4) + 2);
            }
        }

        if (hasAce && score <= 11) {
            score += 10;
        }

        return score;
    }

    public int get(int index) {
        return cardsInHand.get(index);
    }

    public void clear() {
        cardsInHand.clear();
    }
}
