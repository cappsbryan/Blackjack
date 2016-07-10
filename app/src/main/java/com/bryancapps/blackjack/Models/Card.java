package com.bryancapps.blackjack.Models;

import com.bryancapps.blackjack.R;

/**
 * Representation of a standard playing card
 * <p/>
 * Created by bryancapps on 8/12/15.
 */
public class Card {
    public final static Card dealerBlank = new Card(-1);
    public final static Card playerBlank = new Card(-2);
    private final static int[] IMAGE_IDS = {R.raw.ace_of_clubs, R.raw.ace_of_diamonds, R.raw.ace_of_hearts, R.raw.ace_of_spades, R.raw.two_of_clubs, R.raw.two_of_diamonds, R.raw.two_of_hearts, R.raw.two_of_spades, R.raw.three_of_clubs, R.raw.three_of_diamonds, R.raw.three_of_hearts, R.raw.three_of_spades, R.raw.four_of_clubs, R.raw.four_of_diamonds, R.raw.four_of_hearts, R.raw.four_of_spades, R.raw.five_of_clubs, R.raw.five_of_diamonds, R.raw.five_of_hearts, R.raw.five_of_spades, R.raw.six_of_clubs, R.raw.six_of_diamonds, R.raw.six_of_hearts, R.raw.six_of_spades, R.raw.seven_of_clubs, R.raw.seven_of_diamonds, R.raw.seven_of_hearts, R.raw.seven_of_spades, R.raw.eight_of_clubs, R.raw.eight_of_diamonds, R.raw.eight_of_hearts, R.raw.eight_of_spades, R.raw.nine_of_clubs, R.raw.nine_of_diamonds, R.raw.nine_of_hearts, R.raw.nine_of_spades, R.raw.ten_of_clubs, R.raw.ten_of_diamonds, R.raw.ten_of_hearts, R.raw.ten_of_spades, R.drawable.jack_of_clubs, R.drawable.jack_of_diamonds, R.drawable.jack_of_hearts, R.drawable.jack_of_spades, R.drawable.queen_of_clubs, R.drawable.queen_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_spades, R.drawable.king_of_clubs, R.drawable.king_of_diamonds, R.drawable.king_of_hearts, R.drawable.king_of_spades};
    private final int id;

    public Card(int cardId) {
        id = cardId;
    }

    public int getId() {
        return id;
    }

    public String rank() {
        if (id < 0 || id > 52) {
            return "";
        }
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
        if (id < 0 || id > 52) {
            return "";
        }
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

    public int getImageID() {
        if (id == -1) {
            return R.raw.blue_back;
        } else if (id == -2) {
            return R.raw.red_back;
        }
        return IMAGE_IDS[id];
    }
}
