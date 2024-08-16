package com.oplus.wrapper.location;

import android.location.GnssSingleSatCorrection;

/* loaded from: classes.dex */
public class GnssSingleSatCorrection {
    private android.location.GnssSingleSatCorrection mGnssSingleSatCorrection;

    public GnssSingleSatCorrection(android.location.GnssSingleSatCorrection gnssSingleSatCorrection) {
        this.mGnssSingleSatCorrection = gnssSingleSatCorrection;
    }

    public android.location.GnssSingleSatCorrection get() {
        return this.mGnssSingleSatCorrection;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private GnssSingleSatCorrection.Builder mBuilder = new GnssSingleSatCorrection.Builder();

        public GnssSingleSatCorrection build() {
            return new GnssSingleSatCorrection(this.mBuilder.build());
        }

        public Builder setCarrierFrequencyHz(float carrierFrequencyHz) {
            this.mBuilder.setCarrierFrequencyHz(carrierFrequencyHz);
            return this;
        }

        public Builder setConstellationType(int constellationType) {
            this.mBuilder.setConstellationType(constellationType);
            return this;
        }

        public Builder setProbabilityLineOfSight(float probSatIsLos) {
            this.mBuilder.setProbabilityLineOfSight(probSatIsLos);
            return this;
        }

        public Builder setSatelliteId(int satId) {
            this.mBuilder.setSatelliteId(satId);
            return this;
        }
    }
}
