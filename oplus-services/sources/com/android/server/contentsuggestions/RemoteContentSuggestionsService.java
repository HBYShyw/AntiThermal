package com.android.server.contentsuggestions;

import android.app.contentsuggestions.ClassificationsRequest;
import android.app.contentsuggestions.IClassificationsCallback;
import android.app.contentsuggestions.ISelectionsCallback;
import android.app.contentsuggestions.SelectionsRequest;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.HardwareBuffer;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.service.contentsuggestions.IContentSuggestionsService;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RemoteContentSuggestionsService extends AbstractMultiplePendingRequestsRemoteService<RemoteContentSuggestionsService, IContentSuggestionsService> {
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface Callbacks extends AbstractRemoteService.VultureCallback<RemoteContentSuggestionsService> {
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    protected long getTimeoutIdleBindMillis() {
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteContentSuggestionsService(Context context, ComponentName componentName, int i, Callbacks callbacks, boolean z, boolean z2) {
        super(context, "android.service.contentsuggestions.ContentSuggestionsService", componentName, i, callbacks, context.getMainThreadHandler(), z ? AudioDevice.OUT_SPEAKER_SAFE : 0, z2, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IContentSuggestionsService getServiceInterface(IBinder iBinder) {
        return IContentSuggestionsService.Stub.asInterface(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void provideContextImage(final int i, final HardwareBuffer hardwareBuffer, final int i2, final Bundle bundle) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda3
            public final void run(IInterface iInterface) {
                ((IContentSuggestionsService) iInterface).provideContextImage(i, hardwareBuffer, i2, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void suggestContentSelections(final SelectionsRequest selectionsRequest, final ISelectionsCallback iSelectionsCallback) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda2
            public final void run(IInterface iInterface) {
                ((IContentSuggestionsService) iInterface).suggestContentSelections(selectionsRequest, iSelectionsCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void classifyContentSelections(final ClassificationsRequest classificationsRequest, final IClassificationsCallback iClassificationsCallback) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda0
            public final void run(IInterface iInterface) {
                ((IContentSuggestionsService) iInterface).classifyContentSelections(classificationsRequest, iClassificationsCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyInteraction(final String str, final Bundle bundle) {
        scheduleAsyncRequest(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda1
            public final void run(IInterface iInterface) {
                ((IContentSuggestionsService) iInterface).notifyInteraction(str, bundle);
            }
        });
    }
}
