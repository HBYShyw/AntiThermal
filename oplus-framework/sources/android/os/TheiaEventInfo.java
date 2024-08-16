package android.os;

import java.util.Objects;

/* loaded from: classes.dex */
public class TheiaEventInfo {
    public String mExtraInfo;
    public TheiaLogInfo mLogInfo;
    public int mPid;
    public long mTheiaID;
    public long mTimeStamp;
    public int mUid;

    public TheiaEventInfo(long theiaID, long timeStamp, int pid, int uid, TheiaLogInfo logInfo, String extraInfo) {
        this.mTheiaID = theiaID;
        this.mTimeStamp = timeStamp;
        this.mPid = pid;
        this.mUid = uid;
        this.mLogInfo = logInfo;
        this.mExtraInfo = extraInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append(toHex(this.mTheiaID));
        sb.append(" ").append(this.mTimeStamp);
        sb.append(" ").append(toHex(this.mLogInfo.getValue()));
        sb.append(" ").append(this.mPid);
        sb.append(" ").append(this.mUid);
        sb.append(" ").append(this.mExtraInfo);
        sb.append("}");
        return sb.toString();
    }

    public long getTheiaID() {
        return this.mTheiaID;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TheiaEventInfo)) {
            return false;
        }
        TheiaEventInfo that = (TheiaEventInfo) object;
        return this.mTheiaID == that.mTheiaID;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mTheiaID));
    }

    private static String toHex(long n) {
        return String.format("0x%s", Long.toHexString(n));
    }
}
