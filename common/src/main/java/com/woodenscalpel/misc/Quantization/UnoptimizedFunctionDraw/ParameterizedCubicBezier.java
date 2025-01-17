package com.woodenscalpel.misc.Quantization.UnoptimizedFunctionDraw;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ParameterizedCubicBezier extends ParameterizedNormalizedCurve{
    public ParameterizedCubicBezier(List<Vec3> points) {
        super(points);
    }

    @Override
    Vec3 f(double t) {
        Vec3 p0 = points.get(0);
        Vec3 p1 = points.get(1);
        Vec3 p2 = points.get(2);
        Vec3 p3 = points.get(3);


        return p0.scale(Math.pow(1-t,3)).add(p1.scale(3*t*Math.pow((1-t),2))).add(p2.scale(t*t*3*(1-t))).add(p3.scale(t*t*t));
    }
}
