package com.oplus.network.stats;

import java.util.Objects;

/* loaded from: classes.dex */
public class SpeedKey {
    public static final int SPEED_KEY_TYPE_DPI_STREAM = 2;
    public static final int SPEED_KEY_TYPE_SOCKET = 1;
    public int mIfindex;
    public int mKeyType;
    public long value;

    public SpeedKey(int mKeyType, long value) {
        this(mKeyType, 0, value);
    }

    public SpeedKey(int mKeyType, int ifIndex, long value) {
        this.mKeyType = mKeyType;
        this.mIfindex = ifIndex;
        this.value = value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeedKey)) {
            return false;
        }
        SpeedKey speedKey = (SpeedKey) o;
        return this.mKeyType == speedKey.mKeyType && this.value == speedKey.value && this.mIfindex == speedKey.mIfindex;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mKeyType), Integer.valueOf(this.mIfindex), Long.valueOf(this.value));
    }

    public String toString() {
        return "SpeedKey{mKeyType=" + this.mKeyType + ", mIfindex=" + this.mIfindex + ", value=" + this.value + '}';
    }
}
