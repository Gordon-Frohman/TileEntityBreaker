package su.sergiusonesimus.tebreaker;

import java.io.File;
import java.util.function.Function;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
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

    @EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
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
    }

    @EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
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
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static boolean isTileEntityRegistered(TileEntity te) {
        return proxy.isTileEntityRegistered(te);
    }

    public static ResourceLocation[] getTextures(TileEntity te) {
        return proxy.getTextures(te);
    }

    public static ResourceLocation getDestructionTexture(TileEntity te, int destructionStage) {
        ResourceLocation[] stages = getTextures(te);
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

    @SuppressWarnings("unchecked")
    public static DestroyBlockProgress getTileEntityDestroyProgress(TileEntity te) {
        return proxy.getTileEntityDestroyProgress(te);
    }

}
