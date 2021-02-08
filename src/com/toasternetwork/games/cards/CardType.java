package com.toasternetwork.games.cards;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A representation of the card's type
 */
public enum CardType {
    Zero(0, "0"),
    One(1, "1"),
    Two(2, "2"),
    Three(3, "3"),
    Four(4, "4"),
    Five(5, "5"),
    Six(6, "6"),
    Seven(7, "7"),
    Eight(8, "8"),
    Nine(9, "9"),
    Skip(10, "Ø"),
    Reverse(11, "R"),
    DrawTwo(12, "+2"),
    Wild(13, "W"),
    DrawFour(14, "+4"),
    Face(254, "U"),
    Blank(255, " ");

    private final int _type;
    private final String _name;

    private static final Map<Integer, CardType> lookup = new HashMap<>();

    static {
        for(CardType c : EnumSet.allOf(CardType.class)) {
            lookup.put(c.getCard(), c);
        }
    }

    CardType(int type, String name) {
        _type = type;
        _name = name;
    }

    /**
     * Gets the CardType at the specified index
     * @param index The specified index of which to retrieve the CardType.
     * @return The CardType based on the index
     */
    public static CardType get(int index) {
        return lookup.get(index);
    }

    /**
     * Get's the Card in the form of an Integer
     * @return The Integer representation of the CardType.
     */
    public int getCard() {
        return _type;
    }

    /**
     * Gets the card's name
     * @return A String representing the Name
     */
    public String getCardName() {
        return _name;
    }
}
