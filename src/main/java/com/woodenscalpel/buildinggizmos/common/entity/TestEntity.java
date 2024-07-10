package com.woodenscalpel.buildinggizmos.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import static com.woodenscalpel.buildinggizmos.init.EntityInit.ENTITIES;

public class TestEntity extends Entity {
    public TestEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

/*
    public static AttributeSupplier.Builder getExampleAttributes(){
        return Mob.createMobAttributes().add(ForgeMod.ENTITY_GRAVITY.get(),0);
    }

 */
}
