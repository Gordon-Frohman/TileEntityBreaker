package su.sergiusonesimus.tebreaker.mixin.mixins.betterstorage;

import net.mcft.betterstorage.client.renderer.TileEntityBackpackRenderer;
import net.mcft.betterstorage.tile.entity.TileEntityBackpack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;

@Mixin(TileEntityBackpackRenderer.class)
public class MixinTileEntityBackpackRenderer extends MixinTileEntitySpecialRenderer {

    private boolean shouldApplyColor = true;

    @Inject(
        method = "renderTileEntityAt(Lnet/mcft/betterstorage/tile/entity/TileEntityBackpack;DDDF)V",
        remap = false,
        at = @At(value = "HEAD"))
    public void checkForBreaking(TileEntityBackpack backpack, double x, double y, double z, float partialTicks,
        CallbackInfo ci) {
        shouldApplyColor = this.breakTexture == null;
    }

    @WrapOperation(
        method = "renderTileEntityAt(Lnet/mcft/betterstorage/tile/entity/TileEntityBackpack;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcft/betterstorage/utils/RenderUtils;setColorFromInt(I)V",
            remap = false))
    public void setColorFromInt(int color, Operation<Void> original) {
        if (shouldApplyColor) original.call(color);
        else shouldApplyColor = true;
    }

}
