package com.toasternetwork.games.scenes;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.IOSafeTerminal;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import com.toasternetwork.games.cards.*;
import com.toasternetwork.games.graphics.Ansi;

import java.io.IOException;

public class Game implements IScene {
    private static Terminal _terminal;
    private Deck _deck;
    private Deck _discard;
    protected Hand[] Players;

    @Override
    public void init() {
        DefaultTerminalFactory dtf = new DefaultTerminalFactory();
        try {
            _terminal = dtf.createTerminal();
            _terminal.setCursorVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        _deck = new Deck();
        _discard = new Deck();
        Players = new Hand[10];
        _deck.init();
        _deck.shuffle();
        for (int i = 0; i < Players.length; i++) {
            Players[i] = new Hand();
            Players[i].init();
            for(int j = 0; j < 8; j++) {
                Card c = _deck.drawCard();
                Players[i].giveCard(c);
            }
        }
        Card discard = _deck.drawCard();
        _discard.addCard(discard);
        _discard.setTopCard(discard);
    }

    @Override
    public void draw(long deltaTime) throws IOException {

        _terminal.setCursorPosition(0, 0);
        _terminal.setBackgroundColor(TextColor.ANSI.WHITE_BRIGHT);
        _terminal.flush();

        int deckPosY = _terminal.getTerminalSize().getRows() - 2;
        _discard.move(7, deckPosY);
        _discard.draw(deltaTime);
        _deck.move(1, deckPosY);
        _deck.draw(deltaTime);
        int pc = 1;
        _terminal.setCursorPosition(1,1);
        for (Hand player : Players) {
            _terminal.setBackgroundColor(TextColor.ANSI.BLACK);
            _terminal.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
            _terminal.putString(String.format("Player %02d: ", pc++));
            player.draw(deltaTime);
            _terminal.setCursorPosition(1, pc);
        }
    }

    @Override
    public void update(long deltaTime) {
        if(_deck.getCardCount() == 0) {
            // Grab discard and reshuffle
            Card dc;
            while((dc = _discard.drawCard()) != null) {
                _deck.addCard(dc);
                _deck.shuffle();
            }
        }

        _deck.update(deltaTime);
        for(Hand player : Players) {
            player.update(deltaTime);
        }
    }

    @Override
    public String getSceneName() {
        return "Game";
    }

    /**
     * Gets the scene's Terminal.
     * @return A Terminal to the Scene
     */
    public static Terminal getTerminal() {
        return _terminal;
    }
}
