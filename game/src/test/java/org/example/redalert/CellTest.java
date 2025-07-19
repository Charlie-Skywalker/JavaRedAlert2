package org.example.redalert;

import org.example.redalert.controller.MapController;
import org.example.redalert.map.Cell;
import org.example.redalert.map.Map;
import org.junit.jupiter.api.Test;

public class CellTest {
    @Test
    public void testCell() {
        MapController.create(10, 5);
        Map map = MapController.getMap();
        for (Cell cell : map.getCells()) {
            System.out.println("x: " + cell.getCenterX() + ", y: " + cell.getCenterY());
        }
    }
}
