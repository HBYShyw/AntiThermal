package com.oplus.sceneservice.sdk.sceneprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Keep;
import com.oplus.sceneservice.sdk.sceneprovider.SceneBroadcastReceiver;
import j9.SceneDataHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import l9.LogUtils;

@Keep
/* loaded from: classes2.dex */
public class SceneBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_POLICY_SCENE = "oplus.intent.action.POLICY_SCENE";
    private static final String TAG = "BaseSceneBroadcastReceiver";
    private static ExecutorService sSingleThread = Executors.newSingleThreadExecutor();

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onReceive$0(Intent intent) {
        try {
            SceneDataHandler.a(intent);
        } catch (Exception e10) {
            LogUtils.b(TAG, "parseSceneIntent error" + e10);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, final Intent intent) {
        if (intent == null) {
            LogUtils.b(TAG, "onReceive intent is null");
            return;
        }
        String action = intent.getAction();
        LogUtils.a(TAG, "onReceive intent action " + action);
        if (ACTION_POLICY_SCENE.equals(action)) {
            sSingleThread.execute(new Runnable() { // from class: j9.a
                @Override // java.lang.Runnable
                public final void run() {
                    SceneBroadcastReceiver.lambda$onReceive$0(intent);
                }
            });
        }
    }
}
