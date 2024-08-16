package com.oplus.telephony;

import android.hardware.radio.V1_6.QosBandwidth;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public final class EpsQos extends Qos implements Parcelable {
    public static final Parcelable.Creator<EpsQos> CREATOR = new Parcelable.Creator<EpsQos>() { // from class: com.oplus.telephony.EpsQos.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EpsQos createFromParcel(Parcel source) {
            return new EpsQos(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EpsQos[] newArray(int size) {
            return new EpsQos[size];
        }
    };
    int qosClassId;

    public EpsQos() {
        super(1, new QosBandwidth(), new QosBandwidth());
    }

    public EpsQos(android.hardware.radio.V1_6.EpsQos qos) {
        super(1, qos.downlink, qos.uplink);
        this.qosClassId = qos.qci;
    }

    private EpsQos(Parcel source) {
        super(source);
        this.qosClassId = source.readInt();
    }

    public int getQci() {
        return this.qosClassId;
    }

    public static EpsQos createFromParcelBody(Parcel in) {
        return new EpsQos(in);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(1, dest, flags);
        dest.writeInt(this.qosClassId);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.oplus.telephony.Qos
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.qosClassId));
    }

    @Override // com.oplus.telephony.Qos
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof EpsQos)) {
            return false;
        }
        EpsQos other = (EpsQos) o;
        if (this.qosClassId == other.qosClassId && super.equals(other)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "EpsQos { qosClassId=" + this.qosClassId + " downlink=" + this.downlink + " uplink=" + this.uplink + "}";
    }
}
