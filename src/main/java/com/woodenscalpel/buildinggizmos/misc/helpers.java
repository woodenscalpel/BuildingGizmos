package com.woodenscalpel.buildinggizmos.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class helpers {

    public static int[] BlockPostoIntArray(BlockPos pos){
        return new int[] {pos.getX(),pos.getY(),pos.getZ()};
    }
    public static BlockPos intarray2blockpos(int[] arr){
        return new BlockPos(arr[0],arr[1],arr[2]);
    }

    public static void putBlockList(CompoundTag tag, String tagname, ArrayList<BlockPos> poslist){
        int lenBlocks = poslist.size();
        int lenCoords = lenBlocks*3;

        int[] posintarray = new int[lenCoords];
        int i = 0;

        for(int block =0; block<lenBlocks; block++){
            for(int coord=0; coord<3;coord++,i++){
                switch(coord){
                    case 0:
                        posintarray[i] = poslist.get(block).getX();
                        break;
                    case 1:
                        posintarray[i] = poslist.get(block).getY();
                        break;
                    case 2:
                        posintarray[i] = poslist.get(block).getZ();
                        break;
                }

            }

        }

        tag.putIntArray(tagname,posintarray);

    }

    public static int[] arraySlice(int[] arr, int start, int end) {
        // Get the slice of the Array
        int[] slice = new int[end - start];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        // return the slice
        return slice;
    }

    public static TextureAtlasSprite getBlockTexture(Block block, Direction dir){

        BlockState state = block.defaultBlockState();

        BakedModel bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        if (bakedModel != null) {
            // LOGGER.info(String.valueOf(bakedModel));
            List<BakedQuad> quadList = bakedModel.getQuads(state, dir, RandomSource.createNewThreadLocalInstance());
            TextureAtlasSprite sprite = quadList.isEmpty() ? bakedModel.getParticleIcon()
                    : quadList.get(0).getSprite();
            return sprite;
        }
        return null;
    }

    public static BlockPos vec3toBlockPos(Vec3 vec){
       return new BlockPos(vec.x,vec.y,vec.z);
    }
    public static Vec3 blockPostoVec3(BlockPos pos){
        return new Vec3(pos.getX(),pos.getY(),pos.getZ());
    }

}
