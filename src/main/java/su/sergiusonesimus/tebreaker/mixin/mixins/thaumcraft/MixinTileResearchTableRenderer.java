package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import su.sergiusonesimus.tebreaker.ClientProxy;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import su.sergiusonesimus.tebreaker.integration.ThaumcraftIntegration;
import su.sergiusonesimus.tebreaker.mixin.mixins.MixinTileEntitySpecialRenderer;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelResearchTable;
import thaumcraft.client.renderers.tile.TileResearchTableRenderer;
import thaumcraft.common.tiles.TileResearchTable;

@Mixin(TileResearchTableRenderer.class)
public class MixinTileResearchTableRenderer extends MixinTileEntitySpecialRenderer {

    @Shadow(remap = false)
    private ModelResearchTable tableModel;

    @Inject(
        method = "renderTileEntityAt",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/renderers/models/ModelResearchTable;renderAll()V",
            shift = Shift.AFTER))
    public void renderTileEntityAt(TileResearchTable te, double dX, double dY, double dZ, float partialTicks,
        CallbackInfo ci) {
        if (dX == 0 && dY == 0 && dZ == 0 && partialTicks == 0) return;
        DestroyBlockProgress destroyBlockProgress = TileEntityBreaker.getTileEntityDestroyProgress(te);
        int neighbourX = te.xCoord;
        int neighbourY = te.yCoord;
        int neighbourZ = te.zCoord;
        switch (te.getBlockMetadata()) {
            default:
                neighbourX++;
                break;
            case 2:
                neighbourZ--;
                break;
            case 3:
                neighbourZ++;
                break;
            case 4:
                neighbourX--;
                break;
        }
        DestroyBlockProgress destroyNeighboutBlockProgress = TileEntityBreaker
            .getBlockDestroyProgress(neighbourX, neighbourY, neighbourZ);
        if (destroyBlockProgress == null && destroyNeighboutBlockProgress == null) return;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-3.0F, -3.0F);

        Field field;
        ModelRenderer leg1;
        ModelRenderer leg2;

        if (destroyBlockProgress != null) {
            this.bindTexture(
                ClientProxy.destructionTextures.get(ThaumcraftIntegration.RESEARCH_TABLE_LEFT)[destroyBlockProgress
                    .getPartialBlockDamage()]);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

            try {
                field = tableModel.getClass()
                    .getDeclaredField("Leg3");
                field.setAccessible(true);
                leg1 = (ModelRenderer) field.get(tableModel);
                leg1.isHidden = true;
                field = tableModel.getClass()
                    .getDeclaredField("Leg4");
                field.setAccessible(true);
                leg2 = (ModelRenderer) field.get(tableModel);
                leg2.isHidden = true;

                tableModel.renderAll();
                leg1.isHidden = false;
                leg2.isHidden = false;
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (destroyNeighboutBlockProgress != null) {
            this.bindTexture(
                ClientProxy.destructionTextures.get(
                    ThaumcraftIntegration.RESEARCH_TABLE_RIGHT)[destroyNeighboutBlockProgress.getPartialBlockDamage()]);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

            try {
                field = tableModel.getClass()
                    .getDeclaredField("Leg1");
                field.setAccessible(true);
                leg1 = (ModelRenderer) field.get(tableModel);
                leg1.isHidden = true;
                field = tableModel.getClass()
                    .getDeclaredField("Leg2");
                field.setAccessible(true);
                leg2 = (ModelRenderer) field.get(tableModel);
                leg2.isHidden = true;

                tableModel.renderAll();
                leg1.isHidden = false;
                leg2.isHidden = false;
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPopMatrix();
        GL11.glPopAttrib();

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        UtilsFX.bindTexture("textures/models/restable.png");
    }
}
