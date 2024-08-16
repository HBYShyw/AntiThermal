package android.cmccslice;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Objects;

/* loaded from: classes.dex */
public final class TrafficDescriptor implements Parcelable {
    public static final Parcelable.Creator<TrafficDescriptor> CREATOR = new Parcelable.Creator<TrafficDescriptor>() { // from class: android.cmccslice.TrafficDescriptor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrafficDescriptor createFromParcel(Parcel in) {
            return new TrafficDescriptor(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TrafficDescriptor[] newArray(int size) {
            return new TrafficDescriptor[size];
        }
    };
    private String connectionCapabilities;
    private String dnn;
    private String domainDescriptors;
    private IpDescriptors ipDescriptors;
    private boolean matchAll;
    private String osAppId;

    public void setOsAppId(String osAppId) {
        this.osAppId = osAppId;
    }

    public void setIpDescriptors(IpDescriptors ipDescriptors) {
        this.ipDescriptors = ipDescriptors;
    }

    public void setDomainDescriptors(String domainDescriptors) {
        this.domainDescriptors = domainDescriptors;
    }

    public void setDnn(String dnn) {
        this.dnn = dnn;
    }

    public void setConnectionCapabilities(String connectionCapabilities) {
        this.connectionCapabilities = connectionCapabilities;
    }

    public String getOsAppId() {
        return this.osAppId;
    }

    public IpDescriptors getIpDescriptors() {
        return this.ipDescriptors;
    }

    public String getDomainDescriptors() {
        return this.domainDescriptors;
    }

    public String getDnn() {
        return this.dnn;
    }

    public String getConnectionCapabilities() {
        return this.connectionCapabilities;
    }

    public boolean isMatchAll() {
        return this.matchAll;
    }

    public void setMatchAll(boolean matchAll) {
        this.matchAll = matchAll;
    }

    public TrafficDescriptor() {
        this.osAppId = "";
        this.domainDescriptors = "";
        this.dnn = "";
        this.connectionCapabilities = "";
    }

    public TrafficDescriptor(boolean matchAll, String osAppId, IpDescriptors ipDescriptors, String domainDescriptors, String dnn, String connectionCapabilities) {
        this.osAppId = "";
        this.domainDescriptors = "";
        this.dnn = "";
        this.connectionCapabilities = "";
        this.matchAll = matchAll;
        this.osAppId = osAppId;
        this.ipDescriptors = ipDescriptors;
        this.domainDescriptors = domainDescriptors;
        this.dnn = dnn;
        this.connectionCapabilities = connectionCapabilities;
    }

    protected TrafficDescriptor(Parcel in) {
        this.osAppId = "";
        this.domainDescriptors = "";
        this.dnn = "";
        this.connectionCapabilities = "";
        this.matchAll = in.readInt() != 0;
        this.osAppId = in.readString();
        this.ipDescriptors = (IpDescriptors) in.readParcelable(Thread.currentThread().getContextClassLoader());
        this.domainDescriptors = in.readString();
        this.dnn = in.readString();
        this.connectionCapabilities = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.matchAll ? 1 : 0);
        parcel.writeString(this.osAppId);
        parcel.writeParcelable(this.ipDescriptors, 0);
        parcel.writeString(this.domainDescriptors);
        parcel.writeString(this.dnn);
        parcel.writeString(this.connectionCapabilities);
    }

    public String toString() {
        return "TrafficDescriptor{matchAll= '" + this.matchAll + "', osAppId='" + this.osAppId + "', ipDescriptors=" + this.ipDescriptors + ", domainDescriptors='" + this.domainDescriptors + "', dnn='" + this.dnn + "', connectionCapabilities='" + this.connectionCapabilities + "'}";
    }

    public boolean equals(Object obj) {
        boolean result;
        boolean res;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TrafficDescriptor)) {
            return false;
        }
        TrafficDescriptor trafficDescriptor = (TrafficDescriptor) obj;
        if (trafficDescriptor.matchAll != this.matchAll || !equals(trafficDescriptor.getOsAppId(), this.osAppId) || !equals(trafficDescriptor.getConnectionCapabilities(), this.connectionCapabilities) || !equals(trafficDescriptor.getDnn(), this.dnn) || !equals(trafficDescriptor.getDomainDescriptors(), this.domainDescriptors)) {
            result = false;
        } else {
            result = true;
        }
        if (trafficDescriptor.getIpDescriptors() != null) {
            if (!result || !trafficDescriptor.getIpDescriptors().equals(this.ipDescriptors)) {
                return false;
            }
            return true;
        }
        IpDescriptors ipDescriptors = this.ipDescriptors;
        if (ipDescriptors == null) {
            return result;
        }
        if (!TextUtils.isEmpty(ipDescriptors.getIpv4()) || !TextUtils.isEmpty(this.ipDescriptors.getMaskV4()) || !TextUtils.isEmpty(this.ipDescriptors.getIpv6()) || this.ipDescriptors.getPrefixLength() != 0 || this.ipDescriptors.getDestPort() != 0 || this.ipDescriptors.getDestPortStartRange() != 0 || this.ipDescriptors.getDestPortEndRange() != 0 || this.ipDescriptors.getProtocol() != 0) {
            res = false;
        } else {
            res = true;
        }
        if (!result || !res) {
            return false;
        }
        return true;
    }

    private boolean equals(CharSequence a, CharSequence b) {
        int length;
        if ((a == null && b == null) || a == b) {
            return true;
        }
        if (a == null || b == null || (length = a.length()) != b.length()) {
            return false;
        }
        if ((a instanceof String) && (b instanceof String)) {
            return a.equals(b);
        }
        for (int i = 0; i < length; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.matchAll), this.osAppId, this.ipDescriptors, this.domainDescriptors, this.dnn, this.connectionCapabilities);
    }
}
