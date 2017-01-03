package com.bryancapps.blackjack.models;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class Player implements Serializable {
    private final Game game;
    private final Hand hand;
    private final transient Subject<PlayerState> states;
    private long bet;
    private GameStatus status;

    Player(Game game, Hand hand) {
        this.game = game;
        this.hand = hand;
        bet = 0;
        status = GameStatus.BETTING;
        states = BehaviorSubject.create();

        this.hand.getEvents().subscribe(s -> publishState());
        publishState();
    }

    public Observable<PlayerState> getObservable() {
        return states.hide().observeOn(Schedulers.computation());
    }

    private void publishState() {
        states.onNext(PlayerState.builder()
                .setBet(getBet())
                .setCards(cards())
                .setStatus(status())
                .build());
    }

    void reset() {
        hand.clear();
        setStatus(GameStatus.BETTING);
    }

    public Game game() {
        return game;
    }

    public Hand hand() {
        return hand;
    }

    public long getBet() {
        return bet;
    }

    private void setBet(long bet) {
        this.bet = bet;
        publishState();
    }

    void setStatus(GameStatus status) {
        this.status = status;
        publishState();
    }

    public GameStatus status() {
        return status;
    }

    public ImmutableList<Card> cards() {
        return hand.cards();
    }

    private void draw() {
        hand.draw(game.deck());

    }

    public int score() {
        return hand.score();
    }

    // called only on initial bet, not called after split for new hands
    public void initialBet(long bet) {
        setStatus(GameStatus.HITTING);
        setBet(bet);
        game.setMoney(game.money() - bet);
        draw();
        game.drawCardForDealer();
        draw();
        game.drawCardForDealer();
        checkBlackjack();
        game.checkDealerBlackjack();
    }

    private void checkBlackjack() {
        if(hand.score() == 21 && hand.size() == 2) {
            endHand();
        }
    }

    //region Hitting and Staying

    public void hit() {
        hand.draw(game.deck());
        if (hand.score() > 21) {
            endHand();
        }
    }

    public void stay() {
        endHand();
    }

    public void doubleHand() {
        game.setMoney(game.money() - getBet());
        setBet(getBet() * 2);
        hand.draw(game.deck());
        endHand();
    }

    public void split() {
        Card card = hand.removeLastCard();
        Player newPlayer = game.newPlayer();
        newPlayer.setBet(getBet());
        game.setMoney(game.money() - newPlayer.getBet());
        newPlayer.hand.add(card);
        hand.draw(game.deck());
        newPlayer.hand.draw(game.deck());
        newPlayer.setStatus(GameStatus.HITTING);
        newPlayer.checkBlackjack();
    }

    void endHand() {
        setStatus(GameStatus.WAITING);
        if (game.shouldShowdown()) {
            game.showdown();
        }
    }

    public boolean isSplittable() {
        if (cards().size() == 2) {
            Card firstCard = cards().get(0);
            Card secondCard = cards().get(1);
            return firstCard.rank() == secondCard.rank();
        }
        return false;
    }

    public boolean isDoublable() {
        return game.money() >= getBet() && cards().size() == 2;
    }

    //endregion

    //region Hand Outcome

    public long winnings() {
        switch (outcome()) {
            case PLAYER_BLACKJACK:
                return Math.round(getBet() * 2.5);
            case PLAYER_WIN:
                return getBet() * 2;
            case DEALER_BUST:
                return getBet() * 2;
            case PUSH:
                return getBet();
            case DEALER_BLACKJACK:
            case DEALER_WIN:
            case PLAYER_BUST:
            default:
                return 0;
        }
    }

    public GameOutcome outcome() {
        int playerScore = hand.score();
        int dealerScore = game.dealerScore();
        int nPlayerCards = hand.size();
        int nDealerCards = game.dealerCards().size();

        if (dealerScore == playerScore && dealerScore <= 21) {
            // push
            return GameOutcome.PUSH;
        } else if (playerScore == 21 && nPlayerCards == 2) {
            // player has a blackjack!
            return GameOutcome.PLAYER_BLACKJACK;
        } else if (dealerScore == 21 && nDealerCards == 2) {
            // dealer has a blackjack
            return GameOutcome.DEALER_BLACKJACK;
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
        return GameOutcome.ERROR;
    }

    //endregion

}
