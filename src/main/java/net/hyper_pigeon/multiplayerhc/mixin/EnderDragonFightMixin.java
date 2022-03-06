package net.hyper_pigeon.multiplayerhc.mixin;

import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {

    @Shadow
    private EnderDragonSpawnState dragonSpawnState;

    @Inject(
            at = @At("TAIL"),
            method = "<init>"
    )
    public void constructorMixin(ServerWorld world, long gatewaysSeed, NbtCompound nbt, CallbackInfo ci){
        var gameSpace = GameSpaceManager.get().byWorld(world);
        if(gameSpace != null){
            this.dragonSpawnState = EnderDragonSpawnState.END;
        }
    }
}
