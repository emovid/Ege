package com.emovid.ege;

import java.util.ArrayList;
import android.util.Log;

class SpotEgeFilterableList extends ArrayList<SpotEge> {
    public SpotEge nearestSpotEge(double latitude, double longitude, SpotEgeType type) {
        double distance = 0.0d;
        int candidateIndex = 0;
        int i = 0;
        for (SpotEge sp : this) {
            // It is compatible in type
            if (sp.isType(type)) {
                double d = getDistance(sp, latitude, longitude);
                if (candidateIndex == 0 || d == 0.0f || d < distance) {
                    distance = d;
                    candidateIndex = i;
                }

                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotEge().spotege :: " + sp.getName());
                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotEge().spotege-phone :: " + sp.getPhone());
                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotEge().spotege-distance :: " + d);
                Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotEge().spotege-min-distance :: " + distance);
            }
            i++;
        }

        Log.d(QuickCallActivity.PACKAGE_NAME, "nearestSpotEge().index :: " + candidateIndex);
        return this.get(candidateIndex);
    }

    /**
     * @return Diagonal distance of two points
     */
    private double getDistance(SpotEge sp, double lati, double longi) {
        double latiDiff = sp.latitude - lati;
        double longiDiff = sp.longitude - longi;

        // Phytagoras
        return Math.sqrt((latiDiff * latiDiff) + (longiDiff * longiDiff));
    }
}
