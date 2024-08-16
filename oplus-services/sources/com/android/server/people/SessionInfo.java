package com.android.server.people;

import android.app.prediction.AppPredictionContext;
import android.app.prediction.AppTarget;
import android.app.prediction.IPredictionCallback;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.people.data.DataManager;
import com.android.server.people.prediction.AppTargetPredictor;
import java.util.List;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SessionInfo {
    private static final String TAG = "SessionInfo";
    private final AppTargetPredictor mAppTargetPredictor;
    private final RemoteCallbackList<IPredictionCallback> mCallbacks = new RemoteCallbackList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public SessionInfo(AppPredictionContext appPredictionContext, DataManager dataManager, int i, Context context) {
        this.mAppTargetPredictor = AppTargetPredictor.create(appPredictionContext, new Consumer() { // from class: com.android.server.people.SessionInfo$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SessionInfo.this.updatePredictions((List) obj);
            }
        }, dataManager, i, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addCallback(IPredictionCallback iPredictionCallback) {
        this.mCallbacks.register(iPredictionCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeCallback(IPredictionCallback iPredictionCallback) {
        this.mCallbacks.unregister(iPredictionCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppTargetPredictor getPredictor() {
        return this.mAppTargetPredictor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDestroy() {
        this.mCallbacks.kill();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePredictions(List<AppTarget> list) {
        int beginBroadcast = this.mCallbacks.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mCallbacks.getBroadcastItem(i).onResult(new ParceledListSlice(list));
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to calling callback" + e);
            }
        }
        this.mCallbacks.finishBroadcast();
    }
}
