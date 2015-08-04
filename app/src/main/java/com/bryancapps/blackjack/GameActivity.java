package com.bryancapps.blackjack;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class GameActivity extends Activity {
    final private int[] CARD_IDS = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8, R.drawable.card9, R.drawable.card10, R.drawable.card11, R.drawable.card12, R.drawable.card13, R.drawable.card14, R.drawable.card15, R.drawable.card16, R.drawable.card17, R.drawable.card18, R.drawable.card19, R.drawable.card20, R.drawable.card21, R.drawable.card22, R.drawable.card23, R.drawable.card24, R.drawable.card25, R.drawable.card26, R.drawable.card27, R.drawable.card28, R.drawable.card29, R.drawable.card30, R.drawable.card31, R.drawable.card32, R.drawable.card33, R.drawable.card34, R.drawable.card35, R.drawable.card36, R.drawable.card37, R.drawable.card38, R.drawable.card39, R.drawable.card40, R.drawable.card41, R.drawable.card42, R.drawable.card43, R.drawable.card44, R.drawable.card45, R.drawable.card46, R.drawable.card47, R.drawable.card48, R.drawable.card49, R.drawable.card50, R.drawable.card51, R.drawable.card52};
    Random random = new Random();
    TextView moneyTextView;
    TextView betTextView;
    TextView betTextView2;
    TextView playerScoreTextView;
    TextView dealerScoreTextView;
    TextView handOverTextView;
    int playerCurrentScore;
    int dealerCurrentScore;
    int currentBet;
    int currentMoney;
    ArrayList<Integer> playerCards = new ArrayList<>();
    ArrayList<Integer> dealerCards = new ArrayList<>();
    View betDecisionView;
    View hitAndStayView;
    View playAgainView;
    Button betButton;
    ImageView dealerFirstCardView;
    ImageView dealerSecondCardView;
    LinearLayout dealerHand;
    LinearLayout playerHand;
    ImageView playerFirstCardView;
    ImageView playerSecondCardView;
    String playerMoneyLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        betTextView = (TextView) findViewById(R.id.betTextView);
        betTextView2 = (TextView) findViewById(R.id.betTextView2);
        playerScoreTextView = (TextView) findViewById(R.id.playerScoreTextView);
        dealerScoreTextView = (TextView) findViewById(R.id.dealerScoreTextView);
        handOverTextView = (TextView) findViewById(R.id.handOverTextView);
        currentBet = 100;
        currentMoney = 1000;
        betTextView.setText("$" + currentBet);
        betDecisionView = findViewById(R.id.betDecisionView);
        hitAndStayView = findViewById(R.id.hittingAndStayingView);
        playAgainView = findViewById(R.id.playAgainView);
        betButton = (Button) findViewById(R.id.betButton);
        dealerFirstCardView = (ImageView) findViewById(R.id.dealerFirstCardView);
        dealerSecondCardView = (ImageView) findViewById(R.id.dealerSecondCardView);
        dealerHand = (LinearLayout) findViewById(R.id.dealerHand);
        playerHand = (LinearLayout) findViewById(R.id.playerHand);
        playerFirstCardView = (ImageView) findViewById(R.id.playerFirstCardView);
        playerSecondCardView = (ImageView) findViewById(R.id.playerSecondCardView);
        playerMoneyLabel = getResources().getString(R.string.player_money);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void decrementBet(View view) {
        if (currentBet >= 100) {
            currentBet -= 100;
            betTextView.setText("$" + currentBet);
        }
    }

    public void incrementBet(View view) {
        if (currentBet + 100 <= currentMoney) {
            currentBet += 100;
            betTextView.setText("$" + currentBet);
        }
    }

    public void onBet(View view) {
        // change view for player hitting and staying
        betDecisionView.setVisibility(View.GONE);
        hitAndStayView.setVisibility(View.VISIBLE);
        TextView betView = (TextView) findViewById(R.id.betTextView2);
        betView.setText("$" + currentBet);
        moneyTextView.setText(playerMoneyLabel + currentMoney);

        dealerCards.add(nextCard(union(playerCards, dealerCards)));
        playerCards.add(nextCard(union(playerCards, dealerCards)));
        playerCards.add(nextCard(union(playerCards, dealerCards)));
        dealerCards.add(nextCard(union(playerCards, dealerCards)));

        playerFirstCardView.setImageResource(CARD_IDS[playerCards.get(0)]);
        playerSecondCardView.setImageResource(CARD_IDS[playerCards.get(1)]);
        dealerSecondCardView.setImageResource(CARD_IDS[dealerCards.get(1)]);

        updateScores(false);

        // check for blackjacks
        boolean dealerBlackjack = false;
        int dealerFirstCard = dealerCards.get(0);
        int dealerSecondCard = dealerCards.get(1);
        if ((dealerFirstCard >= 0 && dealerFirstCard < 4) && (dealerSecondCard >= 4 && dealerSecondCard < 20)) {
            dealerBlackjack = true;
        } else if ((dealerFirstCard >= 4 && dealerFirstCard < 20) && (dealerSecondCard >= 0 && dealerSecondCard < 4)) {
            dealerBlackjack = true;
        }

        if (playerCurrentScore == 21) {
            if (dealerBlackjack) {
                dealerFirstCardView.setImageResource(CARD_IDS[dealerCards.get(0)]);
                dealerScoreTextView.setText("" + 21);
                hitAndStayView.setVisibility(View.GONE);
                playAgainView.setVisibility(View.VISIBLE);
                handOverTextView.setText(R.string.push);
            } else {
                dealerFirstCardView.setImageResource(CARD_IDS[dealerCards.get(0)]);
                currentMoney += (currentBet * 1.5);
                hitAndStayView.setVisibility(View.GONE);
                playAgainView.setVisibility(View.VISIBLE);
                handOverTextView.setText(R.string.player_blackjack);
            }
        } else if (dealerBlackjack) {
            dealerFirstCardView.setImageResource(CARD_IDS[dealerCards.get(0)]);
            dealerScoreTextView.setText("" + 21);
            hitAndStayView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.VISIBLE);
            handOverTextView.setText(R.string.dealer_blackjack);
        }
    }

    private void updateScores(boolean showDealerFirstCard) {
        List<Integer> playerScores = new ArrayList<>(4);
        playerScores.add(0);
        List<Integer> dealerScores = new ArrayList<>(4);
        dealerScores.add(0);

        for (int card : playerCards) {
            if (card >= 0 && card < 4) {
                ArrayList<Integer> toAdd = new ArrayList<>(4);
                for (int index = 0; index < playerScores.size(); index++) {
                    playerScores.set(index, playerScores.get(index) + 1);
                    toAdd.add(playerScores.get(index) + 10);
                }
                playerScores.addAll(toAdd);
            } else if (card >= 4 && card < 20) {
                for (int index = 0; index < playerScores.size(); index++) {
                    playerScores.set(index, playerScores.get(index) + 10);
                }
            } else {
                for (int index = 0; index < playerScores.size(); index++) {
                    playerScores.set(index, playerScores.get(index) + (((51 - card) / 4) + 2));
                }
            }
        }
        int playerBestScore = 0;
        for (int n = 0; n < playerScores.size(); n++) {
            int score = playerScores.get(n);
            if ((score > playerBestScore && score <= 21) || ((score < playerBestScore) && playerBestScore > 21) || (playerBestScore == 0)) {
                playerBestScore = score;
            }
        }

        playerCurrentScore = playerBestScore;
        playerScoreTextView.setText("" + playerCurrentScore);

        for (int n = 0; n < dealerCards.size(); n++) {
            if (n != 0 || showDealerFirstCard) {
                int card = dealerCards.get(n);
                if (card >= 0 && card < 4) {
                    ArrayList<Integer> toAdd = new ArrayList<>(4);
                    for (int index = 0; index < dealerScores.size(); index++) {
                        dealerScores.set(index, dealerScores.get(index) + 1);
                        toAdd.add(dealerScores.get(index) + 10);
                    }
                    dealerScores.addAll(toAdd);
                } else if (card >= 4 && card < 20) {
                    for (int index = 0; index < dealerScores.size(); index++) {
                        dealerScores.set(index, dealerScores.get(index) + 10);
                    }
                } else {
                    for (int index = 0; index < dealerScores.size(); index++) {
                        dealerScores.set(index, dealerScores.get(index) + (((51 - card) / 4) + 2));
                    }
                }
            }
        }
        int dealerBestScore = 0;
        for (int n = 0; n < dealerScores.size(); n++) {
            int score = dealerScores.get(n);
            if ((score > dealerBestScore && score <= 21) || dealerBestScore == 0) {
                dealerBestScore = score;
            }
        }

        dealerCurrentScore = dealerBestScore;
        dealerScoreTextView.setText("" + dealerCurrentScore);
    }

    public void onStay(View view) {
        dealerFirstCardView.setImageResource(CARD_IDS[dealerCards.get(0)]);
        updateScores(true);

        // hit dealer
        while (dealerCurrentScore < 17) {
            int hitCard = nextCard(union(playerCards, dealerCards));
            dealerCards.add(hitCard);
            ImageView hitCardView = new ImageView(this);
            hitCardView.setImageResource(CARD_IDS[hitCard]);
            dealerHand.addView(hitCardView);
            updateScores(true);
        }

        int playerWinStatus = checkWinner();
        if (playerWinStatus > 0) {
            currentMoney += currentBet;
        } else if (playerWinStatus < 0) {
            currentMoney -= currentBet;
            if (currentMoney < 0) {
                currentMoney = 1000;
            }
        }
    }

    public void onHit(View view) {
        int hitCard = nextCard(union(playerCards, dealerCards));
        playerCards.add(hitCard);
        ImageView hitCardView = new ImageView(this);
        hitCardView.setImageResource(CARD_IDS[hitCard]);
        playerHand.addView(hitCardView);

        updateScores(false);
        if (playerCurrentScore > 21) {
            currentMoney -= currentBet;
            if (currentMoney < 0) {
                currentMoney = 1000;
            }
            // change view to show that player busted
            hitAndStayView.setVisibility(View.GONE);
            playAgainView.setVisibility(View.VISIBLE);
            handOverTextView.setText(R.string.player_busts);
        }
    }

    public void playAgain(View view) {
        playAgainView.setVisibility(View.GONE);
        betDecisionView.setVisibility(View.VISIBLE);
        currentBet = 100;
        betTextView.setText("$" + currentBet);
        moneyTextView.setText(playerMoneyLabel + currentMoney);
        dealerCurrentScore = 0;
        dealerScoreTextView.setText("" + dealerCurrentScore);
        playerCurrentScore = 0;
        playerScoreTextView.setText("" + playerCurrentScore);
        playerCards.clear();
        dealerCards.clear();
        int dealerCardCount = dealerHand.getChildCount();
        for (int i = dealerCardCount - 1; i >= 2; i--) {
            dealerHand.removeViewAt(i);
        }
        int playerCardCount = playerHand.getChildCount();
        for (int i = playerCardCount - 1; i >= 2; i--) {
            playerHand.removeViewAt(i);
        }
        dealerFirstCardView.setImageResource(R.drawable.b1fv);
        dealerSecondCardView.setImageResource(R.drawable.b1fv);
        playerFirstCardView.setImageResource(R.drawable.b2fv);
        playerSecondCardView.setImageResource(R.drawable.b2fv);
    }

    private int checkWinner() {
        hitAndStayView.setVisibility(View.GONE);
        playAgainView.setVisibility(View.VISIBLE);

        if (playerCurrentScore > dealerCurrentScore && playerCurrentScore <= 21) {
            // player wins!
            handOverTextView.setText(R.string.player_wins);
            return 1;
        } else if (playerCurrentScore <= 21 && dealerCurrentScore > 21) {
            // dealer busts!
            handOverTextView.setText(R.string.dealer_busts);
            return 1;
        } else if (dealerCurrentScore > playerCurrentScore && dealerCurrentScore <= 21) {
            // dealer wins
            handOverTextView.setText(R.string.dealer_wins);
            return -1;
        } else if (dealerCurrentScore == playerCurrentScore && dealerCurrentScore <= 21) {
            // push
            handOverTextView.setText(R.string.push);
            return 0;
        } else if (playerCurrentScore > 21) {
            // we shouldn't ever get here...
            // player busts
            handOverTextView.setText(R.string.player_busts);
            return -1;
        }
        return -1;
    }

    private int nextCard(ArrayList<Integer> excludeList) {
        Integer randInt = random.nextInt(CARD_IDS.length);
        while (excludeList.contains(randInt)) {
            randInt = (randInt >= CARD_IDS.length - 1) ? 0 : randInt + 1;
        }
        return randInt;
    }

    private ArrayList<Integer> union(List<Integer> list1, List<Integer> list2) {
        Set<Integer> set = new HashSet<>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<>(set);
    }

}
