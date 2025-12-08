package su.sergiusonesimus.tebreaker.mixin.mixins;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import su.sergiusonesimus.tebreaker.mixin.interfaces.IMixinMinecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft implements IMixinMinecraft {

    private Map<ResourceLocation, ITextureObject> texturesToLoad = new HashMap<ResourceLocation, ITextureObject>();

    @Shadow(remap = true)
    public TextureManager renderEngine;

    @Inject(
        method = "refreshResources",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/IReloadableResourceManager;reloadResources(Ljava/util/List;)V",
            ordinal = 0,
            shift = Shift.AFTER))
    public void refreshResources(CallbackInfo ci) {
        for (Entry<ResourceLocation, ITextureObject> texture : texturesToLoad.entrySet()) {
            renderEngine.loadTexture(texture.getKey(), texture.getValue());
        }
        texturesToLoad.clear();
    }

    public void registerTextureToLoad(ResourceLocation textureName, ITextureObject texture) {
        texturesToLoad.put(textureName, texture);
    }

}
