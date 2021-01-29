package com.toasternetwork.games;

import java.io.IOException;

public interface IGameObject {
    void init();
    void draw(long deltaTime) throws IOException;
    void update(long deltaTime);
}
