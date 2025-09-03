package su.sergiusonesimus.tebreaker.integration;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

import su.sergiusonesimus.tebreaker.BreakTextureGenerator;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;
import thaumcraft.client.renderers.models.ModelArcaneWorkbench;
import thaumcraft.client.renderers.models.ModelBanner;
import thaumcraft.client.renderers.models.ModelBellows;
import thaumcraft.client.renderers.models.ModelBore;
import thaumcraft.client.renderers.models.ModelBoreBase;
import thaumcraft.client.renderers.models.ModelBoreEmit;
import thaumcraft.client.renderers.models.ModelCentrifuge;
import thaumcraft.client.renderers.models.ModelCrystal;
import thaumcraft.client.renderers.models.ModelCube;
import thaumcraft.client.renderers.models.ModelJar;
import thaumcraft.client.renderers.models.ModelResearchTable;
import thaumcraft.client.renderers.models.ModelTable;
import thaumcraft.common.tiles.TileAlchemyFurnaceAdvanced;
import thaumcraft.common.tiles.TileAlembic;
import thaumcraft.common.tiles.TileArcaneBore;
import thaumcraft.common.tiles.TileArcaneBoreBase;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileBanner;
import thaumcraft.common.tiles.TileBellows;
import thaumcraft.common.tiles.TileCentrifuge;
import thaumcraft.common.tiles.TileChestHungry;
import thaumcraft.common.tiles.TileCrystal;
import thaumcraft.common.tiles.TileDeconstructionTable;
import thaumcraft.common.tiles.TileEldritchCap;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;
import thaumcraft.common.tiles.TileEldritchCrystal;
import thaumcraft.common.tiles.TileEssentiaCrystalizer;
import thaumcraft.common.tiles.TileEssentiaReservoir;
import thaumcraft.common.tiles.TileFluxScrubber;
import thaumcraft.common.tiles.TileFocalManipulator;
import thaumcraft.common.tiles.TileInfusionPillar;
import thaumcraft.common.tiles.TileMagicWorkbenchCharger;
import thaumcraft.common.tiles.TileManaPod;
import thaumcraft.common.tiles.TileTable;
import thaumcraft.common.tiles.TileVisRelay;

public class ThaumcraftIntegration {

    public static final String THAUMCRAFT = "thaumcraft";

    public static final String ALCHEMY_FURNACE = "alch_furnace";
    public static final String ALCHEMY_FURNACE_TANK = "alch_furnace_tank";
    public static final String ALEMBIC = "alembic";
    public static final String ARCANE_BORE = "arcane_bore";
    public static final String ARCANE_WORKBENCH = "arcane_workbench";
    public static final String BANNER = "banner";
    public static final String BELLOWS = "bellows";
    public static final String CENTRIFUGE = "centrifuge";
    public static final String CRYSTAL = "crystal";
    public static final String OBELISK_CAP = "obelisk_cap";
    public static final String CRAB_VENT = "crab_vent";
    public static final String ELDRITCH_CRYSTAL = "eldritch_crystal";
    public static final String ELDRITCH_OBELISK = "eldritch_obelisk";
    public static final String ESSENTIA_CRYSTALIZER = "essentia_crystalizer";
    public static final String ESSENTIA_RESERVOIR = "essentia_reservoir";
    public static final String INFUSION_PILLAR = "infusion_pillar";
    public static final String JAR = "jar";
    public static final String MANA_POD = "mana_pod";
    public static final String NODE_STABILIZER = "node_stabilizer";
    public static final String RESEARCH_TABLE_RIGHT = "research_table_right";
    public static final String RESEARCH_TABLE_LEFT = "research_table_left";
    public static final String[] RUNIC_MATRIX = new String[8];
    public static final String TABLE = "table";
    public static final String THAUMATORIUM = "thaumatorium";
    public static final String VIS_RELAY = "vis_relay";

