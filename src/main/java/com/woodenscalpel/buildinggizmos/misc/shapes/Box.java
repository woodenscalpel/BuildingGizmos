package com.woodenscalpel.buildinggizmos.misc.shapes;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class Box {

    private static final Logger LOGGER = LogUtils.getLogger();

    ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
    int minx,miny,minz,maxx,maxy,maxz;


    public Box(BlockPos p1, BlockPos p2){
        //pull out coords
        int x1 = p1.getX();
        int y1 = p1.getY();
        int z1 = p1.getZ();
        int x2 = p2.getX();
        int y2 = p2.getY();
        int z2 = p2.getZ();

        //get new coords from min to max
        minx = Math.min(x1,x2);
        miny = Math.min(y1,y2);
        minz = Math.min(z1,z2);
        maxx = Math.max(x1,x2);
        maxy = Math.max(y1,y2);
        maxz = Math.max(z1,z2);



        //construct block list
        for(int i = minx; i <= maxx; i++){
            for(int j = miny; j <= maxy; j++){
                for(int k = minz; k <= maxz; k++) {
                    blocks.add(new BlockPos(i,j,k));
                }}}

    }

    public Box(int[] b1, int[] b2) {
        this(new BlockPos(b1[0],b1[1],b1[2]), new BlockPos(b2[0],b2[1],b2[2]));
    }

    public Box(Vec3 vec3, Vec3 vec31) {
        this(new int[] {(int) vec3.x, (int) vec3.y, (int) vec3.z}, new int[] {(int) vec31.x, (int) vec31.y, (int) vec31.z});
    }


    public ArrayList<BlockPos> getBlockList(){
        return blocks;
    }

    public boolean contains(BlockPos pos){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return((minx <= x && x <= maxx)&& (miny <= y && y <= maxy) &&( minz <= z && z <= maxz));
    }

    public AABB renderBox(){
        return new AABB(minx,miny,minz,maxx+1,maxy+1,maxz+1);
    }

    public boolean contains(Vec3 r) {


        return((minx <= r.x && r.x <= maxx+1)&& (miny <= r.y && r.y <= maxy+1) &&( minz <= r.z && r.z <= maxz+1));
    }
}
