package org.dis.elevationMapper;

public class ElevationInfo {

    double[][] elevations;
    double maxElevation;
    double minElevation;

    public ElevationInfo(double[][] elevations, double maxElevation, double minElevation) {
        this.elevations = elevations;
        this.maxElevation = maxElevation;
        this.minElevation = minElevation;
    }

    @Override
    public String toString() {
        return minElevation + ":" + maxElevation;
    }
}
