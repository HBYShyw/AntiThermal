package com.android.server.backup.transport;

import android.content.ComponentName;
import android.util.AndroidException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TransportNotRegisteredException extends AndroidException {
    public TransportNotRegisteredException(String str) {
        super("Transport " + str + " not registered");
    }

    public TransportNotRegisteredException(ComponentName componentName) {
        super("Transport for host " + componentName + " not registered");
    }
}
