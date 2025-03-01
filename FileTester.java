package org.dis.elevationMapper;

public class FileTester {


    public static void main(String[] args) {
        long timeBefore = System.currentTimeMillis();
        ElevationInfo elev = null;

        try {
            elev = FileReader.readTiff("src/main/java/org/dis/elevationMapper/testFiles/downtown_lincoln/elevation.tif", -1);
        } catch (Exception e) {
            System.out.println(e);
        }

        int[][] elevations = null;
        if(elev.elevations.length < elev.elevations[0].length) {
            elevations = new int[200][(int) (200 * elev.elevations[0].length / elev.elevations.length)];
        } else {
            elevations = new int[(int) (200 * elev.elevations.length / elev.elevations[0].length)][200];
        }
        elevations = CreateMap.resizeElevationMode(elev, elevations, 100);

        for(int i = 0; i < elevations.length; i++) {
            for(int j = 0; j < elevations[0].length; j++) {
                System.out.print(elevations[i][j] + ", ");
            }
            System.out.println("");
        }

        System.out.println("done - " + (System.currentTimeMillis() - timeBefore));
    }

}
