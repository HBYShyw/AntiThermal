package com.oplus.oms.split.splitinstall;

import java.util.List;

/* loaded from: classes.dex */
public interface SplitInstaller {
    void install(int i, List<SplitVersionInfo> list);

    void skipInstall(int i, SplitInstallInternalSessionState splitInstallInternalSessionState);
}
