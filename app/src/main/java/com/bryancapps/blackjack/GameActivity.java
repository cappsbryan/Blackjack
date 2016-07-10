package com.bryancapps.blackjack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


public class GameActivity extends Activity {

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("money", Game.getInstance().getMoney());
        editor.apply();
    }
}