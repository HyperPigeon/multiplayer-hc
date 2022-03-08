package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;

public class LifestealEvent implements MultiplayerHcEvent{
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
        return 2400;
    }

    @Override
    public void onDamage(LivingEntity livingEntity, DamageSource source, float v) {
        if(livingEntity.getAttacker() != null){
            livingEntity.getAttacker().heal(v);
        }
    }
}
