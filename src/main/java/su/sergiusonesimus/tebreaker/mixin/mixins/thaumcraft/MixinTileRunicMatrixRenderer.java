package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;

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
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;
import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.models.ModelCube;
import thaumcraft.client.renderers.tile.TileRunicMatrixRenderer;
import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(TileRunicMatrixRenderer.class)
public class MixinTileRunicMatrixRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private ModelCube model;

    @Inject(
        method = "renderInfusionMatrix(Lthaumcraft/common/tiles/TileInfusionMatrix;DDDF)V",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V",
            remap = false,
            ordinal = 3,
            shift = Shift.BEFORE),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderDestructionTexture(TileInfusionMatrix is, double x, double y, double z, float ticks,
        CallbackInfo ci, @Local(name = "instability") float instability, @Local(name = "b1") float b1,
        @Local(name = "b2") float b2, @Local(name = "b3") float b3, @Local(name = "aa") int aa,
        @Local(name = "bb") int bb, @Local(name = "cc") int cc) {
        DestroyBlockProgress destroyBlockProgress = TileEntityBreaker.getTileEntityDestroyProgress(is);
        if (destroyBlockProgress == null) return;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-3.0F, -3.0F);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

        for (int a = 0; a < 2; ++a) {
            for (int b = 0; b < 2; ++b) {
                for (int c = 0; c < 2; ++c) {
                    if (is.active) {
                        b1 = MathHelper.sin((ticks + (float) (a * 10)) / (15.0F - instability / 2.0F)) * 0.01F
                            * is.startUp
                            * instability;
                        b2 = MathHelper.sin((ticks + (float) (b * 10)) / (14.0F - instability / 2.0F)) * 0.01F
                            * is.startUp
                            * instability;
                        b3 = MathHelper.sin((ticks + (float) (c * 10)) / (13.0F - instability / 2.0F)) * 0.01F
                            * is.startUp
                            * instability;
                    }

                    aa = a == 0 ? -1 : 1;
                    bb = b == 0 ? -1 : 1;
                    cc = c == 0 ? -1 : 1;
                    GL11.glPushMatrix();
                    GL11.glTranslatef(b1 + (float) aa * 0.25F, b2 + (float) bb * 0.25F, b3 + (float) cc * 0.25F);
                    GL11.glScaled(0.45D, 0.45D, 0.45D);

                    int ordinal = a << 2 | b << 1 | c;
                    bindTexture(
                        ClientProxy.destructionTextures.get(
                            ThaumcraftIntegration.RUNIC_MATRIX[ordinal])[destroyBlockProgress.getPartialBlockDamage()]);
                    this.model.render();
                    GL11.glPopMatrix();
                }
            }
        }

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    }

}
