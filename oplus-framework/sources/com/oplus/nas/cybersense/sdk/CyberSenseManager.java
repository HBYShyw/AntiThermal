package com.oplus.nas.cybersense.sdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.nas.cybersense.sdk.ICyberSenseCallback;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class CyberSenseManager {
    private static final int DEFAULT_SCENE_SIZE = 5;
    public static final String SERVICE_NAME = "ICyberSense";
    private static final String TAG = CyberSenseManager.class.getSimpleName();
    private static final String VERSION = "1.0";
    private static volatile CyberSenseManager sInstance;
    private Context mContext;
    private ServiceHolder mServiceHolder;
    private final HashMap<IEventCallback, EventConfig> mEventCallbacks = new HashMap<>(5);
    private final ICyberSenseCallback mCyberSenseCallback = new ICyberSenseCallback.Stub() { // from class: com.oplus.nas.cybersense.sdk.CyberSenseManager.1
        @Override // com.oplus.nas.cybersense.sdk.ICyberSenseCallback
        public void onEventStateChanged(int type, Bundle result) throws RemoteException {
            Log.d(CyberSenseManager.TAG, "onEventStateChanged type=" + type);
            if (result == null) {
                return;
            }
            synchronized (CyberSenseManager.this.mEventCallbacks) {
                for (IEventCallback item : CyberSenseManager.this.mEventCallbacks.keySet()) {
                    item.onEventStateChanged(type, result.getString("result"));
                    item.onEventStateChanged(type, result);
                }
            }
        }
    };
    private EventConfig mEventConfig = new EventConfig();

    private CyberSenseManager(Context context) {
        this.mContext = context;
        this.mServiceHolder = new ServiceHolder(context);
    }

    public static CyberSenseManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CyberSenseManager.class) {
                if (sInstance == null) {
                    sInstance = new CyberSenseManager(context);
                }
            }
        }
        return sInstance;
    }

    public String getVersion() {
        return VERSION;
    }

    public void registerServiceUpCallback(Handler handler, int what) {
        this.mServiceHolder.registerCyberSenseServiceUpCallback(handler, what);
    }

    public void unregisterServiceUpCallback(Handler handler) {
        this.mServiceHolder.unregisterCyberSenseServiceUpCallback(handler);
    }

    public int registerEventCallback(IEventCallback callback, List<Integer> list) {
        if (callback == null || list == null || list.isEmpty()) {
            return 4;
        }
        EventConfig config = new EventConfig();
        config.addEvents(new HashSet(list));
        addClient(callback, config);
        try {
            int rlt = ((ICyberSense) Objects.requireNonNull(this.mServiceHolder.getCyberSenseService())).listenCyberSenseEvent(this.mContext.getPackageName(), this.mCyberSenseCallback, this.mEventConfig);
            return rlt;
        } catch (RemoteException exception) {
            Log.e(TAG, "registerEventCallback err:" + exception.getMessage());
            return 0;
        }
    }

    public int unregisterEventCallback(IEventCallback callback) {
        if (callback == null) {
            return 4;
        }
        removeClient(callback);
        try {
            int rlt = ((ICyberSense) Objects.requireNonNull(this.mServiceHolder.getCyberSenseService())).listenCyberSenseEvent(this.mContext.getPackageName(), this.mCyberSenseCallback, this.mEventConfig);
            return rlt;
        } catch (RemoteException exception) {
            Log.e(TAG, "unregisterEventCallback err:" + exception.getMessage());
            return 0;
        }
    }

    public void reportEventHandle(int event, boolean isValidNotify, String detailResult) {
        if (event == 201) {
            reportNetworkOptimizeResult(detailResult);
        }
    }

    public void reportNetworkOptimizeResult(String result) {
        try {
            ((ICyberSense) Objects.requireNonNull(this.mServiceHolder.getCyberSenseService())).reportNetworkOptimizeResult((String) Objects.requireNonNull(result));
        } catch (RemoteException e) {
            Log.e(TAG, "reportNetworkOptimizeResult err:" + e.getMessage());
        }
    }

    public void removeAllClient() {
        synchronized (this.mEventCallbacks) {
            this.mEventCallbacks.clear();
        }
        updateEventConfig();
    }

    public Bundle mockCmd(int type, Bundle data) {
        try {
            return ((ICyberSense) Objects.requireNonNull(this.mServiceHolder.getCyberSenseService())).mockCmd(type, data);
        } catch (RemoteException e) {
            Log.e(TAG, "mockCmd err:" + e.getMessage());
            return new Bundle();
        }
    }

    private void addClient(IEventCallback callback, EventConfig config) {
        synchronized (this.mEventCallbacks) {
            this.mEventCallbacks.put(callback, config);
        }
        updateEventConfig();
    }

    private void removeClient(IEventCallback callback) {
        synchronized (this.mEventCallbacks) {
            this.mEventCallbacks.remove(callback);
        }
        updateEventConfig();
    }

    private void updateEventConfig() {
        synchronized (this.mEventCallbacks) {
            this.mEventConfig.removeAllEvent();
            this.mEventCallbacks.values().stream().forEach(new Consumer() { // from class: com.oplus.nas.cybersense.sdk.CyberSenseManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    CyberSenseManager.this.lambda$updateEventConfig$0((EventConfig) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEventConfig$0(EventConfig item) {
        this.mEventConfig.addEvents(item.getAllEvents());
    }
}
