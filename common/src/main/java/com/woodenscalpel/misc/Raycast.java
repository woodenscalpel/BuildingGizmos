package com.woodenscalpel.misc;

import com.woodenscalpel.misc.shapes.Vec3Box;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Raycast {
    List<Vec3> points;
    public Raycast(){
        this.points = new ArrayList<>();
        double maxrange = 50;
        double resolution = 50; //50 points a block is probably overkill


        Vec3 campos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
        Vector3f look = Minecraft.getInstance().getEntityRenderDispatcher().camera.getLookVector();
        //BuildingGizmos.LOGGER.info(String.valueOf(look));

        for(float i = 0; i < maxrange ; i +=  1/resolution){
            points.add(campos.add(new Vec3(look.x()*i,look.y()*i, look.z()*i)));
        }

    }
    public List<Vec3> getPoints(){
        return points;
    }

    public boolean vec3BoxHit(Vec3Box v){
        for(Vec3 p : points){
            if (v.contains(p)){
                return true;
            }
        }
        return false;
    }

}
