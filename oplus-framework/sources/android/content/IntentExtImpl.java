package android.content;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class IntentExtImpl implements IIntentExt {
    public static final Parcelable.Creator<IntentExtImpl> CREATOR = new Parcelable.Creator<IntentExtImpl>() { // from class: android.content.IntentExtImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntentExtImpl createFromParcel(Parcel in) {
            return new IntentExtImpl(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntentExtImpl[] newArray(int size) {
            return new IntentExtImpl[size];
        }
    };
    int mCallingUid;
    int mIsForFreeForm;
    int mIsFromGameSpace;
    int mOplusFlags;
    int mOplusUserId;
    private int mPairLaunchWindowingMode;
    int mPid;
    int mStartFromOcar;
    int mUid;

    public IntentExtImpl(Object object) {
        this.mOplusUserId = 0;
        this.mIsFromGameSpace = 0;
        this.mIsForFreeForm = 0;
        this.mPid = -1;
        this.mUid = -1;
        this.mCallingUid = -1;
        this.mStartFromOcar = 0;
    }

    protected IntentExtImpl(Parcel in) {
        this.mOplusUserId = 0;
        this.mIsFromGameSpace = 0;
        this.mIsForFreeForm = 0;
        this.mPid = -1;
        this.mUid = -1;
        this.mCallingUid = -1;
        this.mStartFromOcar = 0;
        this.mOplusUserId = in.readInt();
        this.mIsFromGameSpace = in.readInt();
        this.mIsForFreeForm = in.readInt();
        this.mPid = in.readInt();
        this.mUid = in.readInt();
        this.mCallingUid = in.readInt();
        this.mStartFromOcar = in.readInt();
        this.mOplusFlags = in.readInt();
        this.mPairLaunchWindowingMode = in.readInt();
    }

    public int fillIn(IIntentExt ext, int flags) {
        if (!(ext instanceof IntentExtImpl)) {
            return 0;
        }
        IntentExtImpl other = (IntentExtImpl) ext;
        this.mOplusUserId = other.mOplusUserId;
        this.mIsFromGameSpace = other.mIsFromGameSpace;
        this.mIsForFreeForm = other.mIsForFreeForm;
        this.mOplusFlags |= other.mOplusFlags;
        this.mCallingUid = other.mCallingUid;
        this.mStartFromOcar = other.mStartFromOcar;
        this.mPairLaunchWindowingMode = other.mPairLaunchWindowingMode;
        return 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mOplusUserId);
        out.writeInt(this.mIsFromGameSpace);
        out.writeInt(this.mIsForFreeForm);
        out.writeInt(this.mOplusFlags);
        out.writeInt(this.mCallingUid);
        out.writeInt(this.mStartFromOcar);
        out.writeInt(this.mPairLaunchWindowingMode);
    }

    public void readFromParcel(Parcel in) {
        this.mOplusUserId = in.readInt();
        this.mIsFromGameSpace = in.readInt();
        this.mIsForFreeForm = in.readInt();
        this.mOplusFlags = in.readInt();
        this.mCallingUid = in.readInt();
        this.mStartFromOcar = in.readInt();
        this.mPairLaunchWindowingMode = in.readInt();
    }

    public void readIntentExt(Intent intent) {
        IIntentExt intentExt = intent.getIntentExt();
        this.mOplusUserId = intentExt.getOplusUserId();
        this.mIsFromGameSpace = intentExt.getIsFromGameSpace();
        this.mIsForFreeForm = intentExt.getIsForFreeForm();
        this.mOplusFlags |= intentExt.getOplusFlags();
        this.mCallingUid = intentExt.getCallingUid();
        this.mStartFromOcar = intentExt.getStartFromOcar();
        this.mPairLaunchWindowingMode = intentExt.getPairLaunchWindowingMode();
    }

    public int getOplusUserId() {
        return this.mOplusUserId;
    }

    public void setOplusUserId(int oplusUserId) {
        this.mOplusUserId = oplusUserId;
    }

    public int getIsFromGameSpace() {
        return this.mIsFromGameSpace;
    }

    public void setIsFromGameSpace(int isFromGameSpace) {
        this.mIsFromGameSpace = isFromGameSpace;
    }

    public int getIsForFreeForm() {
        return this.mIsForFreeForm;
    }

    public void setIsForFreeForm(int isForFreeForm) {
        this.mIsForFreeForm = isForFreeForm;
    }

    public int getOplusFlags() {
        return this.mOplusFlags;
    }

    public void setOplusFlags(int oplusFlags) {
        this.mOplusFlags = oplusFlags;
    }

    public void addOplusFlags(int oplusFlags) {
        this.mOplusFlags |= oplusFlags;
    }

    public void removeOplusFlags(int oplusFlags) {
        this.mOplusFlags &= ~oplusFlags;
    }

    public void setPid(int pid) {
        this.mPid = pid;
    }

    public int getPid() {
        return this.mPid;
    }

    public void setUid(int uid) {
        this.mUid = uid;
    }

    public int getUid() {
        return this.mUid;
    }

    public void setCallingUid(int uid) {
        this.mCallingUid = uid;
    }

    public int getCallingUid() {
        return this.mCallingUid;
    }

    public int getStartFromOcar() {
        return this.mStartFromOcar;
    }

    public void setStartFromOcar(int startFromOcar) {
        this.mStartFromOcar = startFromOcar;
    }

    public void setPairLaunchWindowingMode(int windowingMode) {
        this.mPairLaunchWindowingMode = windowingMode;
    }

    public int getPairLaunchWindowingMode() {
        return this.mPairLaunchWindowingMode;
    }

    public String toString() {
        StringBuilder b = new StringBuilder(128);
        if (this.mOplusFlags != 0) {
            b.append(" oflg=0x").append(Integer.toHexString(this.mOplusFlags));
        }
        if (this.mOplusUserId != 0) {
            b.append(" ouserid=").append(this.mOplusUserId);
        }
        if (this.mIsForFreeForm != 0) {
            b.append(" freeform=").append(this.mIsForFreeForm);
        }
        if (this.mIsFromGameSpace != 0) {
            b.append(" gs=").append(this.mIsFromGameSpace);
        }
        if (this.mCallingUid != -1) {
            b.append(" mCallingUid=").append(this.mCallingUid);
        }
        if (this.mStartFromOcar != 0) {
            b.append(" mStartFromOcar=").append(this.mStartFromOcar);
        }
        return b.toString();
    }
}
