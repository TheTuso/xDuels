package pl.tuso.duels.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Battle;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.GameState;

public class GameListener implements Listener {
    public Duels duels;

    public GameListener(Duels duels) {
        this.duels = duels;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent playerJoinEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerJoinEvent.getPlayer().getUniqueId());
        duelPlayer.getHandle().teleport(this.duels.getGameSystem().getLobby().getLobbyLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        playerJoinEvent.joinMessage(this.duels.getMessages().getLine("system.join", duelPlayer.getSerializedDisplayName()));
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent playerQuitEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerQuitEvent.getPlayer().getUniqueId());
        playerQuitEvent.quitMessage(this.duels.getMessages().getLine("system.quit", duelPlayer.getSerializedDisplayName()));
        if (duelPlayer.isFighting()) {
            final Battle battle = this.duels.getGameSystem().getBattleManager().getBattleWith(duelPlayer);
            if (battle.getGameState() == GameState.END) return;
            final DuelPlayer winner = battle.getRedPlayer().equals(duelPlayer) ? battle.getBluePlayer() : battle.getRedPlayer();
            battle.setWinner(winner);
            battle.setGameState(GameState.END);
        }
    }

    @EventHandler
    public void onWarriorDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerDeathEvent.getPlayer().getUniqueId());
        if (duelPlayer.isFighting()) {
            final Battle battle = this.duels.getGameSystem().getBattleManager().getBattleWith(duelPlayer);
            if (battle.getGameState() == GameState.END) return;
            final DuelPlayer winner = battle.getRedPlayer().equals(duelPlayer) ? battle.getBluePlayer() : battle.getRedPlayer();
            battle.setWinner(winner);
            battle.setGameState(GameState.END);
        }
    }
}
