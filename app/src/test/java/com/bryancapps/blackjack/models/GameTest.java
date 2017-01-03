package com.bryancapps.blackjack.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    private Game testGame;

    @Before
    public void setUp() {
        testGame = new Game();
        System.out.printf("starting money: %d\n", testGame.money());
    }

    @After
    public void tearDown() {
        testGame.resetForNewHand();
        System.out.printf("ending money: %d\n", testGame.money());
    }

    @Test
    public void testGames() throws Exception {
        for (int i = 0; i < 1000; i++) {
            testGame();
        }
    }

    @Test
    public void testGame() throws Exception {
        Strategy strategy = new Strategy();
        Player player;
        if (testGame.players().size() > 0) {
            player = testGame.players().get(0);
        } else {
            player = testGame.newPlayer();
        }
        player.initialBet(100);
        while (!testGame.shouldShowdown()) {
            for (Player p : testGame.players()) {
                if (p.status() == GameStatus.HITTING) {
                    player = p;
                    break;
                }
            }
            if (player.status() == GameStatus.HITTING) {
                System.out.printf("%d against a %d\n", player.score(), testGame.dealerScore());
                Strategy.Decision decision = strategy.getDecision(player.hand(), testGame.dealerScore());
                System.out.println(decision);
                switch (decision) {
                    case HIT:
                        player.hit();
                        break;
                    case STAY:
                        player.stay();
                        break;
                    case H_DOUBLE:
                        if (player.isDoublable()) player.doubleHand();
                        else player.hit();
                        break;
                    case S_DOUBLE:
                        if (player.isDoublable()) player.doubleHand();
                        else player.stay();
                        break;
                    case SPLIT:
                        player.split();
                        break;
                }
            }
        }

        System.out.println("Dealer:");
        System.out.println(testGame.dealerCards());
        System.out.println(testGame.dealerScore());
        System.out.println();

        for (Player p : testGame.players()) {
            System.out.println(p.cards());
            System.out.println(p.score());
            System.out.println(p.outcome());
            System.out.println();
        }
        testGame.resetForNewHand();
    }
}