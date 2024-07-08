package com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes;

import com.woodenscalpel.buildinggizmos.misc.CatenaryHelper.CatHelper;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catenary extends AbstractShape{
    public Catenary(BlockPos p1, BlockPos p2, double length){
        List<Vec3> vecs = new CatHelper().getCatPoints(p1.getX(),p1.getY(),p1.getZ(),p2.getX(),p2.getY(),p2.getZ(),length);
        List<BlockPos> pos = new ArrayList<>();
        for(Vec3 v : vecs){
            pos.add(helpers.vec3toBlockPos(v));
        }
        coords = pos;
    }
}
