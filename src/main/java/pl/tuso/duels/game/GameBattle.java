package pl.tuso.duels.game;

import org.bukkit.entity.Player;
import pl.tuso.duels.api.*;

import java.util.List;

public class GameBattle implements Battle {
    private final Arena arena;
    private final Kit kit;
    private final DuelPlayer red;
    private final DuelPlayer blue;
    private GameState gameState;

    public GameBattle(Arena arena, Kit kit, DuelPlayer red, DuelPlayer blue) {
        this.arena = arena;
        this.kit = kit;
        this.red = red;
        this.blue = blue;
        this.setGameState(GameState.STARTING);
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
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public Kit getKit() {
        return this.kit;
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public DuelPlayer getRedPlayer() {
        return this.red;
    }

    @Override
    public DuelPlayer getBluePlayer() {
        return this.blue;
    }

    @Override
    public List<DuelPlayer> getPlayers() {
        return List.of(this.red, this.blue);
    }
}
