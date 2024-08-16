package android.net.wifi;

import android.net.wifi.IOplusWifiEventCallback;
import android.os.Bundle;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class OplusWifiEventCallbackProxy extends IOplusWifiEventCallback.Stub {
    private OplusWifiEventCallback mCallback;
    private Executor mExecutor;

    public OplusWifiEventCallbackProxy(Executor executor, OplusWifiEventCallback callback) {
        this.mExecutor = executor;
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEvent$0(int event, int action, Bundle extras) {
        this.mCallback.onEvent(event, action, extras);
    }

    @Override // android.net.wifi.IOplusWifiEventCallback
    public void onEvent(final int event, final int action, final Bundle extras) {
        this.mExecutor.execute(new Runnable() { // from class: android.net.wifi.OplusWifiEventCallbackProxy$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OplusWifiEventCallbackProxy.this.lambda$onEvent$0(event, action, extras);
            }
        });
    }
}
