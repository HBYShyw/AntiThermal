package com.android.server.statusbar;

import android.content.Context;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IStatusBarManagerServiceExt {
    default void collapsePanels() {
    }

    default void expandNotificationsPanel() {
    }

    default void init(Context context) {
    }

    default void maybeClearAllNotifications() {
    }

    default boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        return false;
    }
}
