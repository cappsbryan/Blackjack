package com.bryancapps.blackjack;

import com.bryancapps.blackjack.Models.Deck;
import com.bryancapps.blackjack.Models.GameStatus;
import com.bryancapps.blackjack.Models.Hand;
import com.bryancapps.blackjack.Models.Player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bryancapps on 7/3/16.
 */
public class Game implements PropertyChangeListener {
    private static Game ourInstance = new Game();
    private List<PropertyChangeListener> listeners = new ArrayList<>();
    private int money;
    private Hand dealerHand;
    private Deck deck;
    private Map<Player, GameStatus> status;

    private Game() {
        deck = new Deck();
        money = 0;
        dealerHand = new Hand(deck);
        status = new HashMap<>();

        dealerHand.addChangeListener(this);
    }

    public static Game getInstance() {
        return ourInstance;
    }

    public GameStatus getStatus(Player player) {
        return status.get(player);
    }

    public void setStatus(GameStatus status, Player player) {
        String oldValue = this.status.toString();
        this.status.put(player, status);

        // move to showdown if every player is waiting
        if (shouldShowdown()) {
            for (Player p : this.status.keySet()) {
                this.status.put(p, GameStatus.SHOWDOWN);
            }
        }
        notifyListeners(this, "status", oldValue, this.status.toString());
    }

    public void reset() {
        if (getMoney() <= 0) {
            setMoney(1000);
        }
        deck.reset();
        dealerHand.clear();
    }

    public int numPlayers() {
        return status.size();
    }

    private boolean shouldShowdown() {
        boolean allWaiting = true;
        for (GameStatus s : this.status.values()) {
            if (s != GameStatus.WAITING) {
                allWaiting = false;
            }
        }
        return allWaiting;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        String oldValue = String.valueOf(money);
        this.money = money;
        notifyListeners(this, "money", oldValue, String.valueOf(this.money));
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(Hand dealerHand) {
        String oldValue = this.dealerHand.toString();
        this.dealerHand = dealerHand;
        notifyListeners(this, "dealer hand", oldValue, this.dealerHand.toString());
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        String oldValue = this.deck.toString();
        this.deck = deck;
        notifyListeners(this, "deck", oldValue, this.deck.toString());
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
        if (event.getPropertyName().equals("hand")) {
            notifyListeners(this, "dealer hand", event.getOldValue().toString(),
                    event.getNewValue().toString());
        }
    }

    public void removePlayer(Player player, PropertyChangeListener listener) {
        listeners.remove(listener);
        status.remove(player);
    }
}
