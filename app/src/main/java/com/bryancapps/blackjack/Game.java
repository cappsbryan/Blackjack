package com.bryancapps.blackjack;

import com.bryancapps.blackjack.Models.Deck;
import com.bryancapps.blackjack.Models.GameStatus;
import com.bryancapps.blackjack.Models.Hand;
import com.bryancapps.blackjack.Models.Player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryancapps on 7/3/16.
 */
public class Game implements PropertyChangeListener {
    private static final Game ourInstance = new Game();
    private final List<PropertyChangeListener> listeners = new ArrayList<>();
    private int money;
    private Hand dealerHand;
    private Deck deck;
    private List<Player> players;

    private Game() {
        deck = new Deck();
        money = 0;
        dealerHand = new Hand(deck);
        players = new ArrayList<>();

        dealerHand.addChangeListener(this);
    }

    public static Game getInstance() {
        return ourInstance;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameStatus getStatus(Player player) {
        return player.getStatus();
    }

    public void setStatus(GameStatus status, Player player) {
        player.setStatus(status);

        // move to showdown if every player is waiting
        if (shouldShowdown()) {
            for (Player p : players) {
                p.setStatus(GameStatus.SHOWDOWN);
            }
        }
    }

    public void reset() {
        if (getMoney() <= 0) {
            setMoney(1000);
        }
        deck.reset();
        dealerHand.clear();
    }

    public int numPlayers() {
        return players.size();
    }

    private boolean shouldShowdown() {
        boolean allWaiting = true;
        for (Player player : players) {
            if (player.getStatus() != GameStatus.WAITING) {
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

    public void removePlayer(Player player) {
        players.remove(player);
    }

    private void notifyListeners(Object source, String property, Object oldValue, Object newValue) {
        for (PropertyChangeListener name : listeners) {
            name.propertyChange(new PropertyChangeEvent(source, property, oldValue, newValue));
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
            notifyListeners(this, "dealer hand",
                    event.getOldValue(), event.getNewValue());
        } else {
            notifyListeners(event.getSource(), event.getPropertyName(),
                    event.getOldValue(), event.getNewValue());
        }
    }
}
