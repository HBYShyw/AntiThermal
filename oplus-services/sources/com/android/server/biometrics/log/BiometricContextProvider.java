package com.android.server.biometrics.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.biometrics.IBiometricContextListener;
import android.hardware.biometrics.common.OperationContext;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.internal.statusbar.ISessionListener;
import com.android.internal.statusbar.IStatusBarService;
import com.android.server.biometrics.sensors.AuthSessionCoordinator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BiometricContextProvider implements BiometricContext {
    private static final int SESSION_TYPES = 3;
    private static final String TAG = "BiometricContextProvider";
    private static BiometricContextProvider sInstance;
    private final AuthSessionCoordinator mAuthSessionCoordinator;
    private final Handler mHandler;
    private final WindowManager mWindowManager;
    private final Map<OperationContextExt, Consumer<OperationContext>> mSubscribers = new ConcurrentHashMap();
    private final Map<Integer, BiometricContextSessionInfo> mSession = new ConcurrentHashMap();
    private int mDockState = 0;
    private int mFoldState = 0;
    private int mDisplayState = 0;

    @VisibleForTesting
    final BroadcastReceiver mDockStateReceiver = new BroadcastReceiver() { // from class: com.android.server.biometrics.log.BiometricContextProvider.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BiometricContextProvider.this.mDockState = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BiometricContextProvider defaultProvider(Context context) {
        synchronized (BiometricContextProvider.class) {
            if (sInstance == null) {
                try {
                    sInstance = new BiometricContextProvider(context, (WindowManager) context.getSystemService("window"), IStatusBarService.Stub.asInterface(ServiceManager.getServiceOrThrow("statusbar")), null, new AuthSessionCoordinator());
                } catch (ServiceManager.ServiceNotFoundException e) {
                    throw new IllegalStateException("Failed to find required service", e);
                }
            }
        }
        return sInstance;
    }

    @VisibleForTesting
    public BiometricContextProvider(Context context, WindowManager windowManager, IStatusBarService iStatusBarService, Handler handler, AuthSessionCoordinator authSessionCoordinator) {
        this.mWindowManager = windowManager;
        this.mAuthSessionCoordinator = authSessionCoordinator;
        this.mHandler = handler;
        subscribeBiometricContextListener(iStatusBarService);
        subscribeDockState(context);
    }

    private void subscribeBiometricContextListener(IStatusBarService iStatusBarService) {
        try {
            iStatusBarService.setBiometicContextListener(new IBiometricContextListener.Stub() { // from class: com.android.server.biometrics.log.BiometricContextProvider.2
                public void onFoldChanged(int i) {
                    BiometricContextProvider.this.mFoldState = i;
                }

                public void onDisplayStateChanged(int i) {
                    if (i != BiometricContextProvider.this.mDisplayState) {
                        BiometricContextProvider.this.mDisplayState = i;
                        BiometricContextProvider.this.notifyChanged();
                    }
                }
            });
            iStatusBarService.registerSessionListener(3, new ISessionListener.Stub() { // from class: com.android.server.biometrics.log.BiometricContextProvider.3
                public void onSessionStarted(int i, InstanceId instanceId) {
                    BiometricContextProvider.this.mSession.put(Integer.valueOf(i), new BiometricContextSessionInfo(instanceId));
                }

                public void onSessionEnded(int i, InstanceId instanceId) {
                    BiometricContextSessionInfo biometricContextSessionInfo = (BiometricContextSessionInfo) BiometricContextProvider.this.mSession.remove(Integer.valueOf(i));
                    if (biometricContextSessionInfo == null || instanceId == null || biometricContextSessionInfo.getId() == instanceId.getId()) {
                        return;
                    }
                    Slog.w(BiometricContextProvider.TAG, "session id mismatch");
                }
            });
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to register biometric context listener", e);
        }
    }

    private void subscribeDockState(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DOCK_EVENT");
        context.registerReceiver(this.mDockStateReceiver, intentFilter);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public OperationContextExt updateContext(OperationContextExt operationContextExt, boolean z) {
        return operationContextExt.update(this, z);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public BiometricContextSessionInfo getKeyguardEntrySessionInfo() {
        return this.mSession.get(1);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public BiometricContextSessionInfo getBiometricPromptSessionInfo() {
        return this.mSession.get(2);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public boolean isAod() {
        return this.mDisplayState == 4;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public boolean isAwake() {
        int i = this.mDisplayState;
        return i == 0 || i == 1 || i == 3;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public boolean isDisplayOn() {
        return this.mWindowManager.getDefaultDisplay().getState() == 2;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getDockedState() {
        return this.mDockState;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getFoldState() {
        return this.mFoldState;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getCurrentRotation() {
        return this.mWindowManager.getDefaultDisplay().getRotation();
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public int getDisplayState() {
        return this.mDisplayState;
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public void subscribe(OperationContextExt operationContextExt, Consumer<OperationContext> consumer) {
        this.mSubscribers.put(operationContextExt, consumer);
        if (operationContextExt.getDisplayState() != getDisplayState()) {
            consumer.accept(operationContextExt.update(this, operationContextExt.isCrypto()).toAidlContext());
        }
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public void unsubscribe(OperationContextExt operationContextExt) {
        this.mSubscribers.remove(operationContextExt);
    }

    @Override // com.android.server.biometrics.log.BiometricContext
    public AuthSessionCoordinator getAuthSessionCoordinator() {
        return this.mAuthSessionCoordinator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyChanged() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new Runnable() { // from class: com.android.server.biometrics.log.BiometricContextProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BiometricContextProvider.this.notifySubscribers();
                }
            });
        } else {
            notifySubscribers();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySubscribers() {
        this.mSubscribers.forEach(new BiConsumer() { // from class: com.android.server.biometrics.log.BiometricContextProvider$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BiometricContextProvider.this.lambda$notifySubscribers$0((OperationContextExt) obj, (Consumer) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifySubscribers$0(OperationContextExt operationContextExt, Consumer consumer) {
        consumer.accept(operationContextExt.update(this, operationContextExt.isCrypto()).toAidlContext());
    }

    public String toString() {
        return "[keyguard session: " + getKeyguardEntrySessionInfo() + ", bp session: " + getBiometricPromptSessionInfo() + ", displayState: " + getDisplayState() + ", isAwake: " + isAwake() + ", isDisplayOn: " + isDisplayOn() + ", dock: " + getDockedState() + ", rotation: " + getCurrentRotation() + "]";
    }
}
