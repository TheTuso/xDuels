package pl.tuso.duels.api;

import pl.tuso.duels.game.GameChallengeManager;
import pl.tuso.duels.game.GameKitManager;

public interface System {
    GameKitManager getGameKitManager();

    GameChallengeManager getGameChallengeManager();
}
