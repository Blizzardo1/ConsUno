package com.toasternetwork.games.cards;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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
    Reverse(11, "↔"),
    DrawTwo(12, "§"),
    Wild(13, "֍"),
    DrawFour(14, "҈");

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

    public static CardType get(int index) {
        return lookup.get(index);
    }

    public int getCard() {
        return _type;
    }

    public String getCardName() {
        return _name;
    }
}
