package su.sergiusonesimus.tebreaker.integration;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelRenderer;

import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import twilightforest.client.model.ModelTFGhast;
import twilightforest.client.model.ModelTFHydraHead;
import twilightforest.client.model.ModelTFKnightPhantom2;
import twilightforest.client.model.ModelTFKnightlyArmor;
import twilightforest.client.model.ModelTFLich;
import twilightforest.client.model.ModelTFMinoshroom;
import twilightforest.client.model.ModelTFNaga;
import twilightforest.client.model.ModelTFQuestRam;
import twilightforest.client.model.ModelTFSnowQueen;
import twilightforest.client.model.ModelTFTowerBoss;
import twilightforest.client.model.ModelTFYetiAlpha;

public class TwilightForestIntegration {

    public static void registerTileEntities()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ModelTFHydraHead hydraHeadModel = new ModelTFHydraHead();
        Field field1 = hydraHeadModel.getClass()
            .getDeclaredField("head");
        field1.setAccessible(true);
        Field field2 = hydraHeadModel.getClass()
            .getDeclaredField("jaw");
        field2.setAccessible(true);
        Field field3 = hydraHeadModel.getClass()
            .getDeclaredField("frill");
        field3.setAccessible(true);
        TileEntityBreaker.registerModel(
            "hydra_trophy",
            512,
            256,
            (ModelRenderer) field1.get(hydraHeadModel),
            (ModelRenderer) field2.get(hydraHeadModel),
            (ModelRenderer) field3.get(hydraHeadModel));

        ModelTFNaga nagaHeadModel = new ModelTFNaga();
        field1 = nagaHeadModel.getClass()
            .getDeclaredField("head");
        field1.setAccessible(true);
        TileEntityBreaker.registerModel("naga_trophy", 64, 32, (ModelRenderer) field1.get(nagaHeadModel));

        ModelTFLich lichModel = new ModelTFLich();
        TileEntityBreaker.registerModel("lich_trophy", 64, 64, lichModel.bipedHead, lichModel.bipedHeadwear);

        ModelTFTowerBoss urGhastModel = new ModelTFTowerBoss();
        field1 = ModelTFGhast.class.getDeclaredField("body");
        field1.setAccessible(true);
        field2 = ModelTFGhast.class.getDeclaredField("tentacles");
        field2.setAccessible(true);
        TileEntityBreaker.registerModel(
            "ur_ghast_trophy",
            64,
            64,
            (ModelRenderer) field1.get(urGhastModel),
            ((ModelRenderer[]) field2.get(urGhastModel))[0]);

        ModelTFSnowQueen snowQueenModel = new ModelTFSnowQueen();
        TileEntityBreaker
            .registerModel("snow_queen_trophy", 64, 32, snowQueenModel.bipedHead, snowQueenModel.bipedHeadwear);

        ModelTFMinoshroom minoshroomModel = new ModelTFMinoshroom();
        field1 = minoshroomModel.getClass()
            .getDeclaredField("snout");
        field1.setAccessible(true);
        TileEntityBreaker.registerModel(
            "minoshroom_trophy",
            128,
            32,
            minoshroomModel.bipedHead,
            (ModelRenderer) field1.get(minoshroomModel),
            minoshroomModel.lefthorn1,
            minoshroomModel.lefthorn2,
            minoshroomModel.righthorn1,
            minoshroomModel.righthorn2);

        ModelTFKnightPhantom2 knightPhantomModel = new ModelTFKnightPhantom2();
        TileEntityBreaker.registerModel("knight_phantom_trophy_head", 64, 32, knightPhantomModel.bipedHead);

        ModelTFKnightlyArmor knightPhantomArmorModel = new ModelTFKnightlyArmor(0, 0.5F);
        TileEntityBreaker.registerModel(
            "knight_phantom_trophy_helmet",
            64,
            32,
            knightPhantomArmorModel.bipedHead,
            knightPhantomArmorModel.lefthorn1,
            knightPhantomArmorModel.lefthorn2,
            knightPhantomArmorModel.righthorn1,
            knightPhantomArmorModel.righthorn2);

        ModelTFYetiAlpha alphaYetiModel = new ModelTFYetiAlpha().trophySetup();
        ModelRenderer horn1a = new ModelRenderer(alphaYetiModel, 0, 108);
        horn1a.addBox(-9.0F, -5.0F, -5.0F, 10, 10, 10);
        horn1a.setRotationPoint(-24.0F, -58.0F, -8.0F);
        ModelRenderer horn1b = new ModelRenderer(alphaYetiModel, 40, 108);
        horn1b.addBox(-14.0F, -4.0F, -4.0F, 18, 8, 8);
        horn1b.setRotationPoint(-8.0F, 0.0F, 0.0F);
        TileEntityBreaker.registerModel(
            "alpha_yeti_trophy",
            256,
            128,
            alphaYetiModel.bipedBody,
            alphaYetiModel.mouth,
            alphaYetiModel.rightEye,
            horn1a,
            horn1b);

        ModelTFQuestRam questingRamModel = new ModelTFQuestRam();
        TileEntityBreaker.registerModel("questing_ram_trophy", 128, 128, questingRamModel.head);

        // TileEntityBreaker.registerTileEntity(TileEntityTFTrophy.class, (te) -> {
        // TileEntityTFTrophy teTrophy = (TileEntityTFTrophy) te;
        // if (!teTrophy.isConnected()) {
        // return "locker";
        // }
        // return "";
        // });
    }

}
