package com.woodenscalpel.buildinggizmos.init;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.woodenscalpel.buildinggizmos.common.entity.TestEntity;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BuildingGizmos.MODID);

    public static final RegistryObject<EntityType<TestEntity>> TEST =
            ENTITIES.register("testentity",
                    () -> EntityType.Builder.of(TestEntity::new,MobCategory.MISC)
                            .sized(0.5f,0.5f)
                            .build(new ResourceLocation(BuildingGizmos.MODID+":testentity").toString()));

    public static void register(IEventBus eventBus){
        ENTITIES.register(eventBus);
    }
}
