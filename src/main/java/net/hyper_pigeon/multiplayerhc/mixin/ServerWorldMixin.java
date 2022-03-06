package net.hyper_pigeon.multiplayerhc.mixin;

import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGame;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow
    @Final
    @Mutable
    @Nullable
    private EnderDragonFight enderDragonFight;

    @Shadow @NotNull
    public abstract MinecraftServer getServer();

    @Shadow public abstract ServerWorld toServerWorld();

    public MultiplayerHcGame getGame(GameSpace gameSpace){
        for(MultiplayerHcGame game : MultiplayerHcGame.runningGames){
            if(game.gameSpace.equals(gameSpace)){
                return game;
            }
        }
        return null;
    }


    @Inject(
            at = @At("TAIL"),
            method = "<init>"
    )
    public void constructorMixin(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey worldKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long seed, List spawners, boolean shouldTickTime, CallbackInfo ci) {
        var gameSpace = GameSpaceManager.get().byWorld(toServerWorld());
        if(gameSpace != null){
                MultiplayerHcGame game = getGame(gameSpace);
                if(game.end.equals(toServerWorld())){
                    System.out.println("fight check");
                    this.enderDragonFight = new EnderDragonFight(((ServerWorld) (Object) this), this.getServer().getSaveProperties().getGeneratorOptions().getSeed(), this.getServer().getSaveProperties().getDragonFight());
                }
        }

    }
}
