package com.muyategna.backend.location.utils;

import com.uber.h3core.AreaUnit;
import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Utility class for H3 geospatial indexing operations.
 * This class provides convenient static methods to interact with the H3 library,
 * allowing conversion between latitude/longitude and H3 cell indices,
 * hierarchical operations, and spatial queries.
 */
public class H3Utils {

    private static final H3Core h3;

    // Static initializer block to ensure H3Core is initialized once
    static {
        try {
            h3 = H3Core.newInstance();
        } catch (IOException e) {
            // In a real application, you might want more robust error handling
            // or logging here. For simplicity, we'll throw a runtime exception.
            throw new RuntimeException("Failed to initialize H3Core instance", e);
        }
    }

    /**
     * Converts a given latitude and longitude to an H3 cell index at a specified resolution of 7.
     *
     * @param lat The latitude.
     * @param lng The longitude.
     * @return The H3 cell index as a long.
     */
    public static long latLngToCellRes7(double lat, double lng) {
        return h3.latLngToCell(lat, lng, 7);
    }

    /**
     * Converts a given latitude and longitude to an H3 cell index at a specified resolution of 8.
     *
     * @param lat The latitude.
     * @param lng The longitude.
     * @return The H3 cell index as a long.
     */
    public static long latLngToCellRes8(double lat, double lng) {
        return h3.latLngToCell(lat, lng, 8);
    }


    /**
     * Converts a given latitude and longitude to an H3 cell index at a specified resolution of 9.
     *
     * @param lat The latitude.
     * @param lng The longitude.
     * @return The H3 cell index as a long.
     */
    public static long latLngToCellRes9(double lat, double lng) {
        return h3.latLngToCell(lat, lng, 9);
    }


    /**
     * Converts a given latitude and longitude to an H3 cell index at a specified resolution.
     *
     * @param lat The latitude.
     * @param lng The longitude.
     * @return The H3 cell index as a long.
     */
    public static long latLngToCell(double lat, double lng, int resolution) {
        return h3.latLngToCell(lat, lng, resolution);
    }

    /**
     * Converts an H3 cell index to its central latitude and longitude.
     *
     * @param h3Index The H3 cell index.
     * @return A {@link LatLng} object representing the central point of the H3 cell.
     */
    public static LatLng cellToLatLng(long h3Index) {
        return h3.cellToLatLng(h3Index);
    }

    /**
     * Gets the parent H3 cell index of a given H3 cell at a specified lower resolution.
     *
     * @param h3Index    The H3 cell index.
     * @param resolution The resolution of the parent cell (must be less than the resolution of h3Index).
     * @return The H3 index of the parent cell.
     */
    public static long cellToParent(long h3Index, int resolution) {
        return h3.cellToParent(h3Index, resolution);
    }

    /**
     * Gets the children H3 cell indices of a given H3 cell at a specified higher resolution.
     *
     * @param h3Index    The H3 cell index.
     * @param resolution The resolution of the child cells (must be greater than the resolution of h3Index).
     * @return A list of H3 cell indices representing the children.
     */
    public static List<Long> cellToChildren(long h3Index, int resolution) {
        return h3.cellToChildren(h3Index, resolution);
    }

    /**
     * Finds all H3 cell indices within a specified "k-ring" distance from a central H3 cell.
     * A k-ring of 0 returns the cell itself. A k-ring of 1 returns the cell and its immediate neighbors.
     *
     * @param h3Index The central H3 cell index.
     * @param k       The k-ring distance.
     * @return A list of H3 cell indices within the k-ring.
     */
    public static List<Long> gridDisk(long h3Index, int k) {
        return h3.gridDisk(h3Index, k);
    }


    /**
     * Finds all H3 cell indices within a specified "k-ring" distance from a central H3 cell,
     * given the H3 index as a hexadecimal string.
     * A k-ring of 0 returns the cell itself. A k-ring of 1 returns the cell and its immediate neighbors.
     *
     * @param h3Index The central H3 cell index as a hexadecimal string.
     * @param k       The k-ring distance.
     * @return A list of H3 cell indices within the k-ring, represented as hexadecimal strings.
     */
    public static List<String> gridDiskString(String h3Index, int k) {
        return h3.gridDisk(h3Index, k);
    }

    /**
     * Calculates the area of an H3 cell at a given resolution in square kilometers.
     *
     * @param resolution The H3 resolution.
     * @return The area of an H3 cell at the given resolution in square kilometers.
     */
    public static double cellAreaKm2(int resolution) {
        return h3.cellArea(resolution, AreaUnit.km2); // Use AreaUnit from import
    }

    /**
     * Calculates the area of an H3 cell at a given resolution in square meters.
     *
     * @param resolution The H3 resolution.
     * @return The area of an H3 cell at the given resolution in square meters.
     */
    public static double cellAreaM2(int resolution) {
        return h3.cellArea(resolution, AreaUnit.m2); // Use AreaUnit from import
    }

    /**
     * Calculates the average edge length of an H3 cell at a given resolution in kilometers.
     *
     * @param resolution The H3 resolution.
     * @return The average edge length of an H3 cell at the given resolution in kilometers.
     */
    public static double edgeLengthKm(int resolution) {
        return h3.edgeLength(resolution, LengthUnit.km); // Use LengthUnit from import
    }

    /**
     * Calculates the average edge length of an H3 cell at a given resolution in meters.
     *
     * @param resolution The H3 resolution.
     * @return The average edge length of an H3 cell at the given resolution in meters.
     */
    public static double edgeLengthM(int resolution) {
        return h3.edgeLength(resolution, LengthUnit.m); // Use LengthUnit from import
    }

    /**
     * Converts a long H3 index to its hexadecimal string representation.
     *
     * @param h3Index The H3 cell index as a long.
     * @return The H3 index as a hexadecimal string.
     */
    public static String h3ToString(long h3Index) {
        return h3.h3ToString(h3Index);
    }

    /**
     * Converts a hexadecimal string representation of an H3 index to its long value.
     *
     * @param h3String The H3 index as a hexadecimal string.
     * @return The H3 cell index as a long.
     */
    public static long stringToH3(String h3String) {
        return h3.stringToH3(h3String);
    }

    /**
     * Returns the resolution of the given H3 index.
     *
     * @param h3Index The H3 cell index.
     * @return The resolution of the H3 index.
     */
    public static int getResolution(long h3Index) {
        return h3.getResolution(h3Index);
    }

    /**
     * Returns true if the given H3 index is a pentagon, false otherwise.
     *
     * @param h3Index The H3 cell index.
     * @return True if the index is a pentagon, false otherwise.
     */
    public static boolean isPentagon(long h3Index) {
        return h3.isPentagon(h3Index);
    }

    /**
     * Returns true if the given H3 index is a valid H3 index, false otherwise.
     *
     * @param h3Index The H3 cell index.
     * @return True if the index is valid, false otherwise.
     */
    public static boolean h3IsValid(long h3Index) {
        return h3.isValidCell(h3Index);
    }
}

