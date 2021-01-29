package com.toasternetwork.games;

import com.toasternetwork.games.graphics.Ansi;

import java.io.IOException;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        Game _game = new Game();
        _game.init();
        long lastMillis = 0;
        long deltaTime;
        long currentMillis;
        while(_game.getIsAlive()) {
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
    }
}
