package com.woodenscalpel.buildinggizmos.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.List;

@Mod.EventBusSubscriber(modid = BuildingGizmos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GizmoWorldRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();
@SubscribeEvent
   public static void RenderLevelStageEvent(RenderLevelStageEvent event)
{
// public static void onWorldRenderLast(RenderLevelStageEvent event){
        assert Minecraft.getInstance().player != null;
        ItemStack item = Minecraft.getInstance().player.getMainHandItem();
        if (item.getItem() instanceof AbstractWand && item.getOrCreateTag().getBoolean("ready")) {
            List<BlockPos> cplist = helpers.getBlockList(item.getOrCreateTag(),"CONTROLPOINTS");
//            BuildingGizmos.LOGGER.info("CRASHLIST" + cplist.toString());
            int[] b1 = new int[] {cplist.get(0).getX(),cplist.get(0).getY(),cplist.get(0).getZ()};
            int[] b2 = new int[] {cplist.get(1).getX(),cplist.get(1).getY(),cplist.get(1).getZ()};
                    //if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
                        //if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS) {
                        Player player = Minecraft.getInstance().player;

                        Vec3 campos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
                        //Vec3 campos  = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

                        double px = campos.x();
                        double py = campos.y();
                        double pz = campos.z();
                        AABB shape3 = new Box(b1, b2).renderBox().move(-px, -py, -pz);
                        //AABB shape = Shapes.block().bounds().move(0, -10, 0);
                        drawLineBox(event.getPoseStack(), shape3, 1.0f, 0.3f, 1.0f, 1.0f);
                    }

            }

    }

    public static void drawLineBox(PoseStack matrixStack, AABB aabb, float r, float g, float b, float a) {
        RenderSystem.disableDepthTest();
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(matrixStack, vertexConsumer, aabb, r, g, b, a);
        RenderSystem.enableDepthTest();
    }

}
