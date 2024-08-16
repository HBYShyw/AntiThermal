package com.android.server.wm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.HardwareBuffer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.displayhash.DisplayHashParams;
import android.service.displayhash.IDisplayHashingService;
import android.util.Size;
import android.util.Slog;
import android.view.MagnificationSpec;
import android.view.displayhash.DisplayHash;
import android.view.displayhash.VerifiedDisplayHash;
import android.window.ScreenCapture;
import com.android.internal.annotations.GuardedBy;
import com.android.server.wm.DisplayHashController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayHashController {
    private static final boolean DEBUG = false;
    private static final String TAG = "WindowManager";
    private final Context mContext;

    @GuardedBy({"mDisplayHashAlgorithmsLock"})
    private Map<String, DisplayHashParams> mDisplayHashAlgorithms;
    private long mLastRequestTimeMs;
    private int mLastRequestUid;

    @GuardedBy({"mServiceConnectionLock"})
    private DisplayHashingServiceConnection mServiceConnection;
    private final Object mServiceConnectionLock = new Object();
    private final Object mDisplayHashAlgorithmsLock = new Object();
    private final float[] mTmpFloat9 = new float[9];
    private final Matrix mTmpMatrix = new Matrix();
    private final RectF mTmpRectF = new RectF();
    private final Object mIntervalBetweenRequestsLock = new Object();

    @GuardedBy({"mDurationBetweenRequestsLock"})
    private int mIntervalBetweenRequestMillis = -1;
    private boolean mDisplayHashThrottlingEnabled = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final byte[] mSalt = UUID.randomUUID().toString().getBytes();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Command {
        void run(IDisplayHashingService iDisplayHashingService) throws RemoteException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayHashController(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getSupportedHashAlgorithms() {
        return (String[]) getDisplayHashAlgorithms().keySet().toArray(new String[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VerifiedDisplayHash verifyDisplayHash(final DisplayHash displayHash) {
        return (VerifiedDisplayHash) new SyncCommand().run(new BiConsumer() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                DisplayHashController.this.lambda$verifyDisplayHash$0(displayHash, (IDisplayHashingService) obj, (RemoteCallback) obj2);
            }
        }).getParcelable("android.service.displayhash.extra.VERIFIED_DISPLAY_HASH");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyDisplayHash$0(DisplayHash displayHash, IDisplayHashingService iDisplayHashingService, RemoteCallback remoteCallback) {
        try {
            iDisplayHashingService.verifyDisplayHash(this.mSalt, displayHash, remoteCallback);
        } catch (RemoteException unused) {
            Slog.e(TAG, "Failed to invoke verifyDisplayHash command");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDisplayHashThrottlingEnabled(boolean z) {
        this.mDisplayHashThrottlingEnabled = z;
    }

    private void generateDisplayHash(final HardwareBuffer hardwareBuffer, final Rect rect, final String str, final RemoteCallback remoteCallback) {
        connectAndRun(new Command() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.DisplayHashController.Command
            public final void run(IDisplayHashingService iDisplayHashingService) {
                DisplayHashController.this.lambda$generateDisplayHash$1(hardwareBuffer, rect, str, remoteCallback, iDisplayHashingService);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateDisplayHash$1(HardwareBuffer hardwareBuffer, Rect rect, String str, RemoteCallback remoteCallback, IDisplayHashingService iDisplayHashingService) throws RemoteException {
        iDisplayHashingService.generateDisplayHash(this.mSalt, hardwareBuffer, rect, str, remoteCallback);
    }

    private boolean allowedToGenerateHash(int i) {
        if (!this.mDisplayHashThrottlingEnabled) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mLastRequestUid != i) {
            this.mLastRequestUid = i;
            this.mLastRequestTimeMs = currentTimeMillis;
            return true;
        }
        if (currentTimeMillis - this.mLastRequestTimeMs < getIntervalBetweenRequestMillis()) {
            return false;
        }
        this.mLastRequestTimeMs = currentTimeMillis;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void generateDisplayHash(ScreenCapture.LayerCaptureArgs.Builder builder, Rect rect, String str, int i, RemoteCallback remoteCallback) {
        if (!allowedToGenerateHash(i)) {
            sendDisplayHashError(remoteCallback, -6);
            return;
        }
        DisplayHashParams displayHashParams = getDisplayHashAlgorithms().get(str);
        if (displayHashParams == null) {
            Slog.w(TAG, "Failed to generateDisplayHash. Invalid hashAlgorithm");
            sendDisplayHashError(remoteCallback, -5);
            return;
        }
        Size bufferSize = displayHashParams.getBufferSize();
        if (bufferSize != null && (bufferSize.getWidth() > 0 || bufferSize.getHeight() > 0)) {
            builder.setFrameScale(bufferSize.getWidth() / rect.width(), bufferSize.getHeight() / rect.height());
        }
        builder.setGrayscale(displayHashParams.isGrayscaleBuffer());
        ScreenCapture.ScreenshotHardwareBuffer captureLayers = ScreenCapture.captureLayers(builder.build());
        if (captureLayers == null || captureLayers.getHardwareBuffer() == null) {
            Slog.w(TAG, "Failed to generate DisplayHash. Couldn't capture content");
            sendDisplayHashError(remoteCallback, -1);
        } else {
            generateDisplayHash(captureLayers.getHardwareBuffer(), rect, str, remoteCallback);
        }
    }

    private Map<String, DisplayHashParams> getDisplayHashAlgorithms() {
        synchronized (this.mDisplayHashAlgorithmsLock) {
            Map<String, DisplayHashParams> map = this.mDisplayHashAlgorithms;
            if (map != null) {
                return map;
            }
            Bundle run = new SyncCommand().run(new BiConsumer() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda3
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    DisplayHashController.lambda$getDisplayHashAlgorithms$2((IDisplayHashingService) obj, (RemoteCallback) obj2);
                }
            });
            this.mDisplayHashAlgorithms = new HashMap(run.size());
            for (String str : run.keySet()) {
                this.mDisplayHashAlgorithms.put(str, (DisplayHashParams) run.getParcelable(str));
            }
            return this.mDisplayHashAlgorithms;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDisplayHashAlgorithms$2(IDisplayHashingService iDisplayHashingService, RemoteCallback remoteCallback) {
        try {
            iDisplayHashingService.getDisplayHashAlgorithms(remoteCallback);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to invoke getDisplayHashAlgorithms command", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendDisplayHashError(RemoteCallback remoteCallback, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("DISPLAY_HASH_ERROR_CODE", i);
        remoteCallback.sendResult(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void calculateDisplayHashBoundsLocked(WindowState windowState, Rect rect, Rect rect2) {
        rect2.set(rect);
        DisplayContent displayContent = windowState.getDisplayContent();
        if (displayContent == null) {
            return;
        }
        Rect rect3 = new Rect();
        windowState.getBounds(rect3);
        rect3.offsetTo(0, 0);
        rect2.intersectUnchecked(rect3);
        if (rect2.isEmpty()) {
            return;
        }
        windowState.getTransformationMatrix(this.mTmpFloat9, this.mTmpMatrix);
        this.mTmpRectF.set(rect2);
        Matrix matrix = this.mTmpMatrix;
        RectF rectF = this.mTmpRectF;
        matrix.mapRect(rectF, rectF);
        RectF rectF2 = this.mTmpRectF;
        rect2.set((int) rectF2.left, (int) rectF2.top, (int) rectF2.right, (int) rectF2.bottom);
        MagnificationSpec magnificationSpec = displayContent.getMagnificationSpec();
        if (magnificationSpec != null) {
            rect2.scale(magnificationSpec.scale);
            rect2.offset((int) magnificationSpec.offsetX, (int) magnificationSpec.offsetY);
        }
        if (rect2.isEmpty()) {
            return;
        }
        rect2.intersectUnchecked(displayContent.getBounds());
    }

    private int getIntervalBetweenRequestMillis() {
        synchronized (this.mIntervalBetweenRequestsLock) {
            int i = this.mIntervalBetweenRequestMillis;
            if (i != -1) {
                return i;
            }
            int i2 = new SyncCommand().run(new BiConsumer() { // from class: com.android.server.wm.DisplayHashController$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    DisplayHashController.lambda$getIntervalBetweenRequestMillis$3((IDisplayHashingService) obj, (RemoteCallback) obj2);
                }
            }).getInt("android.service.displayhash.extra.INTERVAL_BETWEEN_REQUESTS", 0);
            this.mIntervalBetweenRequestMillis = i2;
            return i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getIntervalBetweenRequestMillis$3(IDisplayHashingService iDisplayHashingService, RemoteCallback remoteCallback) {
        try {
            iDisplayHashingService.getIntervalBetweenRequestsMillis(remoteCallback);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to invoke getDisplayHashAlgorithms command", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectAndRun(Command command) {
        ComponentName serviceComponentName;
        synchronized (this.mServiceConnectionLock) {
            this.mHandler.resetTimeoutMessage();
            if (this.mServiceConnection == null && (serviceComponentName = getServiceComponentName()) != null) {
                Intent intent = new Intent();
                intent.setComponent(serviceComponentName);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    DisplayHashingServiceConnection displayHashingServiceConnection = new DisplayHashingServiceConnection();
                    this.mServiceConnection = displayHashingServiceConnection;
                    this.mContext.bindService(intent, displayHashingServiceConnection, 1);
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
            }
            DisplayHashingServiceConnection displayHashingServiceConnection2 = this.mServiceConnection;
            if (displayHashingServiceConnection2 != null) {
                displayHashingServiceConnection2.runCommandLocked(command);
            }
        }
    }

    private ServiceInfo getServiceInfo() {
        ServiceInfo serviceInfo;
        String servicesSystemSharedLibraryPackageName = this.mContext.getPackageManager().getServicesSystemSharedLibraryPackageName();
        if (servicesSystemSharedLibraryPackageName == null) {
            Slog.w(TAG, "no external services package!");
            return null;
        }
        Intent intent = new Intent("android.service.displayhash.DisplayHashingService");
        intent.setPackage(servicesSystemSharedLibraryPackageName);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ResolveInfo resolveService = this.mContext.getPackageManager().resolveService(intent, 132);
            if (resolveService != null && (serviceInfo = resolveService.serviceInfo) != null) {
                return serviceInfo;
            }
            Slog.w(TAG, "No valid components found.");
            return null;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private ComponentName getServiceComponentName() {
        ServiceInfo serviceInfo = getServiceInfo();
        if (serviceInfo == null) {
            return null;
        }
        ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
        if ("android.permission.BIND_DISPLAY_HASHING_SERVICE".equals(serviceInfo.permission)) {
            return componentName;
        }
        Slog.w(TAG, componentName.flattenToShortString() + " requires permission android.permission.BIND_DISPLAY_HASHING_SERVICE");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class SyncCommand {
        private static final int WAIT_TIME_S = 5;
        private final CountDownLatch mCountDownLatch;
        private Bundle mResult;

        private SyncCommand() {
            this.mCountDownLatch = new CountDownLatch(1);
        }

        public Bundle run(final BiConsumer<IDisplayHashingService, RemoteCallback> biConsumer) {
            DisplayHashController.this.connectAndRun(new Command() { // from class: com.android.server.wm.DisplayHashController$SyncCommand$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.DisplayHashController.Command
                public final void run(IDisplayHashingService iDisplayHashingService) {
                    DisplayHashController.SyncCommand.this.lambda$run$1(biConsumer, iDisplayHashingService);
                }
            });
            try {
                this.mCountDownLatch.await(5L, TimeUnit.SECONDS);
            } catch (Exception e) {
                Slog.e(DisplayHashController.TAG, "Failed to wait for command", e);
            }
            return this.mResult;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(BiConsumer biConsumer, IDisplayHashingService iDisplayHashingService) throws RemoteException {
            biConsumer.accept(iDisplayHashingService, new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.server.wm.DisplayHashController$SyncCommand$$ExternalSyntheticLambda1
                public final void onResult(Bundle bundle) {
                    DisplayHashController.SyncCommand.this.lambda$run$0(bundle);
                }
            }));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(Bundle bundle) {
            this.mResult = bundle;
            this.mCountDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DisplayHashingServiceConnection implements ServiceConnection {

        @GuardedBy({"mServiceConnectionLock"})
        private ArrayList<Command> mQueuedCommands;

        @GuardedBy({"mServiceConnectionLock"})
        private IDisplayHashingService mRemoteService;

        private DisplayHashingServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            synchronized (DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = IDisplayHashingService.Stub.asInterface(iBinder);
                ArrayList<Command> arrayList = this.mQueuedCommands;
                if (arrayList != null) {
                    int size = arrayList.size();
                    for (int i = 0; i < size; i++) {
                        try {
                            this.mQueuedCommands.get(i).run(this.mRemoteService);
                        } catch (RemoteException e) {
                            Slog.w(DisplayHashController.TAG, "exception calling " + componentName + ": " + e);
                        }
                    }
                    this.mQueuedCommands = null;
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            synchronized (DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = null;
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            synchronized (DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = null;
            }
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName componentName) {
            synchronized (DisplayHashController.this.mServiceConnectionLock) {
                this.mRemoteService = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void runCommandLocked(Command command) {
            IDisplayHashingService iDisplayHashingService = this.mRemoteService;
            if (iDisplayHashingService == null) {
                if (this.mQueuedCommands == null) {
                    this.mQueuedCommands = new ArrayList<>(1);
                }
                this.mQueuedCommands.add(command);
                return;
            }
            try {
                command.run(iDisplayHashingService);
            } catch (RemoteException e) {
                Slog.w(DisplayHashController.TAG, "exception calling service: " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class Handler extends android.os.Handler {
        static final int MSG_SERVICE_SHUTDOWN_TIMEOUT = 1;
        static final long SERVICE_SHUTDOWN_TIMEOUT_MILLIS = 10000;

        Handler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                synchronized (DisplayHashController.this.mServiceConnectionLock) {
                    if (DisplayHashController.this.mServiceConnection != null) {
                        DisplayHashController.this.mContext.unbindService(DisplayHashController.this.mServiceConnection);
                        DisplayHashController.this.mServiceConnection = null;
                    }
                }
            }
        }

        void resetTimeoutMessage() {
            removeMessages(1);
            sendEmptyMessageDelayed(1, SERVICE_SHUTDOWN_TIMEOUT_MILLIS);
        }
    }
}
