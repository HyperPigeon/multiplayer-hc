package net.hyper_pigeon.multiplayerhc.mixin;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

import java.util.Optional;
import java.util.OptionalLong;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

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

    @Inject(method = "onBlockAdded",at = {@At("HEAD")})
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci){
        var gameSpace = GameSpaceManager.get().byWorld(world);
        if(gameSpace != null && (world.getDimension().equals(THE_OVERWORLD) || world.getDimension().equals(THE_NETHER))){
            Optional<NetherPortal> optional;
            if ((optional = NetherPortal.getNewPortal(world, pos, Direction.Axis.X)).isPresent()) {
                optional.get().createPortal();
            }
        }


    }

}
