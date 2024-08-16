package android.content.pm;

import android.os.Parcel;

/* loaded from: classes.dex */
public class SessionParamsExtImpl implements ISessionParamsExt {
    private final String TAG = "SessionParamsExtImpl";
    private int extraDexoptFlags = 0;
    private int extraInstallFlags;
    private String extraSessionInfo;

    public SessionParamsExtImpl(Object obj) {
    }

    public void setExtraSessionInfo(String extraSessionInfo) {
        this.extraSessionInfo = extraSessionInfo;
    }

    public String getExtraSessionInfo() {
        return this.extraSessionInfo;
    }

    public void setExtraInstallFlags(int installFlags) {
        this.extraInstallFlags |= installFlags;
    }

    public int getExtraInstallFlags() {
        return this.extraInstallFlags;
    }

    public void setDexoptFlag(int flag) {
        this.extraDexoptFlags |= flag;
    }

    public int getDexoptFlag() {
        return this.extraDexoptFlags;
    }

    public void copyFrom(ISessionParamsExt sessionParamsExt) {
        if (sessionParamsExt != null) {
            this.extraInstallFlags = sessionParamsExt.getExtraInstallFlags();
            this.extraSessionInfo = sessionParamsExt.getExtraSessionInfo();
        }
    }

    public void initFromParcel(Parcel source) {
        this.extraInstallFlags = source.readInt();
        this.extraSessionInfo = source.readString();
        this.extraDexoptFlags = source.readInt();
    }

    public void baseWriteToParcel(Parcel dest) {
        dest.writeInt(this.extraInstallFlags);
        dest.writeString(this.extraSessionInfo);
        dest.writeInt(this.extraDexoptFlags);
    }
}
