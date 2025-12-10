package su.sergiusonesimus.tebreaker.mixin.mixins.late.thaumcraft;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;

import su.sergiusonesimus.tebreaker.ClientProxy;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;
import su.sergiusonesimus.tebreaker.mixin.mixins.tebreaker.MixinTileEntitySpecialRenderer;
import thaumcraft.client.renderers.tile.TileEldritchObeliskRenderer;

@Mixin(TileEldritchObeliskRenderer.class)
public class MixinTileEldritchObeliskRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private IModelCustom model;

    @Shadow(remap = false)
    public void renderSide(int h) {}

    @Inject(
        method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDF)V",
        at = @At(value = "TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f, CallbackInfo ci,
        @Local(name = "bob") float bob) {
        DestroyBlockProgress[] dpb = new DestroyBlockProgress[5];
        boolean renderDestruction = false;
        for (int i = 0; i < dpb.length; i++) {
            dpb[i] = TileEntityBreaker.getBlockDestroyProgress(te.xCoord, te.yCoord + i, te.zCoord);
            if (dpb[i] != null) renderDestruction = true;
        }

        if (!renderDestruction) return;

        TileEntityBreaker.breakpoint();

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-3.0F, -3.0F);

        if (dpb[0] != null) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5D, y + 1.0D + (double) bob, z + 0.5D);
            GL11.glRotated(90.0D, 1.0D, 0.0D, 0.0D);
            bindTexture(
                ClientProxy.destructionTextures.get(ThaumcraftIntegration.OBELISK_CAP)[dpb[0].getPartialBlockDamage()]);
            this.model.renderPart("Cap");
            GL11.glPopMatrix();
        }

        if (dpb[1] != null || dpb[2] != null || dpb[3] != null) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5D, y + 1.0D + (double) bob, z + 0.5D);

            for (int i = 1; i <= 3; i++) {
                if (dpb[i] == null) continue;
                ResourceLocation texture = ClientProxy.destructionTextures
                    .get(ThaumcraftIntegration.ELDRITCH_OBELISK + "_" + i)[dpb[i].getPartialBlockDamage()];
                bindTexture(texture);
                breakTexture = texture;
                for (int a = 0; a < 4; ++a) {
                    GL11.glPushMatrix();
                    GL11.glRotatef((float) (a * 90), 0.0F, 1.0F, 0.0F);
                    GL11.glTranslated(0.0D, 0.0D, -0.5D);
                    this.renderSide(3);
                    GL11.glPopMatrix();
                }
            }

            GL11.glPopMatrix();
        }

        if (dpb[4] != null) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5D, y + 4.0D + (double) bob, z + 0.5D);
            GL11.glRotated(90.0D, -1.0D, 0.0D, 0.0D);
            bindTexture(
                ClientProxy.destructionTextures.get(ThaumcraftIntegration.OBELISK_CAP)[dpb[4].getPartialBlockDamage()]);
            this.model.renderPart("Cap");
            GL11.glPopMatrix();
        }

        breakTexture = null;

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    }

    @Inject(method = "renderSide", remap = false, at = { @At(value = "HEAD") }, cancellable = true)
    public void injectRenderSide(int h, CallbackInfo ci) {
        if (breakTexture != null) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-0.5D, (double) h, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(0.5D, (double) h, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(0.5D, 0.0D, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(-0.5D, 0.0D, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            ci.cancel();
        }
    }

}
