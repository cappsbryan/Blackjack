package com.bryancapps.blackjack.Models;

import com.bryancapps.blackjack.R;

import java.io.Serializable;

/**
 * Representation of a standard playing card
 * <p/>
 * Created by bryancapps on 8/12/15.
 */
public class Card implements Serializable {
    public final static Card dealerBlank = new Card(Rank.BLANK, Suit.DEALER);
    public final static Card playerBlank = new Card(Rank.BLANK, Suit.PLAYER);

    private final static int[] CLUBS_IDS = {R.raw.ace_of_clubs, R.raw.two_of_clubs, R.raw.three_of_clubs, R.raw.four_of_clubs, R.raw.five_of_clubs, R.raw.six_of_clubs, R.raw.seven_of_clubs, R.raw.eight_of_clubs, R.raw.nine_of_clubs, R.raw.ten_of_clubs, R.drawable.jack_of_clubs, R.drawable.queen_of_clubs, R.drawable.king_of_clubs};
    private final static int[] DIAMONDS_IDS = {R.raw.ace_of_diamonds, R.raw.two_of_diamonds, R.raw.three_of_diamonds, R.raw.four_of_diamonds, R.raw.five_of_diamonds, R.raw.six_of_diamonds, R.raw.seven_of_diamonds, R.raw.eight_of_diamonds, R.raw.nine_of_diamonds, R.raw.ten_of_diamonds, R.drawable.jack_of_diamonds, R.drawable.queen_of_diamonds, R.drawable.king_of_diamonds};
    private final static int[] HEARTS_IDS = {R.raw.ace_of_hearts, R.raw.two_of_hearts, R.raw.three_of_hearts, R.raw.four_of_hearts, R.raw.five_of_hearts, R.raw.six_of_hearts, R.raw.seven_of_hearts, R.raw.eight_of_hearts, R.raw.nine_of_hearts, R.raw.ten_of_hearts, R.drawable.jack_of_hearts, R.drawable.queen_of_hearts, R.drawable.king_of_hearts};
    private final static int[] SPADES_IDS = {R.raw.ace_of_spades, R.raw.two_of_spades, R.raw.three_of_spades, R.raw.four_of_spades, R.raw.five_of_spades, R.raw.six_of_spades, R.raw.seven_of_spades, R.raw.eight_of_spades, R.raw.nine_of_spades, R.raw.ten_of_spades, R.drawable.jack_of_spades, R.drawable.queen_of_spades, R.drawable.king_of_spades};

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank rank() {
        return rank;
    }

    public Suit suit() {
        return suit;
    }

    public int value() {
        switch (rank) {
            case ACE:
                return 1;
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case TEN:
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                return 0;
        }
    }

    public int getImageID() {
        int[] array;
        switch (suit) {
            case CLUBS:
                array = CLUBS_IDS;
                break;
            case DIAMONDS:
                array = DIAMONDS_IDS;
                break;
            case HEARTS:
                array = HEARTS_IDS;
                break;
            case SPADES:
                array = SPADES_IDS;
                break;
            case PLAYER:
                return R.raw.red_back;
            case DEALER:
                return R.raw.blue_back;
            default:
                return 0;
        }
        return array[rank.ordinal()];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card card = (Card) obj;
            return rank.equals(card.rank) && suit.equals(card.suit);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + rank.hashCode();
        result = 31 * result + suit.hashCode();

        return result;
    }

    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, BLANK
    }

    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES, PLAYER, DEALER
    }
}
