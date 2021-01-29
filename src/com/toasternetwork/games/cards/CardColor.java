package com.toasternetwork.games.cards;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CardColor {
    Red(0,"Red"),
    Green(1,"Green"),
    Blue(2,"Blue"),
    Yellow(3,"Yellow"),
    Black(4,"Black");

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

    public static CardColor get(int index) {
        return lookup.get(index);
    }

    public int getColorCode() {
        switch (this) {
            case Red: return 9;
            case Green: return 10;
            case Yellow: return 11;
            case Blue: return 12;
            case Black: return 0;
        }
        return -1;
    }

    public String getCardColor() {
        return _color;
    }

    public int getIndex() {
        return _index;
    }

    public void setCardColor(CardColor color) {
        _color = color._color;
    }
}
