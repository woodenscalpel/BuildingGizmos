package com.woodenscalpel.buildinggizmos.common.item.abstractwand;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.misc.Raycast;
import com.woodenscalpel.buildinggizmos.misc.shapes.Vec3Box;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//North : -z
//East : +x
//South : +z
//West : -x
public class ControlPoint {
    Vec3 centerpoint;
    double centerradius = 0.2;
    double arrowx = 0.05;
    double arrowy = 0.3;

    public int idx;

    Vec3Box centerbox,upbox,downbox,northbox,southbox,eastbox,westbox;
    List<Vec3Box> boxes;

    public ControlPoint(Vec3 center){
        centerpoint = center;

        //construct boxes
        centerbox = new Vec3Box(centerpoint.subtract(centerradius,centerradius,centerradius), centerpoint.add(centerradius,centerradius,centerradius));
        upbox = new Vec3Box(centerpoint.add(0,centerradius,0).subtract(arrowx,0,arrowx), centerpoint.add(0,centerradius,0).add(arrowx,arrowy,arrowx));
        downbox = new Vec3Box(centerpoint.add(0,-centerradius,0).subtract(arrowx,0,arrowx), centerpoint.add(0,-centerradius,0).add(arrowx,-arrowy,arrowx));
        northbox = new Vec3Box(centerpoint.add(0,0,-centerradius).subtract(arrowx,arrowx,0), centerpoint.add(0,0,-centerradius).add(arrowx,arrowx,-arrowy));
        southbox = new Vec3Box(centerpoint.add(0,0,centerradius).subtract(arrowx,arrowx,0), centerpoint.add(0,0,centerradius).add(arrowx,arrowx,arrowy));
        eastbox = new Vec3Box(centerpoint.add(centerradius,0,0).subtract(0,arrowx,arrowx), centerpoint.add(centerradius,0,0).add(arrowy,arrowx,arrowx));
        westbox = new Vec3Box(centerpoint.add(-centerradius,0,0).subtract(0,arrowx,arrowx), centerpoint.add(-centerradius,0,0).add(-arrowy,arrowx,arrowx));

        //boxes = Arrays.asList(centerbox, upbox, downbox, northbox, southbox, eastbox, westbox);
        boxes = Arrays.asList(centerbox,upbox,downbox,northbox,southbox,eastbox,westbox);
    }

    public Vec3 getNudgeFromRaycast(Raycast raycast){
        double nudge = 1;
        Vec3 nudgevec = new Vec3(0,0,0);
        for(Vec3 r : raycast.getPoints()){
            if(upbox.contains(r)){
                return nudgevec.add(new Vec3(0,nudge,0));
            }
            if(downbox.contains(r)){
                return nudgevec.add(new Vec3(0,-nudge,0));
            }
            if(eastbox.contains(r)){
                return nudgevec.add(new Vec3(nudge,0,0));
            }
            if(westbox.contains(r)){
                return nudgevec.add(new Vec3(-nudge,0,0));
            }
            if(northbox.contains(r)){
                return nudgevec.add(new Vec3(0,0,-nudge));
            }
            if(southbox.contains(r)){
                return nudgevec.add(new Vec3(0,0,nudge));
            }
        }
        return nudgevec;
    }

    public List<Vec3Box> getBoxes() {
        return boxes;
    }

    public List<AABB> getAABB(){
        List<AABB> bounding = new ArrayList<>();
        for(Vec3Box b : boxes){
           bounding.add(b.getAABB());
        }
        return bounding;
    }
}
