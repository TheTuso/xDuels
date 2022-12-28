package pl.tuso.duels.game;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GameBattleManager implements BattleManager {
    private final Duels duels;
    private final List<Battle> battles;
    private final Random picker;

    public GameBattleManager(Duels duels) {
        this.duels = duels;
        this.battles = new ArrayList<>();
        this.picker = new Random();
    }

    @Override
    public boolean createBattle(@NotNull DuelPlayer red, @NotNull DuelPlayer blue, @NotNull Kit kit) {
        if (red.isFighting()) {
            this.duels.getLogger().warning(String.format("%s is currently in a battle!", red.getHandle().getName()));
            return false;
        }
        if (blue.isFighting()) {
            this.duels.getLogger().warning(String.format("%s is currently in a battle!", blue.getHandle().getName()));
            return false;
        }
        if (this.battles.stream().anyMatch(battle -> battle.getPlayers().contains(red) || battle.getPlayers().contains(blue))) {
            this.duels.getLogger().warning(String.format("There is already a battle with these players (%s, %s)!", red.getHandle().getName(), blue.getHandle().getName()));
            return false;
        }
        if (this.duels.getGameSystem().getArenaManager().getArenas().isEmpty()) {
            this.duels.getLogger().warning("Please create an arena first!");
            return false;
        }
        return this.battles.add(new GameBattle(this.duels, this.pickArena(), kit, red, blue));
    }

    @Override
    public @Nullable Battle getBattleWith(DuelPlayer duelPlayer) {
        final Optional<Battle> optionalBattle = this.battles.stream().filter(battle -> battle.getPlayers().contains(duelPlayer)).findAny();
        return optionalBattle.isPresent() ? optionalBattle.get() : null;
    }

    @Override
    public List<Battle> getBattles() {
        return this.battles;
    }

    private Arena pickArena() {
        final List<Arena> arenas = this.duels.getGameSystem().getArenaManager().getArenas();
        final Arena picked = arenas.get(this.picker.nextInt(arenas.size()));
        Preconditions.checkNotNull(picked, "Arena cannot be null!");
        return picked;
    }
}
