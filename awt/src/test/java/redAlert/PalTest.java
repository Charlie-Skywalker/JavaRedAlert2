package redAlert;

import redAlert.utils.PalFileReader;

public class PalTest {
    public static void main(String[] args) {
        int[] b = PalFileReader.getColorArray("C:\\Users\\R&D1\\Desktop\\RA\\uniturb.pal");
        System.out.println(b[0]);
    }
}
