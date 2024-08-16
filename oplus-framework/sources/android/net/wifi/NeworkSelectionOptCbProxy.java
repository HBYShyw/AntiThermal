package android.net.wifi;

import android.net.wifi.INeworkSelectionOptCb;
import java.util.concurrent.Executor;
import oplus.net.wifi.NeworkSelectionOptCb;

/* loaded from: classes.dex */
public class NeworkSelectionOptCbProxy extends INeworkSelectionOptCb.Stub {
    private NeworkSelectionOptCb mCallback;
    private Executor mExecutor;

    public NeworkSelectionOptCbProxy(Executor executor, NeworkSelectionOptCb callback) {
        this.mExecutor = executor;
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyNetworkSelectionStatus$0(int status, String bssId) {
        this.mCallback.notifyNetworkSelectionStatus(status, bssId);
    }

    @Override // android.net.wifi.INeworkSelectionOptCb
    public void notifyNetworkSelectionStatus(final int status, final String bssId) {
        this.mExecutor.execute(new Runnable() { // from class: android.net.wifi.NeworkSelectionOptCbProxy$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NeworkSelectionOptCbProxy.this.lambda$notifyNetworkSelectionStatus$0(status, bssId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyAvaliableBss$1(String[] bssIdList) {
        this.mCallback.notifyAvaliableBss(bssIdList);
    }

    @Override // android.net.wifi.INeworkSelectionOptCb
    public void notifyAvaliableBss(final String[] bssIdList) {
        this.mExecutor.execute(new Runnable() { // from class: android.net.wifi.NeworkSelectionOptCbProxy$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                NeworkSelectionOptCbProxy.this.lambda$notifyAvaliableBss$1(bssIdList);
            }
        });
    }
}
