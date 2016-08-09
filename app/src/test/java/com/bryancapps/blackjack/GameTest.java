package com.bryancapps.blackjack;

import com.bryancapps.blackjack.Models.Card;
import com.bryancapps.blackjack.Models.Deck;
import com.bryancapps.blackjack.Models.GameOutcome;
import com.bryancapps.blackjack.Models.GameStatus;
import com.bryancapps.blackjack.Models.Hand;
import com.bryancapps.blackjack.Models.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by bryancapps on 7/30/16.
 */
public class GameTest {
    private Game testGame;

    @Before
    public void setUp() throws Exception {
        testGame = Game.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        testGame.resetForNewHand();
        testGame.clearPlayers();
    }

    @Test
    public void testAddPlayer() throws Exception {
        new Player(testGame);
        new Player(testGame);
        new Player(testGame);
    }

    @Test
    public void testGetPlayers() throws Exception {
        testAddPlayer();
        assertEquals(3, testGame.numPlayers());
    }

    @Test
    public void testClearPlayers() throws Exception {
        testAddPlayer();
        testGame.clearPlayers();
        assertEquals(0, testGame.numPlayers());
    }

    @Test
    public void testGetStatus() throws Exception {
        Player player = new Player(testGame);
        testGame.setStatus(GameStatus.HITTING, player);
        assertEquals(GameStatus.HITTING, testGame.getStatus(player));
    }

    @Test
    public void testSetStatus() throws Exception {
        testGame.setStatus(GameStatus.HITTING, new Player(testGame));
    }

    @Test
    public void testResetForNewHand() throws Exception {
        testGame.getDealerHand().add(Card.Rank.ACE, Card.Suit.SPADES);
        testGame.getDealerHand().add(Card.Rank.ACE, Card.Suit.CLUBS);
        testGame.resetForNewHand();
        assertEquals(0, testGame.getDealerHand().size());
    }

    @Test
    public void testNumPlayers() throws Exception {
        testAddPlayer();
        assertEquals(3, testGame.numPlayers());
    }

    @Test
    public void testGetMoney() throws Exception {
        testSetMoney();
        assertEquals(24, testGame.getMoney());
    }

    @Test
    public void testSetMoney() throws Exception {
        testGame.setMoney(24);
    }

    @Test
    public void testGetDealerHand() throws Exception {
        Hand dealerHand = new Hand(testGame.getDeck());
        testGame.setDealerHand(dealerHand);
        assertEquals(dealerHand, testGame.getDealerHand());
    }

    @Test
    public void testSetDealerHand() throws Exception {
        Hand dealerHand = new Hand(testGame.getDeck());
        testGame.setDealerHand(dealerHand);
    }

    @Test
    public void testGetDeck() throws Exception {
        Deck deck = new Deck();
        testGame.setDeck(deck);
        assertEquals(deck, testGame.getDeck());
    }

    @Test
    public void testSetDeck() throws Exception {
        Deck deck = new Deck();
        testGame.setDeck(deck);
    }

    @Test
    public void testRemovePlayer() throws Exception {
        Player player = new Player(testGame);
        testGame.addPlayer(player);
        testGame.removePlayer(player);
    }

    @Test
    public void testDetermineWinnings() throws Exception {
        Player blackjackPlayer = new Player(testGame);
        blackjackPlayer.setBet(400);
        blackjackPlayer.getHand().add(Card.Rank.ACE, Card.Suit.SPADES);
        blackjackPlayer.getHand().add(Card.Rank.KING, Card.Suit.SPADES);

        Hand blackjackDealerHand = new Hand(testGame.getDeck());
        blackjackDealerHand.add(Card.Rank.ACE, Card.Suit.HEARTS);
        blackjackDealerHand.add(Card.Rank.QUEEN, Card.Suit.HEARTS);
        testGame.setDealerHand(blackjackDealerHand);

        // player blackjack + dealer blackjack = push
        assertEquals(400, testGame.determineWinnings(blackjackPlayer));

        Hand twentyOneDealerHand = new Hand(testGame.getDeck());
        twentyOneDealerHand.add(Card.Rank.FIVE, Card.Suit.HEARTS);
        twentyOneDealerHand.add(Card.Rank.FIVE, Card.Suit.CLUBS);
        twentyOneDealerHand.add(Card.Rank.ACE, Card.Suit.DIAMONDS);
        testGame.setDealerHand(twentyOneDealerHand);

        // player blackjack + dealer 21 = player blackjack
        assertEquals(1000, testGame.determineWinnings(blackjackPlayer));

        Player twentyPlayer = new Player(testGame);
        twentyPlayer.setBet(300);
        twentyPlayer.getHand().add(Card.Rank.JACK, Card.Suit.SPADES);
        twentyPlayer.getHand().add(Card.Rank.KING, Card.Suit.SPADES);

        // player 20 + dealer 21 = player loses
        assertEquals(0, testGame.determineWinnings(twentyPlayer));

        Hand seventeenDealerHand = new Hand(testGame.getDeck());
        seventeenDealerHand.add(Card.Rank.EIGHT, Card.Suit.HEARTS);
        seventeenDealerHand.add(Card.Rank.NINE, Card.Suit.SPADES);
        testGame.setDealerHand(seventeenDealerHand);

        // player 20 + dealer 17 = player win
        assertEquals(600, testGame.determineWinnings(twentyPlayer));

        testGame.setDealerHand(blackjackDealerHand);

        // player 20 + dealer blackjack = dealer blackjack / player loses
        assertEquals(0, testGame.determineWinnings(twentyPlayer));

        testGame.setDealerHand(seventeenDealerHand);

        Player bustPlayer = new Player(testGame);
        bustPlayer.setBet(500);
        bustPlayer.getHand().add(Card.Rank.TEN, Card.Suit.CLUBS);
        bustPlayer.getHand().add(Card.Rank.SIX, Card.Suit.HEARTS);
        bustPlayer.getHand().add(Card.Rank.QUEEN, Card.Suit.DIAMONDS);

        // player busts + dealer 17 = player busts / player loses
        assertEquals(0, testGame.determineWinnings(bustPlayer));

        Hand bustDealerHand = new Hand(testGame.getDeck());
        bustDealerHand.add(Card.Rank.SEVEN, Card.Suit.HEARTS);
        bustDealerHand.add(Card.Rank.NINE, Card.Suit.HEARTS);
        bustDealerHand.add(Card.Rank.JACK, Card.Suit.HEARTS);
        testGame.setDealerHand(bustDealerHand);

        // player 20 + dealer busts = dealer busts / player wins
        assertEquals(600, testGame.determineWinnings(twentyPlayer));
    }

