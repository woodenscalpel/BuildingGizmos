package com.woodenscalpel.common.item.BuildWand.BuildShapes;

import com.woodenscalpel.misc.CatenaryHelper.CatHelper;
import com.woodenscalpel.misc.Quantization.Bresenham3D;
import com.woodenscalpel.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedCubicBezier;
import com.woodenscalpel.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedQuadBezier;
import com.woodenscalpel.misc.helpers;
import com.woodenscalpel.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.woodenscalpel.misc.helpers.blockPostoVec3;


public enum ShapeModes {
    LINE(2,"shapeline"),CIRCLE(2,"shapecircle"),CAT(2,"shapecat"),CUBICBEZIER(2,"shapebez3"),QUADBEZIER(2,"shapebez2"),FILLEDCUBE(2,"shapecube");

    private final int npoints;
    public final String name;
    ShapeModes(int Npoints, String pname){
        npoints = Npoints;
        name = "buildinggizmos."+pname;

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
        Vec3 lenvec;
        switch(this){
            case LINE, FILLEDCUBE, CIRCLE, CAT:
                return blockPostoVec3(constructor);
            case CUBICBEZIER:
                control.add(blockPostoVec3(constructor.get(0)));
                lenvec = blockPostoVec3(constructor.get(1)).subtract(blockPostoVec3(constructor.get(0)));
                control.add(blockPostoVec3(constructor.get(0)).add(lenvec.scale(0.3333)));
                control.add(blockPostoVec3(constructor.get(0)).add(lenvec.scale(0.6666)));
                control.add(blockPostoVec3(constructor.get(1)));
                return control;
            case QUADBEZIER:
                control.add(blockPostoVec3(constructor.get(0)));
                lenvec = blockPostoVec3(constructor.get(1)).subtract(blockPostoVec3(constructor.get(0)));
                control.add(blockPostoVec3(constructor.get(0)).add(lenvec.scale(0.5)));
                control.add(blockPostoVec3(constructor.get(1)));
                return control;
        }
        return control;
    }

    public List<BlockPos> getShapeFromControlPoints(List<Vec3> cp) {
        List<BlockPos> queue = new ArrayList<>();
        switch(this){
            case LINE:
                return new Bresenham3D().drawLine(new BlockPos((int) cp.get(0).x, (int) cp.get(0).y, (int) cp.get(0).z),new BlockPos((int) cp.get(1).x, (int) cp.get(1).y, (int) cp.get(1).z));
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

