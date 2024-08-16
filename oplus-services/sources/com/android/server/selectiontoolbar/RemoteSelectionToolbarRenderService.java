package com.android.server.selectiontoolbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.service.selectiontoolbar.ISelectionToolbarRenderService;
import android.util.Slog;
import android.view.selectiontoolbar.ISelectionToolbarCallback;
import android.view.selectiontoolbar.ShowInfo;
import com.android.internal.infra.ServiceConnector;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class RemoteSelectionToolbarRenderService extends ServiceConnector.Impl<ISelectionToolbarRenderService> {
    private static final String TAG = "RemoteSelectionToolbarRenderService";
    private static final long TIMEOUT_IDLE_UNBIND_MS = 0;
    private final ComponentName mComponentName;
    private final IBinder mRemoteCallback;

    protected long getAutoDisconnectTimeoutMs() {
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteSelectionToolbarRenderService(Context context, ComponentName componentName, int i, IBinder iBinder) {
        super(context, new Intent("android.service.selectiontoolbar.SelectionToolbarRenderService").setComponent(componentName), 0, i, new Function() { // from class: com.android.server.selectiontoolbar.RemoteSelectionToolbarRenderService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ISelectionToolbarRenderService.Stub.asInterface((IBinder) obj);
            }
        });
        this.mComponentName = componentName;
        this.mRemoteCallback = iBinder;
        connect();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(ISelectionToolbarRenderService iSelectionToolbarRenderService, boolean z) {
        if (z) {
            try {
                iSelectionToolbarRenderService.onConnected(this.mRemoteCallback);
            } catch (Exception e) {
                Slog.w(TAG, "Exception calling onConnected().", e);
            }
        }
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public void onShow(final int i, final ShowInfo showInfo, final ISelectionToolbarCallback iSelectionToolbarCallback) {
        run(new ServiceConnector.VoidJob() { // from class: com.android.server.selectiontoolbar.RemoteSelectionToolbarRenderService$$ExternalSyntheticLambda2
            public final void runNoResult(Object obj) {
                ((ISelectionToolbarRenderService) obj).onShow(i, showInfo, iSelectionToolbarCallback);
            }
        });
    }

    public void onHide(final long j) {
        run(new ServiceConnector.VoidJob() { // from class: com.android.server.selectiontoolbar.RemoteSelectionToolbarRenderService$$ExternalSyntheticLambda3
            public final void runNoResult(Object obj) {
                ((ISelectionToolbarRenderService) obj).onHide(j);
            }
        });
    }

    public void onDismiss(final int i, final long j) {
        run(new ServiceConnector.VoidJob() { // from class: com.android.server.selectiontoolbar.RemoteSelectionToolbarRenderService$$ExternalSyntheticLambda1
            public final void runNoResult(Object obj) {
                ((ISelectionToolbarRenderService) obj).onDismiss(i, j);
            }
        });
    }
}
