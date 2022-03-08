package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class ComboStarterEvent implements MultiplayerHcEvent{
    @Override
    public Text getName() {
        return Text.of("Combo Starter");
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

    @Override
    public void onDamage(LivingEntity livingEntity, DamageSource source, float v) {
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,60,1));
    }
}
