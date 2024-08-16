package com.android.server.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.input.InputSettings;
import android.net.Uri;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import android.view.PointerIcon;
import com.android.server.location.contexthub.ContextHubStatsLog;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class InputSettingsObserver extends ContentObserver {
    private static final String DEEP_PRESS_ENABLED = "deep_press_enabled";
    static final String TAG = "InputManager";
    private final Context mContext;
    private final Handler mHandler;
    private final NativeInputManagerService mNative;
    private final Map<Uri, Consumer<String>> mObservers;
    private final InputManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputSettingsObserver(Context context, Handler handler, InputManagerService inputManagerService, NativeInputManagerService nativeInputManagerService) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
        this.mService = inputManagerService;
        this.mNative = nativeInputManagerService;
        this.mObservers = Map.ofEntries(Map.entry(Settings.System.getUriFor("pointer_speed"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$0((String) obj);
            }
        }), Map.entry(Settings.System.getUriFor("touchpad_pointer_speed"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$1((String) obj);
            }
        }), Map.entry(Settings.System.getUriFor("touchpad_natural_scrolling"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$2((String) obj);
            }
        }), Map.entry(Settings.System.getUriFor("touchpad_tap_to_click"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$3((String) obj);
            }
        }), Map.entry(Settings.System.getUriFor("touchpad_right_click_zone"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$4((String) obj);
            }
        }), Map.entry(Settings.System.getUriFor("show_touches"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$5((String) obj);
            }
        }), Map.entry(Settings.Secure.getUriFor("accessibility_large_pointer_icon"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$6((String) obj);
            }
        }), Map.entry(Settings.Secure.getUriFor("long_press_timeout"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$7((String) obj);
            }
        }), Map.entry(Settings.Global.getUriFor("maximum_obscuring_opacity_for_touch"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$8((String) obj);
            }
        }), Map.entry(Settings.System.getUriFor("show_key_presses"), new Consumer() { // from class: com.android.server.input.InputSettingsObserver$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InputSettingsObserver.this.lambda$new$9((String) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(String str) {
        updateMousePointerSpeed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(String str) {
        updateTouchpadPointerSpeed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(String str) {
        updateTouchpadNaturalScrollingEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(String str) {
        updateTouchpadTapToClickEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(String str) {
        updateTouchpadRightClickZoneEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(String str) {
        updateShowTouches();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(String str) {
        updateAccessibilityLargePointer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(String str) {
        updateMaximumObscuringOpacityForTouch();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(String str) {
        updateShowKeyPresses();
    }

    public void registerAndUpdate() {
        Iterator<Uri> it = this.mObservers.keySet().iterator();
        while (it.hasNext()) {
            this.mContext.getContentResolver().registerContentObserver(it.next(), true, this, -1);
        }
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.input.InputSettingsObserver.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Iterator it2 = InputSettingsObserver.this.mObservers.values().iterator();
                while (it2.hasNext()) {
                    ((Consumer) it2.next()).accept("user switched");
                }
            }
        }, new IntentFilter("android.intent.action.USER_SWITCHED"), null, this.mHandler);
        Iterator<Consumer<String>> it2 = this.mObservers.values().iterator();
        while (it2.hasNext()) {
            it2.next().accept("just booted");
        }
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z, Uri uri) {
        this.mObservers.get(uri).accept("setting changed");
    }

    private boolean getBoolean(String str, boolean z) {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), str, z ? 1 : 0, -2) != 0;
    }

    private int getPointerSpeedValue(String str) {
        return Math.min(Math.max(Settings.System.getIntForUser(this.mContext.getContentResolver(), str, 0, -2), -7), 7);
    }

    private void updateMousePointerSpeed() {
        this.mNative.setPointerSpeed(getPointerSpeedValue("pointer_speed"));
    }

    private void updateTouchpadPointerSpeed() {
        this.mNative.setTouchpadPointerSpeed(getPointerSpeedValue("touchpad_pointer_speed"));
    }

    private void updateTouchpadNaturalScrollingEnabled() {
        this.mNative.setTouchpadNaturalScrollingEnabled(getBoolean("touchpad_natural_scrolling", true));
    }

    private void updateTouchpadTapToClickEnabled() {
        this.mNative.setTouchpadTapToClickEnabled(getBoolean("touchpad_tap_to_click", true));
    }

    private void updateTouchpadRightClickZoneEnabled() {
        this.mNative.setTouchpadRightClickZoneEnabled(getBoolean("touchpad_right_click_zone", false));
    }

    private void updateShowTouches() {
        this.mNative.setShowTouches(getBoolean("show_touches", false));
    }

    private void updateShowKeyPresses() {
        this.mService.updateFocusEventDebugViewEnabled(getBoolean("show_key_presses", false));
    }

    private void updateAccessibilityLargePointer() {
        PointerIcon.setUseLargeIcons(Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_large_pointer_icon", 0, -2) == 1);
        this.mNative.reloadPointerIcons();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateLongPressTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$new$7(String str) {
        this.mNative.notifyKeyGestureTimeoutsChanged();
        int intForUser = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "long_press_timeout", ContextHubStatsLog.CONTEXT_HUB_LOADED_NANOAPP_SNAPSHOT_REPORTED, -2);
        boolean z = DeviceConfig.getBoolean("input_native_boot", DEEP_PRESS_ENABLED, true);
        boolean z2 = z && intForUser <= 400;
        StringBuilder sb = new StringBuilder();
        sb.append(z2 ? "Enabling" : "Disabling");
        sb.append(" motion classifier because ");
        sb.append(str);
        sb.append(": feature ");
        sb.append(z ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        sb.append(", long press timeout = ");
        sb.append(intForUser);
        Log.i(TAG, sb.toString());
        this.mNative.setMotionClassifierEnabled(z2);
    }

    private void updateMaximumObscuringOpacityForTouch() {
        float maximumObscuringOpacityForTouch = InputSettings.getMaximumObscuringOpacityForTouch(this.mContext);
        if (maximumObscuringOpacityForTouch < 0.0f || maximumObscuringOpacityForTouch > 1.0f) {
            Log.e(TAG, "Invalid maximum obscuring opacity " + maximumObscuringOpacityForTouch + ", it should be >= 0 and <= 1, rejecting update.");
            return;
        }
        this.mNative.setMaximumObscuringOpacityForTouch(maximumObscuringOpacityForTouch);
    }
}
