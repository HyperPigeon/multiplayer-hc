package net.hyper_pigeon.multiplayerhc.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SCPOneSevenThreeEntity extends CreeperEntity implements PolymerEntity {

    private boolean isCharged;
    private boolean hasName;

    public SCPOneSevenThreeEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    boolean isPlayerStaring(PlayerEntity player) {
        Vec3d vec3d = player.getRotationVec(1.0f).normalize();
        Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
        double d = vec3d2.length();
        double e = vec3d.dotProduct(vec3d2 = vec3d2.normalize());
        if (e > 1.0 - 0.025 / d) {
            return player.canSee(this);
        }
        return false;
    }

    public static DefaultAttributeContainer.Builder createSCPOneSevenThreeAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 3.5).add(EntityAttributes.GENERIC_MAX_HEALTH, 400);
    }

    public void tick(){
        if(!hasName){
            setCustomName(Text.of("SCP 173"));
            hasName = true;
        }

        if(this.isAlive() && getTarget() instanceof PlayerEntity && isPlayerStaring((PlayerEntity) getTarget())){
            addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 10));
        }
        super.tick();
    }


    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.CREEPER;
    }
}
