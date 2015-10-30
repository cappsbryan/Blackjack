package com.bryancapps.blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Representation of a deck of playing cards
 * <p/>
 * Created by bryancapps on 8/4/15.
 */
public class CardDeck {
    final private static Integer[] DEFAULT_CARDS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private ArrayList<Integer> cards;

    public CardDeck() {
        cards = new ArrayList<>();
        cards.addAll(Arrays.asList(DEFAULT_CARDS));
    }

    /**
     * Returns a random card from the deck and removes that card from the deck
     *
     * @return An int representing a playing card
     */
    public Card deal() {
        Random random = new Random();
        int index = random.nextInt(cards.size());
        return new Card(cards.remove(index));
    }

    public void reset() {
        cards.clear();
        cards.addAll(Arrays.asList(DEFAULT_CARDS));
    }

}
