package com.svs.farm_app.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.Iterator;
import java.util.List;

import static java.lang.Math.PI;

/**
 * Created by ADMIN on 21-Aug-17.
 */

public class MapUtils {
    public static final double EARTH_RADIUS = 6371009.0D;

    MapUtils() {
    }

    public static double computeLength(List<LatLng> path) {
        if (path.size() < 2) {
            return 0.0D;
        } else {
            double length = 0.0D;
            LatLng prev = (LatLng) path.get(0);
            double prevLat = Math.toRadians(prev.latitude);
            double prevLng = Math.toRadians(prev.longitude);

            double lng;
            for (Iterator var8 = path.iterator(); var8.hasNext(); prevLng = lng) {
                LatLng point = (LatLng) var8.next();
                double lat = Math.toRadians(point.latitude);
                lng = Math.toRadians(point.longitude);
                length += MapUtils.distanceRadians(prevLat, prevLng, lat, lng);
                prevLat = lat;
            }

            return Math.round(length * 6371009.0D);
        }
    }

    public static double computeSignedArea(List<LatLng> path, double radius) {
        int size = path.size();
        if (size < 3) {
            return 0;
        }
        double total = 0;
        LatLng prev = path.get(size - 1);
        double prevTanLat = Math.tan((PI / 2 - Math.toRadians(prev.latitude)) / 2);
        double prevLng = Math.toRadians(prev.longitude);
        // For each edge, accumulate the signed area of the triangle formed by the North Pole
        // and that edge ("polar triangle").
        for (LatLng point : path) {
            double tanLat = Math.tan((PI / 2 - Math.toRadians(point.latitude)) / 2);
            double lng = Math.toRadians(point.longitude);
            total += polarTriangleArea(tanLat, lng, prevTanLat, prevLng);
            prevTanLat = tanLat;
            prevLng = lng;
        }
        /*Area in acreas*/
        double area = Math.abs(Float.valueOf(String.valueOf(total * (radius * radius) * (0.000247105))));
        return (double) Math.round(area * 100) / 100;
    }

    double clamp(double x, double low, double high) {
        return x < low ? low : (x > high ? high : x);
    }

    double wrap(double n, double min, double max) {
        return n >= min && n < max ? n : mod(n - min, max - min) + min;
    }

    double mod(double x, double m) {
        return (x % m + m) % m;
    }

    double mercator(double lat) {
        return Math.log(Math.tan(lat * 0.5D + 0.7853981633974483D));
    }

    double inverseMercator(double y) {
        return 2.0D * Math.atan(Math.exp(y)) - 1.5707963267948966D;
    }

    static double hav(double x) {
        double sinHalf = Math.sin(x * 0.5D);
        return sinHalf * sinHalf;
    }

    static double arcHav(double x) {
        return 2.0D * Math.asin(Math.sqrt(x));
    }

    double sinFromHav(double h) {
        return 2.0D * Math.sqrt(h * (1.0D - h));
    }

    double havFromSin(double x) {
        double x2 = x * x;
        return x2 / (1.0D + Math.sqrt(1.0D - x2)) * 0.5D;
    }

    double sinSumFromHav(double x, double y) {
        double a = Math.sqrt(x * (1.0D - x));
        double b = Math.sqrt(y * (1.0D - y));
        return 2.0D * (a + b - 2.0D * (a * y + b * x));
    }

    static double havDistance(double lat1, double lat2, double dLng) {
        return hav(lat1 - lat2) + hav(dLng) * Math.cos(lat1) * Math.cos(lat2);
    }

    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2));
    }


    /**
     * Returns the signed area of a triangle which has North Pole as a vertex.
     * Formula derived from "Area of a spherical triangle given two edges and the included angle"
     * as per "Spherical Trigonometry" by Todhunter, page 71, section 103, point 2.
     * See http://books.google.com/books?id=3uBHAAAAIAAJ&pg=PA71
     * The arguments named "tan" are tan((pi/2 - latitude)/2).
     */
    private static double polarTriangleArea(double tan1, double lng1, double tan2, double lng2) {
        double deltaLng = lng1 - lng2;
        double t = tan1 * tan2;
        return 2 * Math.atan2(t * Math.sin(deltaLng), 1 + t * Math.cos(deltaLng));
    }
}

