package com.woodenscalpel.buildinggizmos.misc;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class getTextures {
    private static final Logger LOGGER = LogUtils.getLogger();

    public List<Pair<Block, int[]>> getTextures() {
        LOGGER.info("CALLED GETTEXTURES");
        List <Pair<Block,int[]>> list = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS) {
            for(Direction dir : new Direction[]{Direction.UP, Direction.NORTH}) {
            BlockState state = block.defaultBlockState();
           // for (BlockState state : block.getStateDefinition().getPossibleStates()) {

                    //System.out.println(block.toString());

                    BakedModel bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
                    if (bakedModel != null) {
                       // LOGGER.info(String.valueOf(bakedModel));
                        List<BakedQuad> quadList = bakedModel.getQuads(state, dir, RandomSource.createNewThreadLocalInstance());
                        TextureAtlasSprite sprite = quadList.isEmpty() ? bakedModel.getParticleIcon()
                                : quadList.get(0).getSprite();
                        if (sprite == null) {
                            System.out.println("null");

                            // If the sprite exists, parse its pixel information.
                        } else {
                            //LOGGER.info(sprite.toString());
                            int h = sprite.getHeight();
                            int w = sprite.getWidth();

                            int[] accum = {0,0,0,0};

                            for(int i = 0; i<w;i++){
                                for(int j = 0; j<h;j++) {

                                    int[] pixel = getRBGA(sprite.getPixelRGBA(0, i, j));
                                    accum = new int[]{accum[0] + pixel[0], accum[1] + pixel[1], accum[2] + pixel[2], accum[3] + pixel[3]};
                                }
                            }
                            accum = new int[]{accum[0]/(h*w), accum[1] / (h*w), accum[2] /(h*w), accum[3] / (h*w)};
                            //Pair<ResourceLocation,int[]> pair = new Pair<>(sprite.getName(), accum);
                            Pair<Block,int[]> pair = new Pair<>(block, accum);
                            list.add(pair);
                            //LOGGER.debug(pair.toString());
                            //LOGGER.info(sprite.getName().toString());
                            //LOGGER.info(Arrays.toString(accum));
                            //LOGGER.info(String.valueOf(w));
                            //LOGGER.info(String.valueOf(sprite.getPixelRGBA(0,1,1)));
                            //LOGGER.info(Arrays.toString(getRBGA(sprite.getPixelRGBA(0, 1, 1))));
                        }
                    }

               }


            }
        return list;

        }


        int[] getRBGA(int rgbaint){
            int a = (rgbaint & 0xFF000000) >> 24;
            int r = (rgbaint & 0x00FF0000) >> 16;
            int g = (rgbaint & 0x0000FF00) >> 8;
            int b = (rgbaint & 0x000000FF);
            return new int[]{b, g, r, a}; //r g b a. Dont think about it.

        }

    }