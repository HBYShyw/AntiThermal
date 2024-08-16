package com.oplus.favorite;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import cn.teddymobile.free.anteater.AnteaterClient;
import cn.teddymobile.free.anteater.helper.AnteaterHelper;
import cn.teddymobile.free.anteater.resources.RuleResourcesClient;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OplusEngineTeddy extends OplusFavoriteEngine {
    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onInit() {
        AnteaterClient anteater = AnteaterClient.getInstance();
        anteater.init();
    }

    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onRelease() {
        AnteaterClient anteater = AnteaterClient.getInstance();
        anteater.release();
    }

    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onProcessClick(View clickView, OplusFavoriteCallback callback) {
        startSave(clickView, callback, "click");
    }

    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onProcessCrawl(View rootView, OplusFavoriteCallback callback, String param) {
        if (rootView != null) {
            rootView = rootView.findViewById(R.id.content);
        }
        startCrawl(rootView, callback, param, "display");
    }

    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onProcessSave(View rootView, OplusFavoriteCallback callback) {
        if (rootView != null) {
            rootView = rootView.findViewById(R.id.content);
        }
        startSave(rootView, callback, "display");
    }

    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onLogActivityInfo(Activity activity) {
        AnteaterHelper helper = AnteaterHelper.getInstance();
        helper.logActivityInfo(activity);
    }

    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onLogViewInfo(View view) {
        AnteaterHelper helper = AnteaterHelper.getInstance();
        helper.logViewInfo(view);
    }

    @Override // com.oplus.favorite.OplusFavoriteEngine
    protected void onLoadRule(Context context, String data, OplusFavoriteCallback callback) {
        AnteaterClient anteater = AnteaterClient.getInstance();
        anteater.loadRule(context, data, new AntaterLoadCallback(callback));
    }

    @Override // com.oplus.favorite.IOplusFavoriteEngine
    public Handler getWorkHandler() {
        AnteaterClient anteater = AnteaterClient.getInstance();
        return anteater.getWorkHandler();
    }

    private void startSave(View view, OplusFavoriteCallback callback, String action) {
        AnteaterClient.SaveCallback saveCallback = new AnteaterSaveCallback(callback);
        AnteaterClient anteater = AnteaterClient.getInstance();
        anteater.save(action, view, saveCallback);
    }

    private void startCrawl(View view, OplusFavoriteCallback callback, String param, String action) {
        AnteaterClient.ProcessCallback processCallback = new AnteaterProcessCallback(callback);
        AnteaterClient anteater = AnteaterClient.getInstance();
        anteater.process(action, view, processCallback, param);
    }

    /* loaded from: classes.dex */
    private static class AnteaterCallback {
        final WeakReference<OplusFavoriteCallback> mCallback;

        public AnteaterCallback(OplusFavoriteCallback callback) {
            this.mCallback = new WeakReference<>(callback);
        }

        protected boolean isSettingOn(Context context) {
            OplusFavoriteCallback callback = this.mCallback.get();
            if (callback != null) {
                return callback.isSettingOn(context);
            }
            return false;
        }

        protected Handler onCreateWorkHandler(Context context, String name, int priority) {
            OplusFavoriteCallback callback = this.mCallback.get();
            if (callback != null) {
                return callback.createWorkHandler(context, name, priority);
            }
            return null;
        }

        protected void onHandleFinished(Context context, AnteaterClient.ResultData result) {
            OplusFavoriteCallback callback = this.mCallback.get();
            if (callback != null) {
                OplusFavoriteResult favoriteResult = new OplusFavoriteResult();
                favoriteResult.setData(getQueryList(result));
                favoriteResult.setError(getErrorMessage(result.getError()));
                callback.onFavoriteFinished(context, favoriteResult);
            }
        }

        private ArrayList<OplusFavoriteData> getQueryList(AnteaterClient.ResultData result) {
            ArrayList<OplusFavoriteData> data = new ArrayList<>();
            ArrayList<AnteaterClient.QueryData> queryList = result.getQueryList();
            synchronized (queryList) {
                Iterator<AnteaterClient.QueryData> it = queryList.iterator();
                while (it.hasNext()) {
                    AnteaterClient.QueryData queryData = it.next();
                    OplusFavoriteData d = new OplusFavoriteData();
                    d.setTitle(queryData.getTitle());
                    d.setUrl(queryData.getUrl());
                    d.setContent(queryData.getContent());
                    d.setExtra(queryData.getExtra());
                    data.add(d);
                }
            }
            return data;
        }

        private String getErrorMessage(AnteaterClient.ErrorCode errorCode) {
            switch (AnonymousClass1.$SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode[errorCode.ordinal()]) {
                case 1:
                    return OplusFavoriteResult.ERROR_NO_VIEW;
                case 2:
                    return OplusFavoriteResult.ERROR_NOT_INIT;
                case 3:
                    return OplusFavoriteResult.ERROR_NOT_FOUND;
                case 4:
                    return OplusFavoriteResult.ERROR_UNSUPPORT;
                case 5:
                    return OplusFavoriteResult.ERROR_SETTING_OFF;
                case 6:
                    return OplusFavoriteResult.ERROR_SAVE_FAILED;
                default:
                    return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.favorite.OplusEngineTeddy$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode;

        static {
            int[] iArr = new int[AnteaterClient.ErrorCode.values().length];
            $SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode = iArr;
            try {
                iArr[AnteaterClient.ErrorCode.NO_VIEW.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode[AnteaterClient.ErrorCode.NOT_INIT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode[AnteaterClient.ErrorCode.NOT_FOUND.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode[AnteaterClient.ErrorCode.UNSUPPORT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode[AnteaterClient.ErrorCode.SETTING_OFF.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$cn$teddymobile$free$anteater$AnteaterClient$ErrorCode[AnteaterClient.ErrorCode.SAVE_FAILED.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    /* loaded from: classes.dex */
    private static class AntaterLoadCallback extends AnteaterCallback implements RuleResourcesClient.LoadCallback {
        public AntaterLoadCallback(OplusFavoriteCallback callback) {
            super(callback);
        }

        @Override // cn.teddymobile.free.anteater.resources.RuleResourcesClient.LoadCallback
        public void onLoadResult(Context context, boolean noRule, boolean emptyRule, ArrayList<String> sceneList) {
            OplusFavoriteCallback callback = this.mCallback.get();
            if (callback != null) {
                callback.onLoadFinished(context, noRule, emptyRule, sceneList);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnteaterSaveCallback extends AnteaterCallback implements AnteaterClient.SaveCallback {
        public AnteaterSaveCallback(OplusFavoriteCallback callback) {
            super(callback);
        }

        @Override // cn.teddymobile.free.anteater.AnteaterClient.BaseCallback
        public Handler createWorkHandler(Context context, String name, int priority) {
            return onCreateWorkHandler(context, name, priority);
        }

        @Override // cn.teddymobile.free.anteater.AnteaterClient.BaseCallback
        public boolean isSettingOff(Context context) {
            return !isSettingOn(context);
        }

        @Override // cn.teddymobile.free.anteater.AnteaterClient.SaveCallback
        public void onSaveResult(Context context, AnteaterClient.ResultData result) {
            onHandleFinished(context, result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnteaterProcessCallback extends AnteaterCallback implements AnteaterClient.ProcessCallback {
        public AnteaterProcessCallback(OplusFavoriteCallback callback) {
            super(callback);
        }

        @Override // cn.teddymobile.free.anteater.AnteaterClient.BaseCallback
        public Handler createWorkHandler(Context context, String name, int priority) {
            return onCreateWorkHandler(context, name, priority);
        }

        @Override // cn.teddymobile.free.anteater.AnteaterClient.BaseCallback
        public boolean isSettingOff(Context context) {
            return !isSettingOn(context);
        }

        @Override // cn.teddymobile.free.anteater.AnteaterClient.ProcessCallback
        public void onProcessResult(Context context, AnteaterClient.ResultData result) {
            onHandleFinished(context, result);
        }
    }
}
