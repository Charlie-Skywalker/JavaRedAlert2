package org.example.redalert.controller;

import org.example.redalert.ui.cursor.MouseCursor;

import java.awt.image.BufferedImage;

public class MouseCursorController {
    public static final MouseCursor sEmpty;
    
    static {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        sEmpty = new MouseCursor("NoneCursor", image);
    }
}
