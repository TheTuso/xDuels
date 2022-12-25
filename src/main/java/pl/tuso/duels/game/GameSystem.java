package pl.tuso.duels.game;

import pl.tuso.duels.Duels;
import pl.tuso.duels.api.System;

public class GameSystem implements System {
    private final Duels duels;
    private final GameKitManager gameKitManager;
    private final GameChallengeManager gameChallengeManager;

    public GameSystem(Duels duels) {
        this.duels = duels;
        this.gameKitManager = new GameKitManager(this.duels);
        this.gameChallengeManager = new GameChallengeManager(this.duels);
    }

    @Override
    public GameKitManager getGameKitManager() {
        return this.gameKitManager;
    }

    @Override
    public GameChallengeManager getGameChallengeManager() {
        return this.gameChallengeManager;
    }
}
