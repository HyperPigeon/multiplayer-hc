package net.hyper_pigeon.multiplayerhc.mixin;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.AreaHelper;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

import java.util.Optional;
import java.util.OptionalLong;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    public DimensionType THE_NETHER = DimensionType.create(OptionalLong.of(18000L), false, true, true, false, 8.0, false, true, false, true, false, 0, 256, 128, BlockTags.INFINIBURN_NETHER.getId(), DimensionType.THE_NETHER_ID, 0.1f);
    public DimensionType THE_OVERWORLD = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0, false, false, true, false, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD.getId(), DimensionType.OVERWORLD_ID, 0.0f);
    public DimensionType THE_END = DimensionType.create(OptionalLong.of(6000L), false, false, false, false, 1.0, true, false, false, false, true, 0, 256, 256, BlockTags.INFINIBURN_END.getId(), DimensionType.THE_END_ID, 0.0f);

    @Inject(method = "onBlockAdded",at = {@At("HEAD")})
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci){
        var gameSpace = GameSpaceManager.get().byWorld(world);

        if(gameSpace != null && (world.getDimension().equals(THE_OVERWORLD) || world.getDimension().equals(THE_NETHER))){
            Optional<AreaHelper> optional;
            if ((optional = AreaHelper.getNewPortal(world, pos, Direction.Axis.X)).isPresent()) {
                optional.get().createPortal();
                return;
            }
        }


    }

}
