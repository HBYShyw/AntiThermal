package com.android.server.companion.datatransfer.contextsync;

import android.companion.AssociationInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class CrossDeviceSyncControllerCallback {
    static final int TYPE_CONNECTION_SERVICE = 1;
    static final int TYPE_IN_CALL_SERVICE = 2;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface Type {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanUpCallIds(Set<String> set) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void processContextSyncMessage(int i, CallMetadataSyncData callMetadataSyncData) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestCrossDeviceSync(AssociationInfo associationInfo) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateNumberOfActiveSyncAssociations(int i, boolean z) {
    }
}
