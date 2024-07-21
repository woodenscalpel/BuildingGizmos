package com.woodenscalpel.common.item.palettescreen;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.woodenscalpel.BuildingGizmos;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class PaletteData {

    private static final Logger LOGGER = LogUtils.getLogger();

    List<Pair<Block, int[]>> texList = this.texList; // list of blocks and rgba values

    public PaletteData(){

            List <Pair<Block,int[]>> list = new ArrayList<>();
            //for (Block block : ForgeRegistries.BLOCKS) {
                for (Block block : BuiltInRegistries.BLOCK.stream().toList()) {
                for(Direction dir : new Direction[]{Direction.NORTH}) {
                    BlockState state = block.defaultBlockState();

                    BakedModel bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
                    if (bakedModel != null) {
                        List<BakedQuad> quadList = bakedModel.getQuads(state, dir, RandomSource.createNewThreadLocalInstance());
                        TextureAtlasSprite sprite = quadList.isEmpty() ? bakedModel.getParticleIcon()
                                : quadList.get(0).getSprite();
                        if (sprite == null) {
                            System.out.println("null");

                            // If the sprite exists, parse its pixel information.
                        } else {
                            //LOGGER.info(sprite.toString());
                            int h = sprite.contents().height();
                            int w = sprite.contents().width();


                            int[] accum = {0,0,0,0};

                            for(int i = 0; i<w;i++){
                                for(int j = 0; j<h;j++) {

                                    //int[] pixel = new int[] {125,RandomSource.create().nextIntBetweenInclusive(0,100),RandomSource.create().nextIntBetweenInclusive(0,100),125};
                                    int[] pixel = getRBGA(sprite.contents().originalImage.getPixelRGBA(i, j));
                                    accum = new int[]{accum[0] + pixel[0], accum[1] + pixel[1], accum[2] + pixel[2], accum[3] + pixel[3]};
                                }
                            }
                            accum = new int[]{accum[0]/(h*w), accum[1] / (h*w), accum[2] /(h*w), accum[3] / (h*w)};
                            //Pair<ResourceLocation,int[]> pair = new Pair<>(sprite.getName(), accum);
                            Pair<Block,int[]> pair = new Pair<>(block, accum);

                            //check for duplicates TODO deal with collisions instead of ignoring them
                            if(isdifferentrgb(list,pair)) {
                                list.add(pair);
                            }
                        }
                    }

                }




        }

        this.texList = list;

    }

    private boolean isdifferentrgb(List<Pair<Block,int[]>> list, Pair<Block,int[]> pair) {
        boolean different = true;

        for(Pair<Block,int[]>item : list){
            if(item.getSecond()[0] == pair.getSecond()[0] && item.getSecond()[1] == pair.getSecond()[1] && item.getSecond()[2] == pair.getSecond()[2]){
                different = false;
            }
        }

        return different;
    }

    int[] getRBGA(int rgbaint) {
        int a = (rgbaint & 0xFF000000) >> 24;
        int r = (rgbaint & 0x00FF0000) >> 16;
        int g = (rgbaint & 0x0000FF00) >> 8;
        int b = (rgbaint & 0x000000FF);
        return new int[]{b, g, r, a}; //r g b a. Dont think about it.

    }

        public List<Pair<Block, int[]>> getBlockRGBlist(){
        return texList;
    }

    public int[] getBlockRGB(Block block) {
        for(Pair<Block, int[]> pair : texList){
            if(pair.getFirst() == block){
                return pair.getSecond();
            }
        }
        return new int[] {0,0,0,0};
    }

    public Block findNearest(int[] invrgba){

        texList.sort((blockPair, t1) -> {

            //LOGGER.info(String.valueOf(blockPair));
            //LOGGER.info(String.valueOf(t1));
            int[] rgba1 = blockPair.getSecond();
            int[] rgba2 = t1.getSecond();
            float distance1 = (float) Math.sqrt(Math.pow((rgba1[0] - invrgba[0]), 2) + Math.pow((rgba1[1] - invrgba[1]), 2) + Math.pow((rgba1[2] - invrgba[2]), 2));
            float distance2 = (float) Math.sqrt(Math.pow((rgba2[0] - invrgba[0]), 2) + Math.pow((rgba2[1] - invrgba[1]), 2) + Math.pow((rgba2[2] - invrgba[2]), 2));
            //LOGGER.info(String.valueOf(distance1));
            return Float.compare(distance2, distance1);
        });
        Block candidate = texList.get(texList.size()-1).getFirst();
        //LOGGER.info(String.valueOf(candidate));
        return candidate;
    }

    public List<Pair<Block, int[]>> getGradient(Block Block1, Block Block2, double tolerance){
        /*
        Offset by p1
        project onto vector p2
        filter from [0,length(p2)] (along line)
        filter from tolerance to line (distance)

        TODO
         A different implementation to project point P to line AB is
         A + dot(AP,AB) / dot(AB,AB) * AB
         Maybe Better?
         */

        int[] p0 = this.getBlockRGB(Block1);
        int[] p1 = this.getBlockRGB(Block2);
        int[] p1offset = new int[] {p1[0]-p0[0],p1[1]-p0[1],p1[2]-p0[2]};
        double p1dotp1 = Math.pow(p1offset[0],2) + Math.pow(p1offset[1],2 )+Math.pow(p1offset[2],2 );

        List<Pair<Block, int[]>> dummylist = new ArrayList<>();


        texList.sort(((t0, t1) -> {

            //offset by p1
            int[] offset0 = new int[] {t0.getSecond()[0]-p0[0],t0.getSecond()[1]-p0[1],t0.getSecond()[2]-p0[2]};
            int[] offset1 = new int[] {t1.getSecond()[0]-p0[0],t1.getSecond()[1]-p0[1],t1.getSecond()[2]-p0[2]};

            //sort by projection onto p1-p0;
            int proj0 = offset0[0]*p1offset[0]+offset0[1]*p1offset[1]+offset0[2]*p1offset[2];
            int proj1 = offset1[0]*p1offset[0]+offset1[1]*p1offset[1]+offset1[2]*p1offset[2];

            if (proj1>proj0){return 1;}
            if (proj1==proj0){return 0;}
            return -1;

        }));

        //LOGGER.info(String.valueOf(texList.size()));

        dummylist = texList.stream().filter(t0 -> {

            Vec3 a = new Vec3(p0[0],p0[1],p0[2]);
            Vec3 b = new Vec3(p1[0],p1[1],p1[2]);
            Vec3 p = new Vec3(t0.getSecond()[0],t0.getSecond()[1],t0.getSecond()[2]);

            Vec3 ap = p.subtract(a);
            Vec3 ab = b.subtract(a);
            //Vec3 pProjab = a.add(ab.scale(ap.dot(ab) / ab.dot(ab)));
            Vec3 pProjab = ab.scale(ap.dot(ab) / ab.dot(ab));
            double lenpProjab = pProjab.length();

            int[] offset0 = new int[] {t0.getSecond()[0]-p0[0],t0.getSecond()[1]-p0[1],t0.getSecond()[2]-p0[2]};
            int proj0 = offset0[0]*p1offset[0]+offset0[1]*p1offset[1]+offset0[2]*p1offset[2];

            double lenoff0 = Math.sqrt(Math.pow(offset0[0],2) + Math.pow(offset0[1],2 )+Math.pow(offset0[2],2 ));
            double radius = Math.sqrt(Math.pow(lenoff0,2) - pProjab.lengthSqr());

            if (proj0 < 0) return false;
            else if (proj0 > p1dotp1) return false;
            else if (radius > tolerance) return false;
            else {

                return true;
            }


        }).toList();



        return dummylist;
    }

}
