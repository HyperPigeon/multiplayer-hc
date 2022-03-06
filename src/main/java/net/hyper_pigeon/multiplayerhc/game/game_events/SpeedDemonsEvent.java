package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class SpeedDemonsEvent implements MultiplayerHcEvent{

    public final long duration = 2400;
    private boolean isPermanent = false;

    @Override
    public Text getName() {
        return Text.of("Speed Demons");
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
            gamePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) duration, 4));
        });
    }

    @Override
    public void onEntitySpawn(Entity entity) {
        if(entity instanceof LivingEntity){
            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) duration, 4));
        }
    }
}
