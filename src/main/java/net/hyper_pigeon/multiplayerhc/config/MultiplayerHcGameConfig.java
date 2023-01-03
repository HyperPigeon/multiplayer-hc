package net.hyper_pigeon.multiplayerhc.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.dimension.DimensionOptions;

public record MultiplayerHcGameConfig(String title, String mode, DimensionOptions overworld, DimensionOptions nether, DimensionOptions end) {
    public static final Codec<MultiplayerHcGameConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.STRING.fieldOf("title").forGetter(MultiplayerHcGameConfig::title),
                Codec.STRING.fieldOf("mode").forGetter(MultiplayerHcGameConfig::mode),
                DimensionOptions.CODEC.fieldOf("overworld_dimension").forGetter(MultiplayerHcGameConfig::overworld),
                DimensionOptions.CODEC.fieldOf("nether_dimension").forGetter(MultiplayerHcGameConfig::nether),
                DimensionOptions.CODEC.fieldOf("end_dimension").forGetter(MultiplayerHcGameConfig::end)
        ).apply(instance, MultiplayerHcGameConfig::new);
    });
}
