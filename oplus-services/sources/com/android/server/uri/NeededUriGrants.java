package com.android.server.uri;

import android.util.ArraySet;
import android.util.proto.ProtoOutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NeededUriGrants {
    final int flags;
    final String targetPkg;
    final int targetUid;
    final ArraySet<GrantUri> uris = new ArraySet<>();

    public NeededUriGrants(String str, int i, int i2) {
        this.targetPkg = str;
        this.targetUid = i;
        this.flags = i2;
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1138166333441L, this.targetPkg);
        protoOutputStream.write(1120986464258L, this.targetUid);
        protoOutputStream.write(1120986464259L, this.flags);
        int size = this.uris.size();
        for (int i = 0; i < size; i++) {
            this.uris.valueAt(i).dumpDebug(protoOutputStream, 2246267895812L);
        }
        protoOutputStream.end(start);
    }
}
