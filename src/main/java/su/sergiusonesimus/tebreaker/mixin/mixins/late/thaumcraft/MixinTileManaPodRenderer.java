package su.sergiusonesimus.tebreaker.mixin.mixins.late.thaumcraft;

import java.lang.reflect.Field;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.mixin.mixins.tebreaker.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileManaPodRenderer;

@Mixin(TileManaPodRenderer.class)
public class MixinTileManaPodRenderer extends MixinTileEntitySpecialRenderer {

    @WrapOperation(
        method = "renderEntityAt(Lthaumcraft/common/tiles/TileManaPod;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/UtilsFX;bindTexture(Lnet/minecraft/util/ResourceLocation;)V",
            remap = false))
    public void bindTexture(ResourceLocation texture, Operation<Void> original) {
        if (breakTexture == null) original.call(texture);
        else {
            Field pod2tex;
            try {
                pod2tex = TileManaPodRenderer.class.getDeclaredField("pod2tex");
                pod2tex.setAccessible(true);
                if (texture == pod2tex.get(null)) {
                    bindTexture(breakTexture);
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
