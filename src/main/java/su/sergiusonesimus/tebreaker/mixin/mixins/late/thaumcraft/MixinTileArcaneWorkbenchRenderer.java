package su.sergiusonesimus.tebreaker.mixin.mixins.late.thaumcraft;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.mixin.mixins.tebreaker.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileArcaneWorkbenchRenderer;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@Mixin(TileArcaneWorkbenchRenderer.class)
public class MixinTileArcaneWorkbenchRenderer extends MixinTileEntitySpecialRenderer {

    @Inject(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileArcaneWorkbench;DDDF)V",
        remap = false,
        at = @At(value = "HEAD"))
    public void renderTileEntityAt(TileArcaneWorkbench tile, double par2, double par4, double par6, float par8,
        CallbackInfo ci) {
        storedTileEntity = tile;
    }

    @WrapOperation(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileArcaneWorkbench;DDDF)V",
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
            if (storedTileEntity.getWorldObj() != null
                && ((TileArcaneWorkbench) storedTileEntity).getStackInSlot(10) != null
                && ((TileArcaneWorkbench) storedTileEntity).getStackInSlot(10)
                    .getItem() instanceof ItemWandCasting) {
                breakTexture = storedBreakTexture;
            }
        }
    }

    @WrapOperation(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileArcaneWorkbench;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntityWithPosYaw(Lnet/minecraft/entity/Entity;DDDFF)Z"))
    public boolean renderEntityWithPosYaw(RenderManager renderManager, Entity entity, double x, double y, double z,
        float rotation, float delta, Operation<Boolean> original) {
        if (breakTexture == null) return original.call(renderManager, entity, x, y, z, rotation, delta);
        else {
            breakTexture = null;
            return false;
        }
    }

}
