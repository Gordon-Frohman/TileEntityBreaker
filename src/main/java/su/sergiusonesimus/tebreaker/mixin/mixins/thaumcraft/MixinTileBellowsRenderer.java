package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;

import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.models.ModelBellows;
import thaumcraft.client.renderers.tile.TileBellowsRenderer;
import thaumcraft.common.tiles.TileBellows;

@Mixin(TileBellowsRenderer.class)
public class MixinTileBellowsRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private ModelBellows model;

    @Shadow(remap = false)
    private void translateFromOrientation(double x, double y, double z, int orientation) {}

    @Inject(
        method = "renderEntityAt(Lthaumcraft/common/tiles/TileBellows;DDDF)V",
        remap = false,
        at = @At(value = "TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderEntityAt(TileBellows bellows, double x, double y, double z, float fq, CallbackInfo ci,
        @Local(name = "scale") float scale, @Local(name = "tscale") float tscale) {
        if (breakTexture != null) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(-3.0F, -3.0F);

            this.bindTexture(breakTexture);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

            this.translateFromOrientation(x, y, z, bellows.orientation);
            GL11.glTranslatef(0.0F, 1.0F, 0.0F);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, (scale + 0.1F) / 2.0F, 0.5F);
            this.model.Bag.setRotationPoint(0.0F, 0.5F, 0.0F);
            this.model.Bag.render(0.0625F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
            GL11.glTranslatef(0.0F, -1.0F, 0.0F);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -tscale / 2.0F + 0.5F, 0.0F);
            this.model.TopPlank.render(0.0625F);
            GL11.glTranslatef(0.0F, tscale / 2.0F - 0.5F, 0.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, tscale / 2.0F - 0.5F, 0.0F);
            this.model.BottomPlank.render(0.0625F);
            GL11.glTranslatef(0.0F, -tscale / 2.0F + 0.5F, 0.0F);
            GL11.glPopMatrix();
            this.model.render();

            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPopMatrix();
            GL11.glPopAttrib();

            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            breakTexture = null;
        }
    }

}
