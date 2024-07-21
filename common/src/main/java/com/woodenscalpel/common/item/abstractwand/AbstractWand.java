package com.woodenscalpel.common.item.abstractwand;

import com.woodenscalpel.client.ClientHooks;
import com.woodenscalpel.client.keys.KeyBinding;
import com.woodenscalpel.common.item.BuildWand.BuildShapes.ShapeModes;
import com.woodenscalpel.common.item.texturewand.TextureWand;
import com.woodenscalpel.common.item.texturewand.TextureWandPaletteScreen;
import com.woodenscalpel.misc.InteractionLayer.WorldInventoryInterface;
import com.woodenscalpel.misc.Raycast;
import com.woodenscalpel.misc.enumnbt.enumNbt;
import com.woodenscalpel.misc.helpers;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class AbstractWand extends Item implements PaletteInterface{

    //State machine states
    protected static final int SELECTING = 0;
    protected static final int IN_USE = 1;
    protected static final String activeStateTag = "state";

    //Palette modes
    static final String modeTag = "MODE";

    //Placement Modes
    static final String placementModeTag = "PLACEMENTMODE";
    //Swap Modes
    static final String swapModeTag = "SWAPMODE";

    public AbstractWand(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    public void openPaletteScreen(){
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> ClientHooks::openTextureWandScreen);
        if(Minecraft.getInstance().level.isClientSide()) {
            ClientHooks.openGradientScreen();
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 99999;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        CompoundTag nbt = pStack.getOrCreateTag();
        if(Screen.hasShiftDown()){
            if(!(pStack.getItem() instanceof TextureWand)) {
                pTooltipComponents.add(Component.literal("Current Shape: ").append(Component.translatable(ShapeHelper.getShape(nbt).name).withStyle(ChatFormatting.AQUA)).append(Component.literal(".     Hotkey: ").append(Component.keybind(KeyBinding.SHAPESWITCH_KEYMAPPING.getName()).withStyle(ChatFormatting.YELLOW))));
            }
            pTooltipComponents.add(Component.literal("Block Palette Source: ").append(Component.translatable(getPaletteSource(nbt).name).withStyle(ChatFormatting.AQUA)).append(Component.literal(".     Hotkey: ").append(Component.keybind(KeyBinding.MODESWITCH_KEYMAPPING.getName()).withStyle(ChatFormatting.YELLOW))));
            pTooltipComponents.add(Component.literal("Palette Placement Mode: ").append(Component.translatable(getPlacementMode(nbt).name).withStyle(ChatFormatting.AQUA)).append(Component.literal(".     Hotkey: ").append(Component.keybind(KeyBinding.PLACEMENTMODE_KEYMAPPING.getName()).withStyle(ChatFormatting.YELLOW))));
            if(!(pStack.getItem() instanceof TextureWand)) {
            pTooltipComponents.add(Component.literal("Swap/Place Mode: ").append(Component.translatable(getSwapMode(nbt).name).withStyle(ChatFormatting.AQUA)).append(Component.literal(".     Hotkey: ").append(Component.keybind(KeyBinding.SWAPMODE_KEYMAPPING.getName()).withStyle(ChatFormatting.YELLOW))));
            }
            pTooltipComponents.add(Component.literal("Build Shape: ").append(Component.keybind(KeyBinding.BUILD_KEYMAPPING.getName()).withStyle(ChatFormatting.YELLOW)));
            pTooltipComponents.add(Component.literal("Palette Menu: ").append(Component.keybind(KeyBinding.PALETTEMENU_KEYMAPPING.getName()).withStyle(ChatFormatting.YELLOW)));

        }else{
            pTooltipComponents.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);


    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack item = pPlayer.getItemInHand(pUsedHand);
        CompoundTag nbt = item.getOrCreateTag();


        if(Platform.getEnv() == EnvType.CLIENT) {
            if (ShapeHelper.getShapeComplete(nbt)) {

                List<Vec3> controlPoints = ShapeHelper.getControlPoints(nbt);
                List<ControlPoint> controlPointHitboxes = new ArrayList<>();

                Raycast rc = new Raycast();
                int cpidx = 0;
                List<Double> result;
                for (Vec3 cp : controlPoints) {
                    ControlPoint tempcp = new ControlPoint(cp, cpidx);
                    result = tempcp.getIdAxisGrabbedPoint(rc);
                    int axis = (int) Math.round(result.get(1));
                    nbt.putInt("axis", axis);
                    nbt.putInt("cpidx", cpidx);
                    nbt.putDouble("grabx", result.get(2));
                    nbt.putDouble("graby", result.get(3));
                    nbt.putDouble("grabz", result.get(4));
                    if (axis != -1) {
                        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
                    }
                    cpidx++;

                }
            }
        }




        //pPlayer.startUsingItem(pUsedHand);
        // return InteractionResultHolder.consume(item);
        //return ItemUtils.startUsingInstantly(pLevel,pPlayer,pUsedHand);
        //BuildingGizmos.LOGGER.info("USE");
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        CompoundTag nbt = stack.getOrCreateTag();
        int tickmod = nbt.getInt("tickmod");
        int axis = nbt.getInt("axis");
        int cpidx = nbt.getInt("cpidx");
        double grabx = nbt.getDouble("grabx");
        double graby = nbt.getDouble("graby");
        double grabz = nbt.getDouble("grabz");
        Vec3 grabvec = new Vec3(grabx,graby,grabz);
        //BuildingGizmos.LOGGER.info(String.valueOf(axis));

        Vec3 campos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
        Vector3f look = Minecraft.getInstance().getEntityRenderDispatcher().camera.getLookVector();
        Vec3 diffpos = grabvec.subtract(campos);
        //test with Y axis

        List<Vec3> controlPoints = ShapeHelper.getControlPoints(nbt);
        List<Vec3> nudgedControlPoints = new ArrayList<>();
        if(axis ==0){
            double radius = Math.sqrt(diffpos.y*diffpos.y + diffpos.z*diffpos.z);
            double newx = campos.x+look.x()*radius;

            int cpidx1 = 0;
            for(Vec3 cp : controlPoints){
                if(cpidx1 == cpidx){
                    nudgedControlPoints.add(new Vec3(newx,cp.y,cp.z));
                }else{
                    nudgedControlPoints.add(cp);
                }
                cpidx1++;
            }
        }
        if(axis ==1){
           double radius = Math.sqrt(diffpos.x*diffpos.x + diffpos.z*diffpos.z);
           double newy = campos.y+look.y()*radius;

            int cpidx1 = 0;
            for(Vec3 cp : controlPoints){
                if(cpidx1 == cpidx){
                    nudgedControlPoints.add(new Vec3(cp.x,newy,cp.z));
                }else{
                    nudgedControlPoints.add(cp);
                }
                cpidx1++;
            }
        }
        if(axis ==2){
            double radius = Math.sqrt(diffpos.x*diffpos.x + diffpos.y*diffpos.y);
            double newz = campos.z+look.z()*radius;

            int cpidx1 = 0;
            for(Vec3 cp : controlPoints){
                if(cpidx1 == cpidx){
                    nudgedControlPoints.add(new Vec3(cp.x,cp.y,newz));
                }else{
                    nudgedControlPoints.add(cp);
                }
                cpidx1++;
            }
        }
        if(tickmod % 10 == 0) {
            ShapeHelper.redrawShapeAndQueue(nbt, nudgedControlPoints);
        }
        else{
            ShapeHelper.setControlPoints(nbt,nudgedControlPoints);
        }
        nbt.putInt("tickmod",(tickmod+1)%10);

        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        //LOGGER.info("USED ON");
        if(!context.getLevel().isClientSide() && context.getHand() == InteractionHand.MAIN_HAND) {

            ItemStack item = context.getItemInHand();
            CompoundTag nbt = item.getOrCreateTag();

            if (Objects.requireNonNull(context.getPlayer()).isCrouching()) {
                switch (nbt.getInt(activeStateTag)) {
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

            if (nbt.getInt(activeStateTag) == IN_USE) {

                //LOGGER.info("USING!");
                List<BlockPos> blockQueue = ShapeHelper.getQueue(nbt);

                if(blockQueue.isEmpty()) {
                    nbt.putInt(activeStateTag, SELECTING);
                }

            if (nbt.getInt(activeStateTag) == IN_USE) {

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


    public void switchPaletteMode(Player player){
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        cyclePaletteSource(nbt);
    }
    public static ModeEnums.PlacementModes getPlacementMode(CompoundTag nbt) {
        return enumNbt.getplacementenum(nbt, placementModeTag);
    }
    public static void setPlacementMode(CompoundTag nbt, ModeEnums.PlacementModes s) {
        enumNbt.setplacementenum(s,nbt, placementModeTag);
    }
    public static void cyclePlacementMode(CompoundTag nbt) {
        setPlacementMode(nbt, getPlacementMode(nbt).cycle());
    }

    public static ModeEnums.SwapModes getSwapMode(CompoundTag nbt) {
        return enumNbt.getswapenum(nbt, swapModeTag);
    }
    public static void setSwapMode(CompoundTag nbt, ModeEnums.SwapModes s) {
        enumNbt.setswapenum(s,nbt, swapModeTag);
    }
    public static void cycleSwapMode(CompoundTag nbt) {
        setSwapMode(nbt, getSwapMode(nbt).cycle());
    }
    public static ModeEnums.PaletteSourceModes getPaletteSource(CompoundTag nbt) {
        return enumNbt.getpalettesourceenum(nbt, modeTag);
    }
    public static void setPaletteSource(CompoundTag nbt, ModeEnums.PaletteSourceModes s) {
        enumNbt.setpalettesourceenum(s,nbt, modeTag);
    }
    public static void cyclePaletteSource(CompoundTag nbt) {
        setPaletteSource(nbt, getPaletteSource(nbt).cycle());
    }



    public void switchPlacementMode(ItemStack wand) {
        cyclePlacementMode(wand.getOrCreateTag());
    }
    public void switchSwapMode(ItemStack wand) {
        cycleSwapMode(wand.getOrCreateTag());
    }


    public void build(ItemStack wand){
        CompoundTag nbt = wand.getOrCreateTag();
        nbt.putInt(AbstractWand.activeStateTag,AbstractWand.IN_USE);
    }

    public List<ItemStack> getPaletteFromMode(Player player,ItemStack wand, int mode) {
        ModeEnums.PaletteSourceModes MODE = getPaletteSource(wand.getOrCreateTag());
        switch (MODE) {
            case HOTBAR -> {
                return getHotbarpalette(player);
            }
            case PALETTEMENU -> {
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
    public void switchBuildMode(Player player) {
        ItemStack item = player.getMainHandItem();
        CompoundTag nbt = item.getOrCreateTag();
        ShapeHelper.cycleShape(nbt);
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
        if(palette.isEmpty()){
            return ItemStack.EMPTY;
        }
        /*
        BuildingGizmos.LOGGER.info("WTF");
        BuildingGizmos.LOGGER.info(String.valueOf(palette.size()));
        BuildingGizmos.LOGGER.info(String.valueOf((palette.size()-1)));
        BuildingGizmos.LOGGER.info(String.valueOf(Math.floor(((palette.size()-1))*percent)));
         */
        return palette.get((int) Math.round((palette.size()-1)*percent));
    }



    protected void processCoord(Player player, Level level, ItemStack wand, BlockPos nextblock){
        ItemStack item = getItemFromMode(player,wand);

        ModeEnums.SwapModes MODE = getSwapMode(wand.getOrCreateTag());
        switch (MODE){
            case SWAP:
                WorldInventoryInterface.swapBlock(player, item ,level,nextblock);
                break;
            case BUILD:
                WorldInventoryInterface.safePlaceBlock(player, item ,level,nextblock);
                break;
            case SWAP_AND_BUILD:
                WorldInventoryInterface.swaporPlaceBlock(player, item ,level,nextblock);
                break;
        }

    }

    private ItemStack getItemFromMode(Player player, ItemStack wand ) {
        CompoundTag nbt = wand.getOrCreateTag();
        ModeEnums.PlacementModes MODE = getPlacementMode(nbt);
        switch (MODE){
            case RANDOM:
                return getRandomFromPallet(player,wand);
            case GRADIENT:
                int queuelen = wand.getOrCreateTag().getInt("queueLen");
                int blocklen = (ShapeHelper.getQueue(nbt).size()+1);

                float percentage = (float) blocklen/queuelen;

                return getGradientFromPallet(player,wand,percentage);
        }
        return ItemStack.EMPTY;
    }

    ;


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
        public static void setShape(CompoundTag nbt, ShapeModes s) {
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

