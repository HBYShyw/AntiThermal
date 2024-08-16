package com.oplus.wrapper.location;

import android.location.CorrelationVector;

/* loaded from: classes.dex */
public class CorrelationVector {
    private final android.location.CorrelationVector mCorrelationVector;

    public CorrelationVector(android.location.CorrelationVector correlationVector) {
        this.mCorrelationVector = correlationVector;
    }

    public double getFrequencyOffsetMetersPerSecond() {
        return this.mCorrelationVector.getFrequencyOffsetMetersPerSecond();
    }

    public int[] getMagnitude() {
        return this.mCorrelationVector.getMagnitude();
    }

    public double getSamplingStartMeters() {
        return this.mCorrelationVector.getSamplingStartMeters();
    }

    public double getSamplingWidthMeters() {
        return this.mCorrelationVector.getSamplingWidthMeters();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private final CorrelationVector.Builder mBuilder = new CorrelationVector.Builder();

        public CorrelationVector build() {
            return new CorrelationVector(this.mBuilder.build());
        }

        public Builder setSamplingWidthMeters(double samplingWidthMeters) {
            this.mBuilder.setSamplingWidthMeters(samplingWidthMeters);
            return this;
        }

        public Builder setSamplingStartMeters(double samplingStartMeters) {
            this.mBuilder.setSamplingStartMeters(samplingStartMeters);
            return this;
        }

        public Builder setFrequencyOffsetMetersPerSecond(double frequencyOffsetMetersPerSecond) {
            this.mBuilder.setFrequencyOffsetMetersPerSecond(frequencyOffsetMetersPerSecond);
            return this;
        }

        public Builder setMagnitude(int[] magnitude) {
            this.mBuilder.setMagnitude(magnitude);
            return this;
        }
    }
}
