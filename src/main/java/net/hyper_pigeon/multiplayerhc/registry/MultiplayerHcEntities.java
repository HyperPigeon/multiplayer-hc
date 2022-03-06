package net.hyper_pigeon.multiplayerhc.registry;

import eu.pb4.polymer.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.impl.networking.packets.PolymerEntityEntry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hyper_pigeon.multiplayerhc.entity.AngryIronGolemEntity;
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

    public static void init(){
        FabricDefaultAttributeRegistry.register(ANGRY_IRON_GOLEM_ENTITY, IronGolemEntity.createIronGolemAttributes());
        PolymerEntityUtils.registerType(ANGRY_IRON_GOLEM_ENTITY);
    }
}
