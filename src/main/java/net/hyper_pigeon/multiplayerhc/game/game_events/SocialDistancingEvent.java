package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class SocialDistancingEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Social Distancing");
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
        return 1200;
    }

    public void tickEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            if (gamePlayer.getEntityWorld().isPlayerInRange(gamePlayer.getX(), gamePlayer.getY(), gamePlayer.getZ(),6)){
                gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,200));
            }
        });
    }
}
