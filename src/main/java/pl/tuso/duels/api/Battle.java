package pl.tuso.duels.api;

import org.bukkit.entity.Player;

import java.util.List;

public interface Battle {
    void setGameState(GameState gameState);

    Arena getArena();

    Kit getKit();

    DuelPlayer getRedPlayer();

    DuelPlayer getBluePlayer();

    List<DuelPlayer> getPlayers();

    GameState getGameState();
}
