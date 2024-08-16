package com.oplus.wrapper.location;

import android.location.GnssMeasurementCorrections;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class GnssMeasurementCorrections {
    private android.location.GnssMeasurementCorrections mGnssMeasurementCorrections;

    public GnssMeasurementCorrections(android.location.GnssMeasurementCorrections gnssMeasurementCorrections) {
        this.mGnssMeasurementCorrections = gnssMeasurementCorrections;
    }

    public android.location.GnssMeasurementCorrections get() {
        return this.mGnssMeasurementCorrections;
    }

    public float getEnvironmentBearingDegrees() {
        return this.mGnssMeasurementCorrections.getEnvironmentBearingDegrees();
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private GnssMeasurementCorrections.Builder mBuilder = new GnssMeasurementCorrections.Builder();

        public GnssMeasurementCorrections build() {
            return new GnssMeasurementCorrections(this.mBuilder.build());
        }

        public Builder setAltitudeMeters(double altitudeMeters) {
            this.mBuilder.setAltitudeMeters(altitudeMeters);
            return this;
        }

        public Builder setEnvironmentBearingDegrees(float environmentBearingDegrees) {
            this.mBuilder.setEnvironmentBearingDegrees(environmentBearingDegrees);
            return this;
        }

        public Builder setEnvironmentBearingUncertaintyDegrees(float environmentBearingUncertaintyDegrees) {
            this.mBuilder.setEnvironmentBearingUncertaintyDegrees(environmentBearingUncertaintyDegrees);
            return this;
        }

        public Builder setHorizontalPositionUncertaintyMeters(double horizontalPositionUncertaintyMeters) {
            this.mBuilder.setHorizontalPositionUncertaintyMeters(horizontalPositionUncertaintyMeters);
            return this;
        }

        public Builder setLatitudeDegrees(double latitudeDegrees) {
            this.mBuilder.setLatitudeDegrees(latitudeDegrees);
            return this;
        }

        public Builder setLongitudeDegrees(double longitudeDegrees) {
            this.mBuilder.setLongitudeDegrees(longitudeDegrees);
            return this;
        }

        public Builder setSingleSatelliteCorrectionList(List<GnssSingleSatCorrection> wrapperSingleSatCorrectionList) {
            List<android.location.GnssSingleSatCorrection> singleSatCorrections = new ArrayList<>();
            for (int i = 0; i < wrapperSingleSatCorrectionList.size(); i++) {
                singleSatCorrections.add(wrapperSingleSatCorrectionList.get(i).get());
            }
            this.mBuilder.setSingleSatelliteCorrectionList(singleSatCorrections);
            return this;
        }

        public Builder setToaGpsNanosecondsOfWeek(long toaGpsNanosecondsOfWeek) {
            this.mBuilder.setToaGpsNanosecondsOfWeek(toaGpsNanosecondsOfWeek);
            return this;
        }

        public Builder setVerticalPositionUncertaintyMeters(double verticalPositionUncertaintyMeters) {
            this.mBuilder.setVerticalPositionUncertaintyMeters(verticalPositionUncertaintyMeters);
            return this;
        }
    }
}
