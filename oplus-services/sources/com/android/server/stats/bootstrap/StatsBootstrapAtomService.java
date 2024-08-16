package com.android.server.stats.bootstrap;

import android.content.Context;
import android.os.IBinder;
import android.os.IStatsBootstrapAtomService;
import android.os.StatsBootstrapAtom;
import android.os.StatsBootstrapAtomValue;
import android.util.Slog;
import android.util.StatsEvent;
import android.util.StatsLog;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class StatsBootstrapAtomService extends IStatsBootstrapAtomService.Stub {
    private static final boolean DEBUG = false;
    private static final String TAG = "StatsBootstrapAtomService";

    public void reportBootstrapAtom(StatsBootstrapAtom statsBootstrapAtom) {
        int i = statsBootstrapAtom.atomId;
        if (i < 1 || i >= 10000) {
            Slog.e(TAG, "Atom ID " + statsBootstrapAtom.atomId + " is not a valid atom ID");
            return;
        }
        StatsEvent.Builder atomId = StatsEvent.newBuilder().setAtomId(statsBootstrapAtom.atomId);
        for (StatsBootstrapAtomValue statsBootstrapAtomValue : statsBootstrapAtom.values) {
            int tag = statsBootstrapAtomValue.getTag();
            if (tag == 0) {
                atomId.writeBoolean(statsBootstrapAtomValue.getBoolValue());
            } else if (tag == 1) {
                atomId.writeInt(statsBootstrapAtomValue.getIntValue());
            } else if (tag == 2) {
                atomId.writeLong(statsBootstrapAtomValue.getLongValue());
            } else if (tag == 3) {
                atomId.writeFloat(statsBootstrapAtomValue.getFloatValue());
            } else if (tag == 4) {
                atomId.writeString(statsBootstrapAtomValue.getStringValue());
            } else {
                if (tag != 5) {
                    Slog.e(TAG, "Unexpected value type " + statsBootstrapAtomValue.getTag() + " when logging atom " + statsBootstrapAtom.atomId);
                    return;
                }
                atomId.writeByteArray(statsBootstrapAtomValue.getBytesValue());
            }
        }
        StatsLog.write(atomId.usePooledBuffer().build());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Lifecycle extends SystemService {
        private StatsBootstrapAtomService mStatsBootstrapAtomService;

        public Lifecycle(Context context) {
            super(context);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.stats.bootstrap.StatsBootstrapAtomService, android.os.IBinder] */
        public void onStart() {
            ?? statsBootstrapAtomService = new StatsBootstrapAtomService();
            this.mStatsBootstrapAtomService = statsBootstrapAtomService;
            try {
                publishBinderService("statsbootstrap", (IBinder) statsBootstrapAtomService);
            } catch (Exception e) {
                Slog.e(StatsBootstrapAtomService.TAG, "Failed to publishBinderService", e);
            }
        }
    }
}
