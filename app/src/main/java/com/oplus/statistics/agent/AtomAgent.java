package com.oplus.statistics.agent;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.oplus.statistics.data.CommonBean;
import com.oplus.statistics.data.TrackEvent;
import com.oplus.statistics.util.ApkInfoUtil;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;

/* loaded from: classes2.dex */
public class AtomAgent {
    private static final String APP_ID = "appId";
    private static final String APP_PACKAGE = "appPackage";
    private static final Uri ATOM_DELEGATE = Uri.parse("content://com.oplus.atom.db_sys/atom_delegate");
    private static final String EVENT_ID = "eventID";
    private static final String LOG_MAP = "logMap";
    private static final String LOG_TAG = "logTag";
    private static final String TAG = "AtomAgent";

    private static void addTaskForAtom(final Context context, final TrackEvent trackEvent) {
        if (trackEvent != null && context != null) {
            CommonBean commonBean = (CommonBean) trackEvent;
            ContentValues contentValues = new ContentValues();
            contentValues.put(APP_ID, Integer.valueOf(commonBean.getAppID()));
            contentValues.put(APP_PACKAGE, ApkInfoUtil.getPackageName(context));
            contentValues.put(LOG_TAG, commonBean.getLogTag());
            contentValues.put(EVENT_ID, commonBean.getEventID());
            contentValues.put(LOG_MAP, commonBean.getLogMap());
            try {
                context.getContentResolver().insert(ATOM_DELEGATE, contentValues);
                return;
            } catch (Exception e10) {
                e10.printStackTrace();
                return;
            }
        }
        LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.agent.b
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$addTaskForAtom$0;
                lambda$addTaskForAtom$0 = AtomAgent.lambda$addTaskForAtom$0(TrackEvent.this, context);
                return lambda$addTaskForAtom$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$addTaskForAtom$0(TrackEvent trackEvent, Context context) {
        return "AtomAgent add Task error -- bean or context is null--" + trackEvent + "," + context;
    }

    public static void recordAtomCommon(Context context, CommonBean commonBean) {
        addTaskForAtom(context, commonBean);
    }
}
