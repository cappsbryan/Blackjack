package com.bryancapps.blackjack;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

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
    @BindView(R.id.layout_play_again) View playAgainView;
    @BindView(R.id.layout_waiting) View waitingView;
    @BindView(R.id.layout_dealer_hand) LinearLayout dealerHandView;
    @BindView(R.id.layout_player_hand) LinearLayout playerHandView;

    private Unbinder unbinder;
    private TransitionSet transitionSet;

    private Game game;
    private Player player;
    private NumberFormat currencyFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = Game.getInstance();

        if (getArguments() != null && getArguments().getSerializable("player") != null) {
            player = (Player) getArguments().getSerializable("player");
            game.setStatus(GameStatus.HITTING, player);
        } else {
            player = new Player(game);
            game.setStatus(GameStatus.BETTING, player);
        }

        game.addChangeListener(this);
        player.addChangeListener(this);

        transitionSet = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
                .addTransition(new TransitionSet()
                        .setOrdering(TransitionSet.ORDERING_TOGETHER)
                        .addTransition(new CardFlip())
                        .addTransition(new ChangeBounds()))
                .addTransition(new Slide(Gravity.RIGHT));
        currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setMaximumFractionDigits(0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        unbinder = ButterKnife.bind(this, view);

        showDecisionView(player.getStatus());
        updateScoreViews(false);

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
    public void onDestroy() {
        super.onDestroy();

        game.removeChangeListener(this);
        player.removeChangeListener(this);

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
        game.resetForNewHand();
        updateDealerHandView(dealerHandView, game.getDealerHand(), GameStatus.BETTING);
        player.reset();

        if (player.getBet() > game.getMoney()) {
            player.setBet(game.getMoney());
        }
        game.setMoney(game.getMoney() - player.getBet());
    }

    @OnClick(R.id.button_decrement_bet)
    public void decrementBet() {
        long betDecrease;
        if (player.getBet() < 100) {
            betDecrease = player.getBet();
        } else {
            betDecrease = 100;
        }

        player.setBet(player.getBet() - betDecrease);
        game.setMoney(game.getMoney() + betDecrease);
    }

    @OnClick(R.id.button_increment_bet)
    public void incrementBet() {
        long betIncrease;
        if (game.getMoney() < 100) {
            betIncrease = game.getMoney();
        } else {
            betIncrease = 100;
        }

        player.setBet(player.getBet() + betIncrease);
        game.setMoney(game.getMoney() - betIncrease);
    }

    private void setBetViews(long bet) {
        betTextView.setText(currencyFormat.format(bet));
        bigBetView.setText(currencyFormat.format(bet));
        if (bet > 0) {
            betButton.setEnabled(true);
            decrementBetButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
            decrementBetButton.setEnabled(false);
        }

        if (game.getMoney() < 100 && game.getMoney() > 0) {
            incrementBetButton.setText("+ $" + game.getMoney());
        } else {
            incrementBetButton.setText("+ $100");
        }

        if (player.getBet() < 100 && player.getBet() > 0) {
            decrementBetButton.setText("- $" + player.getBet());
        } else {
            decrementBetButton.setText("- $100");
        }
    }

    private void setMoneyView(long money) {
        moneyTextView.setText(currencyFormat.format(money));
        if (money > 0) {
            incrementBetButton.setEnabled(true);
        } else {
            incrementBetButton.setEnabled(false);
        }
    }

    private void setButtonsEnabled() {
        if (player.getHand().isSplittable()) {
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

        if (player.getHand().isSplittable()) {
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

    private void updateDealerHandView(LinearLayout layout, Hand hand, GameStatus status) {
        TransitionManager.beginDelayedTransition(dealerHandView, transitionSet);
        if (status == GameStatus.BETTING) {
            layout.removeAllViews();
            setCardForImageView(Card.dealerBlank, newImageViewForLayout(layout));
            setCardForImageView(Card.dealerBlank, newImageViewForLayout(layout));
        } else {
            for (int i = 0; i < hand.size(); i++) {
                Card card;
                ImageView imageView;
                if (i == 0 && status != GameStatus.SHOWDOWN) {
                    card = Card.dealerBlank;
                } else {
                    card = hand.get(i);
                }
                if (i < layout.getChildCount()) {
                    imageView = (ImageView) layout.getChildAt(i);
                } else {
                    imageView = newImageViewForLayout(layout);
                }
                setCardForImageView(card, imageView);
            }
        }
    }

    private void updatePlayerHandView(LinearLayout view, Hand hand) {
        TransitionManager.beginDelayedTransition(playerHandView, transitionSet);

        if (hand.size() == 1) {
            return;
        }

        // remove any extra views
        if (view.getChildCount() > hand.size()) {
            int count = view.getChildCount() - hand.size();
            view.removeViews(hand.size(), count);
        }
        // set any existing views
        for (int i = 0; i < view.getChildCount(); i++) {
            ImageView cardImageView = (ImageView) view.getChildAt(i);
            setCardForImageView(hand.get(i), cardImageView);
        }
        // add any missing views
        for (int i = view.getChildCount(); i < hand.size(); i++) {
            setCardForImageView(hand.get(i), newImageViewForLayout(view));
        }
        for (int i = hand.size(); i < 2; i++) {
            setCardForImageView(Card.playerBlank, newImageViewForLayout(view));
        }
    }

    private ImageView newImageViewForLayout(LinearLayout handView) {
        ImageView cardView = (ImageView) LayoutInflater.from(handView.getContext())
                .inflate(R.layout.card_item, handView, false);

        if (handView.getChildCount() == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cardView.getLayoutParams());
            params.setMargins(0, 0, 0, 0);
            cardView.setLayoutParams(params);
        }

        handView.addView(cardView);
        return cardView;
    }

    private ImageView setCardForImageView(Card card, ImageView imageView) {
        Card.Rank rank = card.rank();
        if (rank == Card.Rank.KING || rank == Card.Rank.QUEEN || rank == Card.Rank.JACK) {
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
        imageView.setTag(card.rank().toString() + card.suit().toString());
        return imageView;
    }

    @OnClick(R.id.button_double)
    public void onDouble() {
        long betChange = player.getBet();
        player.setBet(player.getBet() + betChange);
        game.setMoney(game.getMoney() - betChange);
        player.setDoubled(true);

        player.getHand().draw();
        onStay();
    }

    @OnClick(R.id.button_split)
    public void onSplit() {
        Player splitPlayer = new Player(game);
        splitPlayer.setBet(player.getBet());
        splitPlayer.setStatus(GameStatus.HITTING);
        game.setMoney(game.getMoney() - player.getBet());

        splitPlayer.getHand().add(player.getHand().remove(1));
        player.getHand().draw();
        splitPlayer.getHand().draw();

        GameFragment splitFragment = new GameFragment();
        Bundle args = new Bundle();
        args.putSerializable("player", splitPlayer);
        splitFragment.setArguments(args);
        if (GameActivity.class.isInstance(getActivity())) {
            ((GameActivity) getActivity()).addGameFragment(splitFragment);
        }

        checkBlackjack(player.getHand());
        splitFragment.checkBlackjack(splitPlayer.getHand());
    }

    private void endHand() {
        Log.d(getClass().getSimpleName(), "dealer hand:");
        for (Card dealerCard : game.getDealerHand()) {
            Log.d(getClass().getSimpleName(), dealerCard.rank() + " of " + dealerCard.suit());
        }
        Log.d(getClass().getSimpleName(), "player hand:");
        for (Card playerCard : player.getHand()) {
            Log.d(getClass().getSimpleName(), playerCard.rank() + " of " + playerCard.suit());
        }

        // gets called by the listener after status is set to showdown

        updateScoreViews(true);
        Resources resources = getResources();

        long winnings = game.determineWinnings(player);
        showMoneyChange(winnings);
        game.setMoney(game.getMoney() + winnings);

        String text;
        switch (game.determineOutcome(player)) {
            case PUSH:
                handOverTextView.setText(R.string.push);
                break;
            case PLAYER_BLACKJACK:
                text = resources.getString(R.string.player_blackjack) + (winnings - player.getBet()) + "!";
                handOverTextView.setText(text);
                break;
            case DEALER_BLACKJACK:
                text = resources.getString(R.string.dealer_blackjack) + player.getBet() + ".";
                handOverTextView.setText(text);
                break;
            case PLAYER_WIN:
                text = resources.getString(R.string.player_wins) + (winnings - player.getBet()) + "!";
                handOverTextView.setText(text);
                break;
            case DEALER_BUST:
                text = resources.getString(R.string.dealer_busts) + (winnings - player.getBet()) + "!";
                handOverTextView.setText(text);
                break;
            case DEALER_WIN:
                text = resources.getString(R.string.dealer_wins) + player.getBet() + ".";
                handOverTextView.setText(text);
                break;
            case PLAYER_BUST:
                text = resources.getString(R.string.player_busts) + player.getBet() + ".";
                handOverTextView.setText(text);
            case ERROR:
                handOverTextView.setText(R.string.hand_outcome_error);
                break;
        }

        if (player.isDoubled()) {
            player.setBet(player.getBet() / 2);
            player.setDoubled(false);
        }
    }

    private void showMoneyChange(double change) {
        if (change > 0) {
            moneyTextView.setText(String.format("%s\n+ %s", moneyTextView.getText(), currencyFormat.format(change)));
        }
    }

    @OnClick(R.id.button_play_again)
    public void playAgain() {
        if (GameActivity.class.isInstance(getActivity())) {
            GameActivity activity = (GameActivity) getActivity();
            activity.resetGame();
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
            case "money":
                setBetViews(player.getBet());
                setMoneyView(game.getMoney());
                break;
            case "status":
                GameStatus status = game.getStatus(player);

                showDecisionView(status);

                if (status == GameStatus.BETTING) {
                    updateDealerHandView(dealerHandView, game.getDealerHand(), status);
                    switchToBetting();
                }
                if (status == GameStatus.HITTING) {
                    updateDealerHandView(dealerHandView, game.getDealerHand(), status);
                    switchToHitting();
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
            waitingView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.GONE);
            betDecisionView.setVisibility(View.VISIBLE);
        } else if (status == GameStatus.HITTING) {
            betDecisionView.setVisibility(View.GONE);
            waitingView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.GONE);
            hitAndStayView.setVisibility(View.VISIBLE);
        } else if (status == GameStatus.WAITING) {
            betDecisionView.setVisibility(View.GONE);
            hitAndStayView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.GONE);
            waitingView.setVisibility(View.VISIBLE);
        } else if (status == GameStatus.SHOWDOWN) {
            betDecisionView.setVisibility(View.GONE);
            hitAndStayView.setVisibility(View.GONE);
            waitingView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.VISIBLE);
        }
    }
}
