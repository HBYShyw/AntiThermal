package com.android.server.companion;

import android.companion.AssociationInfo;
import com.android.server.companion.datatransfer.contextsync.CrossDeviceCall;
import com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback;
import java.util.Collection;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface CompanionDeviceManagerServiceInternal {
    void addSelfOwnedCallId(String str);

    void crossDeviceSync(int i, Collection<CrossDeviceCall> collection);

    void crossDeviceSync(AssociationInfo associationInfo, Collection<CrossDeviceCall> collection);

    void registerCallMetadataSyncCallback(CrossDeviceSyncControllerCallback crossDeviceSyncControllerCallback, int i);

    void removeInactiveSelfManagedAssociations();

    void removeSelfOwnedCallId(String str);

    void sendCrossDeviceSyncMessage(int i, byte[] bArr);

    void sendCrossDeviceSyncMessageToAllDevices(int i, byte[] bArr);
}
