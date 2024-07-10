package com.woodenscalpel.buildinggizmos.client;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.common.entity.TestEntity;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWandPaletteScreen;
import com.woodenscalpel.buildinggizmos.init.EntityInit;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.C;

import static com.woodenscalpel.buildinggizmos.init.EntityInit.ENTITIES;


public class ClientHooks {
    public static void openTextureWandScreen(){
        Minecraft.getInstance().setScreen(new TextureWandPaletteScreen(net.minecraft.network.chat.Component.empty()));
    };

    public static void spawnTestClientsideEntity(){
        BuildingGizmos.LOGGER.info("SPAWN");
        TestEntity testEntity = new TestEntity(EntityInit.TEST.get(),Minecraft.getInstance().level);
        testEntity.moveTo(helpers.blockPostoVec3(Minecraft.getInstance().player.getOnPos()));
        //TestEntity test = new TestEntity( ENTITIES.FART,Minecraft.getInstance().level);
        assert Minecraft.getInstance().level != null;
        Minecraft.getInstance().level.addFreshEntity(testEntity);

        Chicken testChicken = new Chicken(EntityType.CHICKEN,Minecraft.getInstance().level);
        testEntity.moveTo(Minecraft.getInstance().player.position());
        Minecraft.getInstance().level.addFreshEntity(testChicken);
    }



}

