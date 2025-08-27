package su.sergiusonesimus.tebreaker;

import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import su.sergiusonesimus.tebreaker.BreakTextureGenerator.GeneratorData;
import su.sergiusonesimus.tebreaker.integration.BetterStorageIntegration;

public class ClientEvents {

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event)
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        for (GeneratorData data : ClientProxy.generationMaterials) {
            ClientProxy.destructionTextures.put(data.modelName, BreakTextureGenerator.generateBreakTextures(data));
        }
        ClientProxy.generationMaterials.clear();

        ClientProxy.destructionTextures
            .put("double_chest_left", BreakTextureGenerator.generateDoubleChestBreakTextures(false));
        ClientProxy.destructionTextures
            .put("double_chest_right", BreakTextureGenerator.generateDoubleChestBreakTextures(true));

        if (TileEntityBreaker.isBetterStorageLoaded) {
            ClientProxy.destructionTextures
                .put("double_locker_bottom", BetterStorageIntegration.generateDoubleLockerBreakTextures(false));
            ClientProxy.destructionTextures
                .put("double_locker_top", BetterStorageIntegration.generateDoubleLockerBreakTextures(true));
        }
    }
}
