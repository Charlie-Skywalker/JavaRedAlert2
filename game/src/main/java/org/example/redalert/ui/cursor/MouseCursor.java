package org.example.redalert.ui.cursor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MouseCursor {
    public final BufferedImage mImage;
    public final Cursor mCursor;
    
    public MouseCursor(String name, BufferedImage image) {
        this.mImage = image;
        this.mCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            image,
            new Point(0, 0),
            name
        );
    }
}
