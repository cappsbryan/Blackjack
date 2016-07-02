package com.bryancapps.blackjack;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by bryancapps on 6/22/16.
 */
public class CardViewHolder extends RecyclerView.ViewHolder {
    public final ImageView imageView;

    public CardViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.card_image);
    }
}
