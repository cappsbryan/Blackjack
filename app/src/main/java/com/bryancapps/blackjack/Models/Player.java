package com.bryancapps.blackjack.Models;

import com.bryancapps.blackjack.Game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryancapps on 7/3/16.
 */
public class Player implements PropertyChangeListener, Serializable {
    private final List<PropertyChangeListener> listeners = new ArrayList<>();
    private final Hand hand;
    private int bet;
    private boolean doubled;
    private GameStatus gameStatus;

    public Player(Game game) {
        game.addPlayer(this);
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

    public GameStatus getStatus() {
        return gameStatus;
    }

    public void setStatus(GameStatus status) {
        GameStatus oldValue = this.gameStatus;
        this.gameStatus = status;
        notifyListeners(this, "status", oldValue, this.gameStatus);
    }

    private void notifyListeners(Object object, String property, Object oldValue, Object newValue) {
        for (PropertyChangeListener name : listeners) {
            name.propertyChange(new PropertyChangeEvent(object, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }

    public void removeChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getSource() instanceof Hand) {
            notifyListeners(this, "player hand",
                    event.getOldValue(), event.getNewValue());
        } else {
            notifyListeners(event.getSource(), event.getPropertyName(),
                    event.getOldValue(), event.getNewValue());
        }
    }

    public boolean isDoubled() {
        return doubled;
    }

    public void setDoubled(boolean doubled) {
        this.doubled = doubled;
    }
}
