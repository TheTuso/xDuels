package pl.tuso.duels.game;

import org.bukkit.entity.Player;
import pl.tuso.duels.api.Battle;
import pl.tuso.duels.api.GameState;

import java.util.List;

public class GameBattle implements Battle {
    private GameState gameState;
    private final Player red;
    private final Player blue;

    public GameBattle(Player red, Player blue) { // TODO arena, kit
        this.red = red;
        this.blue = blue;
    }

    @Override
    public void setGameState(GameState gameState) {
        if (this.gameState == gameState) return;
        this.gameState = gameState;
        switch (gameState) {
            case STARTING -> {
                // TODO Starting
                break;
            }
            case FIGHT -> {
                // TODO Fighting
                break;
            }
            case END -> {
                // TODO Ending
                break;
            }
        }
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public Player getRedPlayer() {
        return this.red;
    }

    @Override
    public Player getBluePlayer() {
        return this.blue;
    }

    @Override
    public List<Player> getPlayers() {
        return List.of(this.red, this.blue);
    }
}
