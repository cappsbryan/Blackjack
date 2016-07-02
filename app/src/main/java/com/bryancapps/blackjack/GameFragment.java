package com.bryancapps.blackjack;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by bryancapps on 6/27/16.
 */
public class GameFragment extends Fragment {
    private
    @BindView(R.id.moneyTextView)
    TextView moneyTextView;
    private
    @BindView(R.id.betTextView)
    TextView betTextView;
    private
    @BindView(R.id.playerScoreTextView)
    TextView playerScoreTextView;
    private
    @BindView(R.id.dealerScoreTextView)
    TextView dealerScoreTextView;
    private
    @BindView(R.id.handOverTextView)
    TextView handOverTextView;
    private
    @BindView(R.id.betTextView2)
    TextView bigBetView;
    private
    @BindView(R.id.betButton)
    Button betButton;
    private
    @BindView(R.id.incrementBetButton)
    Button incrementBetButton;
    private
    @BindView(R.id.decrementBetButton)
    Button decrementBetButton;
    private
    @BindView(R.id.doubleButton)
    Button doubleButton;
    private
    @BindView(R.id.splitButton)
    Button splitButton;
    private
    @BindView(R.id.betDecisionView)
    View betDecisionView;
    private
    @BindView(R.id.hittingAndStayingView)
    View hitAndStayView;
    private
    @BindView(R.id.playAgainView)
    View playAgainView;
    private
    @BindView(R.id.dealerFirstCardView)
    ImageView dealerFirstCardView;
    private
    @BindView(R.id.dealerSecondCardView)
    ImageView dealerSecondCardView;
    private
    @BindView(R.id.dealerHand)
    LinearLayout dealerHandView;
    private
    @BindView(R.id.playerHand)
    LinearLayout playerHandView;
    private
    @BindView(R.id.playerFirstCardView)
    ImageView playerFirstCardView;
    private
    @BindView(R.id.playerSecondCardView)
    ImageView playerSecondCardView;

    private Unbinder unbinder;

    private int currentBet;
    private int currentMoney;
    private Hand playerHand;
    private Hand dealerHand;
    private Deck deck;
    private NumberFormat currencyFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentBet = 0;
        deck = new Deck();
        dealerHand = new Hand(deck);
        playerHand = new Hand(deck);
        currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setMaximumFractionDigits(0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.saved_money_default);
        currentMoney = sharedPref.getInt("money", defaultValue);
        moneyTextView.setText(String.format(Locale.US, "$%d", currentMoney));
        if (currentBet > 0) {
            betButton.setEnabled(true);
            decrementBetButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
            decrementBetButton.setEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (betDecisionView.getVisibility() == View.VISIBLE) {
            changeBet((-1) * currentBet);
        }

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("money", currentMoney);
        editor.apply();
    }

    @OnClick(R.id.decrementBetButton)
    public void decrementBet() {
        changeBet(-100);
    }

    @OnClick(R.id.incrementBetButton)
    public void incrementBet() {
        changeBet(100);
    }

    @OnClick(R.id.betButton)
    public void onBet() {

        // switch to view for player hitting and staying
        betDecisionView.setVisibility(View.GONE);
        hitAndStayView.setVisibility(View.VISIBLE);
        bigBetView.setText(currencyFormat.format(currentBet));

        dealerHand.draw();
        playerHand.draw();
        playerHand.draw();
        dealerHand.draw();

        playerFirstCardView.setImageResource(playerHand.get(0).drawableId());
        playerSecondCardView.setImageResource(playerHand.get(1).drawableId());
        dealerSecondCardView.setImageResource(dealerHand.get(1).drawableId());

        updateScoreViews(false);

        // enable split option if the cards are the same value
        if (playerHand.get(0).value() == playerHand.get(1).value() && playerHand.size() == 2) {
            splitButton.setEnabled(true);
        }

        // check for blackjacks
        if (playerHand.getScore(true) == 21 || dealerHand.getScore(true) == 21) {
            endHand();
        }
    }

    private void updateScoreViews(boolean showDealerFirstCard) {
        playerScoreTextView.setText(String.valueOf(playerHand.getScore(true)));
        dealerScoreTextView.setText(String.valueOf(dealerHand.getScore(showDealerFirstCard)));
    }

    @OnClick(R.id.stayButton)
    public void onStay() {
        dealerFirstCardView.setImageResource(dealerHand.get(0).drawableId());
        hitDealer();
        endHand();
    }

    private void hitDealer() {
        // dealer stays on all 17s
        while (dealerHand.getScore(true) < 17) {
            Card hitCard = dealerHand.draw();
            addCardToView(dealerHandView, hitCard);
        }
    }

    @OnClick(R.id.hitButton)
    public void onHit() {
        Card hitCard = playerHand.draw();
        addCardToView(playerHandView, hitCard);

        doubleButton.setEnabled(false);
        updateScoreViews(false);
        if (playerHand.getScore(true) > 21) {
            endHand();
        }
    }

