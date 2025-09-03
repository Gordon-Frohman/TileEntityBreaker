package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import su.sergiusonesimus.tebreaker.ClientProxy;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;
import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileNodeConverterRenderer;
import thaumcraft.common.tiles.TileNodeConverter;

@Mixin(TileNodeConverterRenderer.class)
public class MixinTileNodeConverterRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private IModelCustom model;

    @Inject(
        method = "renderTileEntityAt(Lthaumcraft/common/tiles/TileNodeConverter;DDDF)V",
        remap = false,
        at = { @At(value = "TAIL") })
    public void renderTileEntityAt(TileNodeConverter te, double x, double y, double z, float partialTick,
        CallbackInfo ci) {
        DestroyBlockProgress destroyBlockProgress = TileEntityBreaker.getTileEntityDestroyProgress(te);
        if (destroyBlockProgress != null) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(-3.0F, -3.0F);

            this.bindTexture(
                ClientProxy.destructionTextures.get(ThaumcraftIntegration.NODE_STABILIZER)[destroyBlockProgress
                    .getPartialBlockDamage()]);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.0F, (float) z + 0.5F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            float v = (float) Math.min(50, te.count) / 137.0F;
            this.model.renderPart("lock");

            for (int a = 0; a < 4; ++a) {
                GL11.glPushMatrix();

                GL11.glRotatef((float) (90 * a), 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, v);
                this.model.renderPart("piston");

                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();

            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPopMatrix();
            GL11.glPopAttrib();

            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
    }

}
