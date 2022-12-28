package pl.tuso.duels.game;

import pl.tuso.duels.Duels;
import pl.tuso.duels.api.System;
import pl.tuso.duels.api.*;

public class GameSystem implements System {
    private final Duels duels;
    private final GamePlayerManager gamePlayerManager;
    private final GameKitManager gameKitManager;
    private final GameArenaManager gameArenaManager;
    private final GameChallengeManager gameChallengeManager;
    private final GameBattleManager gameBattleManager;
    private final GameLobby gameLobby;

    public GameSystem(Duels duels) {
        this.duels = duels;
        this.gamePlayerManager = new GamePlayerManager(this.duels);
        this.gameKitManager = new GameKitManager(this.duels);
        this.gameArenaManager = new GameArenaManager(this.duels);
        this.gameChallengeManager = new GameChallengeManager(this.duels);
        this.gameBattleManager = new GameBattleManager(this.duels);
        this.gameLobby = new GameLobby(this.duels);
        this.duels.getServer().getPluginManager().registerEvents(new GameListener(this.duels), this.duels);
    }

    @Override
    public DuelPlayerManager getPlayerManager() {
        return this.gamePlayerManager;
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

    @Override
    public BattleManager getBattleManager() {
        return this.gameBattleManager;
    }

    @Override
    public Lobby getLobby() {
        return this.gameLobby;
    }
}
