package su.sergiusonesimus.tebreaker.mixin.mixins;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.BreakTextureGenerator;

@Mixin(TextureMap.class)
public class MixinTextureMap {

    private static Integer breakTextureID = null;

    @WrapOperation(
        method = "loadTextureAtlas",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/IResourceManager;getResource(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/resources/IResource;"))
    public IResource getResource(IResourceManager resourceManager, ResourceLocation resourceLocation,
        Operation<IResource> original) {
        String resourcePath = resourceLocation.getResourcePath();
        if (resourcePath.contains("destroy_stage_")) {
            breakTextureID = Integer.valueOf(resourcePath.substring(30, 31));
        }
        return original.call(resourceManager, resourceLocation);
    }

    @WrapOperation(
        method = "loadTextureAtlas",
        at = @At(
            value = "INVOKE",
            target = "Ljavax/imageio/ImageIO;read(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;"))
    public BufferedImage read(InputStream inputStream, Operation<BufferedImage> original) {
        BufferedImage result = original.call(inputStream);
        if (breakTextureID != null) {
            BreakTextureGenerator.destroyBlockIconsMap.put(breakTextureID, result);
            breakTextureID = null;
        }
        return result;
    }

}
