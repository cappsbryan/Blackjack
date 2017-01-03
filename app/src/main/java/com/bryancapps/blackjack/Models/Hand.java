package com.bryancapps.blackjack.models;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Representation of a hand of playing cards
 * <p/>
 * Created by bryancapps on 6/19/15.
 */
public class Hand implements Serializable, Iterable<Card> {
    private final List<Card> cards;
    private transient final Subject<String> events;

    public Hand() {
        cards = new ArrayList<>();
        events = PublishSubject.create();
    }

    public Observable<String> getEvents() {
        return events.hide();
    }

    public ImmutableList<Card> cards() {
        return ImmutableList.copyOf(cards);
    }

    public void add(Card card) {
        cards.add(card);
        events.onNext("card added");
    }

    public int size() {
        return cards.size();
    }

    public void draw(Deck deck) {
        Card card = deck.draw();
        add(card);
    }

    public Card removeLastCard() {
        Card lastCard = cards.remove(cards.size() - 1);
        events.onNext("card removed");
        return lastCard;
    }

    public int score() {
        int score = 0;
        boolean hasAce = false;

        for (int i = 0; i < cards().size(); i++) {
            Card card = cards().get(i);
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
        return cards.get(index);
    }

    public void clear() {
        cards.clear();
        events.onNext("cards cleared");
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
}
