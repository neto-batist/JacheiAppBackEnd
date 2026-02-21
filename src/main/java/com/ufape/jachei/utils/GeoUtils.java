package com.ufape.jachei.utils;

public class GeoUtils {

    // Raio da Terra em Quilômetros
    private static final double EARTH_RADIUS_KM = 6371.01;

    /**
     * Calcula os limites de Latitude e Longitude baseados num raio em KM.
     * Retorna um array onde: [0]=MinLat, [1]=MaxLat, [2]=MinLon, [3]=MaxLon
     */
    public static double[] getBoundingBox(double lat, double lon, double distanceKm) {
        // Diferença de latitude em radianos
        double latDelta = distanceKm / EARTH_RADIUS_KM;
        // Diferença de longitude em radianos (ajustada pela latitude atual)
        double lonDelta = distanceKm / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(lat)));

        double minLat = lat - Math.toDegrees(latDelta);
        double maxLat = lat + Math.toDegrees(latDelta);
        double minLon = lon - Math.toDegrees(lonDelta);
        double maxLon = lon + Math.toDegrees(lonDelta);

        return new double[]{minLat, maxLat, minLon, maxLon};
    }
}