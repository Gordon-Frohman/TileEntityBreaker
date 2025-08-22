package su.sergiusonesimus.tebreaker;

import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import su.sergiusonesimus.tebreaker.BreakTextureGenerator.GeneratorData;

public class ClientEvents {

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        for (GeneratorData data : ClientProxy.generationMaterials) {
            ClientProxy.destructionTextures.put(data.modelName, BreakTextureGenerator.generateBreakTextures(data));
        }
        ClientProxy.destructionTextures
            .put("double_chest_left", BreakTextureGenerator.generateDoubleChestBreakTextures(false));
        ClientProxy.destructionTextures
            .put("double_chest_right", BreakTextureGenerator.generateDoubleChestBreakTextures(true));
        ClientProxy.generationMaterials.clear();
    }
}
