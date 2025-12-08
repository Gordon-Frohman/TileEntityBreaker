package su.sergiusonesimus.tebreaker.integration;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import net.mcft.betterstorage.client.model.ModelArmorStand;
import net.mcft.betterstorage.client.model.ModelBackpack;
import net.mcft.betterstorage.client.model.ModelLargeLocker;
import net.mcft.betterstorage.client.model.ModelLocker;
import net.mcft.betterstorage.tile.entity.TileEntityBackpack;
import net.mcft.betterstorage.tile.entity.TileEntityLocker;
import net.mcft.betterstorage.tile.entity.TileEntityReinforcedChest;
import net.mcft.betterstorage.tile.stand.TileEntityArmorStand;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

import su.sergiusonesimus.tebreaker.BreakTextureGenerator;
import su.sergiusonesimus.tebreaker.TileEntityBreaker;

public class BetterStorageIntegration {

    public static final String LOCKER = "locker";
    public static final String DOUBLE_LOCKER_TOP = "double_locker_top";
    public static final String DOUBLE_LOCKER_BOTTOM = "double_locker_bottom";
    public static final String BACKPACK = "backpack";
    public static final String ARMOR_STAND = "armor_stand";

    public static void registerTileEntities()
        throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        TileEntityBreaker.registerTileEntity(TileEntityReinforcedChest.class, (te) -> {
            TileEntityReinforcedChest teChest = (TileEntityReinforcedChest) te;
            if (!teChest.isConnected()) {
                return TileEntityBreaker.CHEST;
            }
            return "";
        });

        ModelLocker locker = new ModelLocker();
        TileEntityBreaker.registerTileEntity(TileEntityLocker.class, (te) -> {
            TileEntityLocker teLocker = (TileEntityLocker) te;
            if (!teLocker.isConnected()) {
                return LOCKER;
            }
            return "";
        });
        Field model = locker.getClass()
            .getDeclaredField("model");
        model.setAccessible(true);
        TileEntityBreaker.registerModel(LOCKER, 64, 32, (WavefrontObject) model.get(locker));

        ModelBackpack backpack = new ModelBackpack(true);
        TileEntityBreaker.registerModel(
            BACKPACK,
            32,
            32,
            backpack.main,
            backpack.top,
            backpack.front,
            backpack.left,
            backpack.right);
        TileEntityBreaker.registerTileEntity(TileEntityBackpack.class, BACKPACK);

