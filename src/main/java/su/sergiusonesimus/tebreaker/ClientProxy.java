package su.sergiusonesimus.tebreaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import su.sergiusonesimus.tebreaker.BreakTextureGenerator.GeneratorData;
import su.sergiusonesimus.tebreaker.integration.BetterStorageIntegration;
import su.sergiusonesimus.tebreaker.integration.IronChestsIntegration;
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static Map<String, ResourceLocation[]> destructionTextures = new HashMap<String, ResourceLocation[]>();
    public static Map<Class<? extends TileEntity>, String> texturesMap = new HashMap<Class<? extends TileEntity>, String>();
    public static Map<Class<? extends TileEntity>, Function<TileEntity, String>> selectorsMap = new HashMap<Class<? extends TileEntity>, Function<TileEntity, String>>();
    public static Map<Class<? extends TileEntity>, ChunkCoordinates[]> offsetsMap = new HashMap<Class<? extends TileEntity>, ChunkCoordinates[]>();
    public static List<GeneratorData> generationMaterials = new ArrayList<GeneratorData>();

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        final ClientEvents clientEvents = new ClientEvents();
        MinecraftForge.EVENT_BUS.register(clientEvents);
        FMLCommonHandler.instance()
            .bus()
            .register(clientEvents);
    }

    public void postInit(FMLPostInitializationEvent event) {
        ModelChest chest = new ModelChest();
        registerModel(TileEntityBreaker.CHEST, 64, 64, chest.chestBelow, chest.chestKnob, chest.chestLid);
        registerTileEntity(TileEntityEnderChest.class, TileEntityBreaker.CHEST);
        registerTileEntity(TileEntityChest.class, (te) -> {
            TileEntityChest teChest = (TileEntityChest) te;
            if (teChest.adjacentChestZNeg == null && teChest.adjacentChestXNeg == null
                && teChest.adjacentChestXPos == null
                && teChest.adjacentChestZPos == null) {
                return TileEntityBreaker.CHEST;
            }
            return "";
        });

        ModelSign sign = new ModelSign();
        registerModel(TileEntityBreaker.SIGN, 64, 32, sign.signBoard, sign.signStick);
        registerTileEntity(TileEntitySign.class, TileEntityBreaker.SIGN);

        ModelSkeletonHead skull = new ModelSkeletonHead(0, 0, 64, 32);
        skull.skeletonHead.rotationPointY -= 4;
        ModelSkeletonHead zombieSkull = new ModelSkeletonHead(0, 0, 64, 64);
        zombieSkull.skeletonHead.rotationPointY -= 4;
        registerModel(TileEntityBreaker.SKULL, 64, 32, skull.skeletonHead);
        registerModel(TileEntityBreaker.ZOMBIE_SKULL, 64, 64, zombieSkull.skeletonHead);
        registerTileEntity(TileEntitySkull.class, (te) -> {
            if (((TileEntitySkull) te).func_145904_a() == 2) return TileEntityBreaker.ZOMBIE_SKULL;
            else return TileEntityBreaker.SKULL;
        });

        if (TileEntityBreaker.isBetterStorageLoaded) try {
            BetterStorageIntegration.registerTileEntities();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        if (TileEntityBreaker.areIronChestsLoaded) IronChestsIntegration.registerTileEntities();
        if (TileEntityBreaker.isThaumcraftLoaded) try {
            ThaumcraftIntegration.registerTileEntities();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doOnLoadRegistration() {

    }

    public boolean isTileEntityRegistered(TileEntity te) {
        for (Class<? extends TileEntity> teClass : texturesMap.keySet()) {
            if (teClass.isInstance(te)) return true;
        }
        for (Class<? extends TileEntity> teClass : selectorsMap.keySet()) {
            if (teClass.isInstance(te)) return true;
        }
        return false;
    }

    public void registerTileEntity(Class<? extends TileEntity> teClass, String modelName) {
        texturesMap.put(teClass, modelName);
    }

    public void registerTileEntity(Class<? extends TileEntity> teClass, Function<TileEntity, String> modelSelector) {
        selectorsMap.put(teClass, modelSelector);
    }

    public void registerModel(String modelName, int textureWidth, int textureHeight, ModelRenderer... models) {
        generationMaterials.add(new GeneratorData(modelName, textureWidth, textureHeight, models));
    }

    public void registerModel(String modelName, float scaleX, float scaleY, float scaleZ, int textureWidth,
        int textureHeight, WavefrontObject model, boolean useTextureOffset) {
        generationMaterials.add(
            new GeneratorData(modelName, scaleX, scaleY, scaleZ, textureWidth, textureHeight, model, useTextureOffset));
    }

    public void registerModel(String modelName, String textureGroup) {
        generationMaterials.add(new GeneratorData(modelName, textureGroup));
    }

    public void registerOffsets(Class<? extends TileEntity> teClass, ChunkCoordinates... offsets) {
        offsetsMap.put(teClass, offsets);
    }

    public ResourceLocation[] getTextures(TileEntity te) {
        Class<? extends TileEntity> teClass = null;
        boolean useSelector = false;

        for (Class<? extends TileEntity> registeredClass : texturesMap.keySet()) {
            if (registeredClass.isInstance(te)) {
                if (teClass == null || teClass.isAssignableFrom(registeredClass)) {
                    teClass = registeredClass;
                }
            }
        }

        for (Class<? extends TileEntity> registeredClass : selectorsMap.keySet()) {
            if (registeredClass.isInstance(te)) {
                if (teClass == null || teClass.isAssignableFrom(registeredClass)) {
                    teClass = registeredClass;
                    useSelector = true;
                }
            }
        }

        return teClass == null ? null
            : destructionTextures.get(
                useSelector ? selectorsMap.get(teClass)
                    .apply(te) : texturesMap.get(teClass));
    }

    public ResourceLocation[] getTextures(String texture) {
        return destructionTextures.get(texture);
    }

    @SuppressWarnings("unchecked")
    public DestroyBlockProgress getTileEntityDestroyProgress(TileEntity te) {
        if (te == null) return null;
        List<ChunkCoordinates> blocksToCheck = new ArrayList<ChunkCoordinates>();
        blocksToCheck.add(new ChunkCoordinates(te.xCoord, te.yCoord, te.zCoord));
        ChunkCoordinates[] blockOffsets = offsetsMap.get(te.getClass());
        if (blockOffsets != null) for (ChunkCoordinates currentBlock : blockOffsets) blocksToCheck.add(
            new ChunkCoordinates(
                te.xCoord + currentBlock.posX,
                te.yCoord + currentBlock.posY,
                te.zCoord + currentBlock.posZ));
        Iterator<DestroyBlockProgress> iterator = Minecraft.getMinecraft().renderGlobal.damagedBlocks.values()
            .iterator();

        while (iterator.hasNext()) {
            DestroyBlockProgress destroyblockprogress = iterator.next();
            for (ChunkCoordinates currentBlock : blocksToCheck) {
                if (destroyblockprogress.getPartialBlockX() == currentBlock.posX
                    && destroyblockprogress.getPartialBlockY() == currentBlock.posY
                    && destroyblockprogress.getPartialBlockZ() == currentBlock.posZ) return destroyblockprogress;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public DestroyBlockProgress getBlockDestroyProgress(int x, int y, int z) {
        Iterator<DestroyBlockProgress> iterator = Minecraft.getMinecraft().renderGlobal.damagedBlocks.values()
            .iterator();

        while (iterator.hasNext()) {
            DestroyBlockProgress destroyblockprogress = iterator.next();
            if (destroyblockprogress.getPartialBlockX() == x && destroyblockprogress.getPartialBlockY() == y
                && destroyblockprogress.getPartialBlockZ() == z) return destroyblockprogress;
        }
        return null;
    }

}
