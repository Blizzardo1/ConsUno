package com.toasternetwork.games.scenes;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
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
    private int _currentPlayer;

    private final int TotalPlayers = 4;
    private final com.toasternetwork.games.Game _game;

    protected Hand[] Players;

    public Game(com.toasternetwork.games.Game game) {
        _game = game;
    }

    public Deck getDiscardPile() {
        return _discard;
    }

    public Deck getDrawPile() {
        return _deck;
    }


    @Override
    public void init() {
        _terminal = _game.getTerminal();
        _deck = new Deck(_game);
        _discard = new Deck(_game);
        Players = new Hand[TotalPlayers];
        _deck.init();
        _deck.shuffle();
        for (int i = 0; i < Players.length; i++) {
            Players[i] = new Hand(this, i + 1);
            Players[i].init();
            Players[i].move(1, i + 1);
            for (int j = 0; j < 8; j++) {
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
        _terminal.clearScreen();
        _terminal.setCursorPosition(0, 0);
        _terminal.setBackgroundColor(TextColor.ANSI.WHITE_BRIGHT);
        _terminal.flush();

        int deckPosY = _terminal.getTerminalSize().getRows() - 2;
        _discard.move(7, deckPosY);
        _discard.draw(deltaTime);
        _deck.move(1, deckPosY);
        _deck.draw(deltaTime);

        // _terminal.setCursorPosition(1,1);
        Hand player = Players[_currentPlayer];

        _terminal.setCursorPosition(0, 0);
        _terminal.putString(String.format("Player %d's turn", player.getId()));
        _terminal.setCursorPosition(1, player.getY());
        _terminal.setBackgroundColor(TextColor.ANSI.BLACK);
        _terminal.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        _terminal.putString(String.format("Player %02d: ", _currentPlayer));
        player.draw(deltaTime);
        // _terminal.setCursorPosition(1, pc);

        _terminal.flush();
    }

    @Override
    public void update(long deltaTime) {
        System.out.printf("Delta: %d\tUpdate();\n", deltaTime);
        if (_terminal == null) {
            _game.Die();
        }
        if (_deck.getCardCount() == 0) {
            // Grab discard and reshuffle
            Card dc;
            while ((dc = _discard.drawCard()) != null) {
                _deck.addCard(dc);
                _deck.shuffle();
            }
        }

        _deck.update(deltaTime);

        Hand player = Players[_currentPlayer];

        if (player.IsWinner()) {
            try {
                _terminal.setCursorPosition(0, 0);
                _terminal.putString(String.format("Player %d is the winner!", player.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            _game.Die();
        }

        player.playCard();
        player.update(deltaTime);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        // TODO: Implement Reverse and Skip into this variable
        _currentPlayer += _currentPlayer % TotalPlayers;
        System.out.printf("Next Player: %d\n", _currentPlayer);
    }

    public Terminal getTerminal() {
        return _terminal;
    }

    @Override
    public String getSceneName() {
        return "Game";
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }
}
