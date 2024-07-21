package com.woodenscalpel.misc.Quantization.UnoptimizedFunctionDraw;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ParameterizedLine extends ParameterizedNormalizedCurve{
    public ParameterizedLine(List<Vec3> points) {
        super(points);
    }

    @Override
    Vec3 f(double t) {

        Vec3 p0 = points.get(0);
        Vec3 p1 = points.get(1);

        return p1.scale(1-t).add(p1.scale(t));
    }
}
