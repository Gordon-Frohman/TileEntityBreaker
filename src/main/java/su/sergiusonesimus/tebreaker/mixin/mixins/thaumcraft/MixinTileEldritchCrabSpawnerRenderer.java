package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileEldritchCrabSpawnerRenderer;

@Mixin(TileEldritchCrabSpawnerRenderer.class)
public class MixinTileEldritchCrabSpawnerRenderer extends MixinTileEntitySpecialRenderer {

    @WrapOperation(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileEldritchCrabSpawner;DDDF)V",
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
