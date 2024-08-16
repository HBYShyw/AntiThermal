package com.android.server.ambientcontext;

import android.app.ambientcontext.AmbientContextEvent;
import android.app.ambientcontext.AmbientContextEventRequest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallback;
import android.service.wearable.IWearableSensingService;
import android.util.Slog;
import com.android.internal.infra.ServiceConnector;
import java.io.PrintWriter;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class RemoteWearableSensingService extends ServiceConnector.Impl<IWearableSensingService> implements RemoteAmbientDetectionService {
    private static final String TAG = RemoteWearableSensingService.class.getSimpleName();

    protected long getAutoDisconnectTimeoutMs() {
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteWearableSensingService(Context context, ComponentName componentName, int i) {
        super(context, new Intent("android.service.wearable.WearableSensingService").setComponent(componentName), 67112960, i, new Function() { // from class: com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return IWearableSensingService.Stub.asInterface((IBinder) obj);
            }
        });
        connect();
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void startDetection(final AmbientContextEventRequest ambientContextEventRequest, final String str, final RemoteCallback remoteCallback, final RemoteCallback remoteCallback2) {
        Slog.i(TAG, "Start detection for " + ambientContextEventRequest.getEventTypes());
        post(new ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda1
            public final void runNoResult(Object obj) {
                ((IWearableSensingService) obj).startDetection(ambientContextEventRequest, str, remoteCallback, remoteCallback2);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void stopDetection(final String str) {
        Slog.i(TAG, "Stop detection for " + str);
        post(new ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda0
            public final void runNoResult(Object obj) {
                ((IWearableSensingService) obj).stopDetection(str);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void queryServiceStatus(@AmbientContextEvent.EventCode final int[] iArr, final String str, final RemoteCallback remoteCallback) {
        Slog.i(TAG, "Query status for " + str);
        post(new ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda2
            public final void runNoResult(Object obj) {
                ((IWearableSensingService) obj).queryServiceStatus(iArr, str, remoteCallback);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void dump(String str, PrintWriter printWriter) {
        super.dump(str, printWriter);
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void unbind() {
        super.unbind();
    }
}
