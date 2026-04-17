package su.sergiusonesimus.tebreaker.integration;

import ganymedes01.etfuturum.client.model.ModelBanner;
import ganymedes01.etfuturum.tileentities.TileEntityBanner;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;

public class EtFuturumRequiemIntegration {

    public static final String EFR = "efr_";

    public static final String BANNER = EFR + "banner";

    public static void registerTileEntities() {
        ModelBanner banner = new ModelBanner();
        TileEntityBreaker.registerModel(BANNER, 64, 64, banner.bannerSlate, banner.bannerStand, banner.bannerTop);
        TileEntityBreaker.registerTileEntity(TileEntityBanner.class, BANNER);
    }

}
