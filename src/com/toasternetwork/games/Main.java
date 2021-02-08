package com.toasternetwork.games;

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
        System.out.println("Game is dead");
        System.exit(0); // Any remaining threads are killed thanks to this. Should just call this scissors(int);
    }
}
