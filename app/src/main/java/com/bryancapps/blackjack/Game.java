package com.bryancapps.blackjack;

import android.util.Log;

import com.bryancapps.blackjack.Models.Deck;
import com.bryancapps.blackjack.Models.GameOutcome;
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
    private long money;
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

    public void clearPlayers() {
        players.clear();
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

    public void resetForNewHand() {
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

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
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

    public long determineWinnings(Player player) {
        switch (determineOutcome(player)) {
            case PLAYER_BLACKJACK:
                return Math.round(player.getBet() * 2.5);
            case PLAYER_WIN:
                return player.getBet() * 2;
            case DEALER_BUST:
                return player.getBet() * 2;
            case PUSH:
                return player.getBet();
            case DEALER_BLACKJACK:
            case DEALER_WIN:
            case PLAYER_BUST:
            default:
                return 0;
        }
    }

    public GameOutcome determineOutcome(Player player) {
        int playerScore = player.getHand().getScore(true);
        int dealerScore = getDealerHand().getScore(true);

        if (dealerScore == playerScore && dealerScore <= 21) {
            // push
            return GameOutcome.PUSH;
        } else if (dealerScore == 21 && getDealerHand().size() == 2) {
            // dealer has a blackjack
            return GameOutcome.DEALER_BLACKJACK;
        } else if (playerScore == 21 && player.getHand().size() == 2) {
            // player has a blackjack!
            return GameOutcome.PLAYER_BLACKJACK;
        } else if (playerScore > dealerScore && playerScore <= 21) {
            // player wins!
            return GameOutcome.PLAYER_WIN;
        } else if (playerScore <= 21 && dealerScore > 21) {
            // dealer busts!
            return GameOutcome.DEALER_BUST;
        } else if (dealerScore > playerScore && dealerScore <= 21) {
            // dealer wins
            return GameOutcome.DEALER_WIN;
        } else if (playerScore > 21) {
            // player busts
            return GameOutcome.PLAYER_BUST;
        }
        Log.e(getClass().getSimpleName(), "Error determining winner");
        return GameOutcome.ERROR;
    }
}
