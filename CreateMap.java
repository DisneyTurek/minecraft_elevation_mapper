package org.dis.elevationMapper;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CreateMap {

    public static World voidWorld;

    public CreateMap() {

    }

    public World mapWorld;
    public String directory;
    public ElevationInfo elevationInfo;
    public int[][] elevations;

    /**
     * Places the blocks in the world based on the compressed data. For simplicity, it goes from y=0
     * at each coordinate all the way up to the elevation at that point.
     */
    public void buildMap() {

        //loops through the elevation data and places the blocks in the game. I chose Stone Blocks but you could use anything
        for(int x = 0; x < elevations.length; x++) {
            for(int z = 0; z < elevations[0].length; z++) {
                if(elevations[x][z] >= 0) {
                    for (int y = 0; y < elevations[x][z]; y++) {
                        Location loc = new Location(mapWorld, x, y, z);
                        mapWorld.getBlockAt(loc).setType(Material.STONE);
                    }
                }
            }
        }

        //Teleports all players on the server to the new world at the most northwestern corner of the data.
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(new Location(mapWorld,0,100,0));
        }
    }

    /**
     * creates a small simple map for the given TIF file and type of compression
     * @param fileName the name of the TIF File that has the elevation data
     * @param type the type of compression you want to do either "average, max, median, midpoint, min, or mode"
     */
    public void createSimpleMap(String fileName, String type) {
        //creates a copy of the empty world
        voidWorld = loadWorld("void_world");
        mapWorld = copyWorld(voidWorld, "map_" + System.currentTimeMillis());

        directory = "plugins/ElevationMapper/" + fileName + "/";

        //reads the data from the given file
        try {
            elevationInfo = FileReader.readTiff(directory + "elevation.tif", -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //chooses the size of the compressed data based on which axis is larger
        if(elevationInfo.elevations.length > elevationInfo.elevations[0].length) {
            elevations = new int[200][(int) (200 * elevationInfo.elevations[0].length / elevationInfo.elevations.length)];
        } else {
            elevations = new int[(int) (200 * elevationInfo.elevations.length / elevationInfo.elevations[0].length)][200];
        }

        //chooses the type of compression for the data
        if(type.equalsIgnoreCase("Average")) {
            elevations = resizeElevationAvg(elevationInfo, elevations, 100);
        } else if(type.equalsIgnoreCase("Median")) {
            elevations = resizeElevationMedian(elevationInfo, elevations, 100);
        } else if(type.equalsIgnoreCase("Mode")) {
            elevations = resizeElevationMode(elevationInfo, elevations, 100);
        } else if(type.equalsIgnoreCase("Min")) {
            elevations = resizeElevationMin(elevationInfo, elevations, 100);
        } else if(type.equalsIgnoreCase("Max")) {
            elevations = resizeElevationMax(elevationInfo, elevations, 100);
        } else if(type.equalsIgnoreCase("Midpoint")) {
            elevations = resizeElevationMidpoint(elevationInfo, elevations, 100);
        }
    }


    /**
     * creates a small simple map for the given TIF file and type of compression
     * @param fileName the name of the TIF File that has the elevation data
     * @param type the type of reduction you want to do either "average, max, median, midpoint, min, or mode"
     */
    public void createLargeSimpleMap(String fileName, String type) {
        //creates a copy of the empty world
        voidWorld = loadWorld("void_world");
        mapWorld = copyWorld(voidWorld, "map_" + System.currentTimeMillis());

        directory = "plugins/ElevationMapper/" + fileName + "/";

        //reads the data from the given file
        try {
            elevationInfo = FileReader.readTiff(directory + "elevation.tif", -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //chooses the size of the compressed data based on which axis is larger
        if(elevationInfo.elevations.length > elevationInfo.elevations[0].length) {
            elevations = new int[500][(int) (500 * elevationInfo.elevations[0].length / elevationInfo.elevations.length)];
        } else {
            elevations = new int[(int) (500 * elevationInfo.elevations.length / elevationInfo.elevations[0].length)][500];
        }

        //chooses the type of compression for the data
        if(type.equalsIgnoreCase("Average")) {
            elevations = resizeElevationAvg(elevationInfo, elevations, 150);
        } else if(type.equalsIgnoreCase("Median")) {
            elevations = resizeElevationMedian(elevationInfo, elevations, 150);
        } else if(type.equalsIgnoreCase("Mode")) {
            elevations = resizeElevationMode(elevationInfo, elevations, 150);
        } else if(type.equalsIgnoreCase("Min")) {
            elevations = resizeElevationMin(elevationInfo, elevations, 150);
        } else if(type.equalsIgnoreCase("Max")) {
            elevations = resizeElevationMax(elevationInfo, elevations, 150);
        } else if(type.equalsIgnoreCase("Midpoint")) {
            elevations = resizeElevationMidpoint(elevationInfo, elevations, 150);
        }
    }

    /**
     * Reduces the data from the given ElevationInfo instance based on the size of the resize 2D array and the zRange.
     * Takes the average of the values in the original area being compressed into the new cell
     * @param info file with all the elevation data
     * @param resize the new size that the data needs to be compressed to
     * @param zRange the new range for the elevation data that the data needs to be compressed to
     * @return a 2D integer array containing all the new values for the compressed data. Uses integers because minecraft only uses full blocks
     */
    public static int[][] resizeElevationAvg(ElevationInfo info, int[][] resize, int zRange){

        int originalRows = info.elevations.length;
        int originalColumns = info.elevations[0].length;
        int newRows = resize.length;
        int newColumns = resize[0].length;

        double rowScale = (double) originalRows / newRows;
        double columnScale = (double) originalColumns / newColumns;
        System.out.println(rowScale + " | " + columnScale);
        for(int newRow = 0; newRow < newRows; newRow++) {
            for(int newColumn = 0; newColumn < newColumns; newColumn++) {
                double newZ = 0;
                int count = 0;
                for (int i = (int) (newRow * rowScale); i < (int) ((newRow+1) * rowScale); i++) {
                    for (int j = (int) (newColumn * columnScale); j < (int) ((newColumn+1) * columnScale); j++) {
                        if(info.elevations[i][j] >= info.minElevation) {
                            newZ += info.elevations[i][j];
                            count++;
                        }
                    }
                }

                if(count > 0) {
                    newZ = newZ / count;

                    //scale Z to new range
                    newZ = newZ - info.minElevation;
                    newZ /= (info.maxElevation - info.minElevation + 0.001);

                    resize[newRow][newColumn] = (int) (newZ * zRange);
                } else {
                    resize[newRow][newColumn] = -1;
                }
            }
        }
        return resize;
    }

    /**
     * Reduces the data from the given ElevationInfo instance based on the size of the resize 2D array and the zRange.
     * Takes the median of the values in the original area being compressed into the new cell
     * @param info file with all the elevation data
     * @param resize the new size that the data needs to be compressed to
     * @param zRange the new range for the elevation data that the data needs to be compressed to
     * @return a 2D integer array containing all the new values for the compressed data. Uses integers because minecraft only uses full blocks
     */
    public static int[][] resizeElevationMedian(ElevationInfo info, int[][] resize, int zRange){

        int originalRows = info.elevations.length;
        int originalColumns = info.elevations[0].length;
        int newRows = resize.length;
        int newColumns = resize[0].length;

        double rowScale = (double) originalRows / newRows;
        double columnScale = (double) originalColumns / newColumns;
        System.out.println(rowScale + " | " + columnScale);
        for(int newRow = 0; newRow < newRows; newRow++) {
            for(int newColumn = 0; newColumn < newColumns; newColumn++) {
                ArrayList<Double> nums = new ArrayList<>();
                for (int i = (int) (newRow * rowScale); i < (int) ((newRow+1) * rowScale); i++) {
                    for (int j = (int) (newColumn * columnScale); j < (int) ((newColumn+1) * columnScale); j++) {
                        if(info.elevations[i][j] >= info.minElevation) {
                            nums.add(info.elevations[i][j]);
                        }
                    }
                }

                if(!nums.isEmpty()) {
                    Collections.sort(nums);
                    double newZ = nums.get(nums.size() / 2);

                    //scale Z to new range
                    newZ = newZ - info.minElevation;
                    newZ /= (info.maxElevation - info.minElevation + 0.001);

                    resize[newRow][newColumn] = (int) (newZ * zRange);
                } else {
                    resize[newRow][newColumn] = -1;
                }
            }
        }
        return resize;
    }

    /**
     * Reduces the data from the given ElevationInfo instance based on the size of the resize 2D array and the zRange.
     * Takes the mode of the values in the original area being compressed into the new cell
     * @param info file with all the elevation data
     * @param resize the new size that the data needs to be compressed to
     * @param zRange the new range for the elevation data that the data needs to be compressed to
     * @return a 2D integer array containing all the new values for the compressed data. Uses integers because minecraft only uses full blocks
     */
    public static int[][] resizeElevationMode(ElevationInfo info, int[][] resize, int zRange){

        int originalRows = info.elevations.length;
        int originalColumns = info.elevations[0].length;
        int newRows = resize.length;
        int newColumns = resize[0].length;

        double rowScale = (double) originalRows / newRows;
        double columnScale = (double) originalColumns / newColumns;
        System.out.println(rowScale + " | " + columnScale);
        for(int newRow = 0; newRow < newRows; newRow++) {
            for(int newColumn = 0; newColumn < newColumns; newColumn++) {
                ArrayList<Double> nums = new ArrayList<>();
                for (int i = (int) (newRow * rowScale); i < (int) ((newRow+1) * rowScale); i++) {
                    for (int j = (int) (newColumn * columnScale); j < (int) ((newColumn+1) * columnScale); j++) {
                        if(info.elevations[i][j] >= info.minElevation) {
                            nums.add(info.elevations[i][j]);
                        }
                    }
                }

                if(!nums.isEmpty()) {
                    Collections.sort(nums);
                    double newZ = nums.getFirst();
                    int maxCount = 0;
                    int count = 1;
                    for(int i = 1; i < nums.size(); i++) {
                        if(nums.get(i).equals(nums.get(i-1))) {
                            count++;
                            if(count  > maxCount) {
                                maxCount = count;
                                newZ = nums.get(i);
                            }
                        } else {
                            count = 0;
                        }
                    }

                    //scale Z to new range
                    newZ = newZ - info.minElevation;
                    newZ /= (info.maxElevation - info.minElevation + 0.001);

                    resize[newRow][newColumn] = (int) (newZ * zRange);
                } else {
                    resize[newRow][newColumn] = -1;
                }
            }
        }
        return resize;
    }

    /**
     * Reduces the data from the given ElevationInfo instance based on the size of the resize 2D array and the zRange.
     * Takes the minimum of the values in the original area being compressed into the new cell
     * @param info file with all the elevation data
     * @param resize the new size that the data needs to be compressed to
     * @param zRange the new range for the elevation data that the data needs to be compressed to
     * @return a 2D integer array containing all the new values for the compressed data. Uses integers because minecraft only uses full blocks
     */
    public static int[][] resizeElevationMin(ElevationInfo info, int[][] resize, int zRange){

        int originalRows = info.elevations.length;
        int originalColumns = info.elevations[0].length;
        int newRows = resize.length;
        int newColumns = resize[0].length;

        double rowScale = (double) originalRows / newRows;
        double columnScale = (double) originalColumns / newColumns;
        System.out.println(rowScale + " | " + columnScale);
        for(int newRow = 0; newRow < newRows; newRow++) {
            for(int newColumn = 0; newColumn < newColumns; newColumn++) {
                ArrayList<Double> nums = new ArrayList<>();
                for (int i = (int) (newRow * rowScale); i < (int) ((newRow+1) * rowScale); i++) {
                    for (int j = (int) (newColumn * columnScale); j < (int) ((newColumn+1) * columnScale); j++) {
                        if(info.elevations[i][j] >= info.minElevation) {
                            nums.add(info.elevations[i][j]);
                        }
                    }
                }

                if(!nums.isEmpty()) {
                    Collections.sort(nums);
                    double newZ = nums.getFirst();

                    //scale Z to new range
                    newZ = newZ - info.minElevation;
                    newZ /= (info.maxElevation - info.minElevation + 0.001);

                    resize[newRow][newColumn] = (int) (newZ * zRange);
                } else {
                    resize[newRow][newColumn] = -1;
                }
            }
        }
        return resize;
    }

    /**
     * Reduces the data from the given ElevationInfo instance based on the size of the resize 2D array and the zRange.
     * Takes the maximum of the values in the original area being compressed into the new cell
     * @param info file with all the elevation data
     * @param resize the new size that the data needs to be compressed to
     * @param zRange the new range for the elevation data that the data needs to be compressed to
     * @return a 2D integer array containing all the new values for the compressed data. Uses integers because minecraft only uses full blocks
     */
    public static int[][] resizeElevationMax(ElevationInfo info, int[][] resize, int zRange){

        int originalRows = info.elevations.length;
        int originalColumns = info.elevations[0].length;
        int newRows = resize.length;
        int newColumns = resize[0].length;

        double rowScale = (double) originalRows / newRows;
        double columnScale = (double) originalColumns / newColumns;
        System.out.println(rowScale + " | " + columnScale);
        for(int newRow = 0; newRow < newRows; newRow++) {
            for(int newColumn = 0; newColumn < newColumns; newColumn++) {
                ArrayList<Double> nums = new ArrayList<>();
                for (int i = (int) (newRow * rowScale); i < (int) ((newRow+1) * rowScale); i++) {
                    for (int j = (int) (newColumn * columnScale); j < (int) ((newColumn+1) * columnScale); j++) {
                        if(info.elevations[i][j] >= info.minElevation) {
                            nums.add(info.elevations[i][j]);
                        }
                    }
                }

                if(!nums.isEmpty()) {
                    Collections.sort(nums);
                    double newZ = nums.getLast();

                    //scale Z to new range
                    newZ = newZ - info.minElevation;
                    newZ /= (info.maxElevation - info.minElevation + 0.001);

                    resize[newRow][newColumn] = (int) (newZ * zRange);
                } else {
                    resize[newRow][newColumn] = -1;
                }
            }
        }
        return resize;
    }

    /**
     * Reduces the data from the given ElevationInfo instance based on the size of the resize 2D array and the zRange.
     * Takes the midpoint geographically of the values in the original area being compressed into the new cell
     * @param info file with all the elevation data
     * @param resize the new size that the data needs to be compressed to
     * @param zRange the new range for the elevation data that the data needs to be compressed to
     * @return a 2D integer array containing all the new values for the compressed data. Uses integers because minecraft only uses full blocks
     */
    public static int[][] resizeElevationMidpoint(ElevationInfo info, int[][] resize, int zRange){

        int originalRows = info.elevations.length;
        int originalColumns = info.elevations[0].length;
        int newRows = resize.length;
        int newColumns = resize[0].length;

        double rowScale = (double) originalRows / newRows;
        double columnScale = (double) originalColumns / newColumns;
        System.out.println(rowScale + " | " + columnScale);
        for(int newRow = 0; newRow < newRows; newRow++) {
            for(int newColumn = 0; newColumn < newColumns; newColumn++) {
                ArrayList<Double> nums = new ArrayList<>();
                for (int i = (int) (newRow * rowScale); i < (int) ((newRow+1) * rowScale); i++) {
                    for (int j = (int) (newColumn * columnScale); j < (int) ((newColumn+1) * columnScale); j++) {
                        if(info.elevations[i][j] >= info.minElevation) {
                            nums.add(info.elevations[i][j]);
                        }
                    }
                }

                if(!nums.isEmpty()) {
                    double newZ = nums.get(nums.size() / 2);

                    //scale Z to new range
                    newZ = newZ - info.minElevation;
                    newZ /= (info.maxElevation - info.minElevation + 0.001);

                    resize[newRow][newColumn] = (int) (newZ * zRange);
                } else {
                    resize[newRow][newColumn] = -1;
                }
            }
        }
        return resize;
    }

    private static World loadWorld(String worldName) {
        if(!(new File(worldName)).exists()) {
            return null;
        } else if(Bukkit.getWorld(worldName) == null) {
            World world = Bukkit.createWorld(new WorldCreator(worldName));
            world.setSpawnLocation(new Location(world, 0, 300, 0, 0, 0));
            return world;
        } else {
            return Bukkit.getWorld(worldName);
        }
    }

    private static World copyWorld(World w, String newName) {
        File sourceWorld = w.getWorldFolder();
        File activeWorld = new File(newName);

        try {
            FileUtils.copyDirectory(sourceWorld, activeWorld);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File f: activeWorld.listFiles()) {
            if (f.getName().equals("uid.dat")) {
                f.delete();
            }
        }

        return loadWorld(newName);
    }

}
