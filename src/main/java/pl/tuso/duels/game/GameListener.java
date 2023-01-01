package pl.tuso.duels.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Battle;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.GameState;

public class GameListener implements Listener { // TODO interactions??
    private final Duels duels;

    public GameListener(Duels duels) {
        this.duels = duels;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent playerJoinEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerJoinEvent.getPlayer().getUniqueId());
        duelPlayer.getHandle().teleport(this.duels.getGameSystem().getLobby().getLobbyLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        duelPlayer.getHandle().getInventory().clear();
        this.duels.getServer().getOnlinePlayers().forEach(player -> {
            final DuelPlayer other = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(player.getUniqueId());
            if (other.isFighting()) other.getHandle().hidePlayer(this.duels, duelPlayer.getHandle());
        });
        playerJoinEvent.joinMessage(this.duels.getMessages().getLine("system.join", duelPlayer.getSerializedDisplayName()));
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent playerQuitEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerQuitEvent.getPlayer().getUniqueId());
        playerQuitEvent.quitMessage(this.duels.getMessages().getLine("system.quit", duelPlayer.getSerializedDisplayName()));
        if (!duelPlayer.isFighting()) return;
        final Battle battle = this.duels.getGameSystem().getBattleManager().getBattleWith(duelPlayer);
        if (battle.getGameState() == GameState.END) return;
        final DuelPlayer winner = battle.getRedPlayer().equals(duelPlayer) ? battle.getBluePlayer() : battle.getRedPlayer();
        battle.setWinner(winner);
        battle.setGameState(GameState.END);
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent playerMoveEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerMoveEvent.getPlayer().getUniqueId());
        if (!duelPlayer.isFighting()) return;
        final Battle battle = this.duels.getGameSystem().getBattleManager().getBattleWith(duelPlayer);
        if (battle.getGameState() != GameState.STARTING) return;
        playerMoveEvent.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerDeathEvent.getPlayer().getUniqueId());
        if (!duelPlayer.isFighting()) return;
        final Battle battle = this.duels.getGameSystem().getBattleManager().getBattleWith(duelPlayer);
        if (battle.getGameState() == GameState.END) return;
        final DuelPlayer winner = battle.getRedPlayer().equals(duelPlayer) ? battle.getBluePlayer() : battle.getRedPlayer();
        battle.setWinner(winner);
        battle.setGameState(GameState.END);
        playerDeathEvent.deathMessage(null);
    }

    @EventHandler
    public void onPlayerRespawn(@NotNull PlayerRespawnEvent playerRespawnEvent) {
        playerRespawnEvent.setRespawnLocation(this.duels.getGameSystem().getLobby().getLobbyLocation());
    }

    @EventHandler
    public void onPlayerHit(@NotNull EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getDamager() instanceof Player damager && entityDamageByEntityEvent.getEntity() instanceof Player player) {
            final DuelPlayer damagerDuelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(damager.getUniqueId());
            if (!damagerDuelPlayer.isFighting() || !this.duels.getGameSystem().getBattleManager().getBattleWith(damagerDuelPlayer).getPlayers().contains(this.duels.getGameSystem().getPlayerManager().getDuelPlayer(player.getUniqueId()))) entityDamageByEntityEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(@NotNull BlockBreakEvent blockBreakEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(blockBreakEvent.getPlayer().getUniqueId());
        if (duelPlayer.isFighting()) blockBreakEvent.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(@NotNull BlockPlaceEvent blockPlaceEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(blockPlaceEvent.getPlayer().getUniqueId());
        if (duelPlayer.isFighting()) blockPlaceEvent.setCancelled(true);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent playerInteractEvent) {
        final DuelPlayer duelPlayer = this.duels.getGameSystem().getPlayerManager().getDuelPlayer(playerInteractEvent.getPlayer().getUniqueId());
        if (duelPlayer.isFighting()) playerInteractEvent.setCancelled(true);
    }
}
