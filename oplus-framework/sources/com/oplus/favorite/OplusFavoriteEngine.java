package com.oplus.favorite;

import android.app.Activity;
import android.content.Context;
import android.os.SystemProperties;
import android.view.View;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public abstract class OplusFavoriteEngine implements IOplusFavoriteEngine {
    protected final String TAG = getClass().getSimpleName();
    protected static final boolean DBG = IOplusFavoriteManager.DBG;
    private static final boolean ENGINE_DEBUG = SystemProperties.getBoolean("feature.favorite.debug", false);
    private static final boolean ENGINE_TEST = SystemProperties.getBoolean("feature.favorite.test", false);
    private static final boolean ENGINE_LOG = SystemProperties.getBoolean("feature.favorite.log", false);

    protected abstract void onInit();

    protected abstract void onLoadRule(Context context, String str, OplusFavoriteCallback oplusFavoriteCallback);

    protected abstract void onLogActivityInfo(Activity activity);

    protected abstract void onLogViewInfo(View view);

    protected abstract void onProcessClick(View view, OplusFavoriteCallback oplusFavoriteCallback);

    protected abstract void onProcessCrawl(View view, OplusFavoriteCallback oplusFavoriteCallback, String str);

    protected abstract void onProcessSave(View view, OplusFavoriteCallback oplusFavoriteCallback);

    protected abstract void onRelease();

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void init() {
        OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] init()");
        onInit();
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void release() {
        OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] release()");
        onRelease();
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void loadRule(Context context, String data, OplusFavoriteCallback callback) {
        OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] loadRule() : " + context.getPackageName() + "/" + context);
        onLoadRule(context, data, callback);
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void processClick(View clickView, OplusFavoriteCallback callback) {
        OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] processClick() : " + clickView);
        onProcessClick(clickView, callback);
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void processCrawl(View rootView, OplusFavoriteCallback callback, String param) {
        OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] processCrawl() : " + rootView);
        onProcessCrawl(rootView, callback, param);
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void processSave(View rootView, OplusFavoriteCallback callback) {
        OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] processSave() : " + rootView);
        onProcessSave(rootView, callback);
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void logActivityInfo(Activity activity) {
        try {
            OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] logActivityInfo() : " + activity);
        } catch (Exception e) {
            OplusLog.w(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] logActivityInfo() exception e : " + e);
        }
        onLogActivityInfo(activity);
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final void logViewInfo(View view) {
        try {
            OplusLog.i(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] logViewInfo() : " + view);
        } catch (Exception e) {
            OplusLog.w(DBG, IOplusFavoriteConstans.TAG_UNIFY, "[" + this.TAG + "] logViewInfo() exception e : " + e);
        }
        onLogViewInfo(view);
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final boolean isDebugOn() {
        return ENGINE_DEBUG;
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final boolean isTestOn() {
        return ENGINE_TEST;
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public final boolean isLogOn() {
        return ENGINE_LOG;
    }
}
