package com.toasternetwork.games.cards;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.toasternetwork.games.Game;
import com.toasternetwork.games.IGameObject;

import javax.xml.soap.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * An implementation of a Deck of Cards or a Discard Pile
 */
public class Deck implements IGameObject {
    private final ArrayList<Card> _cards;
    private int _x;
    private int _y;

    private final com.toasternetwork.games.scenes.Game _game;

    private Card _currCard;

    /**
     * A New Deck of Cards
     */
    public Deck(com.toasternetwork.games.scenes.Game game) {
        _cards = new ArrayList<>();
        _currCard = new Card(CardType.Face, CardColor.Red);
        _game = game;
    }

    /**
     * Draws a card from the deck.
     * @return A Card
     */
    public Card drawCard() {
        if(getCardCount() == 0){
            return null;
        }
        Card c = _cards.get(0);
        _cards.remove(c);
        return c;
    }

    /**
     * Adds a new card to the Deck.
     * @param c The card to add to the Deck.
     */
    public void addCard(Card c) {
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
                _cards.add(new Card(CardType.get(x), color));
                _cards.add(new Card(CardType.get(x), color));
            }
            _cards.add(new Card(CardType.Skip, color));
            _cards.add(new Card(CardType.Reverse, color));
            _cards.add(new Card(CardType.DrawTwo, color));
            _cards.add(new Card(CardType.Skip, color));
            _cards.add(new Card(CardType.Reverse, color));
            _cards.add(new Card(CardType.DrawTwo, color));
        }
        for(int i = 0; i < 2; i++) {
            _cards.add(new Card(CardType.Wild, CardColor.Black));
            _cards.add(new Card(CardType.DrawFour, CardColor.Black));
        }

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
        Terminal t = com.toasternetwork.games.scenes.Game.getTerminal();

        t.setCursorPosition(_x, _y);

        if(_cards.size() == 0) {
            t.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT);
            t.putString("    ");
            return;
        }

        t.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        t.setBackgroundColor(TextColor.ANSI.valueOf(_currCard.getColor().toUpperCase(Locale.ROOT)));
        t.putString(_currCard.getValue());
    }

    @Override
    public void update(long deltaTime) {

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
}
