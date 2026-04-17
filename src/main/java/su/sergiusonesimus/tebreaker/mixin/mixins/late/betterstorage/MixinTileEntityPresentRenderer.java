package su.sergiusonesimus.tebreaker.mixin.mixins.late.betterstorage;

import net.mcft.betterstorage.client.model.ModelPresent;
import net.mcft.betterstorage.client.renderer.TileEntityPresentRenderer;
import net.mcft.betterstorage.tile.entity.TileEntityPresent;
import net.minecraft.client.renderer.texture.TextureManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;

import su.sergiusonesimus.tebreaker.mixin.mixins.early.MixinTileEntitySpecialRenderer;

@Mixin(TileEntityPresentRenderer.class)
public class MixinTileEntityPresentRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private ModelPresent model;

    @Inject(
        method = "renderTileEntityAt(Lnet/mcft/betterstorage/tile/entity/TileEntityPresent;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V",
            remap = false,
            shift = Shift.BEFORE),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderDamageTexture(TileEntityPresent present, double x, double y, double z, float partialTicks,
        CallbackInfo ci, @Local(name = "texMan") TextureManager texMan) {
        if (this.breakTexture != null) {
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslated(x + 0.5, y, z + 0.5);

            bindTexture(this.breakTexture);
            model.render(1);
            model.render(2);
            if (present.skojanzaMode) model.render(3);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            ci.cancel();
        }
    }

}
