package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class MoonGravityEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Moon Gravity");
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
        return 0;
    }

    @Override
    public void startEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST,6000,1));
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,6000));
        });
    }
}
