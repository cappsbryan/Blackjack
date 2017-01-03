package com.bryancapps.blackjack.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by bryancapps on 12/31/16.
 */
public class PlayerTest {
    private Player testPlayer;
    private Game testGame;

    @Before
    public void setUp() throws Exception {
        testGame = new Game();
        testPlayer = testGame.newPlayer();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testWinnings() throws Exception {
        for (int i = 0; i < 100000; i++) {
            playAHand(100);
            long winnings = testPlayer.winnings();
            long finalBet = testPlayer.getBet();
            switch (testPlayer.outcome()) {
                case PLAYER_BLACKJACK:
                    assertEquals(Math.round(finalBet * 2.5), winnings);
                    break;
                case DEALER_BLACKJACK:
                    assertEquals(0, winnings);
                    break;
                case PLAYER_WIN:
                    assertEquals(finalBet * 2, winnings);
                    break;
                case PUSH:
                    assertEquals(finalBet, winnings);
                    break;
                case DEALER_WIN:
                    assertEquals(0, winnings);
                    break;
                case PLAYER_BUST:
                    assertEquals(0, winnings);
                    break;
                case DEALER_BUST:
                    assertEquals(finalBet * 2, winnings);
                    break;
                case ERROR:
                    throw new Exception("we don't like errors");
            }
        }
    }

    @Test
    public void testOutcome() throws Exception {
        for (int i = 0; i < 100000; i++) {
            playAHand(100);
            int playerScore = testPlayer.score();
            int dealerScore = testGame.dealerScore();
            switch (testPlayer.outcome()) {
                case PLAYER_BLACKJACK:
                    assertEquals(2, testPlayer.hand().size());
                    assertEquals(21, playerScore);
                    assertFalse(dealerScore == 21 && testGame.dealerCards().size() == 2);
                    break;
                case DEALER_BLACKJACK:
                    assertEquals(2, testGame.dealerCards().size());
                    assertEquals(21, dealerScore);
                    assertFalse(playerScore == 21 && testPlayer.hand().size() == 2);
                    break;
                case PLAYER_WIN:
                    assertTrue(playerScore <= 21);
                    assertTrue(playerScore > dealerScore);
                    break;
                case PUSH:
                    assertEquals(playerScore, dealerScore);
                    break;
                case DEALER_WIN:
                    break;
                case PLAYER_BUST:
                    assertTrue(playerScore > 21);
                    break;
                case DEALER_BUST:
                    assertTrue(dealerScore > 21);
                    break;
                case ERROR:
                    throw new Exception("we don't like errors");
            }
            testGame.resetForNewHand();
        }
    }

    private void playAHand(long bet) {
        Strategy strategy = new Strategy();
        testGame = new Game();
        testPlayer = testGame.newPlayer();

        testPlayer.initialBet(bet);
        while (!testGame.shouldShowdown()) {
            for (Player p : testGame.players()) {
                if (p.status() == GameStatus.HITTING) {
                    testPlayer = p;
                    break;
                }
            }
            if (testPlayer.status() == GameStatus.HITTING) {
                System.out.printf("%d against a %d\n", testPlayer.score(), testGame.dealerScore());
                Strategy.Decision decision = strategy.getDecision(testPlayer.hand(), testGame.dealerScore());
                System.out.println(decision);
                switch (decision) {
                    case HIT:
                        testPlayer.hit();
                        break;
                    case STAY:
                        testPlayer.stay();
                        break;
                    case H_DOUBLE:
                        if (testPlayer.isDoublable()) testPlayer.doubleHand();
                        else testPlayer.hit();
                        break;
                    case S_DOUBLE:
                        if (testPlayer.isDoublable()) testPlayer.doubleHand();
                        else testPlayer.stay();
                        break;
                    case SPLIT:
                        testPlayer.split();
                        break;
                }
            }
        }
    }
}