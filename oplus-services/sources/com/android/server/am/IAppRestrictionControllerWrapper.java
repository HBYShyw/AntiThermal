package com.android.server.am;

import com.android.server.am.AppRestrictionController;
import com.android.server.am.IAppRestrictionControllerExt;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IAppRestrictionControllerWrapper {
    default AppRestrictionController.Injector getInjector() {
        return null;
    }

    default Object getSettingsLock() {
        return null;
    }

    default IAppRestrictionControllerExt.IStaticExt getStaticExtImpl() {
        return new IAppRestrictionControllerExt.IStaticExt() { // from class: com.android.server.am.IAppRestrictionControllerWrapper.1
        };
    }
}
