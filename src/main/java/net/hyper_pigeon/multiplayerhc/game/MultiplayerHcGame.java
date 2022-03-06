package net.hyper_pigeon.multiplayerhc.game;

import net.hyper_pigeon.multiplayerhc.config.MultiplayerHcGameConfig;
import net.hyper_pigeon.multiplayerhc.game.game_events.MultiplayerHcEvent;
import net.hyper_pigeon.multiplayerhc.game.game_events.MultiplayerHcGameEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.quiltservertools.interdimensional.portals.CustomPortalApiRegistry;
import net.quiltservertools.interdimensional.portals.api.CustomPortalBuilder;
import net.quiltservertools.interdimensional.portals.portal.PortalIgnitionSource;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.plasmid.game.GameCloseReason;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.common.widget.BossBarWidget;
import xyz.nucleoid.plasmid.game.event.GameActivityEvents;
import xyz.nucleoid.plasmid.game.event.GamePlayerEvents;
import xyz.nucleoid.plasmid.game.rule.GameRuleType;
import xyz.nucleoid.stimuli.event.entity.EntityDeathEvent;
import xyz.nucleoid.stimuli.event.entity.EntitySpawnEvent;
import xyz.nucleoid.stimuli.event.player.PlayerDeathEvent;

import java.util.ArrayList;

public class MultiplayerHcGame {

    private final MultiplayerHcGameConfig config;
    public final GameSpace gameSpace;
    public final ServerWorld world;
    public final ServerWorld nether;
    public final ServerWorld end;


    public final BossBarWidget timerBar;
    public GamePhase gamePhase;
    public long finishTime;
    public boolean hasWon;

    public final MultiplayerHcEventManager multiplayerHcEventManager;
    private final double eventProbablity = 0.001;
    private static final long EVENT_COOLDOWN = 1200;
    private long nextEventAddTime = 0;

    public static final ArrayList<MultiplayerHcGame> runningGames = new ArrayList<>();


    public MultiplayerHcGame( MultiplayerHcGameConfig config, GameSpace gameSpace, ServerWorld world, ServerWorld nether, ServerWorld end) {
        this.config = config;
        this.gameSpace = gameSpace;
        this.world = world;
        this.nether = nether;
        this.end = end;

        timerBar = new BossBarWidget(Text.of(""), BossBar.Color.YELLOW, BossBar.Style.NOTCHED_12);
        this.gamePhase = GamePhase.CONTINUE_GAME;
        hasWon = false;

        multiplayerHcEventManager = new MultiplayerHcEventManager();

    }


//    public static GameOpenProcedure open(GameOpenContext<MultiplayerHcGameConfig> context) {
//        MultiplayerHcGameConfig config = context.config();
//
//        RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
//                .setDimensionType(DimensionType.OVERWORLD_REGISTRY_KEY)
//                .setDifficulty(Difficulty.HARD)
//                .setGenerator(context.server().getOverworld().getChunkManager().getChunkGenerator());
//
//        return context.openWithWorld(worldConfig, (activity, world) -> {
//            MultiplayerHcGame game = new MultiplayerHcGame(config, activity.getGameSpace(), world);
//
//
//            activity.allow(GameRuleType.PVP);
//            activity.listen(GamePlayerEvents.ADD, game::onPlayerAdd);
//            activity.listen(PlayerDeathEvent.EVENT, game::onPlayerDeath);
//            activity.listen(EntityDeathEvent.EVENT, game::onEntityDeath);
//            activity.listen(GameActivityEvents.ENABLE, game::onEnable);
//        });
//
//    }

    public static void open(GameSpace gameSpace, MultiplayerHcGameConfig  config){

        Long seed = gameSpace.getServer().getOverworld().getRandom().nextLong();


        NoiseChunkGenerator NetherNoiseChunkGenerator =
                new NoiseChunkGenerator(gameSpace.getServer().getRegistryManager().get(Registry.NOISE_WORLDGEN),
                        (BiomeSource)MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(gameSpace.getServer().getRegistryManager().get(Registry.BIOME_KEY),
                                true),
                        seed,
                        () -> gameSpace.getServer().getRegistryManager().get(Registry.CHUNK_GENERATOR_SETTINGS_KEY).getOrThrow(ChunkGeneratorSettings.NETHER));

        NoiseChunkGenerator EndNoiseChunkGenerator =
                new NoiseChunkGenerator(gameSpace.getServer().getRegistryManager().get(Registry.NOISE_WORLDGEN),
                        (BiomeSource)new TheEndBiomeSource(gameSpace.getServer().getRegistryManager().get(Registry.BIOME_KEY), seed),seed,
        () -> gameSpace.getServer().getRegistryManager().get(Registry.CHUNK_GENERATOR_SETTINGS_KEY).getOrThrow(ChunkGeneratorSettings.END));



        RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
                .setDimensionType(DimensionType.OVERWORLD_REGISTRY_KEY)
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.DO_MOB_SPAWNING,true)
                .setGenerator(GeneratorOptions.createOverworldGenerator(gameSpace.getServer().getRegistryManager(),
                        seed));

