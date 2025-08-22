package su.sergiusonesimus.tebreaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.FMLLog;

public class BreakTextureGenerator {

    public static ResourceLocation[] chestdestructionTextures = null;

    public static Map<Integer, BufferedImage> destroyBlockIconsMap = new HashMap<Integer, BufferedImage>();
    private static IIcon[] destroyBlockIcons = Minecraft.getMinecraft().renderGlobal.destroyBlockIcons;

    private static final int[] stretchPriorityX = { 7, 14, 3, 15, 0, 12, 1, 10, 5, 9, 2, 13, 4, 11, 6 };
    private static final int[] stretchPriorityY = { 0, 9, 5, 12, 7, 8, 3, 10, 2, 15, 1, 14, 4, 11, 6 };

    public static ResourceLocation[] generateBreakTextures(GeneratorData data) {
        return generateBreakTextures(data.modelName, data.textureWidth, data.textureHeight, data.models);
    }

    public static ResourceLocation[] generateBreakTextures(String modelName, int textureWidth, int textureHeight,
        ModelRenderer... models) {
        ResourceLocation[] result = new ResourceLocation[destroyBlockIcons.length];
        BufferedImage[] generatedImages = new BufferedImage[destroyBlockIcons.length];
        for (int i = 0; i < result.length; i++) {
            generatedImages[i] = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        }

        for (ModelRenderer model : models) {
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
                int stretchX = 0;
                int stretchY = 0;
                int stretchZ = 0;
                while (minX < 0 && maxX <= 0) {
                    minX += 16;
                    maxX += 16;
                }
                if (sizeX % 16 != 0) {
                    if (sizeX > 16) {
                        stretchX = sizeX - 16;
                    }
                    if (minX * maxX < 0 && Math.abs(minX) == Math.abs(maxX)) {
                        minX += 8 + stretchX / 2;
                        maxX += 8 + stretchX / 2;
                    }
                    if (minX < 16 && maxX > 16 && 16 - minX == maxX - 16) {
                        minX -= 8 + stretchX / 2;
                        maxX -= 8 + stretchX / 2;
                    }
                }
                while (minY < 0 && maxY <= 0) {
                    minY += 16;
                    maxY += 16;
                }
                if (sizeY % 16 != 0) {
                    if (sizeY > 16) {
                        stretchY = sizeY - 16;
                    }
                    if (minY * maxY < 0 && Math.abs(minY) == Math.abs(maxY)) {
                        minY += 8 + stretchY / 2;
                        maxY += 8 + stretchY / 2;
                    }
                    if (minY < 16 && maxY > 16 && 16 - minY == maxY - 16) {
                        minY -= 8 + stretchY / 2;
                        maxY -= 8 + stretchY / 2;
                    }
                }
                while (minZ < 0 && maxZ <= 0) {
                    minZ += 16;
                    maxZ += 16;
                }
                if (sizeZ % 16 != 0) {
                    if (sizeZ > 16) {
                        stretchZ = sizeZ - 16;
                    }
                    if (minZ * maxZ < 0 && Math.abs(minZ) == Math.abs(maxZ)) {
                        minZ += 8 + stretchZ / 2;
                        maxZ += 8 + stretchZ / 2;
                    }
                    if (minZ < 16 && maxZ > 16 && 16 - minZ == maxZ - 16) {
                        minZ -= 8 + stretchZ / 2;
                        maxZ -= 8 + stretchZ / 2;
                    }
                }
                for (int i = 0; i < generatedImages.length; i++) {
                    // Y+ face
                    copyIconToImage(
                        i,
                        minX,
                        minZ,
                        maxX,
                        maxZ,
                        generatedImages[i],
                        offsetX + sizeZ,
                        offsetY,
                        stretchX,
                        stretchZ);
                    // Y- face
                    copyIconToImage(
                        i,
                        minX,
                        minZ,
                        maxX,
                        maxZ,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX,
                        offsetY,
                        stretchX,
                        stretchZ);
                    // X+ face
                    copyIconToImage(
                        i,
                        minZ,
                        minY,
                        maxZ,
                        maxY,
                        generatedImages[i],
                        offsetX,
                        offsetY + sizeZ,
                        stretchZ,
                        stretchY);
                    // Z+ face
                    copyIconToImage(
                        i,
                        minX,
                        minY,
                        maxX,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ,
                        offsetY + sizeZ,
                        stretchX,
                        stretchY);
                    // X- face
                    copyIconToImage(
                        i,
                        minZ,
                        minY,
                        maxZ,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX,
                        offsetY + sizeZ,
                        stretchZ,
                        stretchY);
                    // Z- face
                    copyIconToImage(
                        i,
                        minX,
                        minY,
                        maxX,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX + sizeZ,
                        offsetY + sizeZ,
                        stretchX,
                        stretchY);
                }
            }
        }

        for (int i = 0; i < generatedImages.length; i++) {
            String name = modelName + "_destruction_" + i;
            result[i] = registerDynamicTexture(name, generatedImages[i]);
            saveTextureToFile(generatedImages[i], name);
        }

        return result;
    }

    public static ResourceLocation[] generateDoubleChestBreakTextures(boolean rightHalf) {
        String modelName = "double_chest_" + (rightHalf ? "right" : "left");
        int textureWidth = 128;
        int textureHeight = 64;
        ModelLargeChest doubleChest = new ModelLargeChest();
        ModelRenderer[] models = { doubleChest.chestBelow, doubleChest.chestKnob, doubleChest.chestLid };
        ResourceLocation[] result = new ResourceLocation[destroyBlockIcons.length];
        BufferedImage[] generatedImages = new BufferedImage[destroyBlockIcons.length];
        for (int i = 0; i < result.length; i++) {
            generatedImages[i] = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        }

        for (ModelRenderer model : models) {
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
                if (rightHalf) {
                    minX += sizeX / 2;
                } else {
                    maxX -= sizeX / 2;
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
                    copyIconToImage(
                        i,
                        minX,
                        minZ,
                        maxX,
                        maxZ,
                        generatedImages[i],
                        offsetX + sizeZ + (rightHalf ? sizeX / 2 : 0),
                        offsetY);
                    // Y- face
                    copyIconToImage(
                        i,
                        minX,
                        minZ,
                        maxX,
                        maxZ,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX + (rightHalf ? sizeX / 2 : 0),
                        offsetY);
                    // X+ face
                    if (!rightHalf)
                        copyIconToImage(i, minZ, minY, maxZ, maxY, generatedImages[i], offsetX, offsetY + sizeZ);
                    // Z+ face
                    copyIconToImage(
                        i,
                        minX,
                        minY,
                        maxX,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + (rightHalf ? sizeX / 2 : 0),
                        offsetY + sizeZ);
                    // X- face
                    if (rightHalf) copyIconToImage(
                        i,
                        minZ,
                        minY,
                        maxZ,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX,
                        offsetY + sizeZ);
                    // Z- face
                    copyIconToImage(
                        i,
                        minX,
                        minY,
                        maxX,
                        maxY,
                        generatedImages[i],
                        offsetX + sizeZ + sizeX + sizeZ + (!rightHalf ? sizeX / 2 : 0),
                        offsetY + sizeZ);
                }
            }
        }

        for (int i = 0; i < generatedImages.length; i++) {
            String name = modelName + "_destruction_" + i;
            result[i] = registerDynamicTexture(name, generatedImages[i]);
            saveTextureToFile(generatedImages[i], name);
        }

        return result;
    }

    private static BufferedImage copyIconToImage(int iconID, int iconMinX, int iconMinY, int iconMaxX, int iconMaxY,
        BufferedImage image, int imageMinX, int imageMinY) {
        return copyIconToImage(iconID, iconMinX, iconMinY, iconMaxX, iconMaxY, image, imageMinX, imageMinY, 0, 0);
    }

    private static BufferedImage copyIconToImage(int iconID, int iconMinX, int iconMinY, int iconMaxX, int iconMaxY,
        BufferedImage image, int imageMinX, int imageMinY, int stretchX, int stretchY) {

        BufferedImage icon = destroyBlockIconsMap.get(iconID);
        if (icon != null) {
            if ((stretchX > 0 && stretchX < 16) || (stretchY > 0 && stretchY < 16)) {
                icon = icon.getSubimage(0, 0, 16, 16);
                BufferedImage newIcon = new BufferedImage(
                    icon.getWidth() + stretchX,
                    icon.getHeight() + stretchY,
                    icon.getType());
                List<Integer> stretchListX = new ArrayList<Integer>();
                List<Integer> stretchListY = new ArrayList<Integer>();
                for (int i = 0; i < stretchX; i++) {
                    stretchListX.add(stretchPriorityX[i]);
                }
                for (int i = 0; i < stretchY; i++) {
                    stretchListY.add(stretchPriorityY[i]);
                }
                int newX = 0;
                for (int x = 0; x < 16; x++) {
                    boolean shouldStretchX = stretchListX.contains(x);
                    int newY = 0;
                    for (int y = 0; y < 16; y++) {
                        int pixel = icon.getRGB(x, y);
                        newIcon.setRGB(newX, newY, pixel);
                        boolean shouldStretchY = stretchListY.contains(y);
                        if (shouldStretchX) newIcon.setRGB(newX + 1, newY, pixel);
                        if (shouldStretchX && shouldStretchY) newIcon.setRGB(newX + 1, newY + 1, pixel);
                        if (shouldStretchY) {
                            newIcon.setRGB(newX, newY + 1, pixel);
                            newY++;
                        }
                        newY++;
                    }
                    if (shouldStretchX) newX++;
                    newX++;
                }
                icon = newIcon;
            }
            int dX;
            int dY;
            int startX;
            int startY;
            int endX;
            int endY;
            int localSizeX;
            int localSizeY;
            int segmentX;
            int segmentY;
            int iconSizeX = icon.getWidth();
            int iconSizeY = icon.getHeight();

            dX = iconMinX;
            localSizeX = 0;
            do {
                startX = (dX % iconSizeX + iconSizeX) % iconSizeX;
                endX = iconMaxX - dX >= iconSizeX ? iconSizeX : iconMaxX > iconSizeX ? iconMaxX % iconSizeX : iconMaxX;
                dY = iconMinY;
                localSizeY = 0;
                do {
                    startY = (dY % iconSizeY + iconSizeY) % iconSizeY;
                    endY = iconMaxY - dY >= iconSizeY ? iconSizeY
                        : iconMaxY > iconSizeY ? iconMaxY % iconSizeY : iconMaxY;
                    for (int y = startY; y < endY; y++) {
                        for (int x = startX; x < endX; x++) {
                            int pixel = icon.getRGB(x, y);
                            image.setRGB(
                                imageMinX + x - startX + localSizeX,
                                imageMinY + y - startY + localSizeY,
                                pixel);
                        }
                    }
                    segmentY = endY - startY;
                    localSizeY += segmentY;
                    dY += segmentY;
                } while (dY < iconMaxY);
                segmentX = endX - startX;
                localSizeX += segmentX;
                dX += segmentX;
            } while (dX < iconMaxX);
        }

        return image;
    }

    private static ResourceLocation registerDynamicTexture(String name, BufferedImage image) {
        DynamicTexture dynamicTexture = new DynamicTexture(image);

        ResourceLocation resource = new ResourceLocation("dynamictextures:" + name);
        Minecraft.getMinecraft().renderEngine.loadTexture(resource, dynamicTexture);

        return resource;
    }

    private static void saveTextureToFile(BufferedImage image, String filename) {
        if (TileEntityBreaker.generateTextureFiles) {
            try {
                File texturesDir = new File("config/" + TileEntityBreaker.ID + "/generated_textures/");
                if (!texturesDir.exists()) {
                    texturesDir.mkdirs();
                }

                File outputFile = new File(texturesDir, filename + ".png");
                ImageIO.write(image, "PNG", outputFile);

                FMLLog.info("Saved texture: %s", outputFile.getAbsolutePath());

            } catch (IOException e) {
                FMLLog.severe("Failed to save texture %s: %s", filename, e.getMessage());
            }
        }
    }

    public static class GeneratorData {

        public final String modelName;
        private int textureWidth;
        private int textureHeight;
        private ModelRenderer[] models;

        public GeneratorData(String modelName, int textureWidth, int textureHeight, ModelRenderer[] models) {
            this.modelName = modelName;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.models = models;
        }
    }

}
