package com.bryancapps.blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a hand of playing cards
 * <p/>
 * Created by bryancapps on 6/19/15.
 */
public class CardHand {
    // constructor

    // add method

    // score method

    // read/display cards

    private ArrayList<Integer> cardsInHand;

    public CardHand() {
        cardsInHand = new ArrayList<>();
    }

    public void addCard(int card) {
        cardsInHand.add(card);
    }

    public int getScore() {
        int score = 0;
        boolean hasAce = false;

        for (int card : cardsInHand) {

            if (card >= 0 && card < 4) {
                // card is an ace and should be counted as 11 or 1
                hasAce = true;
                score += 1;
            } else if (card >= 4 && card < 20) {
                // card is a king, queen, jack, or ten and should be counted as 10
                score += 10;
            } else {
                score += (((51 - card) / 4) + 2);
            }
        }

        if (hasAce && score <= 11) {
            score += 10;
        }

        return score;
    }

    public ArrayList<Integer> getCardsInHand() {
        return cardsInHand;
    }
}
