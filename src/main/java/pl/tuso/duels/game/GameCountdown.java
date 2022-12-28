package pl.tuso.duels.game;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.bukkit.scheduler.BukkitRunnable;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Battle;
import pl.tuso.duels.api.Countdown;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.GameState;

import java.time.Duration;
import java.util.List;

public class GameCountdown implements Countdown {
    private final Duels duels;
    private final Battle battle;
    private final int time;
    private final List<DuelPlayer> audience;
    private final BukkitRunnable timer;
    private boolean running;

    public GameCountdown(Duels duels, Battle battle, int time, DuelPlayer... audience) {
        this.duels = duels;
        this.battle = battle;
        this.time = time;
        this.audience = List.of(audience);
        this.timer = new BukkitRunnable() {
            private int currentTime = GameCountdown.this.time;

            @Override
            public void run() {
                GameCountdown.this.count(this.currentTime);
                if (this.currentTime <= 0) GameCountdown.this.stop();
                this.currentTime--;
            }
        };
        this.running = false;
    }

    @Override
    public void start() {
        if (this.running) return;
        this.running = true;
        this.timer.runTaskTimerAsynchronously(this.duels, 0, 20);

    }

    @Override
    public void stop() {
        if (!this.running) return;
        this.running = false;
        this.timer.cancel();
        this.battle.setGameState(GameState.FIGHT);
    }

    @Override
    public List<DuelPlayer> getAudience() {
        return this.audience;
    }

    private void count(int time) {
        this.audience.forEach(duelPlayer -> {
            if (time > 5) {
                if (time % 10 == 0) {
                    duelPlayer.getHandle().sendMessage(this.duels.getMessages().getLine("battle.countdown.chat", time));
                    duelPlayer.getHandle().playSound(Sound.sound(Key.key("minecraft:block.note_block.guitar"), Sound.Source.MASTER, 100.0F, 0.0F));
                }
            } else {
                duelPlayer.getHandle().sendMessage(this.duels.getMessages().getLine("battle.countdown.chat", time));
                duelPlayer.getHandle().showTitle(Title.title(
                        this.duels.getMessages().getLine("battle.countdown.title", time),
                        this.duels.getMessages().getLine("battle.countdown.subtitle", time),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
                duelPlayer.getHandle().playSound(Sound.sound(Key.key("minecraft:block.note_block.guitar"), Sound.Source.MASTER, 100.0F, 1.0F));
            }
        });
    }
}
