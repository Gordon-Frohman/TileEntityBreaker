package su.sergiusonesimus.tebreaker.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class Mixins implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<String>();
        try {
            if (Class.forName("net.mcft.copy.betterstorage.BetterStorage") != null) {
                mixins.add("betterstorage.MixinTileEntityBackpackRenderer");
                mixins.add("betterstorage.MixinTileEntityReinforcedChestRenderer");
                mixins.add("betterstorage.MixinTileEntityLockerRenderer");
            }
        } catch (ClassNotFoundException e) {}
        try {
            if (Class.forName("thaumcraft.api.ThaumcraftApi") != null) {
                mixins.add("thaumcraft.MixinBlockTubeRenderer");
                mixins.add("thaumcraft.MixinRenderGlobal");

                mixins.add("thaumcraft.MixinTileAlchemyFurnaceAdvancedRenderer");
                mixins.add("thaumcraft.MixinTileAlembicRenderer");
                mixins.add("thaumcraft.MixinTileArcaneBoreBaseRenderer");
                mixins.add("thaumcraft.MixinTileArcaneBoreRenderer");
                mixins.add("thaumcraft.MixinTileArcaneWorkbenchRenderer");
                mixins.add("thaumcraft.MixinTileBannerRenderer");
                mixins.add("thaumcraft.MixinTileBellowsRenderer");
                mixins.add("thaumcraft.MixinTileCentrifugeRenderer");
                mixins.add("thaumcraft.MixinTileChestHungryRenderer");
                mixins.add("thaumcraft.MixinTileCrystalRenderer");
                mixins.add("thaumcraft.MixinTileDeconstructionTableRenderer");
                mixins.add("thaumcraft.MixinTileEldritchCapRenderer");
                mixins.add("thaumcraft.MixinTileEldritchCrabSpawnerRenderer");
                mixins.add("thaumcraft.MixinTileEldritchCrystalRenderer");
                mixins.add("thaumcraft.MixinTileEldritchObeliskRenderer");
                mixins.add("thaumcraft.MixinTileEssentiaCrystalizerRenderer");
                mixins.add("thaumcraft.MixinTileEssentiaReservoirRenderer");
                mixins.add("thaumcraft.MixinTileFluxScrubberRenderer");
                mixins.add("thaumcraft.MixinTileFocalManipulatorRenderer");
                mixins.add("thaumcraft.MixinTileInfusionPillarRenderer");
                mixins.add("thaumcraft.MixinTileMagicWorkbenchChargerRenderer");
                mixins.add("thaumcraft.MixinTileManaPodRenderer");
                mixins.add("thaumcraft.MixinTileNodeConverterRenderer");
                mixins.add("thaumcraft.MixinTileNodeStabilizerRenderer");
                mixins.add("thaumcraft.MixinTileResearchTableRenderer");
                mixins.add("thaumcraft.MixinTileRunicMatrixRenderer");
                mixins.add("thaumcraft.MixinTileTableRenderer");
                mixins.add("thaumcraft.MixinTileThaumatoriumRenderer");
                mixins.add("thaumcraft.MixinTileVisRelayRenderer");
            }
        } catch (ClassNotFoundException e) {}
        return mixins;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

}