        ModelArmorStand armorStand = new ModelArmorStand();
        Field bottom = armorStand.getClass()
            .getDeclaredField("bottom");
        bottom.setAccessible(true);
        Field middle = armorStand.getClass()
            .getDeclaredField("middle");
        middle.setAccessible(true);
        Field head = armorStand.getClass()
            .getDeclaredField("head");
        head.setAccessible(true);
        Field shoulder = armorStand.getClass()
            .getDeclaredField("shoulder");
        shoulder.setAccessible(true);
        Field legs = armorStand.getClass()
            .getDeclaredField("legs");
        legs.setAccessible(true);
        TileEntityBreaker.registerModel(
            ARMOR_STAND,
            64,
            32,
            (ModelRenderer) bottom.get(armorStand),
            (ModelRenderer) middle.get(armorStand),
            (ModelRenderer) head.get(armorStand),
            (ModelRenderer) shoulder.get(armorStand),
            (ModelRenderer) legs.get(armorStand));
        TileEntityBreaker.registerTileEntity(TileEntityArmorStand.class, ARMOR_STAND);
        TileEntityBreaker.registerOffset(TileEntityArmorStand.class, 0, 1, 0);
    }

    public static ResourceLocation[] generateDoubleLockerBreakTextures(boolean topPart)
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        float scaleX = 1;
        float scaleY = 2;
        float scaleZ = 1;
        int textureWidth = 64;
        int textureHeight = 64;

        ModelLargeLocker locker = new ModelLargeLocker();
        Field modelField = ModelLocker.class.getDeclaredField("model");
        modelField.setAccessible(true);
        WavefrontObject model = (WavefrontObject) modelField.get(locker);
        ResourceLocation[] result = new ResourceLocation[BreakTextureGenerator.destroyBlockIcons.length];
        BufferedImage[] generatedImages = new BufferedImage[BreakTextureGenerator.destroyBlockIcons.length];
        for (int i = 0; i < result.length; i++) {
            generatedImages[i] = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        }

        Map<Float[][], Float[][]> textureMap = BreakTextureGenerator
            .createTextureToWorldMap(model, scaleX, scaleY, scaleZ, textureWidth, textureHeight);

        for (Entry<Float[][], Float[][]> face : textureMap.entrySet()) {
            Float[][] textureCoords = face.getKey();
            Float[][] worldCoords = face.getValue();
            int length = textureCoords.length - 1;
            // int offsetX = Math.round(textureCoords[length][0]);
            // int offsetY = Math.round(textureCoords[length][1]);
            int offsetX = 0;
            int offsetY = 0;
            Integer minX = null;
            Integer minY = null;
            Integer maxX = null;
            Integer maxY = null;
            Integer worldMinY = null;
            Integer worldMaxY = null;
            for (int i = 0; i < length; i++) {
                if (minX == null || textureCoords[i][0] < minX) minX = Math.round(textureCoords[i][0]);
                if (minY == null || textureCoords[i][1] < minY) minY = Math.round(textureCoords[i][1]);
                if (maxX == null || textureCoords[i][0] > maxX) maxX = Math.round(textureCoords[i][0]);
                if (maxY == null || textureCoords[i][1] > maxY) maxY = Math.round(textureCoords[i][1]);
                if (worldMinY == null || worldCoords[i][1] < worldMinY) worldMinY = Math.round(worldCoords[i][1]);
                if (worldMaxY == null || worldCoords[i][1] > worldMaxY) worldMaxY = Math.round(worldCoords[i][1]);
            }
            int sizeX = maxX - minX;
            int sizeY = maxY - minY;
            if (worldMaxY - worldMinY > 1) {
                sizeY /= 2;
                if (topPart) minY += sizeY;
                else maxY -= sizeY;
            } else {
                if ((topPart && worldMinY < 16) || (!topPart && worldMaxY > 16)) continue;
            }
            int stretchX = 0;
            int stretchY = 0;
            int imageMinX = minX;
            int imageMinY = minY;
            minX += offsetX;
            minY += offsetY;
            maxX += offsetX;
            maxY += offsetY;
            while (minX < 0 && maxX <= 0) {
                minX += 16;
                maxX += 16;
            }
            if (sizeX % 16 != 0) {
                if (sizeX > 16) stretchX = sizeX - 16;
                if (minX * maxX < 0 && Math.abs(minX) == Math.abs(maxX)) {
                    minX += 8 + stretchX / 2;
                    maxX += 8 + stretchX / 2;
                }
                if (minX < 16 && maxX > 16 && 16 - minX == maxX - 16) {
                    minX -= 8 + stretchX / 2;
                    maxX -= 8 + stretchX / 2;
                }
                if (sizeX < 16 && (minX + 16) % 16 > (maxX + 16) % 16) {
                    int dX = maxX % 16;
                    minX -= dX;
                    maxX -= dX;
                }
            }
            while (minY < 0 && maxY <= 0) {
                minY += 16;
                maxY += 16;
            }
            if (sizeY % 16 != 0) {
                if (sizeY > 16) stretchY = sizeY - 16;
                if (minY * maxY < 0 && Math.abs(minY) == Math.abs(maxY)) {
                    minY += 8 + stretchY / 2;
                    maxY += 8 + stretchY / 2;
                }
                if (minY < 16 && maxY > 16 && 16 - minY == maxY - 16) {
                    minY -= 8 + stretchY / 2;
                    maxY -= 8 + stretchY / 2;
                }
                if (sizeY < 16 && (minY + 16) % 16 > (maxY + 16) % 16) {
                    int dY = maxY % 16;
                    minY -= dY;
                    maxY -= dY;
                }
            }
            for (int i = 0; i < generatedImages.length; i++) {
                BreakTextureGenerator.copyIconToImage(
                    i,
                    minX,
                    minY,
                    maxX,
                    maxY,
                    generatedImages[i],
                    imageMinX,
                    imageMinY,
                    stretchX,
                    stretchY);
            }
        }

        for (int i = 0; i < generatedImages.length; i++) {
            String name = (topPart ? DOUBLE_LOCKER_TOP : DOUBLE_LOCKER_BOTTOM) + "_destruction_" + i;
            result[i] = BreakTextureGenerator.registerDynamicTexture(name, generatedImages[i]);
            BreakTextureGenerator.saveTextureToFile(generatedImages[i], name);
        }

        return result;
    }

}
