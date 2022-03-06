package net.hyper_pigeon.multiplayerhc.mixin;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin{



    public MultiplayerHcGame getGame(GameSpace gameSpace){
        for(MultiplayerHcGame game : MultiplayerHcGame.runningGames){
            if(game.gameSpace.equals(gameSpace)){
                return game;
            }
        }
        return null;
    }

    @Inject(at = @At("HEAD"), method = "onEntityCollision")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci){
        //System.out.println("CHECK");
        var gameSpace = GameSpaceManager.get().byWorld(world);
        if(gameSpace != null && world instanceof ServerWorld && !entity.hasVehicle() &&
                !entity.hasPassengers() && entity.canUsePortals() &&
                VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ())), state.getOutlineShape(world, pos), BooleanBiFunction.AND)){
                ServerWorld end = getGame(gameSpace).end;
                TeleportTarget teleportTarget = getTeleportTarget(entity);
                end.setSpawnPos(ServerWorld.END_SPAWN_POS,0);
                ServerWorld.createEndSpawnPlatform(end);
                FabricDimensions.teleport(entity,end,teleportTarget);


//            if (end == null) {
//                return;
//            }
//            this.moveToWorld(entity, (ServerWorld) world, end);
//            ci.cancel();
        }
    }

    public TeleportTarget getTeleportTarget(Entity entity) {
        BlockPos blockPos = ServerWorld.END_SPAWN_POS;
        return new TeleportTarget(new Vec3d((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5), entity.getVelocity(), entity.getYaw(), entity.getPitch());
    }

//    public Entity moveToWorld(Entity entity, ServerWorld world,ServerWorld destination){
//        if (world == null || entity.isRemoved()) {
//            return null;
//        }
//        world.getProfiler().push("changeDimension");
//        entity.detach();
//        world.getProfiler().push("reposition");
//        TeleportTarget teleportTarget = this.getTeleportTarget(entity);
//        if (teleportTarget == null) {
//            return null;
//        }
//        world.getProfiler().swap("reloading");
//        Object entityCopy = entity.getType().create(destination);
//        if (entityCopy != null) {
//            ((Entity)entityCopy).copyFrom(entity);
//            ((Entity)entityCopy).refreshPositionAndAngles(teleportTarget.position.x, teleportTarget.position.y, teleportTarget.position.z, teleportTarget.yaw, ((Entity)entity).getPitch());
//            ((Entity)entityCopy).setVelocity(teleportTarget.velocity);
//            destination.onDimensionChanged((Entity)entity);
//            if (destination.getRegistryKey() == World.END) {
//                ServerWorld.createEndSpawnPlatform(destination);
//            }
//        }
//        entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
//        world.getProfiler().pop();
//        ((ServerWorld)world).resetIdleTimeout();
//        destination.resetIdleTimeout();
//        world.getProfiler().pop();
//        return entity;
//    }

}