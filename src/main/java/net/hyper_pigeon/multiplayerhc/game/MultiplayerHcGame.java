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
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.quiltservertools.interdimensional.portals.CustomPortalApiRegistry;
import net.quiltservertools.interdimensional.portals.api.CustomPortalBuilder;
import net.quiltservertools.interdimensional.portals.portal.PortalIgnitionSource;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.plasmid.game.GameCloseReason;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.common.widget.BossBarWidget;
import xyz.nucleoid.plasmid.game.event.GameActivityEvents;
import xyz.nucleoid.plasmid.game.event.GamePlayerEvents;
import xyz.nucleoid.plasmid.game.player.PlayerOffer;
import xyz.nucleoid.plasmid.game.player.PlayerOfferResult;
import xyz.nucleoid.plasmid.game.rule.GameRuleType;
import xyz.nucleoid.stimuli.event.entity.EntityDamageEvent;
import xyz.nucleoid.stimuli.event.entity.EntityDeathEvent;
import xyz.nucleoid.stimuli.event.entity.EntitySpawnEvent;
import xyz.nucleoid.stimuli.event.player.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.Objects;

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
    private static final long EVENT_COOLDOWN = 750;
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


    public static void open(GameSpace gameSpace, MultiplayerHcGameConfig config){


        Long seed = gameSpace.getServer().getOverworld().getRandom().nextLong();


        ChunkGenerator overworldGen = config.overworld().chunkGenerator();
        ChunkGenerator netherGen = config.nether().chunkGenerator();
        ChunkGenerator endGen = config.end().chunkGenerator();

        RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
                .setDimensionType(config.overworld().dimensionTypeEntry())
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.DO_MOB_SPAWNING,true)
                .setGameRule(GameRules.DO_DAYLIGHT_CYCLE, true)
                .setGenerator(overworldGen)
                .setSeed(seed);

        RuntimeWorldConfig netherConfig = new RuntimeWorldConfig()
                .setDimensionType(config.nether().dimensionTypeEntry())
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.DO_MOB_SPAWNING,true)
                .setGenerator(netherGen)
                .setSeed(seed);

        RuntimeWorldConfig endConfig = new RuntimeWorldConfig()
                .setDimensionType(config.end().dimensionTypeEntry())
                .setDifficulty(Difficulty.HARD)
                .setGameRule(GameRules.DO_MOB_SPAWNING,true)
                .setGenerator(endGen)
                .setSeed(seed);


        ServerWorld world = gameSpace.getWorlds().add(worldConfig);
        ServerWorld nether = gameSpace.getWorlds().add(netherConfig);
        ServerWorld end = gameSpace.getWorlds().add(endConfig);

//        NbtCompound nbtCompound = new NbtCompound();
//        nbtCompound.putBoolean("DragonKilled", false);
//        nbtCompound.putBoolean("PreviouslyKilled", false);
        //EnderDragonFight enderDragonFight = new EnderDragonFight(end, seed, nbtCompound);
        //end.getServer().getSaveProperties().setDragonFight(enderDragonFight.toNbt());
        createDragon(end);
        generateEndPortal(false,end);
        //end.getChunkManager().getPersistentStateManager().save();

        CustomPortalBuilder netherPortal = CustomPortalBuilder.beginPortal();
        netherPortal.frameBlock(Blocks.OBSIDIAN);
        netherPortal.tintColor(10);
        netherPortal.ignitionSource(PortalIgnitionSource.FIRE);
        netherPortal.destDimID(nether.getRegistryKey().getValue());
        netherPortal.returnDim(world.getRegistryKey().getValue(), false);
        netherPortal.registerPortal();



        gameSpace.setActivity(game -> {
            MultiplayerHcGame active = new MultiplayerHcGame(config, gameSpace, world, nether, end);
            runningGames.add(active);

            game.setRule(GameRuleType.PVP,ActionResult.SUCCESS);
            game.listen(GameActivityEvents.TICK, active::tick);
            game.listen(GamePlayerEvents.ADD, active::onPlayerAdd);
            game.listen(GamePlayerEvents.OFFER, active::onPlayerOffer);
            game.listen(PlayerDeathEvent.EVENT, active::onPlayerDeath);
            game.listen(EntityDeathEvent.EVENT, active::onEntityDeath);
            game.listen(EntitySpawnEvent.EVENT, active::onEntitySpawn);
            game.listen(GameActivityEvents.ENABLE, active::onEnable);
            game.listen(GameActivityEvents.DISABLE, active::onDisable);
            game.listen(EntityDamageEvent.EVENT, active::onDamage);


        });

    }


    private PlayerOfferResult onPlayerOffer(PlayerOffer playerOffer) {
        ServerPlayerEntity player = playerOffer.player();
        BlockPos blockPos = this.world.getSpawnPos();
        return playerOffer.accept(this.world, new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .and(() -> {
                    player.changeGameMode(GameMode.SURVIVAL);
                });
    }

    private ActionResult onDamage(LivingEntity livingEntity, DamageSource source, float v) {
        if(config.mode().equals("random_events")) {
            multiplayerHcEventManager.onDamage(this,livingEntity,source,v);
        }
        return ActionResult.PASS;
    }

    private ActionResult onEntitySpawn(Entity entity) {
        if(config.mode().equals("random_events")) {
            multiplayerHcEventManager.onEntitySpawn(this,entity);
        }
        return ActionResult.PASS;
    }

    private void tick() {
        if(config.mode().equals("random_events") && nextEventAddTime <= world.getTime() && eventProbablity >= MultiplayerHcEvent.random.nextFloat()) {

            MultiplayerHcEvent multiplayerHcEvent = MultiplayerHcGameEvents.eventsList.
                    get(MultiplayerHcEvent.random.nextInt
                            (0,MultiplayerHcGameEvents.eventsList.size()));

            multiplayerHcEventManager.addEvent(this, multiplayerHcEvent,this.world);

            nextEventAddTime = world.getTime() + EVENT_COOLDOWN;
        }
        if(config.mode().equals("random_events")){
            multiplayerHcEventManager.tickMultiplayerHcEventManager(this);
        }

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
                }
            }
            case GAME_CLOSED -> {
                this.gameSpace.close(GameCloseReason.FINISHED);
            }
        }

    }


    public Text getText(long ticksUntilEnd){
        long secondsUntilEnd = ticksUntilEnd / 20;
        long seconds = secondsUntilEnd % 60;


        return Text.of("Returning to Hub in " + seconds + " seconds");
    }




    private static EnderDragonEntity createDragon(ServerWorld end) {
        end.getWorldChunk(new BlockPos(0, 128, 0));
        EnderDragonEntity enderDragonEntity = EntityType.ENDER_DRAGON.create(end);
        enderDragonEntity.getPhaseManager().setPhase(PhaseType.HOLDING_PATTERN);
        enderDragonEntity.refreshPositionAndAngles(0.0, 128.0, 0.0, end.random.nextFloat() * 360.0f, 0.0f);
        end.spawnEntity(enderDragonEntity);
        return enderDragonEntity;
    }

    private static void generateEndPortal(boolean previouslyKilled, ServerWorld end) {
        EndPortalFeature endPortalFeature = new EndPortalFeature(previouslyKilled);

        BlockPos exitPortalLocation = end.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.ORIGIN).down();

        while(end.getBlockState(exitPortalLocation).isOf(Blocks.BEDROCK) && exitPortalLocation.getY() > end.getSeaLevel()) {
            exitPortalLocation = exitPortalLocation.down();
        }

        endPortalFeature.generateIfValid(
                FeatureConfig.DEFAULT, end, end.getChunkManager().getChunkGenerator(), Random.create(), exitPortalLocation
        );
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
            this.gameSpace.getPlayers().
                    sendMessage(Text.of("Congratulations! You beat Multiplayer HC!"));

            hasWon = true;

            this.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
                gamePlayer.changeGameMode(GameMode.SPECTATOR);
                this.timerBar.addPlayer(gamePlayer);

            });

            this.finishTime = this.world.getTime() + 600;
            this.gamePhase = GamePhase.GAME_ENDING;

            return ActionResult.SUCCESS;
        }

        if(config.mode().equals("random_events")) {
            multiplayerHcEventManager.onEntityDeath(this,livingEntity,source);

        }

        return ActionResult.PASS;
    }

    private ActionResult onPlayerDeath(ServerPlayerEntity serverPlayerEntity, DamageSource source) {
        spawnPlayerAtCenter(serverPlayerEntity);
        serverPlayerEntity.changeGameMode(GameMode.SPECTATOR);


        if(this.gamePhase != GamePhase.GAME_ENDING) {

            this.gameSpace.getPlayers().stream().forEach(gamePlayer -> {
                gamePlayer.changeGameMode(GameMode.SPECTATOR);
                this.timerBar.addPlayer(gamePlayer);

            });

            this.gameSpace.getPlayers().
                    sendMessage(Text.of(serverPlayerEntity.getGameProfile().getName() + " died and ruined it for everyone!"));


            //this.gameSpace.close(GameCloseReason.FINISHED);
            this.finishTime = this.world.getTime() + 600;
            this.gamePhase = GamePhase.GAME_ENDING;

            return ActionResult.FAIL;
        }


        return ActionResult.PASS;
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
