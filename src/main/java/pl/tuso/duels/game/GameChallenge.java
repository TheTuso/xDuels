package pl.tuso.duels.game;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import pl.tuso.duels.Duels;
import pl.tuso.duels.api.Challenge;
import pl.tuso.duels.api.DuelPlayer;
import pl.tuso.duels.api.Kit;

class GameChallenge implements Challenge {
    private final Duels duels;
    private final DuelPlayer sender;
    private final DuelPlayer receiver;
    private final Kit kit;

    public GameChallenge(Duels duels, DuelPlayer sender, DuelPlayer receiver, Kit kit) {
        this.duels = duels;
        this.sender = sender;
        this.receiver = receiver;
        this.kit = kit;
    }

    @Override
    public boolean accept() { //TODO accept system -> create game
        this.sender.getHandle().sendMessage(this.duels.getMessages().getLine("command.challenge.accepted.sender", this.sender.getSerializedDisplayName(), this.receiver.getSerializedDisplayName(), this.kit.getSerializedName()));
        this.receiver.getHandle().sendMessage(this.duels.getMessages().getLine("command.challenge.accepted.receiver", this.sender.getSerializedDisplayName(), this.receiver.getSerializedDisplayName(), this.kit.getSerializedName()));
        this.duels.getGameSystem().getChallengeManager().getChallenges().remove(this);
        return this.duels.getGameSystem().getBattleManager().createBattle(this.sender, this.receiver, this.kit);
    }

    @Override
    public boolean deny() {
        this.sender.getHandle().sendMessage(this.duels.getMessages().getLine("command.challenge.denied.sender", this.sender.getSerializedDisplayName(), this.receiver.getSerializedDisplayName(), this.kit.getSerializedName()));
        this.receiver.getHandle().sendMessage(this.duels.getMessages().getLine("command.challenge.denied.receiver", this.sender.getSerializedDisplayName(), this.receiver.getSerializedDisplayName(), this.kit.getSerializedName()));
        return this.duels.getGameSystem().getChallengeManager().getChallenges().remove(this);
    }

    @Override
    public void announce() {
        this.sender.getHandle().sendMessage(this.duels.getMessages().getLine("command.challenge.announce.sender",
                this.sender.getSerializedDisplayName(), this.receiver.getSerializedDisplayName(), this.kit.getSerializedName()));
        this.receiver.getHandle().sendMessage(this.duels.getMessages().getLine("command.challenge.announce.receiver",
                this.sender.getSerializedDisplayName(),
                this.receiver.getSerializedDisplayName(),
                this.kit.getSerializedName(),
                this.serializedAcceptButton(),
                this.serializedDenyButton()));
    }

    @Override
    public DuelPlayer getSender() {
        return this.sender;
    }

    @Override
    public DuelPlayer getReceiver() {
        return this.receiver;
    }

    @Override
    public Kit getKit() {
        return this.kit;
    }

    private @NotNull String serializedAcceptButton() {
        return MiniMessage.miniMessage().serialize(this.duels.getMessages().getLine("command.challenge.announce.button.accept.display")
                .hoverEvent(HoverEvent.showText(this.duels.getMessages().getLine("command.challenge.announce.button.accept.hover")))
                .clickEvent(ClickEvent.runCommand(String.format("/duels accept %s %s", this.sender.getHandle().getName(), this.kit.getPlainName()))));
    }

    private @NotNull String serializedDenyButton() {
        return MiniMessage.miniMessage().serialize(this.duels.getMessages().getLine("command.challenge.announce.button.deny.display")
                .hoverEvent(HoverEvent.showText(this.duels.getMessages().getLine("command.challenge.announce.button.deny.hover")))
                .clickEvent(ClickEvent.runCommand(String.format("/duels deny %s %s", this.sender.getHandle().getName(), this.kit.getPlainName()))));
    }
}
