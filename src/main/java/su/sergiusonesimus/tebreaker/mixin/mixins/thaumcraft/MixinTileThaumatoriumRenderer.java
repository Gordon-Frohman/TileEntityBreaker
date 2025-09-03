package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import su.sergiusonesimus.tebreaker.ClientProxy;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;
import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileThaumatoriumRenderer;
import thaumcraft.common.tiles.TileThaumatorium;

@Mixin(TileThaumatoriumRenderer.class)
public class MixinTileThaumatoriumRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private IModelCustom model;

    @Inject(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileThaumatorium;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/client/model/IModelCustom;renderAll()V",
            shift = Shift.AFTER),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderDestructionTexture(TileThaumatorium tile, double x, double y, double z, float ticks,
        CallbackInfo ci) {
        DestroyBlockProgress destroyBlockProgress = TileEntityBreaker.getTileEntityDestroyProgress(tile);
        DestroyBlockProgress destroyNeighbourProgress = TileEntityBreaker
            .getBlockDestroyProgress(tile.xCoord, tile.yCoord + 1, tile.zCoord);
        if (destroyBlockProgress == null && destroyNeighbourProgress == null) return;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-3.0F, -3.0F);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

        if (destroyBlockProgress != null) {
            bindTexture(
                ClientProxy.destructionTextures.get(ThaumcraftIntegration.THAUMATORIUM)[destroyBlockProgress
                    .getPartialBlockDamage()]);
            this.model.renderAll();
        } else {
            if (destroyNeighbourProgress != null) {
                bindTexture(
                    ClientProxy.destructionTextures.get(ThaumcraftIntegration.THAUMATORIUM)[destroyNeighbourProgress
                        .getPartialBlockDamage()]);
                this.model.renderAll();
            }
        }

        UtilsFX.bindTexture("textures/models/thaumatorium.png");

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    }

}
