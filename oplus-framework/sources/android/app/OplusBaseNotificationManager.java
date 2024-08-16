package android.app;

import android.provider.oplus.Telephony;
import oplus.app.OplusCommonManager;

/* loaded from: classes.dex */
public class OplusBaseNotificationManager extends OplusCommonManager {
    private static final String TAG = "OplusBaseNotificationManager";

    public OplusBaseNotificationManager() {
        super(Telephony.ThreadsColumns.NOTIFICATION);
    }
}
