package com.android.server.input;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.FeatureFlagUtils;
import android.view.InputDevice;
import com.android.internal.annotations.GuardedBy;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class KeyRemapper implements InputManager.InputDeviceListener {
    private static final int MSG_CLEAR_ALL_REMAPPING = 3;
    private static final int MSG_REMAP_KEY = 2;
    private static final int MSG_UPDATE_EXISTING_DEVICES = 1;
    private final Context mContext;

    @GuardedBy({"mDataStore"})
    private final PersistentDataStore mDataStore;
    private final Handler mHandler;
    private final NativeInputManagerService mNative;

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int i) {
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyRemapper(Context context, NativeInputManagerService nativeInputManagerService, PersistentDataStore persistentDataStore, Looper looper) {
        this.mContext = context;
        this.mNative = nativeInputManagerService;
        this.mDataStore = persistentDataStore;
        this.mHandler = new Handler(looper, new Handler.Callback() { // from class: com.android.server.input.KeyRemapper$$ExternalSyntheticLambda1
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                boolean handleMessage;
                handleMessage = KeyRemapper.this.handleMessage(message);
                return handleMessage;
            }
        });
    }

    public void systemRunning() {
        InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        Objects.requireNonNull(inputManager);
        inputManager.registerInputDeviceListener(this, this.mHandler);
        this.mHandler.sendMessage(Message.obtain(this.mHandler, 1, inputManager.getInputDeviceIds()));
    }

    public void remapKey(int i, int i2) {
        if (supportRemapping()) {
            this.mHandler.sendMessage(Message.obtain(this.mHandler, 2, i, i2));
        }
    }

    public void clearAllKeyRemappings() {
        if (supportRemapping()) {
            this.mHandler.sendMessage(Message.obtain(this.mHandler, 3));
        }
    }

    public Map<Integer, Integer> getKeyRemapping() {
        Map<Integer, Integer> keyRemapping;
        if (!supportRemapping()) {
            return new ArrayMap();
        }
        synchronized (this.mDataStore) {
            keyRemapping = this.mDataStore.getKeyRemapping();
        }
        return keyRemapping;
    }

    private void addKeyRemapping(int i, int i2) {
        InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        Objects.requireNonNull(inputManager);
        for (int i3 : inputManager.getInputDeviceIds()) {
            InputDevice inputDevice = inputManager.getInputDevice(i3);
            if (inputDevice != null && !inputDevice.isVirtual() && inputDevice.isFullKeyboard()) {
                this.mNative.addKeyRemapping(i3, i, i2);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.server.input.PersistentDataStore] */
    private void remapKeyInternal(int i, int i2) {
        addKeyRemapping(i, i2);
        synchronized (this.mDataStore) {
            try {
                if (i == i2) {
                    this.mDataStore.clearMappedKey(i);
                } else {
                    this.mDataStore.remapKey(i, i2);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    private void clearAllRemappingsInternal() {
        synchronized (this.mDataStore) {
            try {
                Iterator<Integer> it = this.mDataStore.getKeyRemapping().keySet().iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    this.mDataStore.clearMappedKey(intValue);
                    addKeyRemapping(intValue, intValue);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(final int i) {
        if (supportRemapping()) {
            InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
            Objects.requireNonNull(inputManager);
            InputDevice inputDevice = inputManager.getInputDevice(i);
            if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
                return;
            }
            getKeyRemapping().forEach(new BiConsumer() { // from class: com.android.server.input.KeyRemapper$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    KeyRemapper.this.lambda$onInputDeviceAdded$0(i, (Integer) obj, (Integer) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onInputDeviceAdded$0(int i, Integer num, Integer num2) {
        this.mNative.addKeyRemapping(i, num.intValue(), num2.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            for (int i2 : (int[]) message.obj) {
                onInputDeviceAdded(i2);
            }
            return true;
        }
        if (i == 2) {
            remapKeyInternal(message.arg1, message.arg2);
            return true;
        }
        if (i != 3) {
            return false;
        }
        clearAllRemappingsInternal();
        return true;
    }

    private boolean supportRemapping() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_new_keyboard_modifier_key");
    }
}
