package com.woodenscalpel.buildinggizmos.common.item.abstractwand;

import com.woodenscalpel.buildinggizmos.BuildingGizmos;
import com.woodenscalpel.buildinggizmos.client.ClientHooks;
import com.woodenscalpel.buildinggizmos.common.item.BuildWand.BuildShapes.ShapeModes;
import com.woodenscalpel.buildinggizmos.misc.Raycast;
import com.woodenscalpel.buildinggizmos.misc.enumnbt.enumNbt;
import com.woodenscalpel.buildinggizmos.misc.helpers;
import com.woodenscalpel.buildinggizmos.misc.shapes.Box;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class AbstractWand extends Item implements PaletteInterface{

    //State machine states
    static final int SELECTING = 0;
    static final int IN_USE = 1;

    //Palette modes
    static final int MODE_HOTBAR = 0;
    static final int MODE_PALLET = 1;
    static final String modeTag = "MODE";


    public AbstractWand(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    public void openPalletScreen(){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openTextureWandScreen);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 99999;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack item = pPlayer.getItemInHand(pUsedHand);
        CompoundTag nbt = item.getOrCreateTag();


        if(ShapeHelper.getShapeComplete(nbt)) {

            List<Vec3> controlPoints = AbstractWand.ShapeHelper.getControlPoints(nbt);
            List<ControlPoint> controlPointHitboxes = new ArrayList<>();

            Raycast rc = new Raycast();
            List<Vec3> nudgedControlPoints = new ArrayList<>();
            for (Vec3 cp : controlPoints) {
                ControlPoint tempcp = new ControlPoint(cp);
                Vec3 nudgedcp = cp.add(tempcp.getNudgeFromRaycast(rc));
                nudgedControlPoints.add(nudgedcp);
            }
            ShapeHelper.redrawShapeAndQueue(nbt, nudgedControlPoints);
        }




        pPlayer.startUsingItem(pUsedHand);
        // return InteractionResultHolder.consume(item);
        return ItemUtils.startUsingInstantly(pLevel,pPlayer,pUsedHand);
        //BuildingGizmos.LOGGER.info("USE");
        //return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        BuildingGizmos.LOGGER.info("USING");
        super.onUsingTick(stack, player, count);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        BuildingGizmos.LOGGER.info("USING2");
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        //LOGGER.info("USED ON");
        if(!context.getLevel().isClientSide() && context.getHand() == InteractionHand.MAIN_HAND) {

            ItemStack item = context.getItemInHand();
            CompoundTag nbt = item.getOrCreateTag();



            if (!Objects.requireNonNull(context.getPlayer()).isCrouching()) {
                if (ShapeHelper.getShapeComplete(nbt)) {
                    BuildingGizmos.LOGGER.info("SWAPPIGN");


                    //if(context.getItemInHand().getOrCreateTag().getInt("ready") == 1){
                    List<BlockPos> ConstPoints = ShapeHelper.getConstructorPoints(nbt);

                    BlockPos b1 = ConstPoints.get(0);
                    BlockPos b2 = ConstPoints.get(1);

                    Box area = new Box(b1,b2);

                    if (area.contains(context.getClickedPos())) {

                        nbt.putInt("state", IN_USE);
                    }
                    else{
                        BuildingGizmos.LOGGER.info("Not in area");
                    }

                }


            } else {
                switch (nbt.getInt("state")) {
                    case IN_USE:
                        break;
                    case SELECTING:
                        BlockPos p1 = context.getClickedPos();
                        //BuildingGizmos.LOGGER.info("SELECT" + p1.toString());
                        ShapeHelper.addConstructorPoint(nbt,p1);
                }

            }
        }
        return super.useOn(context);
    }


    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, Level level, @NotNull Entity entity, int i, boolean b) {
        if (!level.isClientSide()) {
            CompoundTag nbt = itemStack.getOrCreateTag();

            if (nbt.getInt("state") == IN_USE) {

                //LOGGER.info("USING!");
                List<BlockPos> blockQueue = ShapeHelper.getQueue(nbt);

                if(blockQueue.isEmpty()) {
                    nbt.putInt("state", SELECTING);
                }

            if (nbt.getInt("state") == IN_USE) {

                BlockPos nextblock = blockQueue.get(0);
                blockQueue.remove(0);
                ShapeHelper.setBuildQueue(nbt,blockQueue);

                if(entity instanceof Player player) {
                    processCoord(player,level,itemStack,nextblock);
                }



            }}
            super.inventoryTick(itemStack, level, entity, i, b);
        }
    }


    public void switchMode(Player player){
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        int CURRENT_MODE = nbt.getInt(modeTag);
        switch (CURRENT_MODE){
            case MODE_HOTBAR:

                //LOGGER.info("switching mode to 1");
                player.sendSystemMessage(Component.literal("Pressed Mode Switch"));

                nbt.putInt(modeTag,MODE_PALLET);
                //LOGGER.info(String.valueOf(nbt.getInt("MODE")));
                break;
            case MODE_PALLET:
                nbt.putInt(modeTag,MODE_HOTBAR);
                break;
        }
    }

    public List<ItemStack> getPaletteFromMode(Player player,ItemStack wand, int mode) {
        switch (mode) {
            case MODE_HOTBAR -> {
                return getHotbarpalette(player);
            }
            case MODE_PALLET -> {
                return getPaletteItems(wand);
            }
        }
        return null;
    }

    private List<ItemStack> getHotbarpalette(Player player) {
        List<ItemStack> blocks = new ArrayList<>();
        for(int i = 0; i<=8;i++) {
            ItemStack itemi =  player.getInventory().getItem(i);
            if(itemi.getItem() instanceof BlockItem){
                blocks.add(itemi);
            }
        }
        return  blocks;
    }

    private ItemStack RandomItemStack(List<ItemStack> list){

        if(list.isEmpty()){
            return null;
        }
        int rand = RandomSource.createNewThreadLocalInstance().nextInt(list.size());
        return list.get(rand);
    }

    protected ItemStack getRandomFromPallet(Player player, ItemStack itemStack){
        CompoundTag nbt = itemStack.getOrCreateTag();
        int MODE = nbt.getInt(modeTag);
        //LOGGER.info(String.valueOf(MODE));
        //LOGGER.info(Arrays.toString(nbt.getIntArray("palletIDs")));

        List<ItemStack> palette = getPaletteFromMode(player,itemStack,MODE);
        return RandomItemStack(palette);
    }

    protected ItemStack getGradientFromPallet(Player player, ItemStack itemStack,double percent){
        CompoundTag nbt = itemStack.getOrCreateTag();
        int MODE = nbt.getInt(modeTag);
        //LOGGER.info(String.valueOf(MODE));
        //LOGGER.info(Arrays.toString(nbt.getIntArray("palletIDs")));

        List<ItemStack> palette = getPaletteFromMode(player,itemStack,MODE);
        /*
        BuildingGizmos.LOGGER.info("WTF");
        BuildingGizmos.LOGGER.info(String.valueOf(palette.size()));
        BuildingGizmos.LOGGER.info(String.valueOf((palette.size()-1)));
        BuildingGizmos.LOGGER.info(String.valueOf(Math.floor(((palette.size()-1))*percent)));
         */
        return palette.get((int) Math.floor((palette.size()-1)*percent));
    }



    protected abstract void processCoord(Player player, Level level, ItemStack itemStack, BlockPos nextblock);


    public static class ShapeHelper {

        static String ConstructorPointsTag = "CONSTRUCTORPOINTS";
        static String ControlPointsTag = "CONTROLPOINTS";

        static String ShapeCompleteTag = "READY";
        static String PaletteModeTag = "PALETTEMODE";
        static String ShapeTag = "SHAPE";

        static String BuildQueueTag = "BUILDQUEUE";


        public static void SetShapeComplete(CompoundTag nbt, boolean ready){
            nbt.putBoolean(ShapeCompleteTag,ready);
        }

        public static boolean getShapeComplete(CompoundTag nbt){
            return nbt.getBoolean(ShapeCompleteTag);
        }

        public static void setBuildQueue(CompoundTag nbt,List<BlockPos> blocks){
            helpers.putBlockList(nbt,BuildQueueTag,blocks);
        }

        public static List<BlockPos> getQueue(CompoundTag nbt) {
            return helpers.getBlockList(nbt,BuildQueueTag);
        }

        public static List<BlockPos> getConstructorPoints(CompoundTag nbt) {
            return helpers.getBlockList(nbt, ConstructorPointsTag);
        }

        public static void setConstructorPoints(CompoundTag nbt, List<BlockPos> cp) {
            helpers.putBlockList(nbt, ConstructorPointsTag, cp);
        }

        public static List<Vec3> getControlPoints(CompoundTag nbt) {
            return helpers.getVecList(nbt, ControlPointsTag);
        }

        public static void setControlPoints(CompoundTag nbt, List<Vec3> cp) {
            helpers.putVecList(nbt, ControlPointsTag, cp);
        }


        public static ShapeModes getShape(CompoundTag nbt) {
            return enumNbt.getshapeenum(nbt, ShapeTag);
        }
        public static void setShape(CompoundTag nbt,ShapeModes s) {
            enumNbt.setshapeenum(s,nbt, ShapeTag);
        }

        public static void cycleShape(CompoundTag nbt) {
            setShape(nbt, getShape(nbt).cycle());
            SetShapeComplete(nbt,false);
        }


        public static void addConstructorPoint(CompoundTag nbt, BlockPos p1) {
            List<BlockPos> constPoints = getConstructorPoints(nbt);
            ShapeModes shape = getShape(nbt);
            int MaxN = shape.NCONSTPOINTS();
            int numpoints = constPoints.size();
            // BuildingGizmos.LOGGER.info("num" + numpoints);

            if (numpoints + 1 < MaxN) {
                constPoints.add(p1);
                setConstructorPoints(nbt, constPoints);
            } else if (numpoints + 1 == MaxN) {
                //Shape Is Ready!
                constPoints.add(p1);
                setConstructorPoints(nbt, constPoints);
                List<Vec3> cp = shape.getControlPointsFromConstructorPoints(constPoints);
               // BuildingGizmos.LOGGER.info(String.valueOf(cp));
                setControlPoints(nbt,cp);
                ConstructShapeAndSetQueue(nbt);
                SetShapeComplete(nbt,true);
              //  BuildingGizmos.LOGGER.info(String.valueOf(getShapeComplete(nbt)));
            } else { // CLEAR CONTROL POINTS AND SET THIS ONE AS FIRST ONE
                SetShapeComplete(nbt,false);
                List<BlockPos> newlist = new ArrayList<>();
                newlist.add(p1);
                setConstructorPoints(nbt, newlist);
            }
        }
        public static void ConstructShapeAndSetQueue(CompoundTag nbt){
            List<Vec3> cp = getControlPoints(nbt);
           // BuildingGizmos.LOGGER.info(String.valueOf(cp));
            ShapeModes shape = getShape(nbt);
            List<BlockPos> blocks = shape.getShapeFromControlPoints(cp);
           // BuildingGizmos.LOGGER.info(String.valueOf(blocks));
            nbt.putInt("queueLen",blocks.size()); //TODO hack for gradient
            setBuildQueue(nbt,blocks);
        }
        public static void redrawShapeAndQueue(CompoundTag nbt, List<Vec3> cp){
            setControlPoints(nbt,cp);
            ShapeModes shape = getShape(nbt);
            List<BlockPos> blocks = shape.getShapeFromControlPoints(cp);
            nbt.putInt("queueLen",blocks.size()); //TODO hack for gradient
            setBuildQueue(nbt,blocks);
        }

    }
}

