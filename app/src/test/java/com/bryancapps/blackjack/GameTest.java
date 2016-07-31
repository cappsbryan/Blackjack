package com.bryancapps.blackjack;

import com.bryancapps.blackjack.Models.Card;
import com.bryancapps.blackjack.Models.Deck;
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
}