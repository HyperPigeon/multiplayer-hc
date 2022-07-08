package net.hyper_pigeon.multiplayerhc.registry;

import eu.pb4.polymer.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hyper_pigeon.multiplayerhc.entity.AngryIronGolemEntity;
import net.hyper_pigeon.multiplayerhc.entity.SCPOneSevenThreeEntity;
import net.hyper_pigeon.multiplayerhc.entity.SCPZeroNineSixEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MultiplayerHcEntities {
    public static final EntityType<AngryIronGolemEntity> ANGRY_IRON_GOLEM_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier("multiplayerhc", "angry_iron_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AngryIronGolemEntity::new).size(EntityDimensions.fixed(1.4f, 2.7f)).build()
    );

    public static final EntityType<SCPZeroNineSixEntity> SCP_ZERO_NINE_SIX_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier("multiplayerhc", "scp_zero_nine_six_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SCPZeroNineSixEntity::new).size(EntityDimensions.fixed(0.6f, 2.9f)).build());

    public static final EntityType<SCPOneSevenThreeEntity> SCP_ONE_SEVEN_THREE_ENTITY =
            Registry.register(
                    Registry.ENTITY_TYPE, new Identifier("multiplayerhc", "scp_one_seven_three_entity"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SCPOneSevenThreeEntity::new).size(EntityDimensions.fixed(0.6f, 1.7f)).build());


    public static void init(){
        FabricDefaultAttributeRegistry.register(ANGRY_IRON_GOLEM_ENTITY, IronGolemEntity.createIronGolemAttributes());
        FabricDefaultAttributeRegistry.register(SCP_ZERO_NINE_SIX_ENTITY, SCPZeroNineSixEntity.createSCPZeroNineSixAttributes());
        FabricDefaultAttributeRegistry.register(SCP_ONE_SEVEN_THREE_ENTITY, SCPOneSevenThreeEntity.createSCPOneSevenThreeAttributes());
        PolymerEntityUtils.registerType(ANGRY_IRON_GOLEM_ENTITY);
        PolymerEntityUtils.registerType(SCP_ZERO_NINE_SIX_ENTITY);
        PolymerEntityUtils.registerType(SCP_ONE_SEVEN_THREE_ENTITY);
    }
}
