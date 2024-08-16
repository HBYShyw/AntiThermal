package com.android.server.job.controllers.idle;

import android.content.Context;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IdlenessTracker {
    void dump(ProtoOutputStream protoOutputStream, long j);

    void dump(PrintWriter printWriter);

    boolean isIdle();

    void startTracking(Context context, IdlenessListener idlenessListener);
}
