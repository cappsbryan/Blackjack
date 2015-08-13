package com.bryancapps.blackjack;

import java.util.ArrayList;

/**
 * Representation of a hand of playing cards
 * <p/>
 * Created by bryancapps on 6/19/15.
 */
public class CardHand {

    private ArrayList<Card> cardsInHand;
    private CardDeck deck;

    public CardHand(CardDeck deck) {
        cardsInHand = new ArrayList<>();
        this.deck = deck;
    }

    public void add(int cardId) {
        cardsInHand.add(new Card(cardId));
    }

    public void add(Card card) {
        cardsInHand.add(card);
    }

    public int size() {
        return cardsInHand.size();
    }

    public Card draw() {
        Card card = deck.deal();
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
            Card card = cardsInHand.get(i);
            if (card.value() == 1) {
                hasAce = true;
            }
            score += card.value();
        }

        if (hasAce && score <= 11) {
            score += 10;
        }

        return score;
    }

    public Card get(int index) {
        return cardsInHand.get(index);
    }

    public void clear() {
        cardsInHand.clear();
    }
}
