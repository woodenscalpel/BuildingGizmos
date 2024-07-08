package com.woodenscalpel.buildinggizmos.common.item.BuildWand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.Catenary;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.Circle;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.Line;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.ShapeModes;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedCircle;
import com.woodenscalpel.buildinggizmos.misc.enumnbt.enumNbt;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;



public class BuildWand extends AbstractWand {
    private static final Logger LOGGER = LogUtils.getLogger();

    int TESTLINE = 0;
    int CURRENTSHAPE = 0;
    ShapeModes currentShape = ShapeModes.LINE;



    public BuildWand(Properties properties) {
        super(properties.stacksTo(1));
    }


    public void openPalletScreen(){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openTextureWandScreen);
    }

    @Override
    protected void processCoord(Player player, Level level, ItemStack wand, BlockPos nextblock) {

        ItemStack item = getRandomFromPallet(player,wand);

        //WorldInventoryInterface.placeBlock(player, Blocks.STONE.asItem().getDefaultInstance(),level,nextblock);
        WorldInventoryInterface.safePlaceBlock(player, item ,level,nextblock);

    }

    @Override
    protected void setBlockQueue(List<BlockPos> controlPoints, CompoundTag nbt) {
        BlockPos p1 = controlPoints.get(0);
        BlockPos p2 = controlPoints.get(1);


        //List<BlockPos> linepos = new Bresenham3D().drawLine(p1,p2);

        //List<BlockPos> linepos = new Line(p1,p2).getCoords();
        //List<BlockPos> linepos = new UnoptomizedCatDraw().getblocks(p1,p2);

        ShapeModes shape = enumNbt.getshapeenum(nbt,"BUILDMODE");

        List<BlockPos> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);

        List<BlockPos> queue = new ArrayList<>();

        switch(shape){
            case LINE:
                queue = new Line(p1,p2).getCoords();
                break;
            case CIRCLE:
                queue = new Circle(p1,p2).getCoords();
                break;
            case CAT:
                double defaultlength = helpers.blockPostoVec3(p2).subtract(helpers.blockPostoVec3(p1)).length()*1.1;
                queue = new Catenary(p1,p2,defaultlength).getCoords();
                break;
        }



        helpers.putBlockList(nbt,"blockQueue", (ArrayList<BlockPos>) queue);
    }

    public void switchBuildMode(Player player) {
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        ShapeModes shape = enumNbt.getshapeenum(nbt,"BUILDMODE");
        shape = shape.cycle();
        enumNbt.setshapeenum(shape,nbt,"BUILDMODE");

        player.sendSystemMessage(Component.literal("Pressed Mode Switch"));
    }
}

