package com.toasternetwork.games.cards;

import com.toasternetwork.games.IGameObject;
import com.toasternetwork.games.scenes.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Hand implements IGameObject {
    private ArrayList<Card> _hand;

    private final Game _game;
    private boolean _isWinner;
    private final int _playerId;

    public Hand(Game game, int playerId) {
        _game = game;
        _playerId = playerId;
    }

    public boolean IsWinner() {return _isWinner; }

    /**
     * Adds a card to the player's hand
     * @param card A card to add to the hand
     */
    public void giveCard(Card card) {
        _hand.add(card);
    }

    public void playCard() {
        Card discardReference = _game.getDiscardPile().getTopCard();

        if(_hand.stream().noneMatch(discardReference::isMatch)) {
            giveCard(_game.getDrawPile().drawCard());
        }
        if(_hand.toArray().length == 0) {
            _isWinner = true;
            return;
        }
        for(int i = 0; i < _hand.size(); i++) {
            Card card = _hand.get(i);
            try {
            if(card.isMatch(discardReference)) {
                _hand.remove(card);
                _game.getDiscardPile().addCard(card);
                _game.getDiscardPile().setTopCard(card);
            }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }

    @Override
    public void init() {
        _hand = new ArrayList<>();
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        if(_hand == null) return;

        for (Card c : _hand) {
            c.draw(deltaTime);
        }
    }

    @Override
    public void update(long deltaTime) {

    }

    public int getId() {
        return _playerId;
    }
}
