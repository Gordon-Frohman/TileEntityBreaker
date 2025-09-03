package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileChestHungryRenderer;

@Mixin(TileChestHungryRenderer.class)
public class MixinTileChestHungryRenderer extends MixinTileEntitySpecialRenderer {

    @WrapOperation(
        method = "renderTileEntityChestAt(Lthaumcraft/common/tiles/TileChestHungry;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/UtilsFX;bindTexture(Ljava/lang/String;)V",
            remap = false))
    public void bindTexture(String texture, Operation<Void> original) {
        if (breakTexture == null) original.call(texture);
        else {
            bindTexture(breakTexture);
            breakTexture = null;
        }
    }

}
