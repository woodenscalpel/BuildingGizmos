package com.woodenscalpel.buildinggizmos.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;

public class RenderTypes extends RenderType {
    
    public static final RenderType PREVIEW = new PreviewLayer();
    
    public RenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    private static class PreviewLayer extends RenderType {
        public PreviewLayer() {
            super("bg.preview", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true,
                    () -> {
                        Sheets.translucentCullBlockSheet().setupRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.7F);
                    }, () -> {
                        Sheets.translucentCullBlockSheet().clearRenderState();
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    });
        }
    }
}
