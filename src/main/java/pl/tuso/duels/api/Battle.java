package pl.tuso.duels.api;

import org.bukkit.entity.Player;

import java.util.List;

public interface Battle {
    void setGameState(GameState gameState);

    GameState getGameState();

    Player getRedPlayer();

    Player getBluePlayer();

    List<Player> getPlayers();
}
