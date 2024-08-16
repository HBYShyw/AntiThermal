package com.oplus.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public final class MtkIpDescriptors implements Parcelable {
    public static final Parcelable.Creator<MtkIpDescriptors> CREATOR = new Parcelable.Creator<MtkIpDescriptors>() { // from class: com.oplus.telephony.MtkIpDescriptors.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkIpDescriptors createFromParcel(Parcel in) {
            return new MtkIpDescriptors(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkIpDescriptors[] newArray(int size) {
            return new MtkIpDescriptors[size];
        }
    };
    private int destPort;
    private int destPortEndRange;
    private int destPortStartRange;
    private String ipv4;
    private String ipv6;
    private String maskV4;
    private int prefixLength;
    private int protocol;

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public void setMaskV4(String maskV4) {
        this.maskV4 = maskV4;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public void setPrefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    public void setDestPortStartRange(int destPortStartRange) {
        this.destPortStartRange = destPortStartRange;
    }

    public void setDestPortEndRange(int destPortEndRange) {
        this.destPortEndRange = destPortEndRange;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getIpv4() {
        return this.ipv4;
    }

    public String getMaskV4() {
        return this.maskV4;
    }

    public String getIpv6() {
        return this.ipv6;
    }

    public int getPrefixLength() {
        return this.prefixLength;
    }

    public int getDestPort() {
        return this.destPort;
    }

    public int getDestPortStartRange() {
        return this.destPortStartRange;
    }

    public int getDestPortEndRange() {
        return this.destPortEndRange;
    }

    public int getProtocol() {
        return this.protocol;
    }

    public MtkIpDescriptors() {
    }

    private MtkIpDescriptors(Parcel in) {
        this.ipv4 = in.readString();
        this.maskV4 = in.readString();
        this.ipv6 = in.readString();
        this.prefixLength = in.readInt();
        this.destPort = in.readInt();
        this.destPortStartRange = in.readInt();
        this.destPortEndRange = in.readInt();
        this.protocol = in.readInt();
    }

    protected MtkIpDescriptors(String ipv4, String maskV4, String ipv6, int prefixLength, int destPort, int destPortStartRange, int destPortEndRange, int protocol) {
        this.ipv4 = ipv4;
        this.maskV4 = maskV4;
        this.ipv6 = ipv6;
        this.prefixLength = prefixLength;
        this.destPort = destPort;
        this.destPortStartRange = destPortStartRange;
        this.destPortEndRange = destPortEndRange;
        this.protocol = protocol;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ipv4);
        dest.writeString(this.maskV4);
        dest.writeString(this.ipv6);
        dest.writeInt(this.prefixLength);
        dest.writeInt(this.destPort);
        dest.writeInt(this.destPortStartRange);
        dest.writeInt(this.destPortEndRange);
        dest.writeInt(this.protocol);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "MtkIpDescriptors{ipv4='" + this.ipv4 + "', maskV4='" + this.maskV4 + "', ipv6='" + this.ipv6 + "', prefixLength=" + this.prefixLength + ", destPort=" + this.destPort + ", destPortStartRange=" + this.destPortStartRange + ", destPortEndRange=" + this.destPortEndRange + ", protocol=" + this.protocol + '}';
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MtkIpDescriptors)) {
            return false;
        }
        MtkIpDescriptors ipDescriptors = (MtkIpDescriptors) obj;
        if (!ipDescriptors.getIpv4().equals(this.ipv4) || !ipDescriptors.getMaskV4().equals(this.maskV4) || !ipDescriptors.getIpv6().equals(this.ipv6) || ipDescriptors.getPrefixLength() != this.prefixLength || ipDescriptors.getDestPort() != this.destPort || ipDescriptors.getProtocol() != this.protocol || ipDescriptors.getDestPortStartRange() != this.destPortStartRange || ipDescriptors.getDestPortEndRange() != this.destPortEndRange) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.ipv4, this.maskV4, this.ipv6, Integer.valueOf(this.prefixLength), Integer.valueOf(this.destPort), Integer.valueOf(this.destPortStartRange), Integer.valueOf(this.destPortEndRange), Integer.valueOf(this.protocol));
    }
}
