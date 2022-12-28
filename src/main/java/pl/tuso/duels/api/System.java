package pl.tuso.duels.api;

import org.bukkit.Location;

public interface System {
    DuelPlayerManager getPlayerManager();

    KitManager getKitManager();

    ArenaManager getArenaManager();

    ChallengeManager getChallengeManager();

    BattleManager getBattleManager();

    Lobby getLobby();
}
