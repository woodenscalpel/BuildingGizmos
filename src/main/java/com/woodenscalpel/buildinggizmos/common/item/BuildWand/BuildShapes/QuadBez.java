package com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes;

import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedQuadBezier;
import net.minecraft.core.BlockPos;

import java.util.List;

public class QuadBez extends AbstractShape{
    public QuadBez(List<BlockPos> points){
        coords = new ParameterizedQuadBezier(points).getblocks();

    }
}
