package com.woodenscalpel.buildinggizmos.misc.shapes;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.ArrayList;


public class Vec3Box {

    private static final Logger LOGGER = LogUtils.getLogger();

    double minx,miny,minz,maxx,maxy,maxz;


    public Vec3Box(Vec3 p1, Vec3 p2){
        //pull out coords
        double x1 = p1.x;
        double y1 = p1.y;
        double z1 = p1.z;
        double x2 = p2.x;
        double y2 = p2.y;
        double z2 = p2.z;

        //get new coords from min to max
        minx = Math.min(x1,x2);
        miny = Math.min(y1,y2);
        minz = Math.min(z1,z2);
        maxx = Math.max(x1,x2);
        maxy = Math.max(y1,y2);
        maxz = Math.max(z1,z2);
    }

    public Vec3Box(int[] b1, int[] b2) {
        this(new Vec3(b1[0],b1[1],b1[2]), new Vec3(b2[0],b2[1],b2[2]));
    }


    public AABB renderBox(){
        return new AABB(minx,miny,minz,maxx,maxy,maxz);
    }

    public boolean contains(Vec3 r) {


        return((minx <= r.x && r.x <= maxx)&& (miny <= r.y && r.y <= maxy) &&( minz <= r.z && r.z <= maxz));
    }
}
