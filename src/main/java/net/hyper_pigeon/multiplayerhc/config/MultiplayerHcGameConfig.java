package net.hyper_pigeon.multiplayerhc.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MultiplayerHcGameConfig(String greeting) {
    public static final Codec<MultiplayerHcGameConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.STRING.fieldOf("greeting").forGetter(MultiplayerHcGameConfig::greeting)
        ).apply(instance, MultiplayerHcGameConfig::new);
    });
}
