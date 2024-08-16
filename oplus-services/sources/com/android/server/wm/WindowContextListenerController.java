package com.android.server.wm;

import android.app.IWindowToken;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.view.Display;
import android.window.WindowProviderService;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowContextListenerController {

    @VisibleForTesting
    final ArrayMap<IBinder, WindowContextListenerImpl> mListeners = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerWindowContainerListener(IBinder iBinder, WindowContainer<?> windowContainer, int i, int i2, Bundle bundle) {
        registerWindowContainerListener(iBinder, windowContainer, i, i2, bundle, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerWindowContainerListener(IBinder iBinder, WindowContainer<?> windowContainer, int i, int i2, Bundle bundle, boolean z) {
        WindowContextListenerImpl windowContextListenerImpl = this.mListeners.get(iBinder);
        if (windowContextListenerImpl == null) {
            new WindowContextListenerImpl(iBinder, windowContainer, i, i2, bundle).register(z);
        } else {
            windowContextListenerImpl.updateContainer(windowContainer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterWindowContainerListener(IBinder iBinder) {
        WindowContextListenerImpl windowContextListenerImpl = this.mListeners.get(iBinder);
        if (windowContextListenerImpl == null) {
            return;
        }
        windowContextListenerImpl.unregister();
        if (windowContextListenerImpl.mDeathRecipient != null) {
            windowContextListenerImpl.mDeathRecipient.unlinkToDeath();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchPendingConfigurationIfNeeded(int i) {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            WindowContextListenerImpl valueAt = this.mListeners.valueAt(size);
            if (valueAt.getWindowContainer().getDisplayContent().getDisplayId() == i && valueAt.mHasPendingConfiguration) {
                valueAt.reportConfigToWindowTokenClient();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean assertCallerCanModifyListener(IBinder iBinder, boolean z, int i) {
        WindowContextListenerImpl windowContextListenerImpl = this.mListeners.get(iBinder);
        if (windowContextListenerImpl == null) {
            if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -1136467585, 0, (String) null, (Object[]) null);
            }
            return false;
        }
        if (z || i == windowContextListenerImpl.mOwnerUid) {
            return true;
        }
        throw new UnsupportedOperationException("Uid mismatch. Caller uid is " + i + ", while the listener's owner is from " + windowContextListenerImpl.mOwnerUid);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasListener(IBinder iBinder) {
        return this.mListeners.containsKey(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getWindowType(IBinder iBinder) {
        WindowContextListenerImpl windowContextListenerImpl = this.mListeners.get(iBinder);
        if (windowContextListenerImpl != null) {
            return windowContextListenerImpl.mType;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle getOptions(IBinder iBinder) {
        WindowContextListenerImpl windowContextListenerImpl = this.mListeners.get(iBinder);
        if (windowContextListenerImpl != null) {
            return windowContextListenerImpl.mOptions;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowContainer<?> getContainer(IBinder iBinder) {
        WindowContextListenerImpl windowContextListenerImpl = this.mListeners.get(iBinder);
        if (windowContextListenerImpl != null) {
            return windowContextListenerImpl.mContainer;
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("WindowContextListenerController{");
        sb.append("mListeners=[");
        int size = this.mListeners.values().size();
        for (int i = 0; i < size; i++) {
            sb.append(this.mListeners.valueAt(i));
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class WindowContextListenerImpl implements WindowContainerListener {
        private final IWindowToken mClientToken;
        private WindowContainer<?> mContainer;
        private DeathRecipient mDeathRecipient;
        private boolean mHasPendingConfiguration;
        private Configuration mLastReportedConfig;
        private int mLastReportedDisplay;
        private final Bundle mOptions;
        private final int mOwnerUid;
        private final int mType;

        private WindowContextListenerImpl(IBinder iBinder, WindowContainer<?> windowContainer, int i, int i2, Bundle bundle) {
            this.mLastReportedDisplay = -1;
            this.mClientToken = IWindowToken.Stub.asInterface(iBinder);
            Objects.requireNonNull(windowContainer);
            this.mContainer = windowContainer;
            this.mOwnerUid = i;
            this.mType = i2;
            this.mOptions = bundle;
            DeathRecipient deathRecipient = new DeathRecipient();
            try {
                deathRecipient.linkToDeath();
                this.mDeathRecipient = deathRecipient;
            } catch (RemoteException unused) {
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.e(ProtoLogGroup.WM_ERROR, -2014162875, 0, "Could not register window container listener token=%s, container=%s", new Object[]{String.valueOf(iBinder), String.valueOf(this.mContainer)});
                }
            }
        }

        @VisibleForTesting
        WindowContainer<?> getWindowContainer() {
            return this.mContainer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateContainer(WindowContainer<?> windowContainer) {
            Objects.requireNonNull(windowContainer);
            if (this.mContainer.equals(windowContainer)) {
                return;
            }
            this.mContainer.unregisterWindowContainerListener(this);
            this.mContainer = windowContainer;
            clear();
            register();
        }

        private void register() {
            register(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void register(boolean z) {
            IBinder asBinder = this.mClientToken.asBinder();
            if (this.mDeathRecipient == null) {
                throw new IllegalStateException("Invalid client token: " + asBinder);
            }
            WindowContextListenerController.this.mListeners.putIfAbsent(asBinder, this);
            this.mContainer.registerWindowContainerListener(this, z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregister() {
            this.mContainer.unregisterWindowContainerListener(this);
            WindowContextListenerController.this.mListeners.remove(this.mClientToken.asBinder());
        }

        private void clear() {
            this.mLastReportedConfig = null;
            this.mLastReportedDisplay = -1;
        }

        @Override // com.android.server.wm.ConfigurationContainerListener
        public void onMergedOverrideConfigurationChanged(Configuration configuration) {
            reportConfigToWindowTokenClient();
        }

        @Override // com.android.server.wm.WindowContainerListener
        public void onDisplayChanged(DisplayContent displayContent) {
            reportConfigToWindowTokenClient();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reportConfigToWindowTokenClient() {
            if (this.mDeathRecipient == null) {
                throw new IllegalStateException("Invalid client token: " + this.mClientToken.asBinder());
            }
            DisplayContent displayContent = this.mContainer.getDisplayContent();
            if (displayContent.isReady()) {
                if (!WindowProviderService.isWindowProviderService(this.mOptions) && Display.isSuspendedState(displayContent.getDisplayInfo().state)) {
                    this.mHasPendingConfiguration = true;
                    return;
                }
                Configuration configuration = this.mContainer.getConfiguration();
                int displayId = displayContent.getDisplayId();
                if (this.mLastReportedConfig == null) {
                    this.mLastReportedConfig = new Configuration();
                }
                if (configuration.equals(this.mLastReportedConfig) && displayId == this.mLastReportedDisplay) {
                    return;
                }
                this.mLastReportedConfig.setTo(configuration);
                this.mLastReportedDisplay = displayId;
                try {
                    this.mClientToken.onConfigurationChanged(configuration, displayId);
                } catch (RemoteException unused) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1948483534, 0, "Could not report config changes to the window token client.", (Object[]) null);
                    }
                }
                this.mHasPendingConfiguration = false;
            }
        }

        @Override // com.android.server.wm.WindowContainerListener
        public void onRemoved() {
            DisplayContent displayContent;
            if (this.mDeathRecipient == null) {
                throw new IllegalStateException("Invalid client token: " + this.mClientToken.asBinder());
            }
            WindowToken asWindowToken = this.mContainer.asWindowToken();
            if (asWindowToken != null && asWindowToken.isFromClient() && (displayContent = asWindowToken.mWmService.mRoot.getDisplayContent(this.mLastReportedDisplay)) != null) {
                updateContainer(displayContent.findAreaForToken(asWindowToken));
                return;
            }
            this.mDeathRecipient.unlinkToDeath();
            try {
                this.mClientToken.onWindowTokenRemoved();
            } catch (RemoteException unused) {
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 90764070, 0, "Could not report token removal to the window token client.", (Object[]) null);
                }
            }
            unregister();
        }

        public String toString() {
            return "WindowContextListenerImpl{clientToken=" + this.mClientToken.asBinder() + ", container=" + this.mContainer + "}";
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public class DeathRecipient implements IBinder.DeathRecipient {
            private DeathRecipient() {
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                WindowManagerGlobalLock windowManagerGlobalLock = WindowContextListenerImpl.this.mContainer.mWmService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        WindowContextListenerImpl.this.mDeathRecipient = null;
                        WindowContextListenerImpl.this.unregister();
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }

            void linkToDeath() throws RemoteException {
                WindowContextListenerImpl.this.mClientToken.asBinder().linkToDeath(this, 0);
            }

            void unlinkToDeath() {
                WindowContextListenerImpl.this.mClientToken.asBinder().unlinkToDeath(this, 0);
            }
        }
    }
}
