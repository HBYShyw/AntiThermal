package com.android.server.autofill;

import android.service.autofill.FillContext;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IRemoteFillServiceWrapper {
    default void delayCancelRequest(List<FillContext> list) {
    }

    default IRemoteFillServiceExt getRemoteFillServiceExt() {
        return null;
    }
}
