package com.emovid.ege;

import java.util.ArrayList;
import android.util.Log;

class SpotegeFilterableList extends ArrayList<Spotege> {
    public Spotege nearestSpotege(double latitude, double longitude, SpotegeType type) {
        double distance = 0.0d;
        int idx = 0;
        int i = 0;
        for (Spotege sp : this) {
            // It is compatible in type
            if (sp.isType(type)) {
                double d = getDistance(sp, latitude, longitude);
                if (idx == 0) {
                    distance = d;
                }

                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotege().spotege :: " + sp.getName());
                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotege().spotege-phone :: " + sp.getPhone());
                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotege().spotege-distance :: " + d);
                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotege().spotege-min-distance :: " + distance);

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
