package android.content.pm;

/* loaded from: classes.dex */
public class PackageUserStateExtImpl implements IPackageUserStateExt {
    private int oplusFreezeFlag;
    private int oplusFreezeState;
    public boolean pendingDataMig;

    public PackageUserStateExtImpl(Object base) {
    }

    public void setPendingDataMig(boolean pendingDataMig) {
        this.pendingDataMig = pendingDataMig;
    }

    public boolean isPendingDataMig() {
        return this.pendingDataMig;
    }

    public int getFreezeState() {
        return this.oplusFreezeState;
    }

    public int getFreezeFlag() {
        return this.oplusFreezeFlag;
    }

    public void setFreezeState(int oplusFreezeState) {
        this.oplusFreezeState = oplusFreezeState;
    }

    public void setFreezeFlag(int oplusFreezeFlag) {
        this.oplusFreezeFlag = oplusFreezeFlag;
    }

    public void setExtraData(IPackageUserStateExt mExt) {
        this.oplusFreezeState = mExt.getFreezeState();
        this.oplusFreezeFlag = mExt.getFreezeFlag();
    }

    public boolean ignorePackageDisabledInIsEnabled(int enabled, long flags) {
        if (enabled != 2 || this.oplusFreezeState != 2 || (1073741824 & flags) == 0) {
            return false;
        }
        return true;
    }
}
