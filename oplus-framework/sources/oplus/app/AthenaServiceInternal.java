package oplus.app;

import android.os.Bundle;

/* loaded from: classes.dex */
public abstract class AthenaServiceInternal {
    public abstract void notifyDateChanged();

    public abstract void notifyFgServiceStatusChanged(Bundle bundle);

    public abstract void notifyForegroundActivitiesChanged(int i, int i2, boolean z);

    public abstract void notifyPackageStatusChanged(Bundle bundle);

    public abstract void notifyPipStatusChanged(Bundle bundle);

    public abstract void notifyProcessDied(int i, int i2);

    public abstract void notifyScreenStatusChanged(boolean z);

    public abstract void notifyTaskRemoved(int i);

    public abstract void notifyTopAppChanged(Bundle bundle);

    public abstract void notifyUidActive(int i);

    public abstract void notifyUidGone(int i, boolean z);

    public abstract void notifyUidIdle(int i, boolean z);

    public abstract void notifyWallPaperChanged(Bundle bundle);

    public abstract boolean transact(int i, Bundle bundle, Bundle bundle2, int i2) throws Exception;
}
