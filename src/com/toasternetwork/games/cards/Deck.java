package com.toasternetwork.games.cards;

import com.toasternetwork.games.IGameObject;

import java.util.ArrayList;

public class Deck implements IGameObject {
    private final ArrayList<Card> _cards;

    public Deck() {
        _cards = new ArrayList<>();
    }

    public Card drawCard() {
        if(getCardCount() == 0){
            return null;
        }
        Card c = _cards.get(0);
        _cards.remove(c);
        return c;
    }

    public void addCard(Card c) {
        _cards.add(c);
    }

    @Override
    public void init() {
        for (int y = 0; y < 4; y++) {
            CardColor color = CardColor.get(y);
            for(int x = 0; x < 10; x++) {
                _cards.add(new Card(CardType.get(x), color));
            }
            _cards.add(new Card(CardType.Skip, color));
            _cards.add(new Card(CardType.Reverse, color));
            _cards.add(new Card(CardType.DrawTwo, color));

        }
        for(int i = 0; i < 2; i++) {
            _cards.add(new Card(CardType.Wild, CardColor.Black));
            _cards.add(new Card(CardType.DrawFour, CardColor.Black));
        }

        System.out.printf("Card Count: %d\n", getCardCount());
    }

    @Override
    public void draw(long deltaTime) {

    }

    @Override
    public void update(long deltaTime) {

    }

    public void shuffle() {
        // TODO: Implement shuffle

    }

    public int getCardCount() {
        return _cards.size();
    }
}
