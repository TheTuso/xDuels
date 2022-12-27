package pl.tuso.duels.api;

public interface System {
    DuelPlayerManager getPlayerManager();

    KitManager getKitManager();

    ArenaManager getArenaManager();

    ChallengeManager getChallengeManager();
}
