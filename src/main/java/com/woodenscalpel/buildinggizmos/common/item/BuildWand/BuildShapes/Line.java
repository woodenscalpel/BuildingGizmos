package com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedQuadBezier;
import net.minecraft.core.BlockPos;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Line extends AbstractShape{

    private static final Logger LOGGER = LogUtils.getLogger();
    //List<BlockPos> coords =new ArrayList<>();

    public Line(BlockPos start, BlockPos end){

       //coords = new Bresenham3D().drawLine(start,end);

        List<BlockPos> points = new ArrayList<>();
        BlockPos midpoint = new BlockPos(start.getX()/2 + end.getX()/2,start.getY()/2 + end.getY()/2 + 10,start.getZ()/2 + end.getZ()/2);
        points.add(start);
        points.add(midpoint);
        points.add(end);

        LOGGER.info(points.toString());
        coords = new ParameterizedQuadBezier(points).getblocks();


    }

}
