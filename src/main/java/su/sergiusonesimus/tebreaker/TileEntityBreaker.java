package su.sergiusonesimus.tebreaker;

import java.io.File;
import java.util.function.Function;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Tags.MODID, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class TileEntityBreaker {

    public static final String ID = Tags.MODID;
    public static final String NAME = Tags.MODNAME;

    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);

    @SidedProxy(
        clientSide = "su.sergiusonesimus.tebreaker.ClientProxy",
        serverSide = "su.sergiusonesimus.tebreaker.CommonProxy")
    public static CommonProxy proxy;

    public static boolean generateTextureFiles;

    public static boolean isBetterStorageLoaded;
    public static boolean areIronChestsLoaded;
    public static boolean isThaumcraftLoaded;

    public static final String CHEST = "chest";
    public static final String DOUBLE_CHEST_LEFT = "double_chest_left";
    public static final String DOUBLE_CHEST_RIGHT = "double_chest_right";
    public static final String SIGN = "sign";
    public static final String SKULL = "skull";
    public static final String ZOMBIE_SKULL = "zombie_skull";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Reading config file
        Configuration config = new Configuration(
            new File(event.getModConfigurationDirectory() + "/" + ID, ID + ".cfg"));

        generateTextureFiles = config.getBoolean(
            "generateTextureFiles",
            "debug",
            false,
            "If the mod should create .png images for every generated destruction texture in \"~\\config\\" + ID
                + "\\generated_textures\".\n"
                + "Only set to \'true\' when you want to debug the generator.");

        if (config.hasChanged()) {
            config.save();
        }

        proxy.preInit(event);

        // check if various integrations are required
        isBetterStorageLoaded = Loader.isModLoaded("betterstorage");
        areIronChestsLoaded = Loader.isModLoaded("IronChest");
        isThaumcraftLoaded = Loader.isModLoaded("Thaumcraft");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        // event listeners
        final ClientEvents clientEvents = new ClientEvents();
        MinecraftForge.EVENT_BUS.register(clientEvents);
        FMLCommonHandler.instance()
            .bus()
            .register(clientEvents);

        proxy.init(event);

        // render and other client stuff
        proxy.doOnLoadRegistration();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static boolean isTileEntityRegistered(TileEntity te) {
        return proxy.isTileEntityRegistered(te);
    }

    public static ResourceLocation[] getTextures(TileEntity te) {
        return proxy.getTextures(te);
    }

    public static ResourceLocation[] getTextures(String texture) {
        return proxy.getTextures(texture);
    }

    public static ResourceLocation getDestructionTexture(TileEntity te, int destructionStage) {
        ResourceLocation[] stages = getTextures(te);
        return stages == null ? null : stages[destructionStage];
    }

    public static ResourceLocation getDestructionTexture(String texture, int destructionStage) {
        ResourceLocation[] stages = getTextures(texture);
        return stages == null ? null : stages[destructionStage];
    }

    public static void registerTileEntity(Class<? extends TileEntity> teClass, String modelName) {
        proxy.registerTileEntity(teClass, modelName);
    }

    public static void registerTileEntity(Class<? extends TileEntity> teClass,
        Function<TileEntity, String> modelSelector) {
        proxy.registerTileEntity(teClass, modelSelector);
    }

    public static void registerModel(String modelName, int textureWidth, int textureHeight, ModelRenderer... models) {
        proxy.registerModel(modelName, textureWidth, textureHeight, models);
    }

    public static void registerModel(String modelName, float scaleX, float scaleY, float scaleZ, int textureWidth,
        int textureHeight, WavefrontObject model, boolean useTextureOffset) {
        proxy.registerModel(modelName, scaleX, scaleY, scaleZ, textureWidth, textureHeight, model, useTextureOffset);
    }

    public static void registerModel(String modelName, float scaleX, float scaleY, float scaleZ, int textureWidth,
        int textureHeight, WavefrontObject model) {
        registerModel(modelName, scaleX, scaleY, scaleZ, textureWidth, textureHeight, model, false);
    }

    public static void registerModel(String modelName, int textureWidth, int textureHeight, WavefrontObject model,
        boolean useTextureOffset) {
        registerModel(modelName, 1, 1, 1, textureWidth, textureHeight, model, useTextureOffset);
    }

    public static void registerModel(String modelName, int textureWidth, int textureHeight, WavefrontObject model) {
        registerModel(modelName, 1, 1, 1, textureWidth, textureHeight, model, false);
    }

    /**
     * Register model textures to be read from file.
     * Requires 10 corresponding textures in 'assets' folder.
     * Use only if the generator is unable to produce required textures because of problem with model!
     * 
     * @param modelName    - Name of the model to be read from 'assets' folder
     * @param textureGroup - Group of the model. Most likely - id of the mod this model is related to.
     */
    public static void registerModel(String modelName, String textureGroup) {
        proxy.registerModel(modelName, textureGroup);
    }

    public static void registerOffsets(Class<? extends TileEntity> teClass, ChunkCoordinates... offsets) {
        proxy.registerOffsets(teClass, offsets);
    }

    public static void registerOffset(Class<? extends TileEntity> teClass, int x, int y, int z) {
        registerOffsets(teClass, new ChunkCoordinates(x, y, z));
    }

    public static DestroyBlockProgress getTileEntityDestroyProgress(TileEntity te) {
        return proxy.getTileEntityDestroyProgress(te);
    }

    public static DestroyBlockProgress getBlockDestroyProgress(int x, int y, int z) {
        return proxy.getBlockDestroyProgress(x, y, z);
    }

    public static void breakpoint() {
        int x = 0;
    }

}