        RuntimeWorldConfig netherConfig = new RuntimeWorldConfig()
                .setDimensionType(DimensionType.THE_NETHER_REGISTRY_KEY)
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.DO_MOB_SPAWNING,true)
                .setGenerator(NetherNoiseChunkGenerator);

        RuntimeWorldConfig endConfig = new RuntimeWorldConfig()
                .setDimensionType(DimensionType.THE_END_REGISTRY_KEY)
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.DO_MOB_SPAWNING,true)
                .setGenerator(EndNoiseChunkGenerator);





        ServerWorld world = gameSpace.getWorlds().add(worldConfig);
        ServerWorld nether = gameSpace.getWorlds().add(netherConfig);
        ServerWorld end = gameSpace.getWorlds().add(endConfig);

        end.getServer().getSaveProperties().setDragonFight(end.getEnderDragonFight().toNbt());
        end.getChunkManager().getPersistentStateManager().save();


        CustomPortalBuilder netherPortal = CustomPortalBuilder.beginPortal();
        netherPortal.frameBlock(Blocks.OBSIDIAN);
        netherPortal.tintColor(10);
        netherPortal.ignitionSource(PortalIgnitionSource.FIRE);
        netherPortal.destDimID(nether.getRegistryKey().getValue());
        netherPortal.returnDim(world.getRegistryKey().getValue(), false);
        netherPortal.registerPortal();



//        CustomPortalBuilder endPortal = CustomPortalBuilder.beginPortal();
//        endPortal.frameBlock(Blocks.DIAMOND_BLOCK);
//        endPortal.ignitionSource(PortalIgnitionSource.FIRE);
//        endPortal.destDimID(end.getRegistryKey().getValue());
//        endPortal.returnDim(world.getRegistryKey().getValue(), false);
//        endPortal.registerPortal();





        gameSpace.setActivity(game -> {
            MultiplayerHcGame active = new MultiplayerHcGame(config, gameSpace, world, nether, end);
            runningGames.add(active);

            game.setRule(GameRuleType.PVP,ActionResult.SUCCESS);

            //game.setRule(GameRuleType.PORTALS, ActionResult.SUCCESS);

            game.listen(GameActivityEvents.TICK, active::tick);
            game.listen(GamePlayerEvents.ADD, active::onPlayerAdd);
            game.listen(PlayerDeathEvent.EVENT, active::onPlayerDeath);
            game.listen(EntityDeathEvent.EVENT, active::onEntityDeath);
            game.listen(EntitySpawnEvent.EVENT, active::onEntitySpawn);
            game.listen(GameActivityEvents.ENABLE, active::onEnable);
            game.listen(GameActivityEvents.DISABLE, active::onDisable);


//            game.listen(NetherPortalOpenEvent.EVENT, active::onNetherPortalOpen);
           //game.listen(EndPortalOpenEvent.EVENT, active::onEndPortalOpen);

        });
    }

    private ActionResult onEntitySpawn(Entity entity) {
        multiplayerHcEventManager.onEntitySpawn(this,entity);
        return ActionResult.PASS;
    }

    private void tick() {


        if(nextEventAddTime <= world.getTime() && eventProbablity >= MultiplayerHcEvent.random.nextFloat()) {

            MultiplayerHcEvent multiplayerHcEvent = MultiplayerHcGameEvents.eventsList.
                    get(MultiplayerHcEvent.random.nextInt
                            (0,MultiplayerHcGameEvents.eventsList.size()));

            multiplayerHcEventManager.addEvent(this, multiplayerHcEvent,this.world);

            nextEventAddTime = world.getTime() + EVENT_COOLDOWN;
        }

        multiplayerHcEventManager.tickMultiplayerHcEventManager(this);

        switch(this.gamePhase){
            case CONTINUE_GAME -> {
                break;
            }
            case GAME_ENDING -> {
                if(this.world.getTime() >= this.finishTime){
                    this.gamePhase = GamePhase.GAME_CLOSED;
                }
                else {
                    timerBar.setTitle(getText(this.finishTime - this.world.getTime()));
                    timerBar.setProgress((float) (this.finishTime - this.world.getTime()) / 600);

                    if(hasWon) {
                        victoryMessage(this.finishTime - this.world.getTime());
                    }
                }
            }
            case GAME_CLOSED -> {
//                this.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
//                    this.timerBar.removePlayer(gamePlayer);
//                });
                this.gameSpace.close(GameCloseReason.FINISHED);
            }
        }

    }


    public Text getText(long ticksUntilEnd){
        long secondsUntilEnd = ticksUntilEnd / 20;
        long seconds = secondsUntilEnd % 60;


        return Text.of("Returning to Hub in " + seconds + " seconds");
    }

    public void victoryMessage(long ticksUntilEnd){
        if(ticksUntilEnd == 590){
            this.gameSpace.getPlayers().
                    sendMessage(Text.of("Congratulations! You beat Multiplayer HC!"));
        }
        else if(ticksUntilEnd == 540) {
            this.gameSpace.getPlayers().
                    sendMessage(Text.of("I hope your friendships have remained intact throughout your attempts!"));
        }
        else if(ticksUntilEnd == 480) {
            this.gameSpace.getPlayers().
                    sendMessage(Text.of("Unless this only took a single attempt, which would be very impressive!"));
        }
        else if(ticksUntilEnd == 420) {
            this.gameSpace.getPlayers().
                    sendMessage(Text.of("This game was made by CyborgPigeon, and is a port of a paper plugin made by Hoi_A. " +
                            "I hope you enjoyed playing!"));
        }
    }


