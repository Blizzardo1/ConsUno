package com.toasternetwork.games.cards;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.toasternetwork.games.IGameObject;
import com.toasternetwork.games.graphics.Ansi;
import com.toasternetwork.games.scenes.Game;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

public class Card implements IGameObject {
    private CardColor _color;
    private CardType _type;

    public Card(CardType type, CardColor color) {
        _color = color;
        _type = type;
    }

    public String getColor() {
        return _color.getCardColor();
    }

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
                || card._type.equals(CardType.Wild)
                || card._type.equals(CardType.DrawFour);
    }

    @Override
    public void init() {
        // Do nothing... yet
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        Terminal t = Game.getTerminal();
        t.setBackgroundColor(TextColor.ANSI.valueOf(_color.getCardColor().toUpperCase(Locale.ROOT)));
        t.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        t.putString(_type.getCardName());
        t.resetColorAndSGR();
    }

    @Override
    public void update(long deltaTime) {

    }
}
