package com.bryancapps.blackjack;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryancapps.blackjack.Models.Card;
import com.bryancapps.blackjack.Models.GameStatus;
import com.bryancapps.blackjack.Models.Hand;
import com.bryancapps.blackjack.Models.Player;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by bryancapps on 6/27/16.
 */
public class GameFragment extends Fragment implements PropertyChangeListener {

    @BindView(R.id.text_money) TextView moneyTextView;
    @BindView(R.id.text_bet) TextView betTextView;
    @BindView(R.id.text_player_score) TextView playerScoreTextView;
    @BindView(R.id.text_dealer_score) TextView dealerScoreTextView;
    @BindView(R.id.text_showdown_description) TextView handOverTextView;
    @BindView(R.id.text_bet_reminder) TextView bigBetView;
    @BindView(R.id.button_bet) Button betButton;
    @BindView(R.id.button_increment_bet) Button incrementBetButton;
    @BindView(R.id.button_decrement_bet) Button decrementBetButton;
    @BindView(R.id.button_double) Button doubleButton;
    @BindView(R.id.button_split) Button splitButton;
    @BindView(R.id.layout_bet_decision) View betDecisionView;
    @BindView(R.id.layout_hitting_decision) View hitAndStayView;
    @BindView(R.id.playAgainView) View playAgainView;
    @BindView(R.id.layout_dealer_hand) LinearLayout dealerHandView;
    @BindView(R.id.layout_player_hand) LinearLayout playerHandView;

    private Unbinder unbinder;

    private Game game;
    private Player player;
    private NumberFormat currencyFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = Game.getInstance();
        player = new Player(game);
        game.setStatus(GameStatus.BETTING, player);


