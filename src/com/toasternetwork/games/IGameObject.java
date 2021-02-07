package com.toasternetwork.games;

import java.io.IOException;

public interface IGameObject {
    /**
     * Initializes the Game Object
     */
    void init();

    /**
     * Draws the Game Object
     * @param deltaTime The difference in time between draws and updates
     * @throws IOException An IOException because of Lanterna's Terminal failing to spawn.
     */
    void draw(long deltaTime) throws IOException;

    /**
     * Updates the logic of the Game Object
     * @param deltaTime The difference in time between draws and updates
     */
    void update(long deltaTime) throws IOException;

    int getX();

    int getY();

    void move(int x, int y);
}
