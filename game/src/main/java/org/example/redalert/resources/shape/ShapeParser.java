package org.example.redalert.resources.shape;

import org.example.redalert.utils.ReadUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ShapeParser {
    private ShapeParser() {}

    public static ShapeFile parse(RandomAccessFile reader, boolean half) {
        // 读取缓存
        byte[] buffer = new byte[4];
        // 结果
        ShapeFile shp = new ShapeFile();

        // 文件头部分
        parseHeader(reader, shp, buffer);
        // 帧信息部分
        List<ShapeFrame> frames = parseMetadata(reader, shp, buffer);

        // 帧数据部分
        boolean isUseShadow = false; // TODO 是否包含阴影数据，暂未实现
        int size;
        if (isUseShadow) {
            size = frames.size();
        } else if (frames.size() == 1 || !half) {
            size = frames.size();
        } else {
            size = frames.size() / 2;
        }

        for (int i = 0; i < size; i++) {
            ShapeFrame frame = frames.get(i);
            if (frame.mWidth != 0 && frame.mHeight != 0) {
                int currentX;
                int currentY = frame.mMinY;
                if ((frame.mType & 0x2) != 0x2) {
                    // 不使用游程编码压缩
                    for (int y = 0; y < frame.mHeight; y++) {
                        currentX = frame.mMinX;
                        byte[] bytes = new byte[frame.mWidth];
                        try {
                            reader.read(bytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        for (byte b : bytes) {
                            ShapeFramePixel pixel = new ShapeFramePixel(
                                currentX,
                                currentY,
                                b
                            );
                            frame.mPixels.add(pixel);
                            currentX++;
                        }
                        currentY++;
                    }
                } else {
                    // 使用游程编码压缩
                }
            }
            // frame.mImage = new BufferedImage(shp.mWidth, shp.mHeight, BufferedImage.TYPE_INT_ARGB);
        }

        return shp;
    }

    /**
     * 解析文件头
     * @param reader 读取器
     * @param shp 图形文件
     * @param buffer 缓存
     */
    private static void parseHeader(RandomAccessFile reader, ShapeFile shp, byte[] buffer) {
        // 文件标识（未知意义）
        shp.mFlag = ReadUtil.readToInt(reader, 2, buffer);
        // 图片宽度
        shp.mWidth = ReadUtil.readToInt(reader, 2, buffer);
        // 图片高度
        shp.mHeight = ReadUtil.readToInt(reader, 2, buffer);
        // 图片帧数
        shp.mFrameCount = ReadUtil.readToInt(reader, 2, buffer);
    }

    /**
     * 解析帧信息
     * @param reader 读取器
     * @param shp 图形文件
     * @param buffer 缓存
     */
    private static List<ShapeFrame> parseMetadata(RandomAccessFile reader, ShapeFile shp, byte[] buffer) {
        // 创建帧列表
        List<ShapeFrame> frames = new ArrayList<>(shp.mFrameCount);

        // 读取帧信息
        for (int i = 0; i < shp.mFrameCount; i++) {
            ShapeFrame frame = new ShapeFrame();

            // 最小 X 坐标
            frame.mMinX = ReadUtil.readToInt(reader, 2, buffer);
            // 最小 Y 坐标
            frame.mMinY = ReadUtil.readToInt(reader, 2, buffer);
            // 有效区域宽度
            frame.mWidth = ReadUtil.readToInt(reader, 2, buffer);
            // 有效区域高度
            frame.mHeight = ReadUtil.readToInt(reader, 2, buffer);
            // 帧类型
            frame.mType = ReadUtil.readToInt(reader, 1, buffer);
            // 帧类型对齐
            frame.mTypeAlign = ReadUtil.readToInt(reader, 3, buffer);
            // 颜色参数（未知用途）
            frame.mColor = ReadUtil.readToInt(reader, 4, buffer);
            // 预留参数
            frame.mReversed = ReadUtil.readToInt(reader, 4, buffer);
            // 帧数据偏移
            frame.mOffset = ReadUtil.readToInt(reader, 4, buffer);

            frames.add(frame);
        }

        return frames;
    }
}
