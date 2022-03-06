package net.hyper_pigeon.multiplayerhc.game;

import net.hyper_pigeon.multiplayerhc.config.MultiplayerHcGameConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.map_templates.MapTemplate;
import xyz.nucleoid.plasmid.game.GameOpenContext;
import xyz.nucleoid.plasmid.game.GameOpenProcedure;
import xyz.nucleoid.plasmid.game.GameResult;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.common.GameWaitingLobby;
import xyz.nucleoid.plasmid.game.common.config.PlayerConfig;
import xyz.nucleoid.plasmid.game.event.GameActivityEvents;
import xyz.nucleoid.plasmid.game.event.GamePlayerEvents;
import xyz.nucleoid.plasmid.game.player.PlayerOffer;
import xyz.nucleoid.plasmid.game.player.PlayerOfferResult;
import xyz.nucleoid.plasmid.game.rule.GameRuleType;
import xyz.nucleoid.plasmid.game.world.generator.TemplateChunkGenerator;
import xyz.nucleoid.stimuli.event.player.PlayerDeathEvent;

public class MultiplayerHcGameWaiting {
    private final MultiplayerHcGameConfig config;
    private final GameSpace gameSpace;
    private final ServerWorld world;
    private final GameOpenContext<MultiplayerHcGameConfig> context;

    public MultiplayerHcGameWaiting(MultiplayerHcGameConfig config, GameSpace gameSpace, ServerWorld world, GameOpenContext<MultiplayerHcGameConfig> context) {
        this.config = config;
        this.gameSpace = gameSpace;
        this.world = world;
        this.context = context;
    }

    public static GameOpenProcedure open(GameOpenContext<MultiplayerHcGameConfig> context) {
        MultiplayerHcGameConfig config = context.config();

        MapTemplate template = MapTemplate.createEmpty();

        for(int i = -30; i <= 30; i++){
            for(int k = -30; k <30;k++){
                template.setBlockState(new BlockPos(i, 64, k), Blocks.BEDROCK.getDefaultState());
            }
        }

        //set walls quad I

        TemplateChunkGenerator generator = new TemplateChunkGenerator(context.server(), template);

        RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
                .setGenerator(generator)
                .setTimeOfDay(6000)
                .setGameRule(GameRules.DO_IMMEDIATE_RESPAWN,true);

        PlayerConfig.Countdown countdown = new PlayerConfig.Countdown(30,30);

        return context.openWithWorld(worldConfig, (activity, world) -> {
            MultiplayerHcGameWaiting waiting = new MultiplayerHcGameWaiting(config, activity.getGameSpace(), world, context);

            GameWaitingLobby.addTo(activity, new PlayerConfig(1, 20, 20, countdown));

            activity.deny(GameRuleType.PVP);
            activity.listen(GamePlayerEvents.OFFER, waiting::onPlayerOffer);
            activity.listen(GamePlayerEvents.ADD, waiting::onPlayerAdd);
            activity.listen(PlayerDeathEvent.EVENT, waiting::onPlayerDeath);
            activity.listen(GameActivityEvents.REQUEST_START, waiting::requestStart);
        });

    }

    private ActionResult onPlayerDeath(ServerPlayerEntity serverPlayerEntity, DamageSource source) {
        serverPlayerEntity.setHealth(20.0F);
        serverPlayerEntity.requestTeleport(0,66,0);
        return ActionResult.SUCCESS;
    }

    private GameResult requestStart() {
        MultiplayerHcGame.open(this.gameSpace,this.config);
        return GameResult.ok();
    }

    private void onPlayerAdd(ServerPlayerEntity serverPlayerEntity) {
        LiteralText message = new LiteralText(this.config.greeting());
        this.gameSpace.getPlayers().sendMessage(message);
    }

    private PlayerOfferResult onPlayerOffer(PlayerOffer playerOffer) {
        ServerPlayerEntity player = playerOffer.player();
        return playerOffer.accept(this.world, new Vec3d(0.0, 66.0, 0.0))
                .and(() -> {
                    player.changeGameMode(GameMode.ADVENTURE);
                });
    }

}
