package com.oplus.oms.split.splitload;

import com.oplus.oms.split.splitload.extension.ContentProviderProxy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public abstract class SplitContentProvider extends ContentProviderProxy {
    @Override // com.oplus.oms.split.splitload.extension.ContentProviderProxy
    protected boolean checkRealContentProviderInstallStatus(String splitName) {
        if (getRealContentProvider() != null) {
            return true;
        }
        if (!SplitLoadManagerImpl.hasInstance()) {
            return false;
        }
        SplitLoadManager loadManager = SplitLoadManagerImpl.getInstance();
        List<String> splitNames = new ArrayList<>();
        splitNames.add(splitName);
        loadManager.preloadInstalledSplits(splitNames);
        return getRealContentProvider() != null;
    }
}
