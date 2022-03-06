package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class BlindIdiotGodEvent implements MultiplayerHcEvent{

    private boolean isPermanent = false;
    private final long duration = 600;

    @Override
    public Text getName() {
        return Text.of("Blind Idiot God");
    }

    @Override
    public boolean isPermanent() {
        return isPermanent;
    }

    @Override
    public void setPermanent(boolean isPer) {
        isPermanent = isPer;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void startEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, (int) duration, 1));
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, (int) duration, 3));
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, (int) duration, 4));
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, (int) duration, 4));
        });
    }

    @Override
    public void tickEvent(MultiplayerHcGame game) {
    }
}
