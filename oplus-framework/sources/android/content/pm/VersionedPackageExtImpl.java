package android.content.pm;

/* loaded from: classes.dex */
public class VersionedPackageExtImpl implements IVersionedPackageExt {
    private VersionedPackage mVersionedPackage;
    private int mFlag = 0;
    private int mCallUid = -1;
    private int mCallPid = -1;

    public VersionedPackageExtImpl(Object base) {
        this.mVersionedPackage = (VersionedPackage) base;
    }

    public void setCallInfo(int callUid, int callPid) {
        this.mCallUid = callUid;
        this.mCallPid = callPid;
    }

    public int getCallUid() {
        return this.mCallUid;
    }

    public int getCallPid() {
        return this.mCallPid;
    }

    public void setDeleteFlag(int flag) {
        this.mFlag = flag;
    }

    public int getDeleteFlag() {
        return this.mFlag;
    }
}
