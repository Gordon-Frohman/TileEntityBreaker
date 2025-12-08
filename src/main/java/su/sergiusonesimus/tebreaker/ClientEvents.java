package su.sergiusonesimus.tebreaker;

import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import su.sergiusonesimus.tebreaker.BreakTextureGenerator.GeneratorData;
import su.sergiusonesimus.tebreaker.integration.BetterStorageIntegration;
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;

public class ClientEvents {

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event)
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        for (GeneratorData data : ClientProxy.generationMaterials) {
            ClientProxy.destructionTextures.put(data.modelName, BreakTextureGenerator.generateBreakTextures(data));
        }
        // Commented this out for better resource packs compatibility
        // With it in place all the models from that list won't get their textures updated on resource pack change
        // ClientProxy.generationMaterials.clear();

        ClientProxy.destructionTextures
            .put(TileEntityBreaker.DOUBLE_CHEST_LEFT, BreakTextureGenerator.generateDoubleChestBreakTextures(false));
        ClientProxy.destructionTextures
            .put(TileEntityBreaker.DOUBLE_CHEST_RIGHT, BreakTextureGenerator.generateDoubleChestBreakTextures(true));

        if (TileEntityBreaker.isBetterStorageLoaded) {
            ClientProxy.destructionTextures.put(
                BetterStorageIntegration.DOUBLE_LOCKER_BOTTOM,
                BetterStorageIntegration.generateDoubleLockerBreakTextures(false));
            ClientProxy.destructionTextures.put(
                BetterStorageIntegration.DOUBLE_LOCKER_TOP,
                BetterStorageIntegration.generateDoubleLockerBreakTextures(true));
        }

        if (TileEntityBreaker.isThaumcraftLoaded) {
            ClientProxy.destructionTextures.put(
                ThaumcraftIntegration.RESEARCH_TABLE_LEFT,
                ThaumcraftIntegration.generateResearchTableBreakTextures(false));
            ClientProxy.destructionTextures.put(
                ThaumcraftIntegration.RESEARCH_TABLE_RIGHT,
                ThaumcraftIntegration.generateResearchTableBreakTextures(true));
        }
    }
}
