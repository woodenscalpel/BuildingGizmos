package com.woodenscalpel.misc.Quantization.UnoptimizedFunctionDraw;

import com.woodenscalpel.misc.helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class ParameterizedNormalizedCurve {
    List<Vec3> points;
    public ParameterizedNormalizedCurve(List<Vec3> points){
        this.points = points;
    }

    abstract Vec3 f(double t);

    public List<BlockPos> getblocks(BlockPos start){

        List<BlockPos> result = new ArrayList<>();
        result.add(start);

        BlockPos current = start;

        for(float t = 0; t<1; t+= 0.001F){

            Vec3 point = this.f(t);
            BlockPos closestBlock = closest(point, current);
            //BuildingGizmos.LOGGER.info(point.toString());
           // BuildingGizmos.LOGGER.info(current.toString());
           if(closestBlock != current){
                result.add(closestBlock);
                current = closestBlock;
            }

        }
        return result;
    }

    public List<BlockPos> getblocks() {
        BlockPos start = helpers.vec3toBlockPos(this.points.get(0));
        return getblocks(start);
    }
    BlockPos closest(Vec3 point, BlockPos current){

        double closestdistance = point.distanceTo(new Vec3(current.getX(),current.getY(),current.getZ()));
        BlockPos closestblock = current;
        for(int x = current.getX()-1; x<= current.getX()+1;x++){
            for(int y = current.getY()-1; y<= current.getY()+1;y++){
                for(int z = current.getZ()-1; z<= current.getZ()+1;z++){
                    double distance = point.distanceTo(new Vec3(x,y,z));
                    if(distance < closestdistance){
                        closestdistance = distance;
                        closestblock = new BlockPos(x,y,z);
                    }

                }}}
        return closestblock;
    }
}
