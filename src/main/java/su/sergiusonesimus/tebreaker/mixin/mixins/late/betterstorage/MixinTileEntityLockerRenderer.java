package su.sergiusonesimus.tebreaker.mixin.mixins.late.betterstorage;

import net.mcft.betterstorage.client.model.ModelLocker;
import net.mcft.betterstorage.client.renderer.TileEntityLockerRenderer;
import net.mcft.betterstorage.tile.entity.TileEntityLocker;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;

import su.sergiusonesimus.tebreaker.ClientProxy;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import su.sergiusonesimus.tebreaker.integration.BetterStorageIntegration;
import su.sergiusonesimus.tebreaker.mixin.mixins.tebreaker.MixinTileEntitySpecialRenderer;

@Mixin(TileEntityLockerRenderer.class)
public class MixinTileEntityLockerRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private ModelLocker largeLockerModel;

    @Inject(
        method = "renderTileEntityAt",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcft/betterstorage/client/model/ModelLocker;renderAll(ZF)V",
            remap = false,
            shift = Shift.BEFORE),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderTileEntityAt(TileEntityLocker locker, double x, double y, double z, float partialTicks,
        CallbackInfo ci) {
        GL11.glPushMatrix();
    }

    @Inject(
        method = "renderTileEntityAt",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcft/betterstorage/client/model/ModelLocker;renderAll(ZF)V",
            remap = false,
            shift = Shift.AFTER),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderTileEntityAt(TileEntityLocker locker, double x, double y, double z, float partialTicks,
        CallbackInfo ci, @Local(name = "model") ModelLocker model, @Local(name = "angle") float angle) {
        GL11.glPopMatrix();
        if (model == largeLockerModel) {
            DestroyBlockProgress destroyBlockProgress = TileEntityBreaker.getTileEntityDestroyProgress(locker);
            TileEntityLocker neighbourLocker = (TileEntityLocker) locker.getConnectedTileEntity();
            DestroyBlockProgress destroyNeighboutBlockProgress = TileEntityBreaker
                .getTileEntityDestroyProgress(neighbourLocker);
            if (destroyBlockProgress != null) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glPushMatrix();

                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(-3.0F, -3.0F);

                this.bindTexture(
                    ClientProxy.destructionTextures.get(BetterStorageIntegration.DOUBLE_LOCKER_TOP)[destroyBlockProgress
                        .getPartialBlockDamage()]);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

                model.renderAll(locker.mirror, angle);

                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPopMatrix();
                GL11.glPopAttrib();

                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            }
            if (destroyNeighboutBlockProgress != null) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glPushMatrix();

                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(-3.0F, -3.0F);

                this.bindTexture(
                    ClientProxy.destructionTextures
                        .get(BetterStorageIntegration.DOUBLE_LOCKER_BOTTOM)[destroyNeighboutBlockProgress
                            .getPartialBlockDamage()]);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

                model.renderAll(locker.mirror, angle);

                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPopMatrix();
                GL11.glPopAttrib();

                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            }
        }
    }

}
