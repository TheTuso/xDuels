package pl.tuso.duels.game;

import pl.tuso.duels.Duels;
import pl.tuso.duels.api.ArenaManager;
import pl.tuso.duels.api.ChallengeManager;
import pl.tuso.duels.api.KitManager;
import pl.tuso.duels.api.System;

public class GameSystem implements System {
    private final Duels duels;
    private final GameKitManager gameKitManager;
    private final GameArenaManager gameArenaManager;
    private final GameChallengeManager gameChallengeManager;

    public GameSystem(Duels duels) {
        this.duels = duels;
        this.gameKitManager = new GameKitManager(this.duels);
        this.gameArenaManager = new GameArenaManager(this.duels);
        this.gameChallengeManager = new GameChallengeManager(this.duels);
    }

    @Override
    public KitManager getKitManager() {
        return this.gameKitManager;
    }

    @Override
    public ArenaManager getArenaManager() {
        return this.gameArenaManager;
    }

    @Override
    public ChallengeManager getChallengeManager() {
        return this.gameChallengeManager;
    }
}
