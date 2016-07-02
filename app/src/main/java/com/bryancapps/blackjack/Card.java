package com.bryancapps.blackjack;

/**
 * Representation of a standard playing card
 * <p/>
 * Created by bryancapps on 8/12/15.
 */
public class Card {
    private final static int[] DRAWABLE_IDS = {R.drawable.ace_of_clubs, R.drawable.ace_of_diamonds, R.drawable.ace_of_hearts, R.drawable.ace_of_spades, R.drawable.two_of_clubs, R.drawable.two_of_diamonds, R.drawable.two_of_hearts, R.drawable.two_of_spades, R.drawable.three_of_clubs, R.drawable.three_of_diamonds, R.drawable.three_of_hearts, R.drawable.three_of_spades, R.drawable.four_of_clubs, R.drawable.four_of_diamonds, R.drawable.four_of_hearts, R.drawable.four_of_spades, R.drawable.five_of_clubs, R.drawable.five_of_diamonds, R.drawable.five_of_hearts, R.drawable.five_of_spades, R.drawable.six_of_clubs, R.drawable.six_of_diamonds, R.drawable.six_of_hearts, R.drawable.six_of_spades, R.drawable.seven_of_clubs, R.drawable.seven_of_diamonds, R.drawable.seven_of_hearts, R.drawable.seven_of_spades, R.drawable.eight_of_clubs, R.drawable.eight_of_diamonds, R.drawable.eight_of_hearts, R.drawable.eight_of_spades, R.drawable.nine_of_clubs, R.drawable.nine_of_diamonds, R.drawable.nine_of_hearts, R.drawable.nine_of_spades, R.drawable.ten_of_clubs, R.drawable.ten_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_spades, R.drawable.jack_of_clubs, R.drawable.jack_of_diamonds, R.drawable.jack_of_hearts, R.drawable.jack_of_spades, R.drawable.queen_of_clubs, R.drawable.queen_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_spades, R.drawable.king_of_clubs, R.drawable.king_of_diamonds, R.drawable.king_of_hearts, R.drawable.king_of_spades};

    private final int id;

    public Card(int cardId) {
        id = cardId;
    }

    public int getId() {
        return id;
    }

    public String number() {
        if (id >= 0 && id < 4) {
            return "ace";
        } else if (id >= 48) {
            return "king";
        } else if (id >= 44 && id < 48) {
            return "queen";
        } else if (id >= 40 && id < 44) {
            return "jack";
        } else {
            return String.valueOf((id / 4) + 1);
        }
    }

    public String suit() {
        int remainder = id % 4;
        if (remainder == 0) {
            return "clubs";
        } else if (remainder == 1) {
            return "diamonds";
        } else if (remainder == 2) {
            return "hearts";
        } else {
            return "spades";
        }
    }

    public int value() {
        if (id < 0 || id >= 52) {
            return 0;
        }
        if (id < 4) {
            // card is an ace and should be counted as 11 or 1
            return 1;
        } else if (id > 35) {
            // card is a king, queen, jack, or ten and should be counted as 10
            return 10;
        } else {
            return (id / 4) + 1;
        }
    }

    public int drawableId() {
        return DRAWABLE_IDS[id];
    }
}
