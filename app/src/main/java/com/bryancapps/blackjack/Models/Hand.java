package com.bryancapps.blackjack.Models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a hand of playing cards
 * <p/>
 * Created by bryancapps on 6/19/15.
 */
public class Hand {
    private final ArrayList<Card> cardsInHand;
    private final Deck deck;
    private List<PropertyChangeListener> listeners = new ArrayList<>();

    public Hand(Deck deck) {
        cardsInHand = new ArrayList<>();
        this.deck = deck;
    }

    public void add(int cardId) {
        add(new Card(cardId));
    }

    private void add(Card card) {
        String oldValue = cardsInHand.toString();
        cardsInHand.add(card);
        notifyListeners(this, "hand", oldValue, cardsInHand.toString());
    }

    public int size() {
        return cardsInHand.size();
    }

    public Card draw() {
        Card card = deck.deal();
        add(card);
        return card;
    }

    public int drawUpToSeventeen() {
        while (getScore(true) < 17) {
            draw();
        }
        return getScore(true);
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

    public boolean isSplitable() {
        return size() == 2 && get(0).value() == get(1).value();
    }

    public Card get(int index) {
        return cardsInHand.get(index);
    }

    public void clear() {
        String oldValue = cardsInHand.toString();
        cardsInHand.clear();
        notifyListeners(this, "hand", oldValue, cardsInHand.toString());
    }

    private void notifyListeners(Object object, String property, String oldValue, String newValue) {
        for (PropertyChangeListener name : listeners) {
            name.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }
}
