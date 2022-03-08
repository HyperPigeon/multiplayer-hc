package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class DamageArrowsEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Damage Arrows");
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
        return 2400;
    }

    @Override
    public void onDamage(LivingEntity livingEntity, DamageSource source, float v) {
        if(source.isProjectile()){
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE));
        }
    }
}
