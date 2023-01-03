package net.hyper_pigeon.multiplayerhc.mixin;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

import java.util.Set;
import java.util.function.Predicate;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity {

    private final ServerBossBar serverBossBar = (ServerBossBar) new ServerBossBar(getDisplayName(),  BossBar.Color.PINK, BossBar.Style.PROGRESS)
            .setDragonMusic(true)
            .setThickenFog(true);


    private static final Predicate<Entity> VALID_ENTITY = EntityPredicates.VALID_ENTITY.and(EntityPredicates.maxDistance(0.0, 128.0, 0.0, 192.0));

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

   public void tick(){
       super.tick();

       var gameSpace = GameSpaceManager.get().byWorld(world);

       if(gameSpace != null){
           this.serverBossBar.setVisible(isAlive());
           this.serverBossBar.setPercent(this.getHealth() / this.getMaxHealth());
           updatePlayers();
       }
   }

    private void updatePlayers() {
        if(!this.world.isClient) {
            Set<ServerPlayerEntity> set = Sets.newHashSet();

            for(ServerPlayerEntity serverPlayerEntity : ((ServerWorld)this.getEntityWorld()).getPlayers(VALID_ENTITY)) {
                this.serverBossBar.addPlayer(serverPlayerEntity);
                set.add(serverPlayerEntity);
            }

            Set<ServerPlayerEntity> set2 = Sets.newHashSet(this.serverBossBar.getPlayers());
            set2.removeAll(set);

            for(ServerPlayerEntity serverPlayerEntity2 : set2) {
                this.serverBossBar.removePlayer(serverPlayerEntity2);
            }
        }
    }
}
