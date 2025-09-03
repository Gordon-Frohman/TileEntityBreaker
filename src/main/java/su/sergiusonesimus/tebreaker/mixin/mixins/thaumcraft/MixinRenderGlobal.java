package su.sergiusonesimus.tebreaker.mixin.mixins.thaumcraft;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.blocks.BlockManaPod;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @WrapOperation(
        method = "Lnet/minecraft/client/renderer/RenderGlobal;drawBlockDamageTexture(Lnet/minecraft/client/renderer/Tessellator;Lnet/minecraft/entity/EntityLivingBase;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderBlocks;renderBlockUsingTexture(Lnet/minecraft/block/Block;IIILnet/minecraft/util/IIcon;)V"))
    public void renderBlockUsingTexture(RenderBlocks renderBlocks, Block block, int x, int y, int z, IIcon texture,
        Operation<Void> original) {
        if (!(block instanceof BlockManaPod)) original.call(renderBlocks, block, x, y, z, texture);
    }

}