        game.addChangeListener(this);
        player.addChangeListener(this);

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setMoneyView(game.getMoney());
        setBetViews(player.getBet());
        updatePlayerHandView(playerHandView, player.getHand());
        updateDealerHandView(dealerHandView, game.getDealerHand(), game.getStatus(player));
    }

    @Override
    public void onStop() {
        super.onStop();

        if (game.getStatus(player) == GameStatus.BETTING) {
            game.setMoney(game.getMoney() + player.getBet());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void switchToBetting() {
        game.reset();
        player.reset();
        game.setMoney(game.getMoney() - player.getBet());
    }

    @OnClick(R.id.button_decrement_bet)
    public void decrementBet() {
        player.setBet(player.getBet() - 100);
        game.setMoney(game.getMoney() + 100);
    }

    @OnClick(R.id.button_increment_bet)
    public void incrementBet() {
        player.setBet(player.getBet() + 100);
        game.setMoney(game.getMoney() - 100);
    }

    private void setBetViews(int bet) {
        betTextView.setText(currencyFormat.format(bet));
        bigBetView.setText(currencyFormat.format(bet));
        if (bet > 0) {
            betButton.setEnabled(true);
            decrementBetButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
            decrementBetButton.setEnabled(false);
        }
    }

    private void setMoneyView(int money) {
        moneyTextView.setText(currencyFormat.format(money));
        if (money > 0) {
            incrementBetButton.setEnabled(true);
        } else {
            incrementBetButton.setEnabled(false);
        }
    }

    private void setButtonsEnabled() {
        if (player.getHand().isSplitable()) {
            splitButton.setEnabled(true);
        } else {
            splitButton.setEnabled(false);
        }
        if (player.getHand().size() == 2) {
            doubleButton.setEnabled(true);
        } else {
            doubleButton.setEnabled(false);
        }
    }

    @OnClick(R.id.button_bet)
    public void onBet() {
        game.setStatus(GameStatus.HITTING, player);
    }

    private void switchToHitting() {
        game.getDealerHand().draw();

        player.getHand().draw();
        player.getHand().draw();

        game.getDealerHand().draw();

        if (player.getHand().isSplitable()) {
            splitButton.setEnabled(true);
        }

        checkBlackjack(player.getHand());
        checkBlackjack(game.getDealerHand());
    }

    private void checkBlackjack(Hand hand) {
        if (hand.size() == 2 && hand.getScore(true) == 21) {
            game.setStatus(GameStatus.WAITING, player);
        }
    }

    @OnClick(R.id.button_hit)
    public void onHit() {
        player.getHand().draw();

        if (player.getHand().getScore(true) > 21) {
            game.setStatus(GameStatus.WAITING, player);
        }
    }

    private void updateScoreViews(boolean showDealerFirstCard) {
        playerScoreTextView.setText(String.valueOf(player.getHand().getScore(true)));
        dealerScoreTextView.setText(String.valueOf(game.getDealerHand().getScore(showDealerFirstCard)));
    }

    @OnClick(R.id.button_stay)
    public void onStay() {
        game.setStatus(GameStatus.WAITING, player);
    }

    private void hitDealer() {
        // dealer stays on all 17s
        game.getDealerHand().drawUpToSeventeen();
    }

    private void updateDealerHandView(LinearLayout view, Hand hand, GameStatus status) {
        view.removeAllViews();
        if (status == GameStatus.SHOWDOWN) {
            for (int i = 0; i < hand.size(); i++) {
                addCardToView(view, hand.get(i));
            }
        } else {
            addCardToView(view, Card.dealerBlank);
            if (status != GameStatus.BETTING && hand.size() >= 2) {
                addCardToView(view, hand.get(1));
            } else {
                addCardToView(view, Card.dealerBlank);
            }
        }
    }

    private void updatePlayerHandView(LinearLayout view, Hand hand) {
        // remove any extra views
        if (view.getChildCount() > hand.size()) {
            int count = view.getChildCount() - hand.size();
            view.removeViews(hand.size(), count);
        }
        // set any existing views
        for (int i = 0; i < view.getChildCount(); i++) {
            ImageView cardImageView = (ImageView) view.getChildAt(i);
            setImageForView(hand.get(i), cardImageView);
        }
        // add any missing views
        for (int i = view.getChildCount(); i < hand.size(); i++) {
            addCardToView(view, hand.get(i));
        }
        for (int i = hand.size(); i < 2; i++) {
            addCardToView(view, Card.playerBlank);
        }
    }

    private void addCardToView(LinearLayout handView, Card card) {
        ImageView cardView = (ImageView) LayoutInflater.from(handView.getContext())
                .inflate(R.layout.card_item, handView, false);

        cardView = setImageForView(card, cardView);

        if (handView.getChildCount() == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cardView.getLayoutParams());
            params.setMargins(0, 0, 0, 0);
            cardView.setLayoutParams(params);
        }

        handView.addView(cardView);
    }

    private ImageView setImageForView(Card card, ImageView imageView) {
        String rank = card.rank();
        if (rank.equals("king") || rank.equals("queen") || rank.equals("jack")) {
            imageView.setImageResource(card.getImageID());

        } else {
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            try {
                SVG svg = SVG.getFromResource(getActivity(), card.getImageID());
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                imageView.setImageDrawable(drawable);
            } catch (SVGParseException e) {
                Log.e(GameFragment.class.toString(), "failed to parse svg: " + e.toString());
            }
        }
        return imageView;
    }

    @OnClick(R.id.button_double)
    public void onDouble() {
        player.setBet(player.getBet() * 2);

        player.getHand().draw();
        onStay();
    }

    @OnClick(R.id.button_split)
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

    private void switchToWaiting() {

    }

    private void endHand() {
        // gets called by the listener after status is set to showdown

        int playerScore = player.getHand().getScore(true);
        int dealerScore = game.getDealerHand().getScore(true);
        updateScoreViews(true);
        Resources resources = getResources();

        if (playerScore > dealerScore && playerScore <= 21) {
            // player wins!
            String text = resources.getString(R.string.player_wins) + player.getBet() + "!";
            handOverTextView.setText(text);
            showMoneyChange(player.getBet() * 2);
            game.setMoney(game.getMoney() + (player.getBet() * 2));
        } else if (playerScore > dealerScore && playerScore == 21 && player.getHand().size() == 2) {
            // player has a blackjack!
            int winnings = (int) (player.getBet() * 1.5);
            String text = resources.getString(R.string.player_blackjack) + winnings + "!";
            handOverTextView.setText(text);
            showMoneyChange(player.getBet() * 2.5);
            game.setMoney(game.getMoney() + (int) (player.getBet() * 2.5));
        } else if (playerScore <= 21 && dealerScore > 21) {
            // dealer busts!
            String text = resources.getString(R.string.dealer_busts) + player.getBet() + "!";
            handOverTextView.setText(text);
            showMoneyChange(player.getBet() * 2);
            game.setMoney(game.getMoney() + (player.getBet() * 2));
        } else if (dealerScore > playerScore && dealerScore <= 21) {
            // dealer wins
            String text = resources.getString(R.string.dealer_wins) + player.getBet() + ".";
            handOverTextView.setText(text);
        } else if (dealerScore > playerScore && dealerScore == 21
                && game.getDealerHand().size() == 2) {
            // dealer has a blackjack
            String text = resources.getString(R.string.dealer_blackjack) + player.getBet() + ".";
            handOverTextView.setText(text);
        } else if (dealerScore == playerScore && dealerScore <= 21) {
            // push
            handOverTextView.setText(R.string.push);
            showMoneyChange(player.getBet());
            game.setMoney(game.getMoney() + player.getBet());
        } else if (playerScore > 21) {
            // player busts
            String text = resources.getString(R.string.player_busts) + player.getBet() + ".";
            handOverTextView.setText(text);
        }
    }

    private void showMoneyChange(double change) {
        moneyTextView.setText(String.format("%s\n+ %s", moneyTextView.getText(), currencyFormat.format(change)));
    }

    @OnClick(R.id.button_play_again)
    public void playAgain() {
        if (game.numPlayers() > 1) {
            game.removePlayer(player, this);
            // destroy fragment
        } else {
            game.setStatus(GameStatus.BETTING, player);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        switch (propertyChangeEvent.getPropertyName()) {
            case "player hand":
                updatePlayerHandView(playerHandView, player.getHand());
                updateScoreViews(game.getStatus(player) == GameStatus.SHOWDOWN);
                setButtonsEnabled();
                break;
            case "dealer hand":
                updateDealerHandView(dealerHandView, game.getDealerHand(), game.getStatus(player));
                updateScoreViews(game.getStatus(player) == GameStatus.SHOWDOWN);
                break;
            case "bet":
                setBetViews(player.getBet());
                break;
            case "money":
                setMoneyView(game.getMoney());
                break;
            case "status":
                GameStatus status = game.getStatus(player);

                showDecisionView(status);
//                updateDealerHandView(dealerHandView, game.getDealerHand(), status);

                if (status == GameStatus.BETTING) {
                    switchToBetting();
                }
                if (status == GameStatus.HITTING) {
                    switchToHitting();
                }
                if (status == GameStatus.WAITING) {
                    switchToWaiting();
                }
                if (status == GameStatus.SHOWDOWN) {
                    hitDealer();
                    updateDealerHandView(dealerHandView, game.getDealerHand(), status);
                    endHand();
                }
        }
    }

    private void showDecisionView(GameStatus status) {
        if (status == GameStatus.BETTING) {
            hitAndStayView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.GONE);
            betDecisionView.setVisibility(View.VISIBLE);
        } else if (status == GameStatus.HITTING) {
            betDecisionView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.GONE);
            hitAndStayView.setVisibility(View.VISIBLE);
        } else if (status == GameStatus.SHOWDOWN) {
            betDecisionView.setVisibility(View.GONE);
            hitAndStayView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.VISIBLE);
        }
    }
}