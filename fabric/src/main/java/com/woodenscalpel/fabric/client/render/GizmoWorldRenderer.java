package com.woodenscalpel.fabric.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.woodenscalpel.client.render.RenderTypes;
import com.woodenscalpel.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.common.item.abstractwand.ControlPoint;
import com.woodenscalpel.common.item.texturewand.TextureWand;
import com.woodenscalpel.misc.shapes.Vec3Box;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GizmoWorldRenderer {

   public static boolean RenderLevelStageEvent(WorldRenderContext context, HitResult hitResult)
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
                   // if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            if(true){




                        //BIG BOUNDING BOX***********************************************************************************
                        //AABB shape3 = new Box(cplist.get(0),cplist.get(1)).renderBox().move(-px, -py, -pz);
                        AABB shape3 = getVec3BB(cplist).move(-px, -py, -pz);
                        //BuildingGizmos.LOGGER.info(String.valueOf(shape3));
                       drawLineBox(context.matrixStack(), shape3, 1.0f, 0.3f, 1.0f, 1.0f);

                    }


            //if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) { //TODO want to change this to later stage but strange viewbob desync happens
            if(true){
                //CONTROL POINTS*******************************************************************************
                for(Vec3 cp : cplist){
                    ControlPoint controlPoint = new ControlPoint(cp,-1);
                    for(Vec3Box box: controlPoint.getBoxes()){
                        //BuildingGizmos.LOGGER.info(String.valueOf(box.minx) + " "+String.valueOf(box.maxx));
                        box.move(-px,-py,-pz);
                        //BuildingGizmos.LOGGER.info(String.valueOf(box.minx) + " "+String.valueOf(box.maxx));
                        drawLineBox(context.matrixStack(), box.minx,box.miny, box.minz, box.maxx, box.maxy, box.maxz, 1.0f, 0.5f, 0.5f, 1.0f);
                    }
                }

            }

            }
        return true;
    }

    private static AABB getVec3BB(List<Vec3> cplist) {
    double minx = cplist.get(0).x;
    double miny = cplist.get(0).y;
    double minz = cplist.get(0).z;
    double maxx = cplist.get(0).x;
    double maxy = cplist.get(0).y;
    double maxz = cplist.get(0).z;
    for(Vec3 v : cplist){
        minx = Math.min(minx,v.x);
        miny = Math.min(miny,v.y);
        minz = Math.min(minz,v.z);
        maxx = Math.max(maxx,v.x);
        maxy = Math.max(maxy,v.y);
        maxz = Math.max(maxz,v.z);
    }

    return new AABB((int) minx, (int) miny, (int) minz, (int) maxx+1, (int) maxy+1,(int) maxz+1);
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


    public static void RenderLevelStageEventAfterTranslucent(WorldRenderContext context) {

        ItemStack item = Minecraft.getInstance().player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        if (item.getItem() instanceof AbstractWand && AbstractWand.ShapeHelper.getShapeComplete(nbt)) {
            //Load up relevant data
            List<Vec3> cplist = AbstractWand.ShapeHelper.getControlPoints(nbt);
            List<BlockPos> blockQueue = AbstractWand.ShapeHelper.getQueue(nbt);
            Player player = Minecraft.getInstance().player;

            // if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            if (true) {
                //BLOCK GHOSTS
                if (!(item.getItem() instanceof TextureWand)) {
                    if (!blockQueue.isEmpty()) {
                        for (BlockPos b : blockQueue) {
                            VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.PREVIEW);
                            renderBlockAt(context.matrixStack(), buffer, Blocks.STONE.defaultBlockState(), b);
                        }
                    }
                }

            }

        }
    }
}
