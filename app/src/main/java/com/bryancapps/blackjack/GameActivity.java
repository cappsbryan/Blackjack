package com.bryancapps.blackjack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bryancapps.blackjack.Models.GameStatus;
import com.bryancapps.blackjack.Models.Player;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity {
    GameFragmentPagerAdapter adapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.saved_money_default);
        Game.getInstance().setMoney(sharedPref.getInt("money", defaultValue));

        if (Game.getInstance().getMoney() <= 0) {
            Game.getInstance().setMoney(defaultValue);
        }

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new GameFragmentPagerAdapter(getSupportFragmentManager());
        adapter.add(new GameFragment());
        pager.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("money", Game.getInstance().getMoney());
        editor.apply();
    }

    public void resetGame() {
        pager.setCurrentItem(0);
        for (int i = 1; i < adapter.getCount(); i++) {
            adapter.remove(i);
        }
        Game game = Game.getInstance();
        for (int i = 1; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            game.removePlayer(player);
        }
        adapter.notifyDataSetChanged();
        game.setStatus(GameStatus.BETTING, game.getPlayers().get(0));
    }

    public void addGameFragment(GameFragment fragment) {
        adapter.add(fragment);
//        pager.setCurrentItem(adapter.getCount() - 1);
    }

    public void removeFragment(int index) {
        adapter.remove(index);
    }

    private class GameFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private List<GameFragment> fragments;

        public GameFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragments = new ArrayList<>();
        }

        public void add(GameFragment fragment) {
            fragments.add(fragment);
            notifyDataSetChanged();
        }

        public void remove(int index) {
            fragments.remove(index);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof GameFragment) {
                int index = fragments.indexOf(object);
                if (index == -1)
                    return POSITION_NONE;
                else
                    return index;
            }
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}