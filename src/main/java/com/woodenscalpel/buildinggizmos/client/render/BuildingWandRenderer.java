package com.woodenscalpel.buildinggizmos.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildWand;
import com.woodenscalpel.buildinggizmos.common.item.texturewand.TextureWand;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = BuildingGizmos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BuildingWandRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();
@SubscribeEvent
   public static void RenderLevelStageEvent(RenderLevelStageEvent event)
{
// public static void onWorldRenderLast(RenderLevelStageEvent event){
        assert Minecraft.getInstance().player != null;
        ItemStack item = Minecraft.getInstance().player.getMainHandItem();
        if (item.getItem() instanceof BuildWand && item.getOrCreateTag().getBoolean("ready")) {

            int[] blockQueue = item.getOrCreateTag().getIntArray("blockQueue");
        /*
                int[] b1 = item.getOrCreateTag().getIntArray("P1");
                int[] b2 = item.getOrCreateTag().getIntArray("P2");
                int[] blockQueue = item.getOrCreateTag().getIntArray("blockQueue");
                if (b1.length > 0 && b2.length > 0) {
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

         */
                if (blockQueue.length > 0){
                    if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
                        Vec3 campos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
                        //LOGGER.info("RENDER BLOCK GHOST");
                        for (int i = 0; i < blockQueue.length; i = i + 3) {
                            BlockPos pos = new BlockPos(blockQueue[i], blockQueue[i + 1], blockQueue[i + 2]);
                            //drawBlockTexture(event, event.getPoseStack(), pos, campos, Blocks.STONE);

                            VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderTypes.PREVIEW);
                            renderBlockAt(event.getPoseStack(),buffer,Blocks.STONE.defaultBlockState(),pos);

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


    public static void drawBlockTexture(RenderLevelStageEvent event,PoseStack poseStack, BlockPos blockPos, Vec3 cameraPos, Block block){
        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
        VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.translucent());

        /*
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.7f); // I set the opacity of the draw here

         */

        VertexConsumer vertexConsumer = new VertexConsumer() {
            @Override
            public VertexConsumer vertex(double pX, double pY, double pZ) {
                return null;
            }

            @Override
            public VertexConsumer color(int pRed, int pGreen, int pBlue, int pAlpha) {
                return null;
            }

            @Override
            public VertexConsumer uv(float pU, float pV) {
                return null;
            }

            @Override
            public VertexConsumer overlayCoords(int pU, int pV) {
                return null;
            }

            @Override
            public VertexConsumer uv2(int pU, int pV) {
                return null;
            }

            @Override
            public VertexConsumer normal(float pX, float pY, float pZ) {
                return null;
            }

            @Override
            public void endVertex() {

            }

            @Override
            public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA) {


            }

            @Override
            public void unsetDefaultColor() {

            }
        };

        //renderer.renderBatched(block,blockPos,Minecraft.getInstance().level, buffer,true,Minecraft.getInstance().level.getRandom());
        poseStack.pushPose();
        poseStack.translate(blockPos.getX()-cameraPos.x,blockPos.getY()-cameraPos.y,blockPos.getZ()-cameraPos.z);
        renderer.renderBatched(block.defaultBlockState(),blockPos,Minecraft.getInstance().level, poseStack,buffer,true,Minecraft.getInstance().level.getRandom());

        poseStack.popPose();

        /*
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
         */
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
