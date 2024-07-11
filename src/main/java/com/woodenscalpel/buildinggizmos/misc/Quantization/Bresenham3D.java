package com.woodenscalpel.buildinggizmos.misc.Quantization;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Bresenham3D {

    public List<BlockPos> drawLine(BlockPos start, BlockPos end){
        return plotLine3d(start.getX(),start.getY(),start.getZ(),end.getX(),end.getY(),end.getZ());
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
    public List<Tuple> plot2DCircle(int xm, int ym, int r)
    //http://members.chello.at/~easyfilter/bresenham.html
    {
        List<Tuple> points = new ArrayList<>();
        int x = -r, y = 0, err = 2-2*r;                /* bottom left to top right */
        do {
            points.add(new Tuple(xm-x, ym+y));                            /*   I. Quadrant +x +y */
            points.add(new Tuple(xm-y, ym-x));                            /*  II. Quadrant -x +y */
            points.add(new Tuple(xm+x, ym-y));                            /* III. Quadrant -x -y */
            points.add(new Tuple(xm+y, ym+x));                            /*  IV. Quadrant +x -y */
            r = err;
            if (r <= y) err += ++y*2+1;                                   /* y step */
            if (r > x || err > y) err += ++x*2+1;                         /* x step */
        } while (x < 0);
        return points;
    }


}
