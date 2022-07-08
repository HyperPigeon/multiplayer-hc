package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class HarbingersEvent implements MultiplayerHcEvent{

    public final long duration = 6000;

    @Override
    public Text getName() {
        return Text.of("Harbingers");
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public void setPermanent(boolean isPer) {

    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void startEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer ->
        {
            gamePlayer.addStatusEffect((new StatusEffectInstance(StatusEffects.BAD_OMEN, (int) duration, 2)));
        });
    }

}
