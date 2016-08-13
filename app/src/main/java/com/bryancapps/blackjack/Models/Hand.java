package com.bryancapps.blackjack.Models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Representation of a hand of playing cards
 * <p/>
 * Created by bryancapps on 6/19/15.
 */
public class Hand implements Serializable, Iterable<Card> {
    private final ArrayList<Card> cardsInHand;
    private final Deck deck;
    private final List<PropertyChangeListener> listeners = new ArrayList<>();

    public Hand(Deck deck) {
        cardsInHand = new ArrayList<>();
        this.deck = deck;
    }

    public void add(Card.Rank rank, Card.Suit suit) {
        add(new Card(rank, suit));
    }

    public void add(Card card) {
        int index = cardsInHand.size();
        cardsInHand.add(card);
        notifyListeners("card " + index, null, cardsInHand.get(index));
    }

    public int size() {
        return cardsInHand.size();
    }

    public void draw() {
        Card card = deck.deal();
        add(card);
    }

    public void drawUpToSeventeen() {
        while (getScore(true) < 17) {
            draw();
        }
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

    public boolean isSplittable() {
        return size() == 2 && get(0).value() == get(1).value();
    }

    public Card get(int index) {
        return cardsInHand.get(index);
    }

    public Card remove(int index) {
        return cardsInHand.remove(index);
    }

    public void clear() {
        ArrayList<Card> oldValue = cardsInHand;
        cardsInHand.clear();
        notifyListeners("cards", oldValue, cardsInHand);
    }

    @Override
    public Iterator<Card> iterator() {
        return cardsInHand.iterator();
    }

    private void notifyListeners(String propertyName, Object oldValue, Object newValue) {
        for (PropertyChangeListener name : listeners) {
            name.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }
}
