package com.woodenscalpel.misc.Quantization.UnoptimizedFunctionDraw;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public  class ParameterizedCircle extends ParameterizedNormalizedCurve{
    private static final Logger LOGGER = LogUtils.getLogger();
    public ParameterizedCircle(List<Vec3> points) {
        super(points);
    }

    @Override
    Vec3 f(double t) {
        //TODO Builds circle beziers every call. should only do it on construction. its easy fix but it is also 3am :^)
        //assume vertically facing for now
        Vec3 p0 = points.get(0);
        Vec3 p1 = points.get(1);

        double r = p1.subtract(p0).length();
        double tan =0.552284749831 ; //(4/3)*tan(pi/8) = 4*(sqrt(2)-1)/3 = 0.552284749831

        Vec3 p2 = p0.add(0,r,0);
        Vec3 p3 = p1.add(0,tan*r,0);
        Vec3 p4 = p2.add((p1.x - p0.x)*tan,0,(p1.z-p0.z)*tan);

        List<Vec3> newpoints = new ArrayList<>();
        newpoints.add((p2));
        newpoints.add((p4));
        newpoints.add((p3));
        newpoints.add((p1));

        /*
        LOGGER.info("ttt");
        LOGGER.info(String.valueOf(p0));
        LOGGER.info(String.valueOf(p1));
        LOGGER.info(String.valueOf(p2));
        LOGGER.info(String.valueOf(p3));
        LOGGER.info(String.valueOf(p4));
        */

        return new ParameterizedCubicBezier(newpoints).f(t);
    }
    @Override
    public List<BlockPos> getblocks(BlockPos start){

        List<BlockPos> result = new ArrayList<>();
        result.add(start);

        BlockPos current = start;

        Vec3 p0 =  points.get(0);

        for(float t = 0; t<1; t+= 0.001F){

            Vec3 point = this.f(t);
            BlockPos closestBlock = closest(point, current);
            if(closestBlock != current){
                result.add(closestBlock);
                int dx = (int) (closestBlock.getX() - p0.x);
                int dy = (int) (closestBlock.getY() - p0.y);
                int dz = (int) (closestBlock.getZ() - p0.z);
                result.add(new BlockPos((int) (p0.x-dx), (int) (p0.y-dy), (int) (p0.z-dz)));
                result.add(new BlockPos(closestBlock.getX(), (int) (p0.y-dy),closestBlock.getZ()));
                result.add(new BlockPos((int) (p0.x-dx),closestBlock.getY(), (int) (p0.z-dz)));
                current = closestBlock;
            }

        }
        return result;
    }
}
