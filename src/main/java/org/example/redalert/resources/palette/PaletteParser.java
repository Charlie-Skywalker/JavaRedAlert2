package org.example.redalert.resources.palette;

public class PaletteParser {
    private PaletteParser() {}
    
    /**
     * 从文件字节数据中解析对象
     *
     * @param bytes 文件字节数据
     * @return {@link PaletteFile} 对象
     */
    public static PaletteFile parse(byte[] bytes) {
        PaletteFile file = new PaletteFile();
        if (bytes == null || bytes.length != PaletteFile.LENGTH) {
            return file;
        }
        for (int i = 0, k = 0; i < bytes.length; i += 3, k += 1) {
            byte r = (byte) (bytes[i] << 2);
            byte g = (byte) (bytes[i + 1] << 2);
            byte b = (byte) (bytes[i + 2] << 2);
            PaletteColor color = new PaletteColor(r, g, b);
            file.mColors[k] = color;
        }
        return file;
    }
}
