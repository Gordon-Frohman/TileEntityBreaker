package su.sergiusonesimus.tebreaker.mixin.mixins.tebreaker;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.mixin.interfaces.IMixinTileEntitySpecialRenderer;

@Mixin(TileEntitySpecialRenderer.class)
public class MixinTileEntitySpecialRenderer implements IMixinTileEntitySpecialRenderer {

    @Shadow(remap = true)
    protected void bindTexture(ResourceLocation p_147499_1_) {}

    protected ResourceLocation breakTexture = null;
    protected TileEntity storedTileEntity = null;

    public void setBreakTexture(ResourceLocation breakTexture) {
        this.breakTexture = breakTexture;
    }

    public ResourceLocation getBreakTexture() {
        return this.breakTexture;
    }

    @WrapOperation(
        method = "bindTexture",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public void bindTexture(TextureManager textureManager, ResourceLocation resourceLocation,
        Operation<Void> original) {
        if (breakTexture == null) {
            original.call(textureManager, resourceLocation);
        } else {
            original.call(textureManager, breakTexture);
            breakTexture = null;
        }
    }

}
