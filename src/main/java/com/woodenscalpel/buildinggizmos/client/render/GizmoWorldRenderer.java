package com.woodenscalpel.buildinggizmos.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.ControlPoint;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import com.woodenscalpel.buildinggizmos.misc.shapes.Vec3Box;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
        CompoundTag nbt = item.getOrCreateTag();
        if (item.getItem() instanceof AbstractWand && AbstractWand.ShapeHelper.getShapeComplete(nbt)) {
            //Load up relevant data
            List<Vec3> cplist = AbstractWand.ShapeHelper.getControlPoints(nbt);
            List<BlockPos> blockQueue = AbstractWand.ShapeHelper.getQueue(nbt);
            Player player = Minecraft.getInstance().player;
            Vec3 campos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
            double px = campos.x();
            double py = campos.y();
            double pz = campos.z();
                    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {




                        //BIG BOUNDING BOX***********************************************************************************
                        AABB shape3 = new Box(cplist.get(0),cplist.get(1)).renderBox().move(-px, -py, -pz);
                        //BuildingGizmos.LOGGER.info(String.valueOf(shape3));

                       drawLineBox(event.getPoseStack(), shape3, 1.0f, 0.3f, 1.0f, 1.0f);

                    }

            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                //BLOCK GHOSTS

                if (!blockQueue.isEmpty()){
                    for (BlockPos b : blockQueue) {
                        VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.PREVIEW);
                        renderBlockAt(event.getPoseStack(),buffer, Blocks.STONE.defaultBlockState(),b);
                    }
                }


            }

            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) { //TODO want to change this to later stage but strange viewbob desync happens
                //CONTROL POINTS*******************************************************************************
                for(Vec3 cp : cplist){
                    ControlPoint controlPoint = new ControlPoint(cp,-1);
                    for(Vec3Box box: controlPoint.getBoxes()){
                        //BuildingGizmos.LOGGER.info(String.valueOf(box.minx) + " "+String.valueOf(box.maxx));
                        box.move(-px,-py,-pz);
                        //BuildingGizmos.LOGGER.info(String.valueOf(box.minx) + " "+String.valueOf(box.maxx));
                        drawLineBox(event.getPoseStack(), box.minx,box.miny, box.minz, box.maxx, box.maxy, box.maxz, 1.0f, 0.5f, 0.5f, 1.0f);
                    }
                }

            }

            }

    }

    public static void drawLineBox(PoseStack matrixStack, AABB aabb, float r, float g, float b, float a) {
        RenderSystem.disableDepthTest();
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(matrixStack, vertexConsumer, aabb, r, g, b, a);
        RenderSystem.enableDepthTest();
    }
    public static void drawLineBox(PoseStack matrixStack, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a) {
        RenderSystem.disableDepthTest();
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(matrixStack, vertexConsumer, x1,y1,z1,x2,y2,z2, r, g, b, a);
        RenderSystem.enableDepthTest();
    }
    private static void renderBlockAt(PoseStack ms, VertexConsumer buffer, BlockState state, BlockPos pos) {
        double renderPosX = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().x();
        double renderPosY = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().y();
        double renderPosZ = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().z();

        ms.pushPose();
        ms.translate(-renderPosX, -renderPosY, -renderPosZ);

        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        ms.translate(pos.getX(), pos.getY(), pos.getZ());
        BakedModel model = brd.getBlockModel(state);
        int color = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        // always use entity translucent layer so blending is turned on
        brd.getModelRenderer().renderModel(ms.last(), buffer, state, model, r, g, b, 0xF000F0, OverlayTexture.NO_OVERLAY);

        ms.popPose();
    }


}
