package su.sergiusonesimus.tebreaker.integration;

import cpw.mods.ironchest.TileEntityIronChest;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;

public class IronChestsIntegration {

    public static void registerTileEntities() {
        TileEntityBreaker.registerTileEntity(TileEntityIronChest.class, TileEntityBreaker.CHEST);
    }

}
