package com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw;

import com.woodenscalpel.buildinggizmos.misc.helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ParameterizedCubicBezier extends ParameterizedNormalizedCurve{
    public ParameterizedCubicBezier(List<BlockPos> points) {
        super(points);
    }

    @Override
    Vec3 f(double t) {
        Vec3 p0 = helpers.blockPostoVec3(points.get(0));
        Vec3 p1 = helpers.blockPostoVec3(points.get(1));
        Vec3 p2 = helpers.blockPostoVec3(points.get(2));
        Vec3 p3 = helpers.blockPostoVec3(points.get(3));


        return p0.scale(Math.pow(1-t,3)).add(p1.scale(3*t*Math.pow((1-t),2))).add(p2.scale(t*t*3*(1-t))).add(p3.scale(t*t*t));
    }
}
