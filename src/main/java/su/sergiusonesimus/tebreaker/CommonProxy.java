package su.sergiusonesimus.tebreaker;

import java.util.function.Function;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {

    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}

    public void doOnLoadRegistration() {

    }

    public boolean isTileEntityRegistered(TileEntity te) {
        return false;
    }

    public void registerTileEntity(Class<? extends TileEntity> teClass, String modelName) {

    }

    public void registerTileEntity(Class<? extends TileEntity> teClass, Function<TileEntity, String> modelSelector) {

    }

    public void registerModel(String modelName, int textureWidth, int textureHeight, ModelRenderer... models) {

    }

    public ResourceLocation[] getTextures(TileEntity te) {
        return null;
    }

    public DestroyBlockProgress getTileEntityDestroyProgress(TileEntity te) {
        return null;
    }

}
