package su.sergiusonesimus.tebreaker.mixin.mixins.late.thaumcraft;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.client.renderers.block.BlockTubeRenderer;
import thaumcraft.common.blocks.BlockTube;

@Mixin(BlockTubeRenderer.class)
public class MixinBlockTubeRenderer {

    RenderBlocks storedRenderer;

    @Inject(method = "renderWorldBlock", remap = false, at = @At(value = "HEAD"))
    public void storeRenderer(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer,
        CallbackInfoReturnable<Boolean> ci) {
        storedRenderer = renderer;
    }

    @WrapOperation(
        method = "renderWorldBlock",
        remap = false,
        at = @At(value = "FIELD", target = "Lthaumcraft/common/blocks/BlockTube;icon", remap = false))
    public IIcon[] getTubeIcon(BlockTube tube, Operation<IIcon[]> original) {
        boolean blockBeingBroken = false;
        for (IIcon destroyIcon : storedRenderer.minecraftRB.renderGlobal.destroyBlockIcons) {
            if (storedRenderer.overrideBlockTexture == destroyIcon) {
                blockBeingBroken = true;
                break;
            }
        }

        IIcon[] result = original.call(tube);
        if (blockBeingBroken) {
            result = new IIcon[result.length];
            for (int i = 0; i < result.length; i++) result[i] = storedRenderer.overrideBlockTexture;
        }
        return result;
    }

}
