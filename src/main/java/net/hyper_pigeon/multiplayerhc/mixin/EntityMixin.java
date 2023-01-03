package net.hyper_pigeon.multiplayerhc.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

import java.util.Iterator;
import java.util.OptionalLong;

@Mixin(Entity.class)
public class EntityMixin {

    public DimensionType THE_NETHER = new DimensionType(
            OptionalLong.of(18000L),
            false,
            true,
            true,
            false,
            8.0,
            false,
            true,
            0,
            256,
            128,
            BlockTags.INFINIBURN_NETHER,
            DimensionTypes.THE_NETHER_ID,
            0.1F,
            new DimensionType.MonsterSettings(true, false, ConstantIntProvider.create(7), 15)
    );
    public DimensionType THE_OVERWORLD = new DimensionType(
            OptionalLong.empty(),
            true,
            false,
            false,
            true,
            1.0,
            true,
            false,
            -64,
            384,
            384,
            BlockTags.INFINIBURN_OVERWORLD,
            DimensionTypes.OVERWORLD_ID,
            0.0F,
            new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)
    );
    public DimensionType THE_END =
            new DimensionType(
                    OptionalLong.of(6000L),
                    false,
                    false,
                    false,
                    false,
                    1.0,
                    false,
                    false,
                    0,
                    256,
                    256,
                    BlockTags.INFINIBURN_END,
                    DimensionTypes.THE_END_ID,
                    0.0F,
                    new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)
            );

    @Shadow
    public World world;

    @Shadow
    protected boolean inNetherPortal;

    @Shadow
    public native int getMaxNetherPortalTime();

    @Shadow
    public native boolean hasVehicle();

    @Shadow
    protected int netherPortalTime;

    @Shadow
    public native void resetPortalCooldown();

    @Shadow
    public native Entity moveToWorld(ServerWorld destination);

    @Shadow
    protected native void tickPortalCooldown();


    @Inject(method = "tickPortal",at = @At("HEAD"))
    public void tickNetherPortal(CallbackInfo ci){
        var gameSpace = GameSpaceManager.get().byWorld(world);

        if(gameSpace != null){
            if (!(this.world instanceof ServerWorld)) {
                return;
            }
            int i = this.getMaxNetherPortalTime();
            ServerWorld serverWorld = (ServerWorld)this.world;
            if (this.inNetherPortal) {
                RegistryKey<World> registryKey;
                MinecraftServer minecraftServer = serverWorld.getServer();

                Iterator<ServerWorld> serverWorldIterator = gameSpace.getWorlds().iterator();

                ServerWorld serverWorld2 = null; 
                while(serverWorldIterator.hasNext()){
                  serverWorld2 = serverWorldIterator.next();
                    if(world.getDimension().equals(THE_OVERWORLD) && serverWorld2.getDimension().equals(THE_NETHER)){
                         break;
                    }
                    else if(world.getDimension().equals(THE_NETHER) && serverWorld2.getDimension().equals(THE_OVERWORLD)){
                        break;
                    }
                }

                if (serverWorld2 != null) {
                    this.world.getProfiler().push("portal");
                    this.netherPortalTime = i;
                    this.resetPortalCooldown();
                    this.moveToWorld(serverWorld2);
                    this.world.getProfiler().pop();
                }
                this.inNetherPortal = false;
            }
            this.tickPortalCooldown();
        }

    }

}
