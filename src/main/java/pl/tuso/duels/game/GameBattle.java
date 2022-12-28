package pl.tuso.duels.game;

import org.bukkit.event.player.PlayerTeleportEvent;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.*;

import java.util.List;

public class GameBattle implements Battle {
    private final Duels duels;
    private final Arena arena;
    private final Kit kit;
    private final DuelPlayer red;
    private final DuelPlayer blue;
    private final List<DuelPlayer> players;
    private final Countdown countdown;
    private DuelPlayer winner;
    private GameState gameState;

    public GameBattle(Duels duels, Arena arena, Kit kit, DuelPlayer red, DuelPlayer blue) {
        this.duels = duels;
        this.arena = arena;
        this.kit = kit;
        this.red = red;
        this.blue = blue;
        this.players = List.of(red, blue);
        this.countdown = new GameCountdown(this.duels, this, 10, this.red, this.blue);
        this.winner = null;
        this.setGameState(GameState.STARTING);
    }

    @Override
    public void setGameState(GameState gameState) {
        if (this.gameState == gameState) return;
        this.gameState = gameState;
        switch (gameState) {
            case STARTING -> {
                this.arena.buildWalls(this.red, this.blue);
                this.red.getHandle().teleport(this.arena.getRedSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                this.blue.getHandle().teleport(this.arena.getBlueSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                this.countdown.start();
                this.players.forEach(duelPlayer -> {
                    duelPlayer.setFighting(true);
                    duelPlayer.loadState(this.kit);
                    duelPlayer.getHandle().sendMessage(this.duels.getMessages().getLine("battle.starting"));
                });
            }
            case FIGHT -> {
                this.arena.destroyWalls(this.red, this.blue);
                this.getPlayers().forEach(duelPlayer -> duelPlayer.getHandle().sendMessage(this.duels.getMessages().getLine("battle.fight")));
            }
            case END -> {
                this.getPlayers().forEach(duelPlayer -> {
                    duelPlayer.setFighting(false);
                    duelPlayer.getHandle().teleport(this.duels.getGameSystem().getLobby().getLobbyLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    duelPlayer.loadStateBackup();
                });
                if (this.winner != null) {
                    if (this.winner.equals(this.red)) {
                        this.red.getHandle().sendMessage(this.duels.getMessages().getLine("battle.win"));
                        this.blue.getHandle().sendMessage(this.duels.getMessages().getLine("battle.defeat"));
                    } else {
                        this.blue.getHandle().sendMessage(this.duels.getMessages().getLine("battle.win"));
                        this.red.getHandle().sendMessage(this.duels.getMessages().getLine("battle.defeat"));
                    }
                } else {
                    this.duels.getLogger().warning("No one won???");
                }
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
        return this.players;
    }
}
