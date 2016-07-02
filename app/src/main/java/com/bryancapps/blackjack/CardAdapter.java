package com.bryancapps.blackjack;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by bryancapps on 6/22/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> implements PropertyChangeListener {
    private final Hand hand;

    public CardAdapter(Hand hand) {
        this.hand = hand;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        int image;
        if (hand.size() < 2) {
            image = R.drawable.b2fv;
        } else {
            image = hand.get(position).drawableId();
        }
        holder.imageView.setImageResource(image);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        return new CardViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return hand.size() > 2 ? hand.size() : 2;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        notifyDataSetChanged();
    }
}
