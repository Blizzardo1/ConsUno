package com.toasternetwork.games.cards;

import com.googlecode.lanterna.terminal.Terminal;
import com.toasternetwork.games.IGameObject;
import com.toasternetwork.games.scenes.Game;

import java.io.IOException;
import java.util.ArrayList;

public class Hand implements IGameObject {
    private ArrayList<Card> _hand;
    private int _x;
    private int _y;

    private final Game _game;
    private boolean _isWinner;
    private final int _playerId;
    private Terminal _terminal;

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

    public void playCard() throws IOException {
        if (getCardCount() == 0) {
            _game.declareWinner();
        }
        Deck discardPile = _game.getDiscardPile();
        Card discardReference = discardPile.getTopCard();

        if (_hand.stream().noneMatch(discardReference::isMatch)) {
            giveCard(_game.getDrawPile().drawCard());
        }

        _hand.stream().reduce((card, card2) -> discardReference.isMatch(card) ? card : card2).ifPresent((card) -> {
            try {
                if (discardPile.getNewColorIsExpected() && !card.getColor().equals(discardPile.getExpectedCardColor().getCardColor())) {
                    return;
                }
                if (discardReference.isMatch(card)) {
                    switch (card.getType()) {
                        case Wild:
                            _game.triggerWild();
                            break;
                        case Skip:
                            _game.skipNextPlayer();
                            break;
                        case DrawFour:
                            _game.nextPlayerDrawsFour();
                            break;
                        case DrawTwo:
                            _game.nextPlayerDrawsTwo();
                            break;
                        case Reverse:
                            _game.reverse();
                            break;
                        default:
                            break;
                    }
                    _hand.remove(card);
                    _hand.trimToSize();

                    if (getCardCount() == 0) {
                        _game.declareWinner();
                    }

                    _game.getDiscardPile().addCard(card);
                    _game.getDiscardPile().setTopCard(card);
                    if(discardPile.getNewColorIsExpected()) {
                        discardPile.satisfyExpectedColor();
                    }
                }
            } catch (NullPointerException | IOException npe) {
                npe.printStackTrace();
            }
        });
    }

    @Override
    public void init() {
        _hand = new ArrayList<>();
        _terminal = _game.getTerminal();
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        if(_hand == null) return;
        _terminal.setCursorPosition(_x, _y);
        for (Card c : _hand) {
            if (c == null) continue; // Is this still needed? Safety first!
            c.draw(deltaTime);
        }

        if (getCardCount() == 1) {
            _terminal.setCursorPosition(_x + 6, _y);
            _terminal.putString("UNO!");
        }
    }

    @Override
    public void update(long deltaTime) {
        _hand.trimToSize();
        for(int c = 0; c < getCardCount(); c++) {
            Card card = _hand.get(c);
            card.move(_x + (c * Card.getWidth()), _y);
            _hand.set(c, card);
        }
        if(getCardCount() == 0) {
            _isWinner = true;
        }
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

    public int getCardCount() {
        return _hand.size();
    }

    public void noLongerIsWinner() {
        _isWinner = false;
    }
}
