package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.entity.AngryIronGolemEntity;
import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.hyper_pigeon.multiplayerhc.registry.MultiplayerHcEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WrathOfMekhaneEvent implements MultiplayerHcEvent{

    public boolean isPermanent = false;
    private final long duration = 6000;
    private final double tickProbability = 0.0005;
    private static final long COOLDOWN = 200;
    private long nextUseTime = 0;



    @Override
    public Text getName() {
        return Text.of("Wrath of MEKHANE");
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

    public boolean isAbilityOffCooldown(MultiplayerHcGame game){
        return game.world.getTime() >= nextUseTime;
    }

    @Override
    public void startEvent(MultiplayerHcGame game) {
        game.world.setWeather(0,6000,true,true);
    }

    @Override
    public void tickEvent(MultiplayerHcGame game) {
        if(isAbilityOffCooldown(game) && (tickProbability >= MultiplayerHcEvent.random.nextFloat())){

            int size = game.gameSpace.getPlayers().size();
            ServerPlayerEntity player = game.gameSpace.getPlayers().stream().skip(random.nextInt(size)).findFirst().get();

            if(player.isTouchingWaterOrRain() && !player.isTouchingWater()) {
                BlockPos blockPos = player.getBlockPos().add(player.getBlockPos().getX()+random.nextInt(30 + 30) - 30
                        ,0,player.getBlockPos().getZ()+random.nextInt(30 + 30) - 30);

                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(game.world);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                lightningEntity.setCosmetic(true);
                game.world.spawnEntity(lightningEntity);
            }



            nextUseTime = game.world.getTime() + COOLDOWN;
        }
    }

    @Override
    public void onEntitySpawn(Entity entity) {
        if(entity instanceof LightningEntity){
            BlockPos blockPos = entity.getBlockPos();
            AngryIronGolemEntity angryIronGolemEntity = MultiplayerHcEntities.ANGRY_IRON_GOLEM_ENTITY.create(entity.getEntityWorld());
            angryIronGolemEntity.refreshPositionAndAngles(blockPos,0,0);

            AngryIronGolemEntity angryIronGolemEntity2 = MultiplayerHcEntities.ANGRY_IRON_GOLEM_ENTITY.create(entity.getEntityWorld());
            angryIronGolemEntity2.refreshPositionAndAngles(blockPos,0,0);

            AngryIronGolemEntity angryIronGolemEntity3 = MultiplayerHcEntities.ANGRY_IRON_GOLEM_ENTITY.create(entity.getEntityWorld());
            angryIronGolemEntity3.refreshPositionAndAngles(blockPos,0,0);

            entity.world.spawnEntity(angryIronGolemEntity);
            entity.world.spawnEntity(angryIronGolemEntity2);
            entity.world.spawnEntity(angryIronGolemEntity3);

            angryIronGolemEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,6000,0));
            angryIronGolemEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,6000,0));
            angryIronGolemEntity3.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,6000,0));

            angryIronGolemEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,6000,0));
            angryIronGolemEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,6000,0));
            angryIronGolemEntity3.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,6000,0));

            angryIronGolemEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,6000,0));
            angryIronGolemEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,6000,0));
            angryIronGolemEntity3.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,6000,0));
        }
    }

}
