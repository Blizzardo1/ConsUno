package com.toasternetwork.games;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Main {

    /**
     * The Entry-Point of the Game
     *
     * @param args Dummy Value (Unused)
     */
    public static void main(String[] args) {
        Game _game = new Game();
        _game.init();
//        Thread t = new Thread(() ->
//        {

        long lastMillis = 0;
        long deltaTime;
        long currentMillis;

        while (_game.getIsAlive()) {
            try {
                currentMillis = System.currentTimeMillis();
                deltaTime = currentMillis - lastMillis;
                _game.draw(deltaTime);
                _game.update(deltaTime);
                lastMillis = currentMillis;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        });
//
//        t.start();
//
//        Terminal terminal = _game.getTerminal();
//        KeyStroke ks = KeyStroke.fromString("X");
//        while (ks.getKeyType() != KeyType.Escape) {
//            ks = terminal.readInput();
//            if(ks.getKeyType() == KeyType.F3) {
//                _game.toggleView(ViewType.Debug);
//            }
//            // _game.wait(1);
//        }
//        _game.Die();
        System.out.println("Game is dead");
    }
}
