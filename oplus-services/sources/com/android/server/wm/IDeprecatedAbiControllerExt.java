package com.android.server.wm;

import android.R;
import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDeprecatedAbiControllerExt {
    default String getDeprecatedAbiDialogMessage(Context context, String str) {
        return context.getResources().getString(R.string.face_icon_content_description);
    }
}
