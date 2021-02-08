package com.toasternetwork.games.scenes;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import com.googlecode.lanterna.terminal.Terminal;
import com.toasternetwork.games.ViewType;
import com.toasternetwork.games.cards.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Game implements IScene {
    private static Terminal _terminal;
    private Deck _deck;
    private Deck _discard;
    private int _currentPlayer;

    private final int TotalPlayers = 10;
    private final com.toasternetwork.games.Game _game;

    protected Hand[] Players;
    private boolean _reverse; // Game Rule (Reverse)
    private Timer _flagTimer;
    private final long Limiter = 100;

    public Game(com.toasternetwork.games.Game game) {
        _game = game;
    }

    public Deck getDiscardPile() {
        return _discard;
    }

    public Deck getDrawPile() {
        return _deck;
    }

    private void nextPlayer() {
        // Reverse implemented, Skip implemented in the form of a method.
        if(_reverse) {
            --_currentPlayer;
        } else {
            ++_currentPlayer;
        }
        _currentPlayer = _currentPlayer < 0 ? TotalPlayers - 1 : _currentPlayer % TotalPlayers;
    }

    /**
     * All flags that need to be cleared will run through here
     * @param expected Clear Specific flags
     */
    private void clearFlags(Expected expected) {
        _flagTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (expected) {
                    case NewColor:
                        _discard.satisfyExpectedColor();
                        break;
                    case Win:
                        for(Hand player : Players) {
                            player.noLongerIsWinner();
                        }
                        break;
                    case Loss:
                        // Handle losses for the rest of the AI Muppets.
                        break;
                }
                
            }
        }, 1500);
    }

    private int getXCenterPos(String message) throws IOException {
        return _terminal.getTerminalSize().getColumns() / 2 - message.length() / 2;
    }

    private int getYCenterPos() throws IOException {
        return _terminal.getTerminalSize().getRows() / 2 - 1;
    }

    @Override
    public void init() {
        _terminal = _game.getTerminal();
        _flagTimer = new Timer();

        _deck = new Deck(_game);
        _discard = new Deck(_game);
        Players = new Hand[TotalPlayers];
        _deck.init();
        _deck.shuffle();
        System.out.printf("Players: %d\n", Players.length);
        for (int i = 0; i < Players.length; i++) {
            Players[i] = new Hand(this, i + 1);
            Players[i].init();
            Players[i].move(1, i * Card.getHeight() + 1);
            for (int j = 0; j < 8; j++) {
                Card c = _deck.drawCard();
                Players[i].giveCard(c);
            }
        }
        Card discard = _deck.drawCard();
        _discard.addCard(discard);
        _discard.setTopCard(discard);
        _discard.expectNextCardToBe(CardColor.valueOf(discard.getColor()));
        try {
            _terminal.enterPrivateMode();
            _terminal.addResizeListener(new SimpleTerminalResizeListener(new TerminalSize(100,30)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        // OPTIMIZE: Draw loop

        Hand currPlayer = Players[_currentPlayer];
        _terminal.clearScreen();

        if (currPlayer.IsWinner()) {
            String winner = String.format("Player %d is the winner!", currPlayer.getId());
            String message = "Press any key to quit...";
            int x = getXCenterPos(winner);
            int y = getYCenterPos();
            _terminal.setCursorPosition(x, y);
            _terminal.putString(winner);
            x = getXCenterPos(message);
            _terminal.setCursorPosition(x, y + 1);
            _terminal.putString(message);
        }
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // This section is going to remain hardcoded as it is meant to be at the bottom-left of the screen anyway.
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        int deckPosX = _terminal.getTerminalSize().getColumns() - 10;
        int deckPosY = _terminal.getTerminalSize().getRows() / 2 + 5;
        _discard.move(deckPosX - 20, deckPosY);
        _discard.draw(deltaTime);
        _deck.move(deckPosX - 26, deckPosY);
        _deck.draw(deltaTime);
        // -------------------------------------------------------------------------------------------------------

        TerminalSize ts = _terminal.getTerminalSize();
        TextGraphics tg = _terminal.newTextGraphics();
        TextColor tc = new TextColor.RGB(23,23,23);
        int dx = ts.getColumns() - 32;
        int dy = 0;

        // Ugly, but effective ;)
        if(_game.IsDebugModeSet()) {
            _terminal.setCursorPosition(0,Players[_currentPlayer].getY());
            _terminal.enableSGR(SGR.BOLD);
            _terminal.putCharacter('>');
            _terminal.disableSGR(SGR.BOLD);
            tg.setBackgroundColor(tc);
            tg.fillRectangle(new TerminalPosition(dx, dy), new TerminalSize(32, 5 + Players.length),' ');
            tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
            tg.putString(dx, dy++, "F3: Disable Debug");
            tg.putString(dx, dy++, "F5: Force Clear Color Flag");
            tg.putString(dx, dy++, "ESC: Kill Game");
            tg.putString(dx, dy++, String.format("Current Player:  %02d", _currentPlayer + 1));
            tg.putString(dx, dy++, String.format("Deck count    : %03d", _deck.getCardCount()));
            tg.putString(dx, dy++, String.format("Discard count : %03d", _discard.getCardCount()));
            tg.putString(dx, dy, "Expected Color: ");
            if(_discard.getNewColorIsExpected()) {
                CardColor cc = _discard.getExpectedCardColor();
                switch (cc) {
                    case Red:
                        tg.setBackgroundColor(TextColor.ANSI.RED_BRIGHT);
                    case Blue:
                        tg.setBackgroundColor(TextColor.ANSI.BLUE_BRIGHT);
                    case Green:
                        tg.setBackgroundColor(TextColor.ANSI.GREEN_BRIGHT);
                        tg.setForegroundColor(TextColor.ANSI.BLACK);
                    case Yellow:
                        tg.setBackgroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                        tg.setForegroundColor(TextColor.ANSI.BLACK);
                        break;
                }
                tg.putString(dx+16, dy++, cc.getCardColor());
            }
            dy++;
            for (Hand player: Players) {
                tg.putString(dx, dy++, String.format("Cards for Player %02d: %02d cards%32s", player.getId(), player.getCardCount(), " "));
            }
        } else {
            tg.setBackgroundColor(tc);
            tg.fillRectangle(new TerminalPosition(dx, dy), new TerminalSize(32, 1),' ');
            tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
            tg.putString(dx, dy, "F3: Debug");
        }

        for (int i = 0, playersLength = Players.length; i < playersLength; i++) {
            Hand player = Players[i];
            _terminal.setCursorPosition(0, 0);
            _terminal.setCursorPosition(1, player.getY());
            _terminal.setBackgroundColor(TextColor.ANSI.BLACK);
            _terminal.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
            _terminal.enableSGR(SGR.BOLD);
            _terminal.putString(String.format("Player %02d: ", i + 1));
            _terminal.disableSGR(SGR.BOLD);
            player.move(12, player.getY());
            player.draw(deltaTime);
        }
        _terminal.flush();
    }

    @Override
    public void update(long deltaTime) throws IOException {
        KeyStroke ks = _terminal.pollInput();
        if(ks != null) {
            KeyType kt = ks.getKeyType();
            switch (kt) {
                case F3:
                    _game.toggleView(ViewType.Debug);
                    break;
                case F5:
                    clearFlags(Expected.NewColor);
                    break;
                case Escape:
                    _game.Die();
                    break;
            }
        }

        Hand player = Players[_currentPlayer];

        if(player.IsWinner()) {
            _terminal.readInput();
            _game.Die();
        }

        player.playCard();
        player.update(deltaTime);


        _deck.update(deltaTime);

        // If the deck is presumed empty, let's dump the contents of the discard pile into the deck and reshuffle.
        if (_deck.getIsDeckEmpty()) {
            resetDeck();
        }

        try {
            Thread.sleep(Limiter);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        if(_discard.getNewColorIsExpected()) {
            clearFlags(Expected.NewColor);
        }

        // We dislike negatives because it allows the rats to get in and chew up the wires.
        // Combat the rat at mousetrap point and demand them to leave to keep positivity in the game.
        nextPlayer();
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
        // IGNORE: Method Unused
    }

    @Override
    public int getX() {
        // IGNORE: Method Unused (Always returns 0)
        return 0;
    }

    @Override
    public int getY() {
        // IGNORE: Method Unused (Always returns 0)
        return 0;
    }

    /**
     * Toggles Reverse in game
     */
    // IGNORE: Terrible hack implemented due to time constraints
    public void reverse() {
        _reverse = !_reverse;
    }

    /**
     * Tells the next player to draw two cards
     */
    // IGNORE: Terrible hack implemented due to time constraints
    public void nextPlayerDrawsTwo() {
        if(_deck.getCardCount() < 2) {
            resetDeck();
        }
        nextPlayer();
        int np = _currentPlayer;
        for(int i = 0; i < 2; i++) {
            Players[np].giveCard(_deck.drawCard());
        }
    }

    /**
     * Tells the next player to draw four cards
     */
    // IGNORE: Terrible hack implemented due to time constraints
    public void nextPlayerDrawsFour() {
        if(_deck.getCardCount() < 4) {
            resetDeck();
        }
        triggerWild();
        nextPlayer();
        int np = _currentPlayer % TotalPlayers;
        for(int i = 0; i < 4; i++) {
            Players[np].giveCard(_deck.drawCard());
        }
    }

    /**
     * Resets the deck by transferring the discard back to the deck and then... shuffles.
     */
    private void resetDeck() {
        // Grab discard and reshuffle
        Card dc;
        while (_discard.getCardCount() > 0) {
            dc = _discard.drawCard();
            _deck.addCard(dc);
            // Shuffles the deck... oh I don't know, maximum 10 times, randomly of course, else it's not a real shuffle.
            for(int i = 0; i < com.toasternetwork.games.Game.Random.nextInt(10); i++) {
                _deck.shuffle();
            }
        }
    }

    /**
     * Skips the next player
     */
    // IGNORE: Terrible hack implemented due to time constraints
    public void skipNextPlayer() {
        nextPlayer();
    }

    /**
     * AI Sets new card color
     */
    // IGNORE: Terrible hack implemented due to time constraints
    public void triggerWild() {
        CardColor color = CardColor.getRandom();
        _discard.expectNextCardToBe(color);
    }

    public void declareWinner() throws IOException {
    }
}
