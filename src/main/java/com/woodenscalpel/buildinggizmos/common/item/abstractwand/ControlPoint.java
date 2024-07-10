package com.woodenscalpel.buildinggizmos.common.item.abstractwand;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.misc.Raycast;
import com.woodenscalpel.buildinggizmos.misc.shapes.Vec3Box;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ControlPoint {
    Vec3 centerpoint;
    double centerradius = 0.5;
    double arrowx = 0.2;
    double arrowy = 0.6;

    Vec3Box centerbox,upbox,downbox,northbox,southbox,eastbox,westbox;

    public ControlPoint(Vec3 center){
        centerpoint = center;

        //construct boxes
        centerbox = new Vec3Box(centerpoint.subtract(-centerradius,-centerradius,-centerradius), centerpoint.add(centerradius,centerradius,centerradius));

    }

    public void handleRaycast(Raycast raycast){
        if(raycast.vec3BoxHit(centerbox)){
            BuildingGizmos.LOGGER.info("HIT CENTER");
        }

    }


}
