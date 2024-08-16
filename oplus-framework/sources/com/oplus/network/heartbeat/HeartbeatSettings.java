package com.oplus.network.heartbeat;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;

/* loaded from: classes.dex */
public final class HeartbeatSettings implements Parcelable {
    public static final Parcelable.Creator<HeartbeatSettings> CREATOR = new Parcelable.Creator<HeartbeatSettings>() { // from class: com.oplus.network.heartbeat.HeartbeatSettings.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartbeatSettings createFromParcel(Parcel in) {
            return new HeartbeatSettings(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartbeatSettings[] newArray(int size) {
            return new HeartbeatSettings[size];
        }
    };
    private static final String TAG = "HeartbeatSettings";
    private int mCycle;
    private byte[] mDaddr;
    private int mDport;
    private HeartbeatManager mHeartbeatManager;
    private boolean mIsAllowDynamicCycle;
    private boolean mIsIPv6;
    private int mMaxCycle;
    private byte[] mReply;
    private byte[] mSaddr;
    private byte[] mSend;
    private Socket mSocket;
    private int mSport;
    private int mStepCycle;
    private int mStepCycleSuccessNum;
    private int mTcpRetries2;

    private HeartbeatSettings(Parcel in) {
        this.mIsIPv6 = in.readBoolean();
        byte[] bArr = new byte[in.readInt()];
        this.mSaddr = bArr;
        in.readByteArray(bArr);
        byte[] bArr2 = new byte[in.readInt()];
        this.mDaddr = bArr2;
        in.readByteArray(bArr2);
        this.mSport = in.readInt();
        this.mDport = in.readInt();
        byte[] bArr3 = new byte[in.readInt()];
        this.mSend = bArr3;
        in.readByteArray(bArr3);
        byte[] bArr4 = new byte[in.readInt()];
        this.mReply = bArr4;
        in.readByteArray(bArr4);
        this.mIsAllowDynamicCycle = in.readBoolean();
        this.mCycle = in.readInt();
        this.mMaxCycle = in.readInt();
        this.mStepCycle = in.readInt();
        this.mStepCycleSuccessNum = in.readInt();
        this.mTcpRetries2 = in.readInt();
    }

    public HeartbeatSettings() {
    }

    public boolean isHeartbeatAvailabel() {
        if (this.mHeartbeatManager == null) {
            this.mHeartbeatManager = HeartbeatManager.getInstance();
        }
        return this.mHeartbeatManager.isHeartbeatAvailabel();
    }

    public boolean isHeartbeatDynamicCycleEnabled() {
        if (this.mHeartbeatManager == null) {
            this.mHeartbeatManager = HeartbeatManager.getInstance();
        }
        return this.mHeartbeatManager.isHeartbeatDynamicCycleEnabled();
    }

    public void setSocket(Socket socket) {
        this.mSocket = socket;
        if (socket != null && socket.isConnected()) {
            InetAddress saddr = socket.getLocalAddress();
            if (saddr != null) {
                this.mSaddr = saddr.getAddress();
            }
            this.mSport = socket.getLocalPort();
            InetAddress daddr = socket.getInetAddress();
            if (daddr != null) {
                this.mDaddr = daddr.getAddress();
            }
            this.mDport = socket.getPort();
            if (daddr instanceof Inet6Address) {
                this.mIsIPv6 = true;
            } else {
                this.mIsIPv6 = false;
            }
        }
    }

    public void setPayload(byte[] send, byte[] reply) {
        this.mSend = send;
        this.mReply = reply;
    }

    public void setCycle(int cycle) {
        this.mCycle = cycle;
    }

    public void setDynamicCycle(int initCycle, int maxCycle, int stepCycle, int successNum) {
        this.mIsAllowDynamicCycle = true;
        this.mCycle = initCycle;
        this.mMaxCycle = maxCycle;
        this.mStepCycle = stepCycle;
        this.mStepCycleSuccessNum = successNum;
    }

    public void setTcpRetries2(int count) {
        this.mTcpRetries2 = count;
    }

    public Socket getSocket() {
        return this.mSocket;
    }

    public int getIsIPv6() {
        return this.mIsIPv6 ? 1 : 0;
    }

    public byte[] getSaddr() {
        return this.mSaddr;
    }

    public byte[] getDaddr() {
        return this.mDaddr;
    }

    public int getSport() {
        return this.mSport;
    }

    public int getDport() {
        return this.mDport;
    }

    public int getIsAllowDynamicCycle() {
        return this.mIsAllowDynamicCycle ? 1 : 0;
    }

    public int getCycle() {
        return this.mCycle;
    }

    public int getMaxCycle() {
        return this.mMaxCycle;
    }

    public int getStepCycle() {
        return this.mStepCycle;
    }

    public int getStepCycleSuccessNum() {
        return this.mStepCycleSuccessNum;
    }

    public int getTcpRetries2() {
        return this.mTcpRetries2;
    }

    public byte[] getSendPayload() {
        return this.mSend;
    }

    public byte[] getReplyPayload() {
        return this.mReply;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeBoolean(this.mIsIPv6);
        out.writeInt(this.mSaddr.length);
        out.writeByteArray(this.mSaddr);
        out.writeInt(this.mDaddr.length);
        out.writeByteArray(this.mDaddr);
        out.writeInt(this.mSport);
        out.writeInt(this.mDport);
        out.writeInt(this.mSend.length);
        out.writeByteArray(this.mSend);
        out.writeInt(this.mReply.length);
        out.writeByteArray(this.mReply);
        out.writeBoolean(this.mIsAllowDynamicCycle);
        out.writeInt(this.mCycle);
        out.writeInt(this.mMaxCycle);
        out.writeInt(this.mStepCycle);
        out.writeInt(this.mStepCycleSuccessNum);
        out.writeInt(this.mTcpRetries2);
    }
}
