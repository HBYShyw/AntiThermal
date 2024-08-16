package android.cmccslice;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public final class UrspRule implements Parcelable {
    public static final Parcelable.Creator<UrspRule> CREATOR = new Parcelable.Creator<UrspRule>() { // from class: android.cmccslice.UrspRule.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UrspRule createFromParcel(Parcel in) {
            return new UrspRule(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UrspRule[] newArray(int size) {
            return new UrspRule[size];
        }
    };
    public byte precedence;
    public TrafficDescriptor trafficDescriptors;

    public UrspRule(byte precedence, TrafficDescriptor trafficDescriptors) {
        this.precedence = (byte) 0;
        this.precedence = precedence;
        this.trafficDescriptors = trafficDescriptors;
    }

    public UrspRule() {
        this.precedence = (byte) 0;
    }

    protected UrspRule(Parcel in) {
        this.precedence = (byte) 0;
        this.precedence = in.readByte();
        this.trafficDescriptors = (TrafficDescriptor) in.readParcelable(TrafficDescriptor.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.precedence);
        dest.writeParcelable(this.trafficDescriptors, flags);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UrspRule urspRule = (UrspRule) o;
        if (this.precedence == urspRule.precedence && Objects.equals(this.trafficDescriptors, urspRule.trafficDescriptors)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Byte.valueOf(this.precedence), this.trafficDescriptors);
    }

    public String toString() {
        return "UrspRule{precedence=" + ((int) this.precedence) + ", trafficDescriptors=" + this.trafficDescriptors + '}';
    }
}
