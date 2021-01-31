package com.toasternetwork.games.scenes;

import com.toasternetwork.games.IGameObject;

public interface IScene extends IGameObject {
    /**
     * Gets the current Scene in the representation of a String
     * @return The Name of the current Scene.
     */
    String getSceneName();
}
