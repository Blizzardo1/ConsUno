package com.toasternetwork.games.cards;

import com.toasternetwork.games.IGameObject;
import com.toasternetwork.games.scenes.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Hand implements IGameObject {
    private ArrayList<Card> _hand;
    private int _x;
    private int _y;

    private final Game _game;
    private boolean _isWinner;
    private final int _playerId;

    public Hand(Game game, int playerId) {
        _game = game;
        _playerId = playerId;
    }

    public boolean IsWinner() {
        return _isWinner;
    }

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
            playCard();
            return;
        }
        if(_hand.toArray().length == 0) {
            _isWinner = true;
            return;
        }
        for(int i = 0; i < _hand.size(); i++) {
            Card card = _hand.get(i);
            if(_hand.size() == 0) {
                _isWinner = true;
            }
            try {
            if(card.isMatch(discardReference)) {
                _hand.remove(card);
                if(_hand.size() == 1) {
                    _game.getTerminal().putString("UNO!");
                }
                _game.getDiscardPile().addCard(card);
                _game.getDiscardPile().setTopCard(card);
            }
            } catch (NullPointerException | IOException npe) {
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
        _game.getTerminal().setCursorPosition(_x, _y);
        for (Card c : _hand) {
            c.draw(deltaTime);
        }
    }

    @Override
    public void update(long deltaTime) {
        // _hand.trimToSize();
        System.out.printf("Cards for Player %d, %d.\n", _playerId, _hand.size());
    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }

    public int getId() {
        return _playerId;
    }

    @Override
    public void move(int x, int y) {
        _x = x;
        _y = y;
    }
}
