package android.app;

import android.common.OplusFeatureCache;
import android.content.Intent;
import android.provider.oplus.Telephony;
import com.oplus.permission.IOplusPermissionCheckInjector;

/* loaded from: classes.dex */
public class InstrumentationExtImpl implements IInstrumentationExt {
    public InstrumentationExtImpl(Object obj) {
    }

    public boolean beginHookExecStartActivity(Intent intent, int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission(intent, pid, uid, Telephony.BaseMmsColumns.START);
    }
}
