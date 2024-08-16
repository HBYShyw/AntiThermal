package com.oplus.network;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes.dex */
public class OlkStreamParam implements Parcelable {
    public static final Parcelable.Creator<OlkStreamParam> CREATOR = new Parcelable.Creator<OlkStreamParam>() { // from class: com.oplus.network.OlkStreamParam.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OlkStreamParam createFromParcel(Parcel in) {
            String srcIp = in.readString();
            String dstIp = in.readString();
            int srcPort = in.readInt();
            int dstPort = in.readInt();
            int protocol = in.readInt();
            int tag = in.readInt();
            int priority = in.readInt();
            return new OlkStreamParam(srcIp, dstIp, srcPort, dstPort, protocol, tag, priority);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OlkStreamParam[] newArray(int size) {
            return new OlkStreamParam[size];
        }
    };
    public static final int MAX_PORT_ID = 65535;
    public static final int MAX_PROTOCOL_ID = 255;
    public static final int PROTOCOL_ID_TCP = 6;
    public static final int PROTOCOL_ID_UDP = 17;
    private static final String TAG = "OlkStreamParam";
    public String mDstIp;
    public int mDstPort;
    public int mPriority;
    public int mProtocol;
    public String mSrcIp;
    public int mSrcPort;
    public int mTag;

    public OlkStreamParam(String mSrcIp, String mDstIp, int mSrcPort, int mDstPort, int mProtocol, int mTag, int mPriority) {
        this.mSrcIp = mSrcIp;
        this.mDstIp = mDstIp;
        this.mSrcPort = mSrcPort;
        this.mDstPort = mDstPort;
        this.mProtocol = mProtocol;
        this.mTag = mTag;
        this.mPriority = mPriority;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSrcIp);
        dest.writeString(this.mDstIp);
        dest.writeInt(this.mSrcPort);
        dest.writeInt(this.mDstPort);
        dest.writeInt(this.mProtocol);
        dest.writeInt(this.mTag);
        dest.writeInt(this.mPriority);
    }

    public String toString() {
        return "OlkStreamParam{mSrcIp='" + this.mSrcIp + "', mDstIp='" + this.mDstIp + "', mSrcPort=" + this.mSrcPort + ", mDstPort=" + this.mDstPort + ", mProtocol=" + this.mProtocol + ", mTag=" + this.mTag + ", mPriority=" + this.mPriority + '}';
    }

    public static boolean checkIpStrValid(String ipstr) {
        String[] ip = ipstr.split("\\.");
        if (ip.length != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            try {
                int v = Integer.parseInt(ip[i]);
                if (v < 0 || v > 255) {
                    return false;
                }
            } catch (Exception e) {
                Log.d(TAG, "parse ip failed! " + ip[i]);
                return false;
            }
        }
        return true;
    }

    public boolean checkParamValid() {
        boolean srcIpValid = true;
        boolean dstIpValid = true;
        boolean srcPortValid = true;
        boolean dstPortValid = true;
        boolean protocolValid = true;
        if (TextUtils.isEmpty(this.mSrcIp)) {
            srcIpValid = false;
        } else if (!checkIpStrValid(this.mSrcIp)) {
            Log.d(TAG, "srcIp invalid!");
            return false;
        }
        if (TextUtils.isEmpty(this.mDstIp)) {
            dstIpValid = false;
        } else if (!checkIpStrValid(this.mDstIp)) {
            Log.d(TAG, "dstIp invalid!");
            return false;
        }
        int i = this.mSrcPort;
        if (i == 0) {
            srcPortValid = false;
        } else if (i < 0 || i > 65535) {
            Log.d(TAG, "srcPort invalid!" + this.mSrcPort);
            return false;
        }
        int i2 = this.mDstPort;
        if (i2 == 0) {
            dstPortValid = false;
        } else if (i2 < 0 || i2 > 65535) {
            Log.d(TAG, "mDstPort invalid!" + this.mDstPort);
            return false;
        }
        int i3 = this.mProtocol;
        if (i3 == 0) {
            protocolValid = false;
        } else if (i3 != 6 && i3 != 17) {
            Log.d(TAG, "mProtocol invalid!" + this.mProtocol);
            return false;
        }
        if (!srcIpValid && !dstIpValid && !srcPortValid && !dstPortValid && !protocolValid) {
            Log.d(TAG, "all param is invalid! at list one param valid!");
            return false;
        }
        return true;
    }

    public boolean keyEquals(OlkStreamParam param) {
        return this.mSrcIp.equals(param.mSrcIp) && this.mDstIp.equals(param.mDstIp) && this.mSrcPort == param.mSrcPort && this.mDstPort == param.mDstPort && this.mProtocol == param.mProtocol;
    }
}
