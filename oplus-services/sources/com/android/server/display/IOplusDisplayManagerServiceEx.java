package com.android.server.display;

import android.common.OplusFeatureList;
import com.android.server.IOplusCommonManagerServiceEx;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusDisplayManagerServiceEx extends IOplusCommonManagerServiceEx {
    public static final IOplusDisplayManagerServiceEx DEFAULT = new IOplusDisplayManagerServiceEx() { // from class: com.android.server.display.IOplusDisplayManagerServiceEx.1
    };
    public static final String NAME = "IOplusDisplayManagerServiceEx";

    default DisplayManagerService getDisplayManagerService() {
        return null;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDisplayManagerServiceEx;
    }

    default IOplusDisplayManagerServiceEx getDefault() {
        return DEFAULT;
    }
}
