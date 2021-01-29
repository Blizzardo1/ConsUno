package com.toasternetwork.games.cards;

import com.toasternetwork.games.IGameObject;

import java.io.IOException;
import java.util.ArrayList;

public class Hand implements IGameObject {
    private ArrayList<Card> _hand;

    public void giveCard(Card card) {
        _hand.add(card);
    }

    @Override
    public void init() {
        _hand = new ArrayList<>();
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        for (Card c : _hand) {
            c.draw(deltaTime);
        }
    }

    @Override
    public void update(long deltaTime) {

    }
}
