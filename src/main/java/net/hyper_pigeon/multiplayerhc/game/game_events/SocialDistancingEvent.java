package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

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
            List<ServerPlayerEntity> players = gamePlayer.getEntityWorld().getEntitiesByClass(ServerPlayerEntity.class,gamePlayer.getBoundingBox().expand(6), playerEntity -> {return true;});
            if (players.size() >= 2){
                gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,200));
            }
        });
    }
}
