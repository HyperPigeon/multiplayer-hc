package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class DirtInYourEyeEvent implements MultiplayerHcEvent{

    public final long duration = 1200;

    @Override
    public Text getName() {
        return null;
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
    public void onDamage(LivingEntity livingEntity, DamageSource source, float v) {
        if(livingEntity instanceof ServerPlayerEntity && source.getAttacker() != null){
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200));
        }
    }
}