    @Test
    public void testDetermineOutcome() throws Exception {
        Player blackjackPlayer = new Player(testGame);
        blackjackPlayer.setBet(400);
        blackjackPlayer.getHand().add(Card.Rank.ACE, Card.Suit.SPADES);
        blackjackPlayer.getHand().add(Card.Rank.KING, Card.Suit.SPADES);

        Hand blackjackDealerHand = new Hand(testGame.getDeck());
        blackjackDealerHand.add(Card.Rank.ACE, Card.Suit.HEARTS);
        blackjackDealerHand.add(Card.Rank.QUEEN, Card.Suit.HEARTS);
        testGame.setDealerHand(blackjackDealerHand);

        // player blackjack + dealer blackjack = push
        assertEquals(GameOutcome.PUSH, testGame.determineOutcome(blackjackPlayer));

        Hand twentyOneDealerHand = new Hand(testGame.getDeck());
        twentyOneDealerHand.add(Card.Rank.FIVE, Card.Suit.HEARTS);
        twentyOneDealerHand.add(Card.Rank.FIVE, Card.Suit.CLUBS);
        twentyOneDealerHand.add(Card.Rank.ACE, Card.Suit.DIAMONDS);
        testGame.setDealerHand(twentyOneDealerHand);

        // player blackjack + dealer 21 = player blackjack
        assertEquals(GameOutcome.PLAYER_BLACKJACK, testGame.determineOutcome(blackjackPlayer));

        Player twentyPlayer = new Player(testGame);
        twentyPlayer.setBet(300);
        twentyPlayer.getHand().add(Card.Rank.JACK, Card.Suit.SPADES);
        twentyPlayer.getHand().add(Card.Rank.KING, Card.Suit.SPADES);

        // player 20 + dealer 21 = dealer wins
        assertEquals(GameOutcome.DEALER_WIN, testGame.determineOutcome(twentyPlayer));

        Hand seventeenDealerHand = new Hand(testGame.getDeck());
        seventeenDealerHand.add(Card.Rank.EIGHT, Card.Suit.HEARTS);
        seventeenDealerHand.add(Card.Rank.NINE, Card.Suit.SPADES);
        testGame.setDealerHand(seventeenDealerHand);

        // player 20 + dealer 17 = player win
        assertEquals(GameOutcome.PLAYER_WIN, testGame.determineOutcome(twentyPlayer));

        testGame.setDealerHand(blackjackDealerHand);

        // player 20 + dealer blackjack = dealer blackjack / player loses
        assertEquals(GameOutcome.DEALER_BLACKJACK, testGame.determineOutcome(twentyPlayer));

        testGame.setDealerHand(seventeenDealerHand);

        Player bustPlayer = new Player(testGame);
        bustPlayer.setBet(500);
        bustPlayer.getHand().add(Card.Rank.TEN, Card.Suit.CLUBS);
        bustPlayer.getHand().add(Card.Rank.SIX, Card.Suit.HEARTS);
        bustPlayer.getHand().add(Card.Rank.QUEEN, Card.Suit.DIAMONDS);

        // player busts + dealer 17 = player busts / player loses
        assertEquals(GameOutcome.PLAYER_BUST, testGame.determineOutcome(bustPlayer));

        Hand bustDealerHand = new Hand(testGame.getDeck());
        bustDealerHand.add(Card.Rank.SEVEN, Card.Suit.HEARTS);
        bustDealerHand.add(Card.Rank.NINE, Card.Suit.HEARTS);
        bustDealerHand.add(Card.Rank.JACK, Card.Suit.HEARTS);
        testGame.setDealerHand(bustDealerHand);

        // player 20 + dealer busts = dealer busts / player wins
        assertEquals(GameOutcome.DEALER_BUST, testGame.determineOutcome(twentyPlayer));
    }
}