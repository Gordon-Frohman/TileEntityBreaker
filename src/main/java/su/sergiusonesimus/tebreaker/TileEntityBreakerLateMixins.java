package su.sergiusonesimus.tebreaker;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import su.sergiusonesimus.tebreaker.mixin.Mixins;

@LateMixin
public class TileEntityBreakerLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.tebreaker.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Mixins.getLateMixins(loadedMods);
    }
}
