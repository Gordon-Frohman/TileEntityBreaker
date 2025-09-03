package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.ClientProxy;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;
import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileAlchemyFurnaceAdvancedRenderer;
import thaumcraft.common.tiles.TileAlchemyFurnaceAdvanced;

@Mixin(TileAlchemyFurnaceAdvancedRenderer.class)
public class MixinTileAlchemyFurnaceAdvancedRenderer extends MixinTileEntitySpecialRenderer {

    @Inject(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileAlchemyFurnaceAdvanced;DDDF)V",
        remap = false,
        at = @At(value = "HEAD"))
    public void renderTileEntityAt(TileAlchemyFurnaceAdvanced tile, double par2, double par4, double par6, float par8,
        CallbackInfo ci) {
        storedTileEntity = tile;
    }

    @WrapOperation(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileAlchemyFurnaceAdvanced;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/UtilsFX;bindTexture(Ljava/lang/String;)V",
            remap = false))
    public void bindTexture(String texture, Operation<Void> original) {
        if (breakTexture == null) original.call(texture);
        else {
            bindTexture(breakTexture);
            if (!texture.contains("tank")) {
                breakTexture = ClientProxy.destructionTextures
                    .get(ThaumcraftIntegration.ALCHEMY_FURNACE_TANK)[TileEntityBreaker
                        .getTileEntityDestroyProgress(storedTileEntity)
                        .getPartialBlockDamage()];
            } else {
                breakTexture = null;
            }
        }
    }

}
