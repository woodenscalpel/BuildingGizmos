package com.woodenscalpel.buildinggizmos.misc.Quantization;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Bresenham3D {

    public List<BlockPos> drawLine(BlockPos start, BlockPos end){
        List<BlockPos> line = plotLine3d(start.getX(),start.getY(),start.getZ(),end.getX(),end.getY(),end.getZ());
        return line;
    }

    private List<BlockPos> plotLine3d(int x0, int y0, int z0, int x1, int y1, int z1)
    //http://members.chello.at/~easyfilter/bresenham.html
    {
        int dx = Math.abs(x1-x0), sx = x0<x1 ? 1 : -1;
        int dy = Math.abs(y1-y0), sy = y0<y1 ? 1 : -1;
        int dz = Math.abs(z1-z0), sz = z0<z1 ? 1 : -1;
        int dm = Math.max(Math.max(dx,dy),dz), i = dm; /* maximum difference */
        x1 = y1 = z1 = dm/2; /* error offset */

        List<BlockPos> blockPosList = new ArrayList<>();
        for(;;) {  /* loop */
            blockPosList.add(new BlockPos(x0,y0,z0));
            if (i-- == 0) break;
            x1 -= dx; if (x1 < 0) { x1 += dm; x0 += sx; }
            y1 -= dy; if (y1 < 0) { y1 += dm; y0 += sy; }
            z1 -= dz; if (z1 < 0) { z1 += dm; z0 += sz; }
        }
        return  blockPosList;
    }


}
