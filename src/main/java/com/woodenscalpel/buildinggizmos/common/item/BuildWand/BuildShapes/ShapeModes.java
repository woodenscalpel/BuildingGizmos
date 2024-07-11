package com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.misc.CatenaryHelper.CatHelper;
import com.woodenscalpel.buildinggizmos.misc.Quantization.Bresenham3D;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedCubicBezier;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedQuadBezier;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public enum ShapeModes {
    LINE(2),CIRCLE(2),CAT(2),CUBICBEZIER(2),QUADBEZIER(2),FILLEDCUBE(2);

    private final int npoints;
    ShapeModes(int Npoints){
        npoints = Npoints;

    }

    public ShapeModes cycle(){
        int length = values().length;
        int next = ordinal()+1;
        if (next >=length){
            next -= length;
        }
        return values()[next];
    }

    public int NCONSTPOINTS(){
        return npoints;
    }

    public List<Vec3> getControlPointsFromConstructorPoints(List<BlockPos> constructor) {
        List<Vec3> control = new ArrayList<>();
        switch(this){
            case LINE, FILLEDCUBE, CIRCLE, CAT:
                return helpers.blockPostoVec3(constructor);
            case CUBICBEZIER:
                control.add(helpers.blockPostoVec3(constructor.get(0)));
                control.add(helpers.blockPostoVec3(constructor.get(1)).scale((double) 1 /3));
                control.add(helpers.blockPostoVec3(constructor.get(1)).scale((double) 2 /3));
                control.add(helpers.blockPostoVec3(constructor.get(1)));
                return control;
            case QUADBEZIER:
                control.add(helpers.blockPostoVec3(constructor.get(0)));
                control.add(helpers.blockPostoVec3(constructor.get(1)).scale((double) 1 /2));
                control.add(helpers.blockPostoVec3(constructor.get(1)));
                return control;
        }
        return control;
    }

    public List<BlockPos> getShapeFromControlPoints(List<Vec3> cp) {
        List<BlockPos> queue = new ArrayList<>();
        switch(this){
            case LINE:
                return new Bresenham3D().drawLine(new BlockPos(cp.get(0)),new BlockPos(cp.get(1)));
            case CIRCLE:
                Vec3 start = cp.get(0);
                Vec3 end = cp.get(1);
                double r = end.subtract(start).length();
                List<Tuple> circ2d = new Bresenham3D().plot2DCircle((int) start.x,(int) start.z,(int) r);
                for(Tuple t : circ2d){
                    queue.add(new BlockPos((int) t.getA(),(int) start.y,(int)t.getB()));
                }
                return queue;
            case CAT:
                double defaultlength = cp.get(1).subtract(cp.get(0)).length()*1.1;
                List<Vec3> vecs = new CatHelper().getCatPoints(cp.get(0),cp.get(1),defaultlength);
                List<BlockPos> pos = new ArrayList<>();
                for(Vec3 v : vecs){
                    queue.add(helpers.vec3toBlockPos(v));
                }
                return queue;

            case CUBICBEZIER:
                return new ParameterizedCubicBezier(cp).getblocks();
            case QUADBEZIER:
                return new ParameterizedQuadBezier(cp).getblocks();
            case FILLEDCUBE:
                Box area = new Box(helpers.vec3toBlockPos(cp.get(0)),helpers.vec3toBlockPos(cp.get(1)));
                return area.getBlockList();
        }

        return queue;
    }



}

