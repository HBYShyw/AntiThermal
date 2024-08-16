package com.oplus.network;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusNetworkKPI implements Parcelable {
    public static final Parcelable.Creator<OplusNetworkKPI> CREATOR = new Parcelable.Creator<OplusNetworkKPI>() { // from class: com.oplus.network.OplusNetworkKPI.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusNetworkKPI createFromParcel(Parcel in) {
            OplusNetworkKPI networkKPI = new OplusNetworkKPI();
            networkKPI.link_index = in.readInt();
            networkKPI.uplink_srtt = in.readInt();
            networkKPI.uplink_packets = in.readInt();
            networkKPI.uplink_retrans_packets = in.readInt();
            networkKPI.uplink_retrans_rate = in.readInt();
            networkKPI.downlink_srtt = in.readInt();
            networkKPI.downlink_packets = in.readInt();
            networkKPI.downlink_retrans_packets = in.readInt();
            networkKPI.downlink_retrans_rate = in.readInt();
            networkKPI.downlink_rate = in.readInt();
            return networkKPI;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusNetworkKPI[] newArray(int size) {
            return new OplusNetworkKPI[size];
        }
    };
    public int downlink_packets;
    public int downlink_rate;
    public int downlink_retrans_packets;
    public int downlink_retrans_rate;
    public int downlink_srtt;
    public int link_index;
    public int uplink_packets;
    public int uplink_retrans_packets;
    public int uplink_retrans_rate;
    public int uplink_srtt;

    public String toString() {
        return "OplusNetworkKPI{uplink_srtt=" + this.uplink_srtt + ", uplink_packets=" + this.uplink_packets + ", uplink_retrans_packets=" + this.uplink_retrans_packets + ", uplink_retrans_rate=" + this.uplink_retrans_rate + ", downlink_srtt=" + this.downlink_srtt + ", downlink_packets=" + this.downlink_packets + ", downlink_retrans_packets=" + this.downlink_retrans_packets + ", downlink_retrans_rate=" + this.downlink_retrans_rate + ", downlink_rate=" + this.downlink_rate + '}';
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.link_index);
        dest.writeInt(this.uplink_srtt);
        dest.writeInt(this.uplink_packets);
        dest.writeInt(this.uplink_retrans_packets);
        dest.writeInt(this.uplink_retrans_rate);
        dest.writeInt(this.downlink_srtt);
        dest.writeInt(this.downlink_packets);
        dest.writeInt(this.downlink_retrans_packets);
        dest.writeInt(this.downlink_retrans_rate);
        dest.writeInt(this.downlink_rate);
    }
}
