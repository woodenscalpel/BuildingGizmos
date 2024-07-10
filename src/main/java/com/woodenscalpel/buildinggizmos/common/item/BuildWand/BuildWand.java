package com.woodenscalpel.buildinggizmos.common.item.BuildWand;

import com.mojang.logging.LogUtils;
import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.*;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.AbstractWand;
import com.woodenscalpel.buildinggizmos.common.item.abstractwand.ControlPoint;
import com.woodenscalpel.buildinggizmos.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedCircle;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedCubicBezier;
import com.woodenscalpel.buildinggizmos.misc.Quantization.UnoptimizedFunctionDraw.ParameterizedQuadBezier;
import com.woodenscalpel.buildinggizmos.misc.Raycast;
import com.woodenscalpel.buildinggizmos.misc.enumnbt.enumNbt;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ControlPoint testbox = new ControlPoint(new Vec3(0,100,0));

        testbox.handleRaycast(new Raycast());

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    protected void processCoord(Player player, Level level, ItemStack wand, BlockPos nextblock) {
        //ItemStack item = getRandomFromPallet(player,wand);

        int queuelen = wand.getOrCreateTag().getInt("queueLen");
        int blocklen = (wand.getOrCreateTag().getIntArray("blockQueue").length+3)/3;

        BuildingGizmos.LOGGER.info("LOG");
        BuildingGizmos.LOGGER.info(String.valueOf(queuelen));
        BuildingGizmos.LOGGER.info(String.valueOf(blocklen));

        float percentage = (float) blocklen/queuelen;

        BuildingGizmos.LOGGER.info(String.valueOf(percentage));

        ItemStack item = getGradientFromPallet(player,wand,percentage);

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
            case CUBICBEZIER:
                queue = new ParameterizedCubicBezier(controlPoints).getblocks();
                break;
            case QUADBEZIER:
                queue = new QuadBez(controlPoints).getCoords();
                BuildingGizmos.LOGGER.info(queue.toString());
                break;
        }

        //Hack to determine gradient at runtime by storing total length of list and then can determine how far you are through the list by comparing current length of queue to original length
        nbt.putInt("queueLen",queue.size());

        helpers.putBlockList(nbt,"blockQueue", (ArrayList<BlockPos>) queue);
    }

    public void switchBuildMode(Player player) {
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        ShapeModes shape = enumNbt.getshapeenum(nbt,"BUILDMODE");
        shape = shape.cycle();
        enumNbt.setshapeenum(shape,nbt,"BUILDMODE");

        switch(shape){
            case LINE:
                setNumControlPoints(nbt,2);
                break;
            case CIRCLE:
                setNumControlPoints(nbt,2);
                break;
            case CAT:
                setNumControlPoints(nbt,2);
                break;
            case CUBICBEZIER:
                setNumControlPoints(nbt,4);
                break;
            case QUADBEZIER:
                setNumControlPoints(nbt,3);
                break;
        }

        player.sendSystemMessage(Component.literal("Pressed Mode Switch"));
    }
}

