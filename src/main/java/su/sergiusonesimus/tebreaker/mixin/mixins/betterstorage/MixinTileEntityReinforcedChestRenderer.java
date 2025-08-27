package su.sergiusonesimus.tebreaker.mixin.mixins.betterstorage;

import net.mcft.copy.betterstorage.client.renderer.TileEntityReinforcedChestRenderer;
import net.mcft.copy.betterstorage.tile.entity.TileEntityReinforcedChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.util.ForgeDirection;

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
import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;

@Mixin(TileEntityReinforcedChestRenderer.class)
public class MixinTileEntityReinforcedChestRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private ModelChest largeChestModel;

    @Inject(
        method = "renderTileEntityAt",
        remap = false,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelChest;renderAll()V", shift = Shift.AFTER),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderTileEntityAt(TileEntityReinforcedChest chest, double x, double y, double z, float partialTicks,
        CallbackInfo ci, @Local(name = "model") ModelChest model) {
        if (model == largeChestModel) {
            DestroyBlockProgress destroyBlockProgress = TileEntityBreaker.getTileEntityDestroyProgress(chest);
            TileEntityReinforcedChest neighbourChest = (TileEntityReinforcedChest) chest.getConnectedTileEntity();
            DestroyBlockProgress destroyNeighboutBlockProgress = TileEntityBreaker
                .getTileEntityDestroyProgress(neighbourChest);
            if (destroyBlockProgress != null) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glPushMatrix();

                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(-3.0F, -3.0F);

                this.bindTexture(
                    ClientProxy.destructionTextures.get(
                        "double_chest_" + ((chest.getOrientation() == ForgeDirection.EAST
                            && chest.getConnected() == ForgeDirection.SOUTH)
                            || (chest.getOrientation() == ForgeDirection.NORTH
                                && chest.getConnected() == ForgeDirection.EAST) ? "right"
                                    : "left"))[destroyBlockProgress.getPartialBlockDamage()]);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

                model.renderAll();

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
                    ClientProxy.destructionTextures.get(
                        "double_chest_" + ((chest.getOrientation() == ForgeDirection.WEST
                            && chest.getConnected() == ForgeDirection.SOUTH)
                            || (chest.getOrientation() == ForgeDirection.SOUTH
                                && chest.getConnected() == ForgeDirection.EAST) ? "right"
                                    : "left"))[destroyNeighboutBlockProgress.getPartialBlockDamage()]);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

                model.renderAll();

                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPopMatrix();
                GL11.glPopAttrib();

                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            }
        }
    }

}
