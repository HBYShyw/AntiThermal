package android.app;

import android.common.OplusFeatureCache;
import com.oplus.darkmode.IOplusDarkModeManager;

/* loaded from: classes.dex */
public class ApplicationExtImpl implements IApplicationExt {
    public ApplicationExtImpl(Object base) {
    }

    public void hookOnCreate(Application application) {
        ((IOplusCommonInjector) OplusFeatureCache.getOrCreate(IOplusCommonInjector.DEFAULT, new Object[0])).onCreateForApplication(application);
    }

    public void hookAttach(Application application) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).initDarkModeStatus(application);
    }
}
