package org.example.redalert;

import org.example.redalert.controller.MapController;
import org.example.redalert.ui.element.Cell;

public class CellTest {
    public static void main(String[] args) {
        MapController.createTiles(400, 200);
        for (Cell cell : MapController.sCells) {
            System.out.println("x: " + cell.mCenterX + ", y: " + cell.mCenterY);
        }
    }
}
