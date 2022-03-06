package net.hyper_pigeon.multiplayerhc.game.game_events;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class PhantomMenaceEvent implements MultiplayerHcEvent{


    @Override
    public Text getName() {
        return Text.of("The Phantom Menace");
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
        return 0;
    }

    @Override
    public void startEvent(MultiplayerHcGame game) {
        game.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            BlockPos blockPos = gamePlayer.getBlockPos().add(0,10,0);
            ServerWorld world = gamePlayer.getWorld();

            PhantomEntity phantomEntity1 = EntityType.PHANTOM.create(world);
            phantomEntity1.refreshPositionAndAngles(blockPos,0,0);
            SkeletonEntity skeletonEntity = EntityType.SKELETON.create(world);
            skeletonEntity.setCanPickUpLoot(true);
            skeletonEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            skeletonEntity.refreshPositionAndAngles(blockPos,0,0);


            world.spawnEntity(phantomEntity1);
            world.spawnEntity(skeletonEntity);

            skeletonEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,1200,0));
            skeletonEntity.startRiding(phantomEntity1);



            PhantomEntity phantomEntity2 = EntityType.PHANTOM.create(world);
            phantomEntity2.refreshPositionAndAngles(blockPos,0,0);
            SkeletonEntity skeletonEntity2 = EntityType.SKELETON.create(world);
            skeletonEntity2.setCanPickUpLoot(true);
            skeletonEntity2.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            skeletonEntity2.refreshPositionAndAngles(blockPos,0,0);


            world.spawnEntity(phantomEntity2);
            world.spawnEntity(skeletonEntity2);

            skeletonEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,1200,0));
            skeletonEntity2.startRiding(phantomEntity2);


            PhantomEntity phantomEntity3 = EntityType.PHANTOM.create(world);
            phantomEntity3.refreshPositionAndAngles(blockPos,0,0);
            WitchEntity witchEntity = EntityType.WITCH.create(world);
            witchEntity.refreshPositionAndAngles(blockPos,0,0);

            world.spawnEntity(phantomEntity3);
            world.spawnEntity(witchEntity);

            witchEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,1200,0));
            witchEntity.startRiding(phantomEntity3);

        });
    }

}
