package com.bryancapps.blackjack;

/**
 * Representation of a standard playing card
 * <p/>
 * Created by bryancapps on 8/12/15.
 */
public class Card {
    final private static int[] DRAWABLE_IDS = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8, R.drawable.card9, R.drawable.card10, R.drawable.card11, R.drawable.card12, R.drawable.card13, R.drawable.card14, R.drawable.card15, R.drawable.card16, R.drawable.card17, R.drawable.card18, R.drawable.card19, R.drawable.card20, R.drawable.card21, R.drawable.card22, R.drawable.card23, R.drawable.card24, R.drawable.card25, R.drawable.card26, R.drawable.card27, R.drawable.card28, R.drawable.card29, R.drawable.card30, R.drawable.card31, R.drawable.card32, R.drawable.card33, R.drawable.card34, R.drawable.card35, R.drawable.card36, R.drawable.card37, R.drawable.card38, R.drawable.card39, R.drawable.card40, R.drawable.card41, R.drawable.card42, R.drawable.card43, R.drawable.card44, R.drawable.card45, R.drawable.card46, R.drawable.card47, R.drawable.card48, R.drawable.card49, R.drawable.card50, R.drawable.card51, R.drawable.card52};
    private int id;

    public Card(int cardId) {
        id = cardId;
    }

    public int getId() {
        return id;
    }

    public String number() {
        if (id >= 0 && id < 4) {
            return "ace";
        } else if (id >= 4 && id < 8) {
            return "king";
        } else if (id >= 8 && id < 12) {
            return "queen";
        } else if (id >= 12 && id < 16) {
            return "jack";
        } else {
            return String.valueOf((((51 - id) / 4) + 2));
        }
    }

    public String suit() {
        int remainder = id % 4;
        if (remainder == 0) {
            return "clubs";
        } else if (remainder == 1) {
            return "spades";
        } else if (remainder == 2) {
            return "hearts";
        } else {
            return "diamonds";
        }
    }

    public int value() {
        if (id >= 0 && id < 4) {
            // card is an ace and should be counted as 11 or 1
            return 1;
        } else if (id >= 4 && id < 20) {
            // card is a king, queen, jack, or ten and should be counted as 10
            return 10;
        } else {
            return (((51 - id) / 4) + 2);
        }
    }

    public int drawableId() {
        return DRAWABLE_IDS[id];
    }
}
