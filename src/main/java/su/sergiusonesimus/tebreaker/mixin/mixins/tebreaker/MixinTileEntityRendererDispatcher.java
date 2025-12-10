package su.sergiusonesimus.tebreaker.mixin.mixins.tebreaker;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import su.sergiusonesimus.tebreaker.mixin.interfaces.IMixinTileEntitySpecialRenderer;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRendererDispatcher {

    @WrapOperation(
        method = "renderTileEntityAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDF)V"))
    public void renderTileEntityAt(TileEntitySpecialRenderer renderer, TileEntity te, double dX, double dY, double dZ,
        float partialTicks, Operation<Void> original) {
        original.call(renderer, te, dX, dY, dZ, partialTicks);

        if (TileEntityBreaker.isTileEntityRegistered(te) && (dX != 0 || dY != 0 || dZ != 0 || partialTicks != 0)) {
            DestroyBlockProgress destroyblockprogress = TileEntityBreaker.getTileEntityDestroyProgress(te);
            if (destroyblockprogress != null) {
                ResourceLocation breakTexture = TileEntityBreaker
                    .getDestructionTexture(te, destroyblockprogress.getPartialBlockDamage());
                if (breakTexture != null) {
                    GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                    GL11.glPushMatrix();

                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);

                    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                    GL11.glPolygonOffset(-3.0F, -3.0F);

                    ((IMixinTileEntitySpecialRenderer) renderer).setBreakTexture(breakTexture);

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);

                    original.call(renderer, te, dX, dY, dZ, partialTicks);

                    GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                    GL11.glPopMatrix();
                    GL11.glPopAttrib();

                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                }
            }
        }
    }

}
