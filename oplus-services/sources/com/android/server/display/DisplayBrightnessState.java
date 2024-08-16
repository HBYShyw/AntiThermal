package com.android.server.display;

import com.android.server.display.brightness.BrightnessReason;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayBrightnessState {
    private final float mBrightness;
    private final BrightnessReason mBrightnessReason;
    private final String mDisplayBrightnessStrategyName;
    private final float mSdrBrightness;

    private DisplayBrightnessState(Builder builder) {
        this.mBrightness = builder.getBrightness();
        this.mSdrBrightness = builder.getSdrBrightness();
        this.mBrightnessReason = builder.getBrightnessReason();
        this.mDisplayBrightnessStrategyName = builder.getDisplayBrightnessStrategyName();
    }

    public float getBrightness() {
        return this.mBrightness;
    }

    public float getSdrBrightness() {
        return this.mSdrBrightness;
    }

    public BrightnessReason getBrightnessReason() {
        return this.mBrightnessReason;
    }

    public String getDisplayBrightnessStrategyName() {
        return this.mDisplayBrightnessStrategyName;
    }

    public String toString() {
        return "DisplayBrightnessState:\n    brightness:" + getBrightness() + "\n    sdrBrightness:" + getSdrBrightness() + "\n    brightnessReason:" + getBrightnessReason();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DisplayBrightnessState)) {
            return false;
        }
        DisplayBrightnessState displayBrightnessState = (DisplayBrightnessState) obj;
        return this.mBrightness == displayBrightnessState.getBrightness() && this.mSdrBrightness == displayBrightnessState.getSdrBrightness() && this.mBrightnessReason.equals(displayBrightnessState.getBrightnessReason()) && this.mDisplayBrightnessStrategyName.equals(displayBrightnessState.getDisplayBrightnessStrategyName());
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.mBrightness), Float.valueOf(this.mSdrBrightness), this.mBrightnessReason);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Builder {
        private float mBrightness;
        private BrightnessReason mBrightnessReason = new BrightnessReason();
        private String mDisplayBrightnessStrategyName;
        private float mSdrBrightness;

        public float getBrightness() {
            return this.mBrightness;
        }

        public Builder setBrightness(float f) {
            this.mBrightness = f;
            return this;
        }

        public float getSdrBrightness() {
            return this.mSdrBrightness;
        }

        public Builder setSdrBrightness(float f) {
            this.mSdrBrightness = f;
            return this;
        }

        public BrightnessReason getBrightnessReason() {
            return this.mBrightnessReason;
        }

        public Builder setBrightnessReason(BrightnessReason brightnessReason) {
            this.mBrightnessReason = brightnessReason;
            return this;
        }

        public String getDisplayBrightnessStrategyName() {
            return this.mDisplayBrightnessStrategyName;
        }

        public Builder setDisplayBrightnessStrategyName(String str) {
            this.mDisplayBrightnessStrategyName = str;
            return this;
        }

        public DisplayBrightnessState build() {
            return new DisplayBrightnessState(this);
        }
    }
}
