package android.content.pm.parsing.component;

import android.os.Parcel;

/* loaded from: classes.dex */
public class ParsedMainComponentExtImpl implements IParsedMainComponentExt {
    private int mOplusFlags;

    public ParsedMainComponentExtImpl(Object base) {
    }

    public void init(IParsedMainComponentExt otherExt) {
        this.mOplusFlags = otherExt.getFlags();
    }

    public void init(Parcel in) {
        this.mOplusFlags = in.readInt();
    }

    public void setFlags(int flags) {
        this.mOplusFlags = flags;
    }

    public int getFlags() {
        return this.mOplusFlags;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mOplusFlags);
    }
}
