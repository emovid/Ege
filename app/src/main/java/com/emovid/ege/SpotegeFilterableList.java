package com.emovid.ege;

import java.util.ArrayList;

class SpotegeFilterableList extends ArrayList<Spotege> {
    public Spotege nearestSpotege(double latitude, double longitude, SpotegeType type) {
        double distance = 0.0d;
        int idx = -1;
        int i = 0;
        for (Spotege sp : this) {
            if (sp.isType(type)) {
                // It is compatible in type
                double d = getDistance(sp, latitude, longitude);
                if (d == 0.0d) {
                    distance = d;
                }

                if (d < distance) {
                    distance = d;
                    idx = i;
                }
            }
            i++;
        }

        return this.get(idx);
    }

    /**
     * @return Diagonal distance of two points
     */
    private double getDistance(Spotege sp, double lati, double longi) {
        double latiDiff = sp.latitude - lati;
        double longiDiff = sp.longitude - longi;

        // Phytagoras
        return Math.sqrt((latiDiff * latiDiff) + (longiDiff * longiDiff));
    }
}