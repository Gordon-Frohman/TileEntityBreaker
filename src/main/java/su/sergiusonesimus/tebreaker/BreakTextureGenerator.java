package su.sergiusonesimus.tebreaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;

import cpw.mods.fml.common.FMLLog;

public class BreakTextureGenerator {

    public static ResourceLocation[] chestdestructionTextures = null;

    public static Map<Integer, BufferedImage> destroyBlockIconsMap = new HashMap<Integer, BufferedImage>();
    public static IIcon[] destroyBlockIcons = Minecraft.getMinecraft().renderGlobal.destroyBlockIcons;

    private static final int[] stretchPriorityX = { 7, 14, 3, 15, 0, 12, 1, 10, 5, 9, 2, 13, 4, 11, 6 };
    private static final int[] stretchPriorityY = { 0, 9, 5, 12, 7, 8, 3, 10, 2, 15, 1, 14, 4, 11, 6 };

    public static ResourceLocation[] generateBreakTextures(GeneratorData data) {
        if (data.model != null) {
            return generateBreakTextures(
                data.modelName,
                data.scaleX,
                data.scaleY,
                data.scaleZ,
                data.textureWidth,
                data.textureHeight,
                data.model);
        }
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
                while (minZ < 0 && maxZ <= 0) {
                    minZ += 16;
                    maxZ += 16;
                }
                if (sizeZ % 16 != 0) {
                    if (sizeZ > 16) stretchZ = sizeZ - 16;
                    if (minZ * maxZ < 0 && Math.abs(minZ) == Math.abs(maxZ)) {
                        minZ += 8 + stretchZ / 2;
                        maxZ += 8 + stretchZ / 2;
                    }
                    if (minZ < 16 && maxZ > 16 && 16 - minZ == maxZ - 16) {
                        minZ -= 8 + stretchZ / 2;
                        maxZ -= 8 + stretchZ / 2;
                    }
                    if (sizeZ < 16 && (minZ + 16) % 16 > (maxZ + 16) % 16) {
                        int dZ = maxZ % 16;
                        minZ -= dZ;
                        maxZ -= dZ;
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

    public static ResourceLocation[] generateBreakTextures(String modelName, int textureWidth, int textureHeight,
        WavefrontObject model) {
        return generateBreakTextures(modelName, 1, 1, 1, textureWidth, textureHeight, model);
    }

    public static ResourceLocation[] generateBreakTextures(String modelName, float scaleX, float scaleY, float scaleZ,
        int textureWidth, int textureHeight, WavefrontObject model) {
        ResourceLocation[] result = new ResourceLocation[destroyBlockIcons.length];
        BufferedImage[] generatedImages = new BufferedImage[destroyBlockIcons.length];
        for (int i = 0; i < result.length; i++) {
            generatedImages[i] = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        }

        Map<Float[][], Float[][]> textureMap = createTextureToWorldMap(
            model,
            scaleX,
            scaleY,
            scaleZ,
            textureWidth,
            textureHeight);

        for (Entry<Float[][], Float[][]> face : textureMap.entrySet()) {
            Float[][] textureCoords = face.getKey();
            int length = textureCoords.length - 1;
            // int offsetX = Math.round(textureCoords[length][0]);
            // int offsetY = Math.round(textureCoords[length][1]);
            int offsetX = 0;
            int offsetY = 0;
            Integer minX = null;
            Integer minY = null;
            Integer maxX = null;
            Integer maxY = null;
            for (int i = 0; i < length; i++) {
                if (minX == null || textureCoords[i][0] < minX) minX = Math.round(textureCoords[i][0]);
                if (minY == null || textureCoords[i][1] < minY) minY = Math.round(textureCoords[i][1]);
                if (maxX == null || textureCoords[i][0] > maxX) maxX = Math.round(textureCoords[i][0]);
                if (maxY == null || textureCoords[i][1] > maxY) maxY = Math.round(textureCoords[i][1]);
            }
            int sizeX = maxX - minX;
            int sizeY = maxY - minY;
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
                copyIconToImage(
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
            String name = modelName + "_destruction_" + i;
            result[i] = registerDynamicTexture(name, generatedImages[i]);
            saveTextureToFile(generatedImages[i], name);
        }

        return result;
    }

    public static Map<Float[][], Float[][]> createTextureToWorldMap(WavefrontObject obj, float scaleX, float scaleY,
        float scaleZ, int textureWidth, int textureHeight) {
        Map<Float[][], Float[][]> textureWorldMap = new HashMap<Float[][], Float[][]>();

        float[] overallBounds = getOverallBounds(obj);
        float minX = overallBounds[0], minY = overallBounds[1], minZ = overallBounds[2];
        float maxX = overallBounds[3], maxY = overallBounds[4], maxZ = overallBounds[5];
        float centerX = 0, centerY = 0, centerZ = 0;

        for (Vertex vertex : obj.vertices) {
            centerX += vertex.x;
            centerY += vertex.y;
            centerZ += vertex.z;
        }
        centerX /= obj.vertices.size();
        centerY /= obj.vertices.size();
        centerZ /= obj.vertices.size();

        for (GroupObject group : obj.groupObjects) {
            for (Face face : group.faces) {
                int length = face.vertices.length;
                if (length >= 3) {
                    // Last variable should contain texture offset relative to block center
                    Float[][] textureCoords = new Float[length + 1][];
                    Float[][] worldCoords = new Float[length][];
                    float centerU = 0, centerV = 0;

                    for (int i = 0; i < length; i++) {
                        Vertex vertex = face.vertices[i];
                        TextureCoordinate textureCoordinate = face.textureCoordinates[i];

                        centerU += textureCoordinate.u;
                        centerV += textureCoordinate.v;

                        float texU = textureCoordinate.u * textureWidth;
                        float texV = textureCoordinate.v * textureHeight;
                        textureCoords[i] = new Float[] { texU, texV };

                        float worldX = 16.0f * ((vertex.x - minX) / (maxX - minX)) * scaleX;
                        float worldY = 16.0f * ((vertex.y - minY) / (maxY - minY)) * scaleY;
                        float worldZ = 16.0f * ((vertex.z - minZ) / (maxZ - minZ)) * scaleZ;
                        worldCoords[i] = new Float[] { worldX, worldY, worldZ };
                    }

                    TextureCoordinate projectedCenter = projectPointToFaceTextureCoord(centerX, centerY, centerZ, face);
                    centerU /= length;
                    centerV /= length;
                    textureCoords[length] = new Float[] { (projectedCenter.u - centerU) * textureWidth,
                        (projectedCenter.v - centerV) * textureHeight };

                    textureWorldMap.put(textureCoords, worldCoords);
                }
            }
        }

        return textureWorldMap;
    }

    public static float[] getOverallBounds(WavefrontObject obj) {
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE, maxZ = Float.MIN_VALUE;

        for (GroupObject group : obj.groupObjects) {
            for (Face face : group.faces) {
                for (Vertex vertex : face.vertices) {
                    minX = Math.min(minX, vertex.x);
                    minY = Math.min(minY, vertex.y);
                    minZ = Math.min(minZ, vertex.z);
                    maxX = Math.max(maxX, vertex.x);
                    maxY = Math.max(maxY, vertex.y);
                    maxZ = Math.max(maxZ, vertex.z);
                }
            }
        }

        return new float[] { minX, minY, minZ, maxX, maxY, maxZ };
    }

    public static TextureCoordinate projectPointToFaceTextureCoord(float pointX, float pointY, float pointZ,
        Face face) {
        Vertex v0 = face.vertices[0];
        Vertex v1 = face.vertices[1];
        Vertex v2 = face.vertices[2];

        TextureCoordinate tc0 = face.textureCoordinates[0];
        TextureCoordinate tc1 = face.textureCoordinates[1];
        TextureCoordinate tc2 = face.textureCoordinates[2];

        Vec3 v0Vec = Vec3.createVectorHelper(v0.x, v0.y, v0.z);
        Vec3 v1Vec = Vec3.createVectorHelper(v1.x, v1.y, v1.z);
        Vec3 v2Vec = Vec3.createVectorHelper(v2.x, v2.y, v2.z);
        Vec3 pointVec = Vec3.createVectorHelper(pointX, pointY, pointZ);

        Vec3 v0v1 = Vec3
            .createVectorHelper(v1Vec.xCoord - v0Vec.xCoord, v1Vec.yCoord - v0Vec.yCoord, v1Vec.zCoord - v0Vec.zCoord);
        Vec3 v0v2 = Vec3
            .createVectorHelper(v2Vec.xCoord - v0Vec.xCoord, v2Vec.yCoord - v0Vec.yCoord, v2Vec.zCoord - v0Vec.zCoord);
        Vec3 v0p = Vec3.createVectorHelper(
            pointVec.xCoord - v0Vec.xCoord,
            pointVec.yCoord - v0Vec.yCoord,
            pointVec.zCoord - v0Vec.zCoord);

        double d00 = v0v1.xCoord * v0v1.xCoord + v0v1.yCoord * v0v1.yCoord + v0v1.zCoord * v0v1.zCoord;
        double d01 = v0v1.xCoord * v0v2.xCoord + v0v1.yCoord * v0v2.yCoord + v0v1.zCoord * v0v2.zCoord;
        double d11 = v0v2.xCoord * v0v2.xCoord + v0v2.yCoord * v0v2.yCoord + v0v2.zCoord * v0v2.zCoord;
        double d20 = v0p.xCoord * v0v1.xCoord + v0p.yCoord * v0v1.yCoord + v0p.zCoord * v0v1.zCoord;
        double d21 = v0p.xCoord * v0v2.xCoord + v0p.yCoord * v0v2.yCoord + v0p.zCoord * v0v2.zCoord;

        double denom = d00 * d11 - d01 * d01;
        double v = (d11 * d20 - d01 * d21) / denom;
        double w = (d00 * d21 - d01 * d20) / denom;
        double u = 1.0 - v - w;

        float uCoord = (float) (u * tc0.u + v * tc1.u + w * tc2.u);
        float vCoord = (float) (u * tc0.v + v * tc1.v + w * tc2.v);

        return new TextureCoordinate(uCoord, vCoord);
    }

    private static BufferedImage copyIconToImage(int iconID, int iconMinX, int iconMinY, int iconMaxX, int iconMaxY,
        BufferedImage image, int imageMinX, int imageMinY) {
        return copyIconToImage(iconID, iconMinX, iconMinY, iconMaxX, iconMaxY, image, imageMinX, imageMinY, 0, 0);
    }

    public static BufferedImage copyIconToImage(int iconID, int iconMinX, int iconMinY, int iconMaxX, int iconMaxY,
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
                if (endX == 0) endX = 16;
                dY = iconMinY;
                localSizeY = 0;
                do {
                    startY = (dY % iconSizeY + iconSizeY) % iconSizeY;
                    endY = iconMaxY - dY >= iconSizeY ? iconSizeY
                        : iconMaxY > iconSizeY ? iconMaxY % iconSizeY : iconMaxY;
                    if (endY == 0) endY = 16;
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

    public static ResourceLocation registerDynamicTexture(String name, BufferedImage image) {
        DynamicTexture dynamicTexture = new DynamicTexture(image);

        ResourceLocation resource = new ResourceLocation("dynamictextures:" + name);
        Minecraft.getMinecraft().renderEngine.loadTexture(resource, dynamicTexture);

        return resource;
    }

    public static void saveTextureToFile(BufferedImage image, String filename) {
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
        private float scaleX;
        private float scaleY;
        private float scaleZ;
        private ModelRenderer[] models;
        private WavefrontObject model;

        public GeneratorData(String modelName, int textureWidth, int textureHeight, ModelRenderer[] models) {
            this.modelName = modelName;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.models = models;
        }

        public GeneratorData(String modelName, float scaleX, float scaleY, float scaleZ, int textureWidth,
            int textureHeight, WavefrontObject model) {
            this.modelName = modelName;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
            this.model = model;
        }
    }

}
