package com.bryancapps.blackjack.Models;

import com.bryancapps.blackjack.Game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryancapps on 7/3/16.
 */
public class Player implements PropertyChangeListener {
    private final List<PropertyChangeListener> listeners = new ArrayList<>();
    private Hand hand;
    private int bet;

    public Player(Game game) {
        hand = new Hand(game.getDeck());
        bet = 0;

        hand.addChangeListener(this);
    }

    public void reset() {
        hand.clear();
    }

    public int getScore() {
        return hand.getScore(true);
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        String oldValue = String.valueOf(this.bet);
        this.bet = bet;
        notifyListeners(this, "bet", oldValue, String.valueOf(this.bet));
    }

    public Hand getHand() {
        return hand;
    }

    private void notifyListeners(Object object, String property, String oldValue, String newValue) {
        for (PropertyChangeListener name : listeners) {
            name.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        notifyListeners(this, "player hand", event.getOldValue().toString(),
                event.getNewValue().toString());
    }
}