//    private ActionResult onEndPortalOpen(ItemUsageContext itemUsageContext, BlockPattern.Result result) {
//        EndPortalFeature endPortalFeature = new EndPortalFeature(false);
//        BlockPos exitPortalLocation = this.end.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.ORIGIN).down();
//        while (this.end.getBlockState(exitPortalLocation).isOf(Blocks.BEDROCK) && exitPortalLocation.getY() > this.end.getSeaLevel()) {
//            exitPortalLocation = exitPortalLocation.down();
//        }
//        endPortalFeature.configure(FeatureConfig.DEFAULT).generate(this.end, this.end.getChunkManager().getChunkGenerator(), new Random(), exitPortalLocation);
//
//
//        return ActionResult.SUCCESS;
//    }

    private EnderDragonEntity createDragon() {
        this.end.getWorldChunk(new BlockPos(0, 128, 0));
        EnderDragonEntity enderDragonEntity = EntityType.ENDER_DRAGON.create(this.world);
        enderDragonEntity.getPhaseManager().setPhase(PhaseType.HOLDING_PATTERN);
        enderDragonEntity.refreshPositionAndAngles(0.0, 128.0, 0.0, this.world.random.nextFloat() * 360.0f, 0.0f);
        this.end.spawnEntity(enderDragonEntity);
        return enderDragonEntity;
    }

    private void onDisable() {
        this.gameSpace.getPlayers().forEach(gamePlayer -> {
            this.timerBar.removePlayer(gamePlayer);
        });
        CustomPortalApiRegistry.removePortal(Blocks.OBSIDIAN);
        runningGames.remove(this);
    }



    private void onEnable() {
        this.gameSpace.getPlayers().stream().forEach(this::spawnPlayerAtCenter);
    }

    private ActionResult onEntityDeath(LivingEntity livingEntity, DamageSource source) {
        if(livingEntity instanceof EnderDragonEntity){
            //this.gameSpace.close(GameCloseReason.FINISHED);

            hasWon = true;

            this.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
                gamePlayer.changeGameMode(GameMode.SPECTATOR);
                this.timerBar.addPlayer(gamePlayer);

            });

            this.finishTime = this.world.getTime() + 600;
            this.gamePhase = GamePhase.GAME_ENDING;

            return ActionResult.SUCCESS;
        }


        multiplayerHcEventManager.onEntityDeath(this,livingEntity,source);

        return ActionResult.PASS;
    }

    private ActionResult onPlayerDeath(ServerPlayerEntity serverPlayerEntity, DamageSource source) {
        spawnPlayerAtCenter(serverPlayerEntity);

        this.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
            gamePlayer.changeGameMode(GameMode.SPECTATOR);
            this.timerBar.addPlayer(gamePlayer);

        });

        this.gameSpace.getPlayers().
                sendMessage(Text.of(serverPlayerEntity.getName().asString() + " died and ruined it for everyone!"));


        //this.gameSpace.close(GameCloseReason.FINISHED);
        this.finishTime = this.world.getTime() + 600;
        this.gamePhase = GamePhase.GAME_ENDING;


        return ActionResult.FAIL;
    }


    private void onPlayerAdd(ServerPlayerEntity serverPlayerEntity) {
        serverPlayerEntity.changeGameMode(GameMode.SURVIVAL);
    }



    //ripped from https://github.com/NucleoidMC/UHC/blob/1.18/src/main/java/com/hugman/uhc/game/UHCSpawner.java

    public static BlockPos getSurfaceBlock(ServerWorld world, int x, int z) {
        WorldChunk chunk = world.getWorldChunk(new BlockPos(x, 0, z));
        return new BlockPos(new Vec3d(x, chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) + 1, z));
    }

    public void spawnPlayerAtCenter(ServerPlayerEntity player) {
        this.spawnPlayerAt(player, MultiplayerHcGame.getSurfaceBlock(this.world,0, 0));
    }

    public void spawnPlayerAt(ServerPlayerEntity player, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
        this.world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, player.getId());
        player.teleport(this.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0F, 0.0F);
        player.setSpawnPoint(this.world.getRegistryKey(),player.getBlockPos(),0,true,false);
    }



}
