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
        return 2750;
    }

    public void tickEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            if (gamePlayer.getEntityWorld().getClosestPlayer(gamePlayer,6) != null &&
                    gamePlayer.getEntityWorld().getClosestPlayer(gamePlayer,6) != gamePlayer){
                gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,200));
            }
        });
    }
}
