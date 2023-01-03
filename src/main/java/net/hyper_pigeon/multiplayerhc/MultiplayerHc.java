package net.hyper_pigeon.multiplayerhc;

import net.fabricmc.api.ModInitializer;
import net.hyper_pigeon.multiplayerhc.config.MultiplayerHcGameConfig;
import net.hyper_pigeon.multiplayerhc.game.MultiplayerHcGameWaiting;
import net.hyper_pigeon.multiplayerhc.registry.MultiplayerHcEntities;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.nucleoid.plasmid.game.GameType;

public class MultiplayerHc implements ModInitializer {

    public static final String ID = "multiplayerhc";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final GameType<MultiplayerHcGameConfig> TYPE = GameType.register(
            new Identifier(ID, "multiplayerhc"),
            MultiplayerHcGameConfig.CODEC,
            MultiplayerHcGameWaiting::open
    );

    @Override
    public void onInitialize() {
        MultiplayerHcEntities.init();
    }
}
