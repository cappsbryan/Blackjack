package com.bryancapps.blackjack.models;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/**
 * Created by bryancapps on 12/27/16.
 */
@AutoValue
public abstract class PlayerState {
    static Builder builder() {
        return new AutoValue_PlayerState.Builder();
    }

    public abstract long bet();

    public abstract ImmutableList<Card> cards();

    public abstract GameStatus status();

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setBet(long value);

        abstract Builder setCards(ImmutableList<Card> value);

        abstract Builder setStatus(GameStatus value);

        abstract PlayerState build();
    }
}
