package com.oplus.resolver;

import android.app.Activity;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.oshare.IOplusOshareCallback;
import com.oplus.oshare.IOplusOshareInitListener;
import com.oplus.oshare.OplusOshareDevice;
import com.oplus.oshare.OplusOshareServiceUtil;
import com.oplus.resolver.OplusOShareManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusOShareManager {
    private static final String TAG = "OplusOShareManager";
    private Context mContext;
    private List<OplusOshareDevice> mDeviceList;
    private OplusOshareServiceUtil mOShareServiceUtil;
    private final List<OShareListener> mOShareListeners = new ArrayList();
    private IOplusOshareCallback mOShareCallback = new OplusOshareCallback(this);
    private IOplusOshareInitListener mOShareInitListener = new AnonymousClass1();

    /* loaded from: classes.dex */
    public interface OShareListener {
        void onDevicesChange(boolean z, int i);
    }

    public OplusOShareManager(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.resolver.OplusOShareManager$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IOplusOshareInitListener.Stub {
        AnonymousClass1() {
        }

        @Override // com.oplus.oshare.IOplusOshareInitListener
        public void onShareUninit() throws RemoteException {
            Log.d(OplusOShareManager.TAG, "onShareUninit");
            if (OplusOShareManager.this.mOShareServiceUtil != null) {
                OplusOShareManager.this.mOShareServiceUtil.unregisterCallback(OplusOShareManager.this.mOShareCallback);
            }
        }

        @Override // com.oplus.oshare.IOplusOshareInitListener
        public void onShareInit() throws RemoteException {
            Log.d(OplusOShareManager.TAG, "onShareInit");
            if (OplusOShareManager.this.mContext != null && (OplusOShareManager.this.mContext instanceof Activity)) {
                ((Activity) OplusOShareManager.this.mContext).runOnUiThread(new Runnable() { // from class: com.oplus.resolver.OplusOShareManager$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusOShareManager.AnonymousClass1.this.lambda$onShareInit$1();
                    }
                });
            }
            if (OplusOShareManager.this.mOShareServiceUtil != null) {
                OplusOShareManager.this.mOShareServiceUtil.registerCallback(OplusOShareManager.this.mOShareCallback);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShareInit$1() {
            OplusOShareManager.this.mOShareListeners.forEach(new Consumer() { // from class: com.oplus.resolver.OplusOShareManager$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OplusOShareManager.AnonymousClass1.this.lambda$onShareInit$0((OplusOShareManager.OShareListener) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShareInit$0(OShareListener de) {
            de.onDevicesChange(OplusOShareManager.this.mOShareServiceUtil != null && OplusOShareManager.this.mOShareServiceUtil.isSendOn(), 0);
        }
    }

    public void initOShareService() {
        if (this.mOShareServiceUtil == null) {
            Log.d(TAG, "initOShareService");
            OplusOshareServiceUtil oplusOshareServiceUtil = new OplusOshareServiceUtil(this.mContext, this.mOShareInitListener);
            this.mOShareServiceUtil = oplusOshareServiceUtil;
            oplusOshareServiceUtil.initShareEngine();
        }
    }

    public void onResume() {
        OplusOshareServiceUtil oplusOshareServiceUtil = this.mOShareServiceUtil;
        if (oplusOshareServiceUtil != null) {
            try {
                oplusOshareServiceUtil.resume();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onPause() {
        OplusOshareServiceUtil oplusOshareServiceUtil = this.mOShareServiceUtil;
        if (oplusOshareServiceUtil != null) {
            try {
                oplusOshareServiceUtil.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        if (this.mOShareServiceUtil != null) {
            Log.d(TAG, "onDestroy");
            this.mOShareServiceUtil.stop();
            this.mOShareServiceUtil.unregisterCallback(this.mOShareCallback);
            this.mOShareServiceUtil = null;
        }
    }

    public boolean isSwitchSendOn() {
        OplusOshareServiceUtil oplusOshareServiceUtil = this.mOShareServiceUtil;
        return oplusOshareServiceUtil != null && oplusOshareServiceUtil.isSendOn();
    }

    public int getDeviceSize() {
        List<OplusOshareDevice> list = this.mDeviceList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void addListener(OShareListener listener) {
        this.mOShareListeners.add(listener);
    }

    public void removeListener(OShareListener listener) {
        this.mOShareListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OplusOshareCallback extends IOplusOshareCallback.Stub {
        private WeakReference<OplusOShareManager> mWeakReference;

        OplusOshareCallback(OplusOShareManager manager) {
            this.mWeakReference = new WeakReference<>(manager);
        }

        @Override // com.oplus.oshare.IOplusOshareCallback
        public void onDeviceChanged(final List<OplusOshareDevice> deviceList) throws RemoteException {
            Log.d(OplusOShareManager.TAG, "onDeviceChanged:" + (deviceList == null ? 0 : deviceList.size()));
            OplusOShareManager manager = this.mWeakReference.get();
            if (manager == null) {
                return;
            }
            manager.mDeviceList = deviceList;
            if (manager.mContext instanceof Activity) {
                ((Activity) manager.mContext).runOnUiThread(new Runnable() { // from class: com.oplus.resolver.OplusOShareManager$OplusOshareCallback$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusOShareManager.OplusOshareCallback.this.lambda$onDeviceChanged$1(deviceList);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeviceChanged$1(final List deviceList) {
            final OplusOShareManager oShareManager = this.mWeakReference.get();
            if (oShareManager == null) {
                return;
            }
            oShareManager.mOShareListeners.forEach(new Consumer() { // from class: com.oplus.resolver.OplusOShareManager$OplusOshareCallback$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OplusOShareManager oplusOShareManager = OplusOShareManager.this;
                    ((OplusOShareManager.OShareListener) obj).onDevicesChange(oShareManager.mOShareServiceUtil != null && oShareManager.mOShareServiceUtil.isSendOn(), deviceList != null ? deviceList.size() : 0);
                }
            });
        }

        @Override // com.oplus.oshare.IOplusOshareCallback
        public void onSendSwitchChanged(final boolean isOn) {
            Log.d(OplusOShareManager.TAG, "onSendSwitchChanged:" + isOn);
            OplusOShareManager manager = this.mWeakReference.get();
            if (manager != null && (manager.mContext instanceof Activity)) {
                ((Activity) manager.mContext).runOnUiThread(new Runnable() { // from class: com.oplus.resolver.OplusOShareManager$OplusOshareCallback$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusOShareManager.OplusOshareCallback.this.lambda$onSendSwitchChanged$3(isOn);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSendSwitchChanged$3(final boolean isOn) {
            final OplusOShareManager oShareManager = this.mWeakReference.get();
            if (oShareManager == null) {
                return;
            }
            oShareManager.mOShareListeners.forEach(new Consumer() { // from class: com.oplus.resolver.OplusOShareManager$OplusOshareCallback$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((OplusOShareManager.OShareListener) obj).onDevicesChange(isOn, oShareManager.mDeviceList == null ? 0 : oShareManager.mDeviceList.size());
                }
            });
        }
    }
}
