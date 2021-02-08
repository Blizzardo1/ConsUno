package com.toasternetwork.games.cards;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.toasternetwork.games.Game;
import com.toasternetwork.games.IGameObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * A Basic Card
 */
public class Card implements IGameObject {
    private static final int _cardWidth = 5;
    private static final int _cardHeight = 3;

    private CardColor _color;
    private CardType _type;
    private final Game _game;
    private int _x;
    private int _y;

    public static int getWidth() {
        return _cardWidth;
    }
    public static int getHeight() {
        return _cardHeight;
    }

    /**
     * A new Card
     * @param type The value of the card, from 0 to Wild Draw4
     * @param color The color representing the face of the card.
     */
    public Card(CardType type, CardColor color, Game game) {
        _color = color;
        _type = type;
        _game = game;
    }

    /**
     * Gets the given color
     * @return A String representing the current color of the card's face.
     */
    public String getColor() {
        return _color.getCardColor();
    }

    /**
     * Sets the color of the face of the card
     * @param color The given colors: RED, BLUE, GREEN, YELLOW, or BLACK.
     */
    public void setColor(String color) {
        _color = CardColor.valueOf(color);
    }


    /**
     * Gets the card name
     * @return The following cards: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, Reverse, Skip, Draw2, Wild, Wild Draw4
     */
    public String getValue() {
        return _type.getCardName();
    }

    /**
     * Gives a card its identity
     * @param value A number between 0 and 12
     */
    public void setValue(int value) {
        Optional<CardType> s = Arrays.stream(CardType.values()).filter(c -> c.getCard() == value).findFirst();
        if(!s.isPresent()) return;
        _type = s.get();
    }

    /**
     * Checks if the card passed meets the criteria for a discard
     * @param card The card to be scrutinized by the parent card
     * @return true if the following match, the color, the number for a color change, a wild, or a wild draw4
     */
    public boolean isMatch(Card card) {
        return card.getColor().equals(this.getColor())
                || card.getValue().equals(this.getValue())
                || card._type == CardType.DrawFour
                || card._type == CardType.Wild
                || this._type == CardType.DrawFour
                || this._type == CardType.Wild;
    }

    @Override
    public void init() {

    }

    @Override
    public void draw(long deltaTime) throws IOException {
        Terminal t = _game.getTerminal();
        TextColor bg = TextColor.ANSI.valueOf(String.format("%s%s", _color.getCardColor().toUpperCase(Locale.ROOT), "_BRIGHT"));
        t.setBackgroundColor(bg);
        if(bg == TextColor.ANSI.YELLOW_BRIGHT) {
            t.setForegroundColor(TextColor.ANSI.WHITE);
        } else {
            t.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        }

        t.setCursorPosition(_x, _y-1);
        t.putString("|   |");
        t.setCursorPosition(_x, _y);
        t.putString(String.format("|%2s |", _type.getCardName()));
        t.setCursorPosition(_x, _y+1);
        t.putString("|   |");
        t.resetColorAndSGR();
    }

    @Override
    public void update(long deltaTime) {

    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }

    @Override
    public void move(int x, int y) {
        _x = x;
        _y = y;
    }

    public CardType getType() {
        return _type;
    }
}
