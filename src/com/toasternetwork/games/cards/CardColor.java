package com.toasternetwork.games.cards;

import com.toasternetwork.games.Game;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Base color of a standard card
 */
public enum CardColor {
    Red(0,"Red"),
    Green(1,"Green"),
    Blue(2,"Blue"),
    Yellow(3,"Yellow"),
    Black(4,"Black"),
    Gray(5, "Gray");

    private static final Map<Integer, CardColor> lookup = new HashMap<>();

    static {
        for(CardColor c : EnumSet.allOf(CardColor.class)) {
            lookup.put(c.getIndex(), c);
        }
    }

    private String _color;
    private final int _index;
    CardColor(int index, String color) {
        _color = color;
        _index = index;
    }

    /**
     * Gets the card color at the specified index
     * @param index The index to retrieve the given CardColor
     * @return The CardColor at the index
     */
    public static CardColor get(int index) {
        return lookup.get(index);
    }

    public static CardColor getRandom() {
        return get(Game.Random.nextInt(4));
    }

    /**
     * Gets the card Color in the form of a String
     * @return A String representing the card's color
     */
    public String getCardColor() {
        return _color;
    }

    /**
     * Gets the index of the CardColor
     * @return The specified index of the CardColor
     */
    public int getIndex() {
        return _index;
    }

    /**
     * Sets the new color of the card. (Ability to manipulate the card)
     * @param color The new CardColor
     */
    public void setCardColor(CardColor color) {
        _color = color._color;
    }
}
