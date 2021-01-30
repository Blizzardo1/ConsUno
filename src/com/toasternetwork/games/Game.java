package com.toasternetwork.games;

import com.toasternetwork.games.scenes.IScene;
import com.toasternetwork.games.scenes.Options;
import com.toasternetwork.games.scenes.Title;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Game implements IGameObject {
    private IScene _currentScene;
    private HashMap<String, IScene> _scenes;

    private boolean _isAlive;

    public static Random Random;

    static {
        Random = new Random();
    }

    @Override
    public void init() {

        _isAlive = true;
        _scenes = new HashMap<>();
        Title t = new Title();
        Options o = new Options();
        com.toasternetwork.games.scenes.Game g = new com.toasternetwork.games.scenes.Game();
        t.init();
        o.init();
        g.init();

        _scenes.put(t.getSceneName(), t);
        _scenes.put(o.getSceneName(), o);
        _scenes.put(g.getSceneName(), g);
        setScene(g.getSceneName());         // Such a ridiculous hack, but it should work
    }

    protected static void clearScreen() {
        System.out.print("\033[H\033[2J");
    }

    public boolean getIsAlive() {
        return _isAlive;
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        clearScreen();
        _currentScene.draw(deltaTime);
    }

    @Override
    public void update(long deltaTime) {
        _currentScene.update(deltaTime);
    }

    public void setScene(String scene) {
        _currentScene = _scenes.get(scene);
    }
}
