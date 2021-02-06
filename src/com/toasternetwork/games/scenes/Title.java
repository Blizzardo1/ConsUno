package com.toasternetwork.games.scenes;

import com.toasternetwork.games.Game;

public class Title implements IScene {
    private final Game _game;

    public Title(Game game) {
        _game = game;
    }

    @Override
    public void init() {
        // System.out.println("This works! Title Screen Initialized");
    }

    @Override
    public void draw(long deltaTime) {

    }

    @Override
    public void update(long deltaTime) {
        // If exit is pushed, send signal to the game loop to die.

    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public String getSceneName() {
        return "Title";
    }
}
