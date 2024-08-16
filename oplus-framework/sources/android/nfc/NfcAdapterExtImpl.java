package android.nfc;

import android.common.OplusFeatureCache;
import android.os.Binder;
import com.oplus.permission.IOplusPermissionCheckInjector;

/* loaded from: classes.dex */
public class NfcAdapterExtImpl implements INfcAdapterExt {
    public NfcAdapterExtImpl(Object obj) {
    }

    public boolean hookEnable() {
        return !OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.NFC", Binder.getCallingPid(), Binder.getCallingUid(), "enableNfc");
    }

    public boolean hookDisable() {
        return !OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.NFC", Binder.getCallingPid(), Binder.getCallingUid(), "disableNfc");
    }
}
