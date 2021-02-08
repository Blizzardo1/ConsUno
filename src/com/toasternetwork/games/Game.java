package com.toasternetwork.games;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.toasternetwork.games.scenes.IScene;
import com.toasternetwork.games.scenes.Options;
import com.toasternetwork.games.scenes.Title;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * A Basic implementation of The Game.
 */
public class Game implements IGameObject {
    private IScene _currentScene;
    private HashMap<String, IScene> _scenes;

    private Terminal _terminal;
    private boolean _debugMode;
    private boolean _developerMode;

    public Terminal getTerminal() {
        return _terminal;
    }

    private boolean _isAlive;

    public static Random Random;

    static {
        Random = new Random();
    }

    public boolean IsDebugModeSet() {
        return _debugMode;
    }

    public boolean IsDeveloperModeSet() {
        return _developerMode;
    }

    public void Die() throws IOException {
        _isAlive = false;
        _terminal.close();
    }

    @Override
    public void init() {
        _debugMode = true; // Test with this to ensure Diagnostics are alive at start
        _isAlive = true;
        _scenes = new HashMap<>();
        DefaultTerminalFactory dtf = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(125,50));
        try {
            _terminal = dtf.createTerminal();
            _terminal.setCursorVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Title t = new Title(this);
        Options o = new Options(this);
        com.toasternetwork.games.scenes.Game g = new com.toasternetwork.games.scenes.Game(this);
        t.init();
        o.init();
        g.init();

        _scenes.put(t.getSceneName(), t);
        _scenes.put(o.getSceneName(), o);
        _scenes.put(g.getSceneName(), g);
        setScene(g.getSceneName());         // Such a ridiculous hack, but it should work
    }

    /**
     * A Boolean to check if the game is still alive.
     * @return Whether or not the game is alive.
     */
    public boolean getIsAlive() {
        return _isAlive;
    }

    @Override
    public void draw(long deltaTime) throws IOException {
        _currentScene.draw(deltaTime);
    }

    @Override
    public void update(long deltaTime) throws IOException {
        _currentScene.update(deltaTime);
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    /**
     * Sets the scene of the current Game.
     * @param scene A Scene to transition to
     */
    public void setScene(String scene) {
        _currentScene = _scenes.get(scene);
    }

    public void move(int x, int y) {

    }

    public void toggleView(ViewType vt) {
        switch (vt) {
            case Debug:
                _debugMode = !_debugMode;
                break;
            case Developer:
                _developerMode = !_developerMode;
                break;
        }
    }
}
