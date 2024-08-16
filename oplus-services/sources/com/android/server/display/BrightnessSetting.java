package com.android.server.display;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.display.DisplayManagerService;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BrightnessSetting {
    private static final int MSG_BRIGHTNESS_CHANGED = 1;
    private static final String TAG = "BrightnessSetting";

    @GuardedBy({"mSyncRoot"})
    private float mBrightness;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.server.display.BrightnessSetting.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                BrightnessSetting.this.notifyListeners(Float.intBitsToFloat(message.arg1));
            }
        }
    };
    private final CopyOnWriteArraySet<BrightnessSettingListener> mListeners = new CopyOnWriteArraySet<>();
    private final LogicalDisplay mLogicalDisplay;
    private final PersistentDataStore mPersistentDataStore;
    private final DisplayManagerService.SyncRoot mSyncRoot;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface BrightnessSettingListener {
        void onBrightnessChanged(float f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BrightnessSetting(PersistentDataStore persistentDataStore, LogicalDisplay logicalDisplay, DisplayManagerService.SyncRoot syncRoot) {
        this.mPersistentDataStore = persistentDataStore;
        this.mLogicalDisplay = logicalDisplay;
        this.mBrightness = persistentDataStore.getBrightness(logicalDisplay.getPrimaryDisplayDeviceLocked());
        this.mSyncRoot = syncRoot;
    }

    public float getBrightness() {
        float f;
        synchronized (this.mSyncRoot) {
            f = this.mBrightness;
        }
        return f;
    }

    public void registerListener(BrightnessSettingListener brightnessSettingListener) {
        if (this.mListeners.contains(brightnessSettingListener)) {
            Slog.wtf(TAG, "Duplicate Listener added");
        }
        this.mListeners.add(brightnessSettingListener);
    }

    public void unregisterListener(BrightnessSettingListener brightnessSettingListener) {
        this.mListeners.remove(brightnessSettingListener);
    }

    public void setBrightness(float f) {
        if (Float.isNaN(f)) {
            Slog.w(TAG, "Attempting to set invalid brightness");
            return;
        }
        synchronized (this.mSyncRoot) {
            if (f != this.mBrightness) {
                this.mPersistentDataStore.setBrightness(this.mLogicalDisplay.getPrimaryDisplayDeviceLocked(), f);
            }
            this.mBrightness = f;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, Float.floatToIntBits(f), 0));
        }
    }

    public float getBrightnessNitsForDefaultDisplay() {
        return this.mPersistentDataStore.getBrightnessNitsForDefaultDisplay();
    }

    public void setBrightnessNitsForDefaultDisplay(float f) {
        this.mPersistentDataStore.setBrightnessNitsForDefaultDisplay(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListeners(float f) {
        Iterator<BrightnessSettingListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onBrightnessChanged(f);
        }
    }
}
