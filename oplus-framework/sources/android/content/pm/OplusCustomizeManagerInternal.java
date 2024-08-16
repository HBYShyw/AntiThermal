package android.content.pm;

import java.util.List;

/* loaded from: classes.dex */
public abstract class OplusCustomizeManagerInternal {
    public abstract List<String> getInstallSourceList();

    public abstract boolean isAppInstallAllowed(String str);

    public abstract boolean isInstallSourceEnable();

    public abstract void sendBroadcastForDisallowStore();
}
