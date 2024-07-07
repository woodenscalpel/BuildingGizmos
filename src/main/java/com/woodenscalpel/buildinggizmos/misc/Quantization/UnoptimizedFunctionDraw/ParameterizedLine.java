package com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ParameterizedLine extends ParameterizedNormalizedCurve{
    public ParameterizedLine(List<BlockPos> points) {
        super(points);
    }

    @Override
    Vec3 f(double t) {

        Vec3 p0 = new Vec3(points.get(0).getX(),points.get(0).getY(),points.get(0).getZ());
        Vec3 p1 = new Vec3(points.get(1).getX(),points.get(1).getY(),points.get(1).getZ());

        return p1.scale(1-t).add(p1.scale(t));
    }
}
