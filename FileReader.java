package org.dis.elevationMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileReader {

    public static ElevationInfo readTiff(String fileName,int noData) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        BufferedImage image = ImageIO.read(file);
        Raster raster = image.getData();

        System.out.println(String.format("%d,%d | %d,%d",raster.getMinX(),raster.getMinY(),raster.getWidth(),raster.getHeight()));
        double[][] data2D = new double[raster.getWidth()][raster.getHeight()];
        double[] data1D = new double[raster.getWidth() * raster.getHeight()];

        raster.getSamples(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight(), 0, data1D);
        double maxValue = data1D[0];
        double minValue = data1D[0];

        for(int j = 0; j < data2D[0].length; j++) {
            for (int i = 0; i < data2D.length; i++) {
                data2D[i][j] = data1D[i + j * data2D.length];
                if(data1D[i + j * data2D.length] > maxValue && data1D[i + j * data2D.length] > noData) {
                    maxValue = data1D[i + j * data2D.length];
                    if(minValue <= noData) {
                        minValue = maxValue;
                    }
                } else if(data1D[i + j * data2D.length] < minValue && data1D[i + j * data2D.length] > noData) {
                    minValue = data1D[i + j * data2D.length];
                }
            }
        }
        ElevationInfo out = new ElevationInfo(data2D,maxValue,minValue);
        System.out.println(out);

        return out;
    }

}
