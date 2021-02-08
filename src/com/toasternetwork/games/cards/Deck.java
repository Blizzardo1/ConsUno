package com.toasternetwork.games.cards;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.toasternetwork.games.Game;
import com.toasternetwork.games.IGameObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * An implementation of a Deck of Cards
 */
public class Deck implements IGameObject {
    private final ArrayList<Card> _cards;
    private int _x;
    private int _y;

    private final com.toasternetwork.games.Game _game;

    private Card _currCard;
    private boolean _isEmpty;
    private boolean _expected;
    private CardColor _expectedColor;

    /**
     * A New Deck of Cards
     */
    public Deck(com.toasternetwork.games.Game game) {
        _cards = new ArrayList<>();
        _game = game;
        _currCard = new Card(CardType.Face, CardColor.Red, _game);
    }

    /**
     * Gets information whether or not the deck is empty
     * @return True if the deck is empty, else false.
     */
    public boolean getIsDeckEmpty() {
        return _isEmpty;
    }

    /**
     * Draws a card from the deck.
     * @return A Card
     */
    public Card drawCard() {
        if(getCardCount() == 1){
            _isEmpty = true;
        }
        Card c = _cards.get(0);
        _cards.remove(c);
        return c;
    }

    public CardColor getExpectedCardColor() {
        return _expectedColor;
    }

    /**
     * Adds a new card to the Deck.
     * @param c The card to add to the Deck.
     */
    public void addCard(Card c) {
        c.move(_x, _y);
        _cards.add(c);
    }

    public Card getTopCard() {
        return _currCard;
    }

    @Override
    public void init() {
        for (int y = 0; y < 4; y++) {
            CardColor color = CardColor.get(y);
            for(int x = 0; x < 10; x++) {
                _cards.add(new Card(CardType.get(x), color, _game));
                _cards.add(new Card(CardType.get(x), color, _game));
            }
            _cards.add(new Card(CardType.Skip, color, _game));
            _cards.add(new Card(CardType.Reverse, color, _game));
            _cards.add(new Card(CardType.DrawTwo, color, _game));
            _cards.add(new Card(CardType.Skip, color, _game));
            _cards.add(new Card(CardType.Reverse, color, _game));
            _cards.add(new Card(CardType.DrawTwo, color, _game));
        }
        for(int i = 0; i < 2; i++) {
            _cards.add(new Card(CardType.Wild, CardColor.Black, _game));
            _cards.add(new Card(CardType.DrawFour, CardColor.Black, _game));
        }

        organizeCards();

        // DEBUG: Show the card count
        // System.out.printf("Card Count: %d\n", getCardCount());
    }

    /**
     * Sets the top card of the Deck.
     * @param c The card to show at the top of the deck.
     */
    public void setTopCard(Card c) {
        _currCard = c;
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        Terminal t = _game.getTerminal();

        t.setCursorPosition(_x, _y);

        if(_cards.size() == 0) {
            t.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
            t.putString("    ");
            return;
        }

        _currCard.draw(deltaTime);
    }

    @Override
    public void update(long deltaTime) {
        organizeCards();
        _isEmpty = _cards.size() == 0;
    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        for(int i = 0; i < _cards.size(); i++) {
            int ri = Game.Random.nextInt(_cards.size());
            Card c = _cards.get(ri);
            Card a = _cards.get(i);
            _cards.set(i,c);
            _cards.set(ri,a);
        }
    }

    /**
     * Moves the deck around the playing field
     * @param x The X-Coordinate or Left Offset of the Terminal Window
     * @param y The Y-Coordinate or Top Offset of the Terminal Window
     */
    @Override
    public void move(int x, int y) {
        _x = x;
        _y = y;
    }

    /**
     * Gets the total count within the Deck.
     * @return An integer representation of the grand total of cards in the Deck.
     */
    public int getCardCount() {
        return _cards.size();
    }

    /**
     * Expects the next card to be a certain color
     * @param color The color to expect
     */
    // IGNORE: Terrible hack implemented due to time constraints
    public void expectNextCardToBe(CardColor color) {
        _expected = true;
        _expectedColor = color;
    }

    public boolean getNewColorIsExpected() {
        return _expected;
    }

    public void satisfyExpectedColor() {
        _expected = false;
    }

    private void organizeCards() {
        _currCard.move(_x, _y);
        if(_currCard.getType().getCardName().equals(CardType.Face.getCardName())) {
            return;
        }
        for (int i = 0, cardsSize = _cards.size(); i < cardsSize; i++) {
            Card c = _cards.get(i);
            c.move(_x, _y);
            _cards.set(i, c);
        }
    }
}
