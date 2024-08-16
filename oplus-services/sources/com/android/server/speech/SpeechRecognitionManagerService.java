package com.android.server.speech;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.UserHandle;
import android.speech.IRecognitionServiceManager;
import android.speech.IRecognitionServiceManagerCallback;
import android.util.Slog;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.FrameworkResourcesServiceNameResolver;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SpeechRecognitionManagerService extends AbstractMasterSystemService<SpeechRecognitionManagerService, SpeechRecognitionManagerServiceImpl> {
    private static final int MAX_TEMP_SERVICE_SUBSTITUTION_DURATION_MS = 60000;
    private static final String TAG = "SpeechRecognitionManagerService";

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 60000;
    }

    public SpeechRecognitionManagerService(Context context) {
        super(context, new FrameworkResourcesServiceNameResolver(context, R.string.config_platformVpnConfirmDialogComponent), null);
    }

    public void onStart() {
        publishBinderService("speech_recognition", new SpeechRecognitionManagerServiceStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_SPEECH_RECOGNITION", TAG);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public SpeechRecognitionManagerServiceImpl newServiceLocked(int i, boolean z) {
        return new SpeechRecognitionManagerServiceImpl(this, this.mLock, i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class SpeechRecognitionManagerServiceStub extends IRecognitionServiceManager.Stub {
        SpeechRecognitionManagerServiceStub() {
        }

        public void createSession(ComponentName componentName, IBinder iBinder, boolean z, IRecognitionServiceManagerCallback iRecognitionServiceManagerCallback) {
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (((AbstractMasterSystemService) SpeechRecognitionManagerService.this).mLock) {
                ((SpeechRecognitionManagerServiceImpl) SpeechRecognitionManagerService.this.getServiceForUserLocked(callingUserId)).createSessionLocked(componentName, iBinder, z, iRecognitionServiceManagerCallback);
            }
        }

        public void setTemporaryComponent(ComponentName componentName) {
            int callingUserId = UserHandle.getCallingUserId();
            if (componentName == null) {
                SpeechRecognitionManagerService.this.resetTemporaryService(callingUserId);
                Slog.i(SpeechRecognitionManagerService.TAG, "Reset temporary service for user " + callingUserId);
                return;
            }
            SpeechRecognitionManagerService.this.setTemporaryService(callingUserId, componentName.flattenToString(), 60000);
            Slog.i(SpeechRecognitionManagerService.TAG, "SpeechRecognition temporarily set to " + componentName + " for 60000ms");
        }
    }
}
