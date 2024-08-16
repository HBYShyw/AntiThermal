package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class QosParametersResult implements Parcelable {
    public static final Parcelable.Creator<QosParametersResult> CREATOR = new Parcelable.Creator<QosParametersResult>() { // from class: com.oplus.telephony.QosParametersResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public QosParametersResult createFromParcel(Parcel source) {
            return new QosParametersResult(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public QosParametersResult[] newArray(int size) {
            return new QosParametersResult[size];
        }
    };
    public static final int QOS_TYPE_EPS = 1;
    public static final int QOS_TYPE_NR = 2;
    private static final String TAG = "QosParametersResult";
    private final Qos mDefaultQos;
    private final List<QosBearerSession> mQosBearerSessions;

    public QosParametersResult() {
        this.mDefaultQos = null;
        this.mQosBearerSessions = new ArrayList();
    }

    public QosParametersResult(Qos defaultQos, List<QosBearerSession> qosBearerSessions) {
        this.mDefaultQos = defaultQos;
        this.mQosBearerSessions = qosBearerSessions == null ? new ArrayList() : new ArrayList(qosBearerSessions);
    }

    public QosParametersResult(Parcel source) {
        this.mDefaultQos = (Qos) source.readParcelable(Qos.class.getClassLoader());
        ArrayList arrayList = new ArrayList();
        this.mQosBearerSessions = arrayList;
        source.readList(arrayList, QosBearerSession.class.getClassLoader());
    }

    public Qos getDefaultQos() {
        return this.mDefaultQos;
    }

    public List<QosBearerSession> getQosBearerSessions() {
        return this.mQosBearerSessions;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("QosParametersResult: {").append(" defaultQos=").append(this.mDefaultQos).append(" qosBearerSessions=").append(this.mQosBearerSessions).append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        boolean isQosSame;
        boolean isQosBearerSessionsSame;
        Qos qos;
        if (this == o) {
            return true;
        }
        if (!(o instanceof QosParametersResult)) {
            return false;
        }
        QosParametersResult other = (QosParametersResult) o;
        Qos qos2 = this.mDefaultQos;
        if (qos2 == null || (qos = other.mDefaultQos) == null) {
            isQosSame = qos2 == other.mDefaultQos;
        } else {
            isQosSame = qos2.equals(qos);
        }
        List<QosBearerSession> list = this.mQosBearerSessions;
        if (list == null || other.mQosBearerSessions == null) {
            isQosBearerSessionsSame = list == other.mQosBearerSessions;
        } else {
            isQosBearerSessionsSame = list.size() == other.mQosBearerSessions.size() && this.mQosBearerSessions.containsAll(other.mQosBearerSessions);
        }
        return isQosSame && isQosBearerSessionsSame;
    }

    public int hashCode() {
        return Objects.hash(this.mDefaultQos, this.mQosBearerSessions);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Qos qos = this.mDefaultQos;
        if (qos != null) {
            if (qos.getType() == 1) {
                dest.writeParcelable((EpsQos) this.mDefaultQos, flags);
            } else {
                dest.writeParcelable((NrQos) this.mDefaultQos, flags);
            }
        } else {
            dest.writeParcelable(null, flags);
        }
        dest.writeList(this.mQosBearerSessions);
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private Qos mDefaultQos;
        private List<QosBearerSession> mQosBearerSessions = new ArrayList();

        public Builder setDefaultQos(Qos defaultQos) {
            this.mDefaultQos = defaultQos;
            return this;
        }

        public Builder setQosBearerSessions(List<QosBearerSession> qosBearerSessions) {
            this.mQosBearerSessions = qosBearerSessions;
            return this;
        }

        public QosParametersResult build() {
            return new QosParametersResult(this.mDefaultQos, this.mQosBearerSessions);
        }
    }
}
