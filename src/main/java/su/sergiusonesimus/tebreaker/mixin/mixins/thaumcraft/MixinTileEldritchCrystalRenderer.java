package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileEldritchCrystalRenderer;

@Mixin(TileEldritchCrystalRenderer.class)
public class MixinTileEldritchCrystalRenderer extends MixinTileEntitySpecialRenderer {

    private int breakRenderCounter = 0;

    @WrapOperation(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileCrystal;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/UtilsFX;bindTexture(Ljava/lang/String;)V",
            remap = false))
    public void bindTexture(String texture, Operation<Void> original) {
        if (breakTexture == null) original.call(texture);
        else {
            ResourceLocation storedBreakTexture = breakTexture;
            bindTexture(breakTexture);
            breakRenderCounter++;
            if (breakRenderCounter == 2) {
                breakRenderCounter = 0;
                breakTexture = null;
            } else {
                breakTexture = storedBreakTexture;
            }
        }
    }

}
