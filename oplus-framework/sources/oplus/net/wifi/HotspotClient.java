package oplus.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class HotspotClient implements Parcelable {
    public static final Parcelable.Creator<HotspotClient> CREATOR = new Parcelable.Creator<HotspotClient>() { // from class: oplus.net.wifi.HotspotClient.1
        @Override // android.os.Parcelable.Creator
        public HotspotClient createFromParcel(Parcel in) {
            HotspotClient result = new HotspotClient(in.readString(), in.readString(), in.readString());
            return result;
        }

        @Override // android.os.Parcelable.Creator
        public HotspotClient[] newArray(int size) {
            return new HotspotClient[size];
        }
    };
    public String conTime;
    public String deviceAddress;
    public String name;

    public HotspotClient(String address) {
        this.deviceAddress = address;
    }

    public HotspotClient(String address, String name, String time) {
        this.deviceAddress = address;
        this.name = name;
        this.conTime = time;
    }

    public HotspotClient(String address, String name) {
        this.deviceAddress = address;
        this.name = name;
    }

    public HotspotClient(HotspotClient source) {
        if (source != null) {
            this.deviceAddress = source.deviceAddress;
            this.name = source.name;
            this.conTime = source.conTime;
        }
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(" deviceAddress: ").append(this.deviceAddress);
        sbuf.append('\n');
        sbuf.append(" name: ").append(this.name);
        sbuf.append("\n");
        sbuf.append(" conTime: ").append(this.conTime);
        sbuf.append("\n");
        return sbuf.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceAddress);
        dest.writeString(this.name);
        dest.writeString(this.conTime);
    }
}
