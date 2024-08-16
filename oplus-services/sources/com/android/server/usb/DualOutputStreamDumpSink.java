package com.android.server.usb;

import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.utils.EventLogger;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class DualOutputStreamDumpSink implements EventLogger.DumpSink {
    private final DualDumpOutputStream mDumpOutputStream;
    private final long mId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DualOutputStreamDumpSink(DualDumpOutputStream dualDumpOutputStream, long j) {
        this.mDumpOutputStream = dualDumpOutputStream;
        this.mId = j;
    }

    @Override // com.android.server.utils.EventLogger.DumpSink
    public void sink(String str, List<EventLogger.Event> list) {
        this.mDumpOutputStream.write("USB Event Log", this.mId, str);
        Iterator<EventLogger.Event> it = list.iterator();
        while (it.hasNext()) {
            this.mDumpOutputStream.write("USB Event", this.mId, it.next().toString());
        }
    }
}
