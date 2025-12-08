package su.sergiusonesimus.tebreaker.mixin.interfaces;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

public interface IMixinMinecraft {

    public void registerTextureToLoad(ResourceLocation textureName, ITextureObject texture);

}
