package com.bryancapps.blackjack.models;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/**
 * Created by bryancapps on 12/27/16.
 */
@AutoValue
public abstract class GameState {
    static Builder builder() {
        return new AutoValue_GameState.Builder();
    }

    public abstract long money();

    public abstract ImmutableList<Card> dealerCards();

    public abstract int playerCount();

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setMoney(long value);

        abstract Builder setDealerCards(ImmutableList<Card> value);

        abstract Builder setPlayerCount(int value);

        abstract GameState build();
    }
}