    /**
     * Changes the current bet value, the player's money, and the corresponding views
     *
     * @param change How much to increase the bet by, negative values represent a decrease
     */
    private void changeBet(int change) {
        if (currentBet >= (-1) * change && currentMoney >= change) {
            currentBet = currentBet + change;
            currentMoney = currentMoney - change;
            betTextView.setText(currencyFormat.format(currentBet));
            moneyTextView.setText(currencyFormat.format(currentMoney));
        }
        if (currentBet > 0) {
            betButton.setEnabled(true);
            decrementBetButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
            decrementBetButton.setEnabled(false);
        }
        if (currentMoney > 0) {
            incrementBetButton.setEnabled(true);
        } else {
            incrementBetButton.setEnabled(false);
        }
    }

    private void addCardToView(LinearLayout handView, Card card) {
        ImageView cardView = (ImageView) LayoutInflater.from(handView.getContext())
                .inflate(R.layout.card_item, handView, false);
        cardView.setImageResource(card.drawableId());

        handView.addView(cardView);
    }

    @OnClick(R.id.doubleButton)
    public void onDouble() {
        changeBet(currentBet);
        Card hitCard = playerHand.draw();
        addCardToView(playerHandView, hitCard);
        hitDealer();
        endHand();
    }

    @OnClick(R.id.splitButton)
    public void onSplit() {
        /*
        bet * 2
        your bet -> total bet
        store second card
        replace second card with dealt card
        recalc score
        switchToSecondHand instead of endHand
        endSplitHand
         */
    }

    @OnClick(R.id.playAgainButton)
    public void playAgain() {
        if (currentMoney <= 0) {
            currentMoney = 1000;
        }
        playAgainView.setVisibility(View.GONE);
        betDecisionView.setVisibility(View.VISIBLE);
        splitButton.setEnabled(false);
        doubleButton.setEnabled(true);
        deck.reset();
        currentBet = 0;
        changeBet(0);
        dealerScoreTextView.setText(String.valueOf(0));
        playerScoreTextView.setText(String.valueOf(0));
        playerHand.clear();
        dealerHand.clear();
        int dealerCardCount = dealerHandView.getChildCount();
        for (int i = dealerCardCount - 1; i >= 2; i--) {
            dealerHandView.removeViewAt(i);
        }
        int playerCardCount = playerHandView.getChildCount();
        for (int i = playerCardCount - 1; i >= 2; i--) {
            playerHandView.removeViewAt(i);
        }
        dealerFirstCardView.setImageResource(R.drawable.b1fv);
        dealerSecondCardView.setImageResource(R.drawable.b1fv);
        playerFirstCardView.setImageResource(R.drawable.b2fv);
        playerSecondCardView.setImageResource(R.drawable.b2fv);
    }

    private void endHand() {

        int playerScore = playerHand.getScore(true);
        int dealerScore = dealerHand.getScore(true);
        updateScoreViews(true);
        dealerFirstCardView.setImageResource(dealerHand.get(0).drawableId());
        Resources resources = getResources();

        if (playerScore > dealerScore && playerScore <= 21) {
            // player wins!
            String text = resources.getString(R.string.player_wins) + currentBet + "!";
            handOverTextView.setText(text);
            showMoneyChange(currentBet * 2);
            currentMoney += (currentBet * 2);
        } else if (playerScore > dealerScore && playerScore == 21 && playerHand.size() == 2) {
            // player has a blackjack!
            int winnings = (int) (currentBet * 1.5);
            String text = resources.getString(R.string.player_blackjack) + winnings + "!";
            handOverTextView.setText(text);
            showMoneyChange(currentBet * 2.5);
            currentMoney += (currentBet * 2.5);
        } else if (playerScore <= 21 && dealerScore > 21) {
            // dealer busts!
            String text = resources.getString(R.string.dealer_busts) + currentBet + "!";
            handOverTextView.setText(text);
            showMoneyChange(currentBet * 2);
            currentMoney += (currentBet * 2);
        } else if (dealerScore > playerScore && dealerScore <= 21) {
            // dealer wins
            String text = resources.getString(R.string.dealer_wins) + currentBet + ".";
            handOverTextView.setText(text);
        } else if (dealerScore > playerScore && dealerScore == 21 && dealerHand.size() == 2) {
            // dealer has a blackjack
            String text = resources.getString(R.string.dealer_blackjack) + currentBet + ".";
            handOverTextView.setText(text);
        } else if (dealerScore == playerScore && dealerScore <= 21) {
            // push
            handOverTextView.setText(R.string.push);
            showMoneyChange(currentBet);
            currentMoney += currentBet;
        } else if (playerScore > 21) {
            // player busts
            String text = resources.getString(R.string.player_busts) + currentBet + ".";
            handOverTextView.setText(text);
        }

        currentBet = 0;

        hitAndStayView.setVisibility(View.GONE);
        playAgainView.setVisibility(View.VISIBLE);
    }

    private void showMoneyChange(double change) {
        moneyTextView.setText(String.format("%s\n+ %s", moneyTextView.getText(), currencyFormat.format(change)));
    }

}