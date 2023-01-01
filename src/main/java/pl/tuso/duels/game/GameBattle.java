package pl.tuso.duels.game;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.*;

import java.time.Duration;
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
                this.getPlayers().forEach(duelPlayer -> {
                    duelPlayer.getHandle().sendMessage(this.duels.getMessages().getLine("battle.fight.chat"));
                    duelPlayer.getHandle().showTitle(Title.title(
                            this.duels.getMessages().getLine("battle.fight.title"),
                            this.duels.getMessages().getLine("battle.fight.subtitle"),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1))));
                    duelPlayer.getHandle().playSound(Sound.sound(Key.key("minecraft:entity.generic.explode"), Sound.Source.MASTER, 4.0F, 1.0F));
                });
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
                this.duels.getGameSystem().getBattleManager().getBattles().remove(this);
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

    @Override
    public void setWinner(DuelPlayer duelPlayer) {
        Preconditions.checkArgument(this.players.contains(duelPlayer), "The winner must take part in the battle, not some random guy you give!");
        this.winner = duelPlayer;
    }

    @Override
    public DuelPlayer getWinner() {
        Preconditions.checkNotNull(this.winner, "No one won yet!");
        return this.winner;
    }
}
