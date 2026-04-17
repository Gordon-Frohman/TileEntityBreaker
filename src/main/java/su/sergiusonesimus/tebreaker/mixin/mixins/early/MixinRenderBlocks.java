package su.sergiusonesimus.tebreaker.mixin.mixins.early;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public class MixinRenderBlocks {

    @Shadow(remap = true)
    public IIcon overrideBlockTexture;

    // TODO

    @Shadow(remap = true)
    public void setRenderBounds(double p_147782_1_, double p_147782_3_, double p_147782_5_, double p_147782_7_,
        double p_147782_9_, double p_147782_11_) {}

    @Shadow(remap = true)
    public boolean renderStandardBlock(Block blockType, int blockX, int blockY, int blockZ) {
        return false;
    }

    @Inject(method = "renderBlockBeacon", at = @At(value = "HEAD"), cancellable = true)
    public void renderBlockBeacon(BlockBeacon beacon, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (overrideBlockTexture != null) {
            this.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.renderStandardBlock(beacon, x, y, z);
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

}
