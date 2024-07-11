package com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw;

import com.woodenscalpel.buildinggizmos.misc.CatenaryHelper.CatHelper;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class UnoptomizedCatDraw {
    public UnoptomizedCatDraw(){}

    public List<BlockPos> getblocks(BlockPos start, BlockPos end){

        List<BlockPos> result = new ArrayList<>();
        result.add(start);

        BlockPos current = start;

        double tests = Math.sqrt(start.distSqr(new Vec3i(end.getX(),end.getY(),end.getZ())))*1.1;

        List<Vec3> points = new CatHelper().getCatPoints(helpers.blockPostoVec3(start), helpers.blockPostoVec3(end),tests);

        for(Vec3 point: points){

            BlockPos closestBlock = closest(point, current);
            if(closestBlock != current){
                result.add(closestBlock);
                current = closestBlock;
            }

        }
        return result;
    }
    private BlockPos closest(Vec3 point, BlockPos current){

        double closestdistance = point.distanceTo(new Vec3(current.getX(),current.getY(),current.getZ()));
        BlockPos closestblock = current;
       for(int x = current.getX()-1; x<= current.getX()+1;x++){
           for(int y = current.getZ()-1; y<= current.getY()+1;y++){
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
