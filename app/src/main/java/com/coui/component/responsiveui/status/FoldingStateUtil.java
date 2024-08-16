package com.coui.component.responsiveui.status;

import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import android.util.Log;
import kotlin.Metadata;
import za.k;

/* compiled from: FoldingStateUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u0002H\u0007¨\u0006\r"}, d2 = {"Lcom/coui/component/responsiveui/status/FoldingStateUtil;", "", "Landroid/content/Context;", "context", "Landroid/database/ContentObserver;", "observer", "Lma/f0;", "registerFoldingStateObserver", "unregisterFoldingStateObserver", "Lcom/coui/component/responsiveui/status/FoldingState;", "getFoldingState", "<init>", "()V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class FoldingStateUtil {
    public static final FoldingStateUtil INSTANCE = new FoldingStateUtil();

    private FoldingStateUtil() {
    }

    public static final FoldingState getFoldingState(Context context) {
        FoldingState foldingState;
        k.e(context, "context");
        int i10 = Settings.Global.getInt(context.getContentResolver(), "oplus_system_folding_mode", -1);
        if (i10 == 0) {
            foldingState = FoldingState.FOLD;
        } else if (i10 == 1) {
            foldingState = FoldingState.UNFOLD;
        } else {
            foldingState = FoldingState.UNKNOWN;
        }
        Log.d("FoldingStateUtil", k.l("[getFoldingState]: ", foldingState));
        return foldingState;
    }

    public static final void registerFoldingStateObserver(Context context, ContentObserver contentObserver) {
        k.e(context, "context");
        k.e(contentObserver, "observer");
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("oplus_system_folding_mode"), false, contentObserver);
    }

    public static final void unregisterFoldingStateObserver(Context context, ContentObserver contentObserver) {
        k.e(context, "context");
        k.e(contentObserver, "observer");
        context.getContentResolver().unregisterContentObserver(contentObserver);
    }
}
