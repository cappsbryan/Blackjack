package com.bryancapps.blackjack.models;

import com.google.common.collect.ImmutableList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by bryancapps on 12/22/16.
 */

class DealerHand extends Hand {
    private final transient Subject<Boolean> events;
    private boolean firstCardVisibility;

    DealerHand() {
        super();
        firstCardVisibility = false;
        events = PublishSubject.create();
    }

    @Override
    public Observable<String> getEvents() {
        return Observable.merge(super.getEvents(),
                events.hide()
                        .map(b -> String.format("firstCardVisibility set to %b", b)));
    }

    void setFirstCardVisibility(boolean firstCardVisibility) {
        this.firstCardVisibility = firstCardVisibility;
        events.onNext(firstCardVisibility);
    }

    int realScore() {
        if (firstCardVisibility) {
            return score();
        } else {
            Card firstCard = super.cards().get(0);
            int score = score() + firstCard.value();
            if (score <= 11 && firstCard.rank() == Card.Rank.ACE) {
                score = score + 10;
            }
            return score;
        }
    }

    @Override
    public ImmutableList<Card> cards() {
        ImmutableList<Card> cards = super.cards();
        if (firstCardVisibility || cards.isEmpty()) {
            return cards;
        } else {
            return new ImmutableList.Builder<Card>()
                    .add(Card.dealerBlank)
                    .addAll(cards.subList(1, cards.size()))
                    .build();
        }
    }

    void drawUpToSeventeen(Deck deck) {
        while (score() < 17) {
            draw(deck);
        }
    }
}