    public static void registerTileEntities()
        throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        TileEntityBreaker.registerTileEntity(TileAlchemyFurnaceAdvanced.class, ALCHEMY_FURNACE);
        TileEntityBreaker.registerModel(
            ALCHEMY_FURNACE,
            128,
            128,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/adv_alch_furnace.obj")));
        TileEntityBreaker.registerModel(
            ALCHEMY_FURNACE_TANK,
            32,
            32,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/adv_alch_furnace.obj")));

        TileEntityBreaker.registerTileEntity(TileAlembic.class, ALEMBIC);
        TileEntityBreaker.registerModel(
            ALEMBIC,
            64,
            64,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/alembic.obj")));

        ModelBoreBase modelBoreBase = new ModelBoreBase();
        ModelBoreEmit modelBoreEmit = new ModelBoreEmit();
        ModelBore modelBore = new ModelBore();
        Field field1 = modelBoreBase.getClass()
            .getDeclaredField("Base1");
        field1.setAccessible(true);
        Field field2 = modelBoreBase.getClass()
            .getDeclaredField("PillarMid");
        field2.setAccessible(true);
        Field field3 = modelBoreBase.getClass()
            .getDeclaredField("Pillar2");
        field3.setAccessible(true);
        Field field4 = modelBoreBase.getClass()
            .getDeclaredField("Nozzle1");
        field4.setAccessible(true);
        Field field5 = modelBoreEmit.getClass()
            .getDeclaredField("Knob");
        field5.setAccessible(true);
        Field field6 = modelBoreEmit.getClass()
            .getDeclaredField("Cross1");
        field6.setAccessible(true);
        Field field7 = modelBoreEmit.getClass()
            .getDeclaredField("Rod");
        field7.setAccessible(true);
        Field field8 = modelBore.getClass()
            .getDeclaredField("Base");
        field8.setAccessible(true);
        Field field9 = modelBore.getClass()
            .getDeclaredField("Side1");
        field9.setAccessible(true);
        Field field10 = modelBore.getClass()
            .getDeclaredField("NozCrossbar");
        field10.setAccessible(true);
        Field field11 = modelBore.getClass()
            .getDeclaredField("NozFront");
        field11.setAccessible(true);
        Field field12 = modelBore.getClass()
            .getDeclaredField("NozMid");
        field12.setAccessible(true);
        TileEntityBreaker.registerTileEntity(TileArcaneBoreBase.class, ARCANE_BORE);
        TileEntityBreaker.registerTileEntity(TileArcaneBore.class, ARCANE_BORE);
        TileEntityBreaker.registerModel(
            ARCANE_BORE,
            128,
            64,
            (ModelRenderer) field1.get(modelBoreBase),
            (ModelRenderer) field2.get(modelBoreBase),
            (ModelRenderer) field3.get(modelBoreBase),
            (ModelRenderer) field4.get(modelBoreBase),
            (ModelRenderer) field5.get(modelBoreEmit),
            (ModelRenderer) field6.get(modelBoreEmit),
            (ModelRenderer) field7.get(modelBoreEmit),
            (ModelRenderer) field8.get(modelBore),
            (ModelRenderer) field9.get(modelBore),
            (ModelRenderer) field10.get(modelBore),
            (ModelRenderer) field11.get(modelBore),
            (ModelRenderer) field12.get(modelBore));

        TileEntityBreaker.registerTileEntity(TileArcaneWorkbench.class, ARCANE_WORKBENCH);
        ModelArcaneWorkbench modelWorkbench = new ModelArcaneWorkbench();
        field1 = modelWorkbench.getClass()
            .getDeclaredField("Top");
        field1.setAccessible(true);
        field2 = modelWorkbench.getClass()
            .getDeclaredField("Base");
        field2.setAccessible(true);
        field3 = modelWorkbench.getClass()
            .getDeclaredField("Leg1");
        field3.setAccessible(true);
        TileEntityBreaker.registerModel(
            ARCANE_WORKBENCH,
            128,
            64,
            (ModelRenderer) field1.get(modelWorkbench),
            (ModelRenderer) field2.get(modelWorkbench),
            (ModelRenderer) field3.get(modelWorkbench));

        TileEntityBreaker.registerTileEntity(TileBanner.class, BANNER);
        ModelBanner modelBanner = new ModelBanner();
        field1 = modelBanner.getClass()
            .getDeclaredField("B1");
        field1.setAccessible(true);
        field2 = modelBanner.getClass()
            .getDeclaredField("Beam");
        field2.setAccessible(true);
        modelBanner.Banner.rotationPointY -= 9;
        field3 = modelBanner.getClass()
            .getDeclaredField("Pole");
        field3.setAccessible(true);
        TileEntityBreaker.registerModel(
            BANNER,
            128,
            64,
            (ModelRenderer) field1.get(modelBanner),
            (ModelRenderer) field2.get(modelBanner),
            modelBanner.Banner,
            (ModelRenderer) field3.get(modelBanner));

        TileEntityBreaker.registerTileEntity(TileBellows.class, BELLOWS);
        ModelBellows modelBellows = new ModelBellows();
        TileEntityBreaker
            .registerModel(BELLOWS, 128, 64, modelBellows.BottomPlank, modelBellows.Bag, modelBellows.Nozzle);

        TileEntityBreaker.registerTileEntity(TileCentrifuge.class, CENTRIFUGE);
        ModelCentrifuge modelModelCentrifuge = new ModelCentrifuge();
        field1 = modelModelCentrifuge.getClass()
            .getDeclaredField("Crossbar");
        field1.setAccessible(true);
        field2 = modelModelCentrifuge.getClass()
            .getDeclaredField("Dingus1");
        field2.setAccessible(true);
        field3 = modelModelCentrifuge.getClass()
            .getDeclaredField("Core");
        field3.setAccessible(true);
        field4 = modelModelCentrifuge.getClass()
            .getDeclaredField("Top");
        field4.setAccessible(true);
        field5 = modelModelCentrifuge.getClass()
            .getDeclaredField("Bottom");
        field5.setAccessible(true);
        TileEntityBreaker.registerModel(
            CENTRIFUGE,
            64,
            32,
            (ModelRenderer) field1.get(modelModelCentrifuge),
            (ModelRenderer) field2.get(modelModelCentrifuge),
            (ModelRenderer) field3.get(modelModelCentrifuge),
            (ModelRenderer) field4.get(modelModelCentrifuge),
            (ModelRenderer) field5.get(modelModelCentrifuge));

        TileEntityBreaker.registerTileEntity(TileChestHungry.class, TileEntityBreaker.CHEST);

        TileEntityBreaker.registerTileEntity(TileCrystal.class, CRYSTAL);
        ModelCrystal modelCrystal = new ModelCrystal();
        field1 = modelCrystal.getClass()
            .getDeclaredField("Crystal");
        field1.setAccessible(true);
        ((ModelRenderer) field1.get(modelCrystal)).rotationPointX += 40;
        ((ModelRenderer) field1.get(modelCrystal)).rotationPointY += 40;
        ((ModelRenderer) field1.get(modelCrystal)).rotationPointZ += 40;
        TileEntityBreaker.registerModel(CRYSTAL, 64, 32, (ModelRenderer) field1.get(modelCrystal));

        TileEntityBreaker.registerTileEntity(TileDeconstructionTable.class, ARCANE_WORKBENCH);

        TileEntityBreaker.registerTileEntity(TileEldritchCap.class, OBELISK_CAP);
        TileEntityBreaker.registerModel(
            OBELISK_CAP,
            32,
            32,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/obelisk_cap.obj")));

        TileEntityBreaker.registerTileEntity(TileEldritchCrabSpawner.class, CRAB_VENT);
        TileEntityBreaker.registerModel(
            CRAB_VENT,
            16,
            16,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/crabvent.obj")));

        TileEntityBreaker.registerTileEntity(TileEldritchCrystal.class, ELDRITCH_CRYSTAL);
        TileEntityBreaker.registerModel(ELDRITCH_CRYSTAL, THAUMCRAFT);

        for (int i = 1; i <= 3; i++) {
            TileEntityBreaker.registerModel(ELDRITCH_OBELISK + "_" + i, THAUMCRAFT);
        }

        TileEntityBreaker.registerTileEntity(TileEssentiaCrystalizer.class, ESSENTIA_CRYSTALIZER);
        TileEntityBreaker.registerModel(
            ESSENTIA_CRYSTALIZER,
            64,
            64,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/crystalizer.obj")));

        TileEntityBreaker.registerTileEntity(TileEssentiaReservoir.class, ESSENTIA_RESERVOIR);
        TileEntityBreaker.registerModel(
            ESSENTIA_RESERVOIR,
            64,
            64,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/reservoir.obj")));

        TileEntityBreaker.registerTileEntity(TileFluxScrubber.class, OBELISK_CAP);

        TileEntityBreaker.registerTileEntity(TileFocalManipulator.class, ARCANE_WORKBENCH);

        TileEntityBreaker.registerTileEntity(TileInfusionPillar.class, INFUSION_PILLAR);
        TileEntityBreaker.registerModel(
            INFUSION_PILLAR,
            64,
            64,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/pillar.obj")));

        ModelJar modelJar = new ModelJar();
        TileEntityBreaker.registerModel(JAR, 64, 32, modelJar.Core, modelJar.Lid);

        TileEntityBreaker.registerTileEntity(TileMagicWorkbenchCharger.class, VIS_RELAY);

        TileEntityBreaker.registerTileEntity(TileManaPod.class, MANA_POD);
        TileEntityBreaker.registerModel(MANA_POD, THAUMCRAFT);

        TileEntityBreaker.registerModel(
            NODE_STABILIZER,
            32,
            32,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/node_stabilizer.obj")),
            true);

        ModelCube modelCube = new ModelCube(0);
        modelCube.textureWidth = 32;
        modelCube.textureHeight = 32;
        float negCorner = 0.0F;
        float posCorner = 8.0F;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    int ordinal = i << 2 | j << 1 | k;
                    RUNIC_MATRIX[ordinal] = "runic_matrix_" + ordinal;
                    field1 = modelCube.getClass()
                        .getDeclaredField("cube");
                    field1.setAccessible(true);
                    field1.set(modelCube, new ModelRenderer(modelCube, 0, 0));
                    ModelRenderer cube = (ModelRenderer) field1.get(modelCube);
                    cube.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
                    cube.setRotationPoint(
                        i == 1 ? posCorner : negCorner,
                        j == 1 ? posCorner : negCorner,
                        k == 0 ? posCorner : negCorner);
                    cube.setTextureSize(32, 32);
                    cube.mirror = true;
                    TileEntityBreaker.registerModel(RUNIC_MATRIX[ordinal], 32, 32, cube);
                }
            }
        }

        TileEntityBreaker.registerTileEntity(TileTable.class, TABLE);
        ModelTable modelTable = new ModelTable();
        field1 = modelTable.getClass()
            .getDeclaredField("Top");
        field1.setAccessible(true);
        field2 = modelTable.getClass()
            .getDeclaredField("Leg1");
        field2.setAccessible(true);
        field3 = modelTable.getClass()
            .getDeclaredField("Crossbar");
        field3.setAccessible(true);
        TileEntityBreaker.registerModel(
            TABLE,
            64,
            32,
            (ModelRenderer) field1.get(modelTable),
            (ModelRenderer) field2.get(modelTable),
            (ModelRenderer) field3.get(modelTable));

        TileEntityBreaker.registerModel(
            THAUMATORIUM,
            1,
            2,
            1,
            64,
            64,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/thaumatorium.obj")));

        TileEntityBreaker.registerTileEntity(TileVisRelay.class, VIS_RELAY);
        TileEntityBreaker.registerModel(
            VIS_RELAY,
            32,
            32,
            (WavefrontObject) AdvancedModelLoader
                .loadModel(new ResourceLocation(THAUMCRAFT, "textures/models/vis_relay.obj")));
    }

    public static ResourceLocation[] generateResearchTableBreakTextures(boolean rightHalf)
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        String modelName = rightHalf ? RESEARCH_TABLE_RIGHT : RESEARCH_TABLE_LEFT;
        int textureWidth = 128;
        int textureHeight = 64;
        ModelResearchTable researchTable = new ModelResearchTable();
        Field top = researchTable.getClass()
            .getDeclaredField("Top");
        top.setAccessible(true);
        ((ModelRenderer) top.get(researchTable)).rotationPointX = -16;
        ((ModelRenderer) top.get(researchTable)).rotationPointZ = 0;
        Field leg = researchTable.getClass()
            .getDeclaredField(rightHalf ? "Leg4" : "Leg1");
        leg.setAccessible(true);
        Field crossbar = researchTable.getClass()
            .getDeclaredField("Crossbar");
        crossbar.setAccessible(true);
        ((ModelRenderer) crossbar.get(researchTable)).rotationPointX = -12;
        Field inkwell = researchTable.getClass()
            .getDeclaredField("Inkwell");
        inkwell.setAccessible(true);
        ModelRenderer[] models = { (ModelRenderer) top.get(researchTable), (ModelRenderer) leg.get(researchTable),
            (ModelRenderer) crossbar.get(researchTable),
            !rightHalf ? (ModelRenderer) crossbar.get(researchTable) : null };
        ResourceLocation[] result = new ResourceLocation[BreakTextureGenerator.destroyBlockIcons.length];
        BufferedImage[] generatedImages = new BufferedImage[BreakTextureGenerator.destroyBlockIcons.length];
        for (int i = 0; i < result.length; i++) {
            generatedImages[i] = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        }

        for (ModelRenderer model : models) {
            if (model == null) continue;
            int offsetX = model.textureOffsetX;
            int offsetY = model.textureOffsetY;
            for (Object modelObject : model.cubeList) {
                ModelBox modelBox = (ModelBox) modelObject;
                // Getting bounds of the box in space of one block (in pixels)
                int minX = (int) (model.rotationPointX + modelBox.posX1);
                int minY = (int) (model.rotationPointY + modelBox.posY1);
                int minZ = (int) (model.rotationPointZ + modelBox.posZ1);
                int maxX = (int) (model.rotationPointX + modelBox.posX2);
                int maxY = (int) (model.rotationPointY + modelBox.posY2);
                int maxZ = (int) (model.rotationPointZ + modelBox.posZ2);
                int sizeX = maxX - minX;
                int sizeY = maxY - minY;
                int sizeZ = maxZ - minZ;
                if (sizeX > 16) {
                    if (rightHalf) {
                        minX += sizeX / 2;
                    } else {
                        maxX -= sizeX / 2;
                    }
                }
                while (minX < 0 && maxX <= 0) {
                    minX += 16;
                    maxX += 16;
                }
                if (sizeX % 16 != 0) {
                    if (minX * maxX < 0 && Math.abs(minX) == Math.abs(maxX)) {
                        minX += 8;
                        maxX += 8;
                    }
                    if (minX < 16 && maxX > 16 && 16 - minX == maxX - 16) {
                        minX -= 8;
                        maxX -= 8;
                    }
                }
                while (minY < 0 && maxY <= 0) {
                    minY += 16;
                    maxY += 16;
                }
                if (sizeY % 16 != 0) {
                    if (minY * maxY < 0 && Math.abs(minY) == Math.abs(maxY)) {
                        minY += 8;
                        maxY += 8;
                    }
                    if (minY < 16 && maxY > 16 && 16 - minY == maxY - 16) {
                        minY -= 8;
                        maxY -= 8;
                    }
                }
                while (minZ < 0 && maxZ <= 0) {
                    minZ += 16;
                    maxZ += 16;
                }
                if (sizeZ % 16 != 0) {
                    if (minZ * maxZ < 0 && Math.abs(minZ) == Math.abs(maxZ)) {
                        minZ += 8;
                        maxZ += 8;
                    }
                    if (minZ < 16 && maxZ > 16 && 16 - minZ == maxZ - 16) {
                        minZ -= 8;
                        maxZ -= 8;
                    }
                }
                for (int i = 0; i < generatedImages.length; i++) {
                    // Y+ face
                    BreakTextureGenerator.copyIconToImage(
                        i,
                        minX,
                        minZ,
                        maxX,
                        maxZ,
                        generatedImages[i],
                        offsetX + sizeZ + (sizeX > 16 && rightHalf ? sizeX / 2 : 0),
                        offsetY);
                    // Y- face
                    BreakTextureGenerator.copyIconToImage(
                        i,
                        minX,
                        minZ,
                        maxX,
                        maxZ,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX + (sizeX > 16 && rightHalf ? sizeX / 2 : 0),
                        offsetY);
                    // X+ face
                    if (sizeZ < 16 || !rightHalf) BreakTextureGenerator
                        .copyIconToImage(i, minZ, minY, maxZ, maxY, generatedImages[i], offsetX, offsetY + sizeZ);
                    // Z+ face
                    BreakTextureGenerator.copyIconToImage(
                        i,
                        minX,
                        minY,
                        maxX,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + (sizeX > 16 && rightHalf ? sizeX / 2 : 0),
                        offsetY + sizeZ);
                    // X- face
                    if (sizeZ < 16 || rightHalf) BreakTextureGenerator.copyIconToImage(
                        i,
                        minZ,
                        minY,
                        maxZ,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX,
                        offsetY + sizeZ);
                    // Z- face
                    BreakTextureGenerator.copyIconToImage(
                        i,
                        minX,
                        minY,
                        maxX,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX + sizeZ + (sizeX > 16 && !rightHalf ? sizeX / 2 : 0),
                        offsetY + sizeZ);
                }
            }
        }

        for (int i = 0; i < generatedImages.length; i++) {
            String name = modelName + "_destruction_" + i;
            result[i] = BreakTextureGenerator.registerDynamicTexture(name, generatedImages[i]);
            BreakTextureGenerator.saveTextureToFile(generatedImages[i], name);
        }

        return result;
    }

}
