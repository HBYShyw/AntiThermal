package com.android.server.people.prediction;

import android.app.prediction.AppPredictionContext;
import android.app.prediction.AppPredictionManager;
import android.app.prediction.AppPredictor;
import android.app.prediction.AppTarget;
import android.app.prediction.AppTargetEvent;
import android.app.prediction.AppTargetId;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.people.data.ConversationInfo;
import com.android.server.people.data.DataManager;
import com.android.server.people.data.EventHistory;
import com.android.server.people.data.PackageData;
import com.android.server.people.prediction.ShareTargetPredictor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class ShareTargetPredictor extends AppTargetPredictor {
    private static final String REMOTE_APP_PREDICTOR_KEY = "remote_app_predictor";
    private final IntentFilter mIntentFilter;
    private final AppPredictor mRemoteAppPredictor;
    private static final String TAG = "ShareTargetPredictor";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShareTargetPredictor(AppPredictionContext appPredictionContext, Consumer<List<AppTarget>> consumer, DataManager dataManager, int i, Context context) {
        super(appPredictionContext, consumer, dataManager, i);
        this.mIntentFilter = (IntentFilter) appPredictionContext.getExtras().getParcelable("intent_filter", IntentFilter.class);
        if (DeviceConfig.getBoolean("systemui", "dark_launch_remote_prediction_service_enabled", false)) {
            appPredictionContext.getExtras().putBoolean(REMOTE_APP_PREDICTOR_KEY, true);
            this.mRemoteAppPredictor = ((AppPredictionManager) context.createContextAsUser(UserHandle.of(i), 0).getSystemService(AppPredictionManager.class)).createAppPredictionSession(appPredictionContext);
        } else {
            this.mRemoteAppPredictor = null;
        }
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    /* renamed from: reportAppTargetEvent */
    void lambda$onAppTargetEvent$0(AppTargetEvent appTargetEvent) {
        if (DEBUG) {
            Slog.d(TAG, "reportAppTargetEvent");
        }
        if (this.mIntentFilter != null) {
            getDataManager().reportShareTargetEvent(appTargetEvent, this.mIntentFilter);
        }
        AppPredictor appPredictor = this.mRemoteAppPredictor;
        if (appPredictor != null) {
            appPredictor.notifyAppTargetEvent(appTargetEvent);
        }
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    void predictTargets() {
        if (DEBUG) {
            Slog.d(TAG, "predictTargets");
        }
        if (this.mIntentFilter == null) {
            updatePredictions(List.of());
            return;
        }
        List<ShareTarget> directShareTargets = getDirectShareTargets();
        SharesheetModelScorer.computeScore(directShareTargets, getShareEventType(this.mIntentFilter), System.currentTimeMillis());
        Collections.sort(directShareTargets, Comparator.comparing(new Function() { // from class: com.android.server.people.prediction.ShareTargetPredictor$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Float.valueOf(((ShareTargetPredictor.ShareTarget) obj).getScore());
            }
        }, Collections.reverseOrder()).thenComparing(new Function() { // from class: com.android.server.people.prediction.ShareTargetPredictor$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$predictTargets$0;
                lambda$predictTargets$0 = ShareTargetPredictor.lambda$predictTargets$0((ShareTargetPredictor.ShareTarget) obj);
                return lambda$predictTargets$0;
            }
        }));
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < Math.min(getPredictionContext().getPredictedTargetCount(), directShareTargets.size()); i++) {
            arrayList.add(directShareTargets.get(i).getAppTarget());
        }
        updatePredictions(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$predictTargets$0(ShareTarget shareTarget) {
        return Integer.valueOf(shareTarget.getAppTarget().getRank());
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    /* renamed from: sortTargets */
    void lambda$onSortAppTargets$1(List<AppTarget> list, Consumer<List<AppTarget>> consumer) {
        if (DEBUG) {
            Slog.d(TAG, "sortTargets");
        }
        if (this.mIntentFilter == null) {
            consumer.accept(list);
            return;
        }
        List<ShareTarget> appShareTargets = getAppShareTargets(list);
        SharesheetModelScorer.computeScoreForAppShare(appShareTargets, getShareEventType(this.mIntentFilter), getPredictionContext().getPredictedTargetCount(), System.currentTimeMillis(), getDataManager(), this.mCallingUserId);
        Collections.sort(appShareTargets, new Comparator() { // from class: com.android.server.people.prediction.ShareTargetPredictor$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$sortTargets$1;
                lambda$sortTargets$1 = ShareTargetPredictor.lambda$sortTargets$1((ShareTargetPredictor.ShareTarget) obj, (ShareTargetPredictor.ShareTarget) obj2);
                return lambda$sortTargets$1;
            }
        });
        ArrayList arrayList = new ArrayList();
        for (ShareTarget shareTarget : appShareTargets) {
            AppTarget appTarget = shareTarget.getAppTarget();
            arrayList.add(new AppTarget.Builder(appTarget.getId(), appTarget.getPackageName(), appTarget.getUser()).setClassName(appTarget.getClassName()).setRank(shareTarget.getScore() > 0.0f ? (int) (shareTarget.getScore() * 1000.0f) : 0).build());
        }
        consumer.accept(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sortTargets$1(ShareTarget shareTarget, ShareTarget shareTarget2) {
        return -Float.compare(shareTarget.getScore(), shareTarget2.getScore());
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    void destroy() {
        AppPredictor appPredictor = this.mRemoteAppPredictor;
        if (appPredictor != null) {
            appPredictor.destroy();
        }
    }

    private List<ShareTarget> getDirectShareTargets() {
        ConversationInfo conversationInfo;
        ArrayList arrayList = new ArrayList();
        for (ShortcutManager.ShareShortcutInfo shareShortcutInfo : getDataManager().getShareShortcuts(this.mIntentFilter, this.mCallingUserId)) {
            ShortcutInfo shortcutInfo = shareShortcutInfo.getShortcutInfo();
            AppTarget build = new AppTarget.Builder(new AppTargetId(shortcutInfo.getId()), shortcutInfo).setClassName(shareShortcutInfo.getTargetComponent().getClassName()).setRank(shortcutInfo.getRank()).build();
            PackageData packageData = getDataManager().getPackage(shortcutInfo.getPackage(), shortcutInfo.getUserId());
            EventHistory eventHistory = null;
            if (packageData != null) {
                String id = shortcutInfo.getId();
                conversationInfo = packageData.getConversationInfo(id);
                if (conversationInfo != null) {
                    eventHistory = packageData.getEventHistory(id);
                }
            } else {
                conversationInfo = null;
            }
            arrayList.add(new ShareTarget(build, eventHistory, conversationInfo));
        }
        return arrayList;
    }

    private List<ShareTarget> getAppShareTargets(List<AppTarget> list) {
        ArrayList arrayList = new ArrayList();
        for (AppTarget appTarget : list) {
            PackageData packageData = getDataManager().getPackage(appTarget.getPackageName(), appTarget.getUser().getIdentifier());
            arrayList.add(new ShareTarget(appTarget, packageData == null ? null : packageData.getClassLevelEventHistory(appTarget.getClassName()), null));
        }
        return arrayList;
    }

    private int getShareEventType(IntentFilter intentFilter) {
        return getDataManager().mimeTypeToShareEventType(intentFilter != null ? intentFilter.getDataType(0) : null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ShareTarget {
        private final AppTarget mAppTarget;
        private final ConversationInfo mConversationInfo;
        private final EventHistory mEventHistory;
        private float mScore = 0.0f;

        @VisibleForTesting
        ShareTarget(AppTarget appTarget, EventHistory eventHistory, ConversationInfo conversationInfo) {
            this.mAppTarget = appTarget;
            this.mEventHistory = eventHistory;
            this.mConversationInfo = conversationInfo;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @VisibleForTesting
        public AppTarget getAppTarget() {
            return this.mAppTarget;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @VisibleForTesting
        public EventHistory getEventHistory() {
            return this.mEventHistory;
        }

        @VisibleForTesting
        ConversationInfo getConversationInfo() {
            return this.mConversationInfo;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @VisibleForTesting
        public float getScore() {
            return this.mScore;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @VisibleForTesting
        public void setScore(float f) {
            this.mScore = f;
        }
    }
}
