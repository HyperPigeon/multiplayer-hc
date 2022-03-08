package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;

public class TotalMayhemEvent implements MultiplayerHcEvent{

    private boolean isPermanent = false;
    private final long duration = 2400;

    @Override
    public Text getName() {
        return Text.of("Total Mayhem");
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
    public void onEntityDeath(LivingEntity livingEntity, DamageSource source) {
        BlockPos blockPos = livingEntity.getBlockPos();
        if(blockPos != null){
            livingEntity.getEntityWorld().createExplosion(livingEntity, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0f, Explosion.DestructionType.NONE);
        }
    }
}
