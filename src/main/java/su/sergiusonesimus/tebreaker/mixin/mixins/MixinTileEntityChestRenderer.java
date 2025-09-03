package su.sergiusonesimus.tebreaker.mixin.mixins;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntityChest;

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

@Mixin(TileEntityChestRenderer.class)
public class MixinTileEntityChestRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private ModelChest field_147511_i;

    @Inject(
        method = "renderTileEntityAt",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelChest;renderAll()V", shift = Shift.AFTER),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderTileEntityAt(TileEntityChest te, double dX, double dY, double dZ, float partialTicks,
        CallbackInfo ci, @Local(name = "modelchest") ModelChest modelchest) {
        if (modelchest == this.field_147511_i) {
            DestroyBlockProgress destroyBlockProgress = TileEntityBreaker.getTileEntityDestroyProgress(te);
            TileEntityChest neighbourTE = te.adjacentChestXPos != null ? te.adjacentChestXPos : te.adjacentChestZPos;
            DestroyBlockProgress destroyNeighboutBlockProgress = TileEntityBreaker
                .getTileEntityDestroyProgress(neighbourTE);
            if (destroyBlockProgress == null && destroyNeighboutBlockProgress == null) return;

            GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(-3.0F, -3.0F);

            int meta = te.getBlockMetadata();
            if (destroyBlockProgress != null) {
                this.bindTexture(
                    ClientProxy.destructionTextures.get(
                        meta == 2 || meta == 5 ? TileEntityBreaker.DOUBLE_CHEST_RIGHT
                            : TileEntityBreaker.DOUBLE_CHEST_LEFT)[destroyBlockProgress.getPartialBlockDamage()]);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

                modelchest.renderAll();
            }
            if (destroyNeighboutBlockProgress != null) {
                this.bindTexture(
                    ClientProxy.destructionTextures.get(
                        meta == 3 || meta == 4 ? TileEntityBreaker.DOUBLE_CHEST_RIGHT
                            : TileEntityBreaker.DOUBLE_CHEST_LEFT)[destroyNeighboutBlockProgress
                                .getPartialBlockDamage()]);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

                modelchest.renderAll();
            }

            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPopMatrix();
            GL11.glPopAttrib();

            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
    }

}
