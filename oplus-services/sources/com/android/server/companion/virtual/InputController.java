package com.android.server.companion.virtual;

import android.graphics.PointF;
import android.hardware.display.DisplayManagerInternal;
import android.hardware.input.InputDeviceIdentifier;
import android.hardware.input.InputManager;
import android.hardware.input.InputManagerGlobal;
import android.hardware.input.VirtualKeyEvent;
import android.hardware.input.VirtualMouseButtonEvent;
import android.hardware.input.VirtualMouseRelativeEvent;
import android.hardware.input.VirtualMouseScrollEvent;
import android.hardware.input.VirtualTouchEvent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.InputDevice;
import android.view.WindowManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.input.InputManagerInternal;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class InputController {
    private static final int DEVICE_NAME_MAX_LENGTH = 80;
    static final String NAVIGATION_TOUCHPAD_DEVICE_TYPE = "touchNavigation";
    static final String PHYS_TYPE_DPAD = "Dpad";
    static final String PHYS_TYPE_KEYBOARD = "Keyboard";
    static final String PHYS_TYPE_MOUSE = "Mouse";
    static final String PHYS_TYPE_NAVIGATION_TOUCHPAD = "NavigationTouchpad";
    static final String PHYS_TYPE_TOUCHSCREEN = "Touchscreen";
    private static final String TAG = "VirtualInputController";
    private static final AtomicLong sNextPhysId = new AtomicLong(1);
    private final DisplayManagerInternal mDisplayManagerInternal;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, InputDeviceDescriptor> mInputDeviceDescriptors;
    private final InputManagerInternal mInputManagerInternal;
    final Object mLock;
    private final NativeWrapper mNativeWrapper;
    private final DeviceCreationThreadVerifier mThreadVerifier;
    private final WindowManager mWindowManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface DeviceCreationThreadVerifier {
        boolean isValidThread();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface PhysType {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addDeviceForTesting$6() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeCloseUinput(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputDpad(String str, int i, int i2, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputKeyboard(String str, int i, int i2, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputMouse(String str, int i, int i2, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputTouchscreen(String str, int i, int i2, String str2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteButtonEvent(long j, int i, int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteDpadKeyEvent(long j, int i, int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteKeyEvent(long j, int i, int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteRelativeEvent(long j, float f, float f2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteScrollEvent(long j, float f, float f2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteTouchEvent(long j, int i, int i2, int i3, float f, float f2, float f3, float f4, long j2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputController(final Handler handler, WindowManager windowManager) {
        this(new NativeWrapper(), handler, windowManager, new DeviceCreationThreadVerifier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda1
            @Override // com.android.server.companion.virtual.InputController.DeviceCreationThreadVerifier
            public final boolean isValidThread() {
                boolean lambda$new$0;
                lambda$new$0 = InputController.lambda$new$0(handler);
                return lambda$new$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$0(Handler handler) {
        return !handler.getLooper().isCurrentThread();
    }

    @VisibleForTesting
    InputController(NativeWrapper nativeWrapper, Handler handler, WindowManager windowManager, DeviceCreationThreadVerifier deviceCreationThreadVerifier) {
        this.mLock = new Object();
        this.mInputDeviceDescriptors = new ArrayMap<>();
        this.mHandler = handler;
        this.mNativeWrapper = nativeWrapper;
        this.mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
        this.mInputManagerInternal = (InputManagerInternal) LocalServices.getService(InputManagerInternal.class);
        this.mWindowManager = windowManager;
        this.mThreadVerifier = deviceCreationThreadVerifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        synchronized (this.mLock) {
            Iterator<Map.Entry<IBinder, InputDeviceDescriptor>> it = this.mInputDeviceDescriptors.entrySet().iterator();
            if (it.hasNext()) {
                Map.Entry<IBinder, InputDeviceDescriptor> next = it.next();
                IBinder key = next.getKey();
                InputDeviceDescriptor value = next.getValue();
                it.remove();
                closeInputDeviceDescriptorLocked(key, value);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createDpad(final String str, final int i, final int i2, IBinder iBinder, int i3) {
        final String createPhys = createPhys(PHYS_TYPE_DPAD);
        try {
            createDeviceInternal(4, str, i, i2, iBinder, i3, createPhys, new Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda6
                @Override // java.util.function.Supplier
                public final Object get() {
                    Long lambda$createDpad$1;
                    lambda$createDpad$1 = InputController.this.lambda$createDpad$1(str, i, i2, createPhys);
                    return lambda$createDpad$1;
                }
            });
        } catch (DeviceCreationException e) {
            throw new RuntimeException("Failed to create virtual dpad device '" + str + "'.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Long lambda$createDpad$1(String str, int i, int i2, String str2) {
        return Long.valueOf(this.mNativeWrapper.openUinputDpad(str, i, i2, str2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createKeyboard(final String str, final int i, final int i2, IBinder iBinder, int i3, String str2, String str3) {
        final String createPhys = createPhys(PHYS_TYPE_KEYBOARD);
        this.mInputManagerInternal.addKeyboardLayoutAssociation(createPhys, str2, str3);
        try {
            createDeviceInternal(1, str, i, i2, iBinder, i3, createPhys, new Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda3
                @Override // java.util.function.Supplier
                public final Object get() {
                    Long lambda$createKeyboard$2;
                    lambda$createKeyboard$2 = InputController.this.lambda$createKeyboard$2(str, i, i2, createPhys);
                    return lambda$createKeyboard$2;
                }
            });
        } catch (DeviceCreationException e) {
            this.mInputManagerInternal.removeKeyboardLayoutAssociation(createPhys);
            throw new RuntimeException("Failed to create virtual keyboard device '" + str + "'.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Long lambda$createKeyboard$2(String str, int i, int i2, String str2) {
        return Long.valueOf(this.mNativeWrapper.openUinputKeyboard(str, i, i2, str2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createMouse(final String str, final int i, final int i2, IBinder iBinder, int i3) {
        final String createPhys = createPhys(PHYS_TYPE_MOUSE);
        try {
            createDeviceInternal(2, str, i, i2, iBinder, i3, createPhys, new Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    Long lambda$createMouse$3;
                    lambda$createMouse$3 = InputController.this.lambda$createMouse$3(str, i, i2, createPhys);
                    return lambda$createMouse$3;
                }
            });
            this.mInputManagerInternal.setVirtualMousePointerDisplayId(i3);
        } catch (DeviceCreationException e) {
            throw new RuntimeException("Failed to create virtual mouse device: '" + str + "'.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Long lambda$createMouse$3(String str, int i, int i2, String str2) {
        return Long.valueOf(this.mNativeWrapper.openUinputMouse(str, i, i2, str2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createTouchscreen(final String str, final int i, final int i2, IBinder iBinder, int i3, final int i4, final int i5) {
        final String createPhys = createPhys(PHYS_TYPE_TOUCHSCREEN);
        try {
            createDeviceInternal(3, str, i, i2, iBinder, i3, createPhys, new Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final Object get() {
                    Long lambda$createTouchscreen$4;
                    lambda$createTouchscreen$4 = InputController.this.lambda$createTouchscreen$4(str, i, i2, createPhys, i4, i5);
                    return lambda$createTouchscreen$4;
                }
            });
        } catch (DeviceCreationException e) {
            throw new RuntimeException("Failed to create virtual touchscreen device '" + str + "'.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Long lambda$createTouchscreen$4(String str, int i, int i2, String str2, int i3, int i4) {
        return Long.valueOf(this.mNativeWrapper.openUinputTouchscreen(str, i, i2, str2, i3, i4));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createNavigationTouchpad(final String str, final int i, final int i2, IBinder iBinder, int i3, final int i4, final int i5) {
        final String createPhys = createPhys(PHYS_TYPE_NAVIGATION_TOUCHPAD);
        this.mInputManagerInternal.setTypeAssociation(createPhys, NAVIGATION_TOUCHPAD_DEVICE_TYPE);
        try {
            createDeviceInternal(5, str, i, i2, iBinder, i3, createPhys, new Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda4
                @Override // java.util.function.Supplier
                public final Object get() {
                    Long lambda$createNavigationTouchpad$5;
                    lambda$createNavigationTouchpad$5 = InputController.this.lambda$createNavigationTouchpad$5(str, i, i2, createPhys, i4, i5);
                    return lambda$createNavigationTouchpad$5;
                }
            });
        } catch (DeviceCreationException e) {
            this.mInputManagerInternal.unsetTypeAssociation(createPhys);
            throw new RuntimeException("Failed to create virtual navigation touchpad device '" + str + "'.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Long lambda$createNavigationTouchpad$5(String str, int i, int i2, String str2, int i3, int i4) {
        return Long.valueOf(this.mNativeWrapper.openUinputTouchscreen(str, i, i2, str2, i3, i4));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterInputDevice(IBinder iBinder) {
        synchronized (this.mLock) {
            InputDeviceDescriptor remove = this.mInputDeviceDescriptors.remove(iBinder);
            if (remove == null) {
                throw new IllegalArgumentException("Could not unregister input device for given token");
            }
            closeInputDeviceDescriptorLocked(iBinder, remove);
        }
    }

    @GuardedBy({"mLock"})
    private void closeInputDeviceDescriptorLocked(IBinder iBinder, InputDeviceDescriptor inputDeviceDescriptor) {
        iBinder.unlinkToDeath(inputDeviceDescriptor.getDeathRecipient(), 0);
        this.mNativeWrapper.closeUinput(inputDeviceDescriptor.getNativePointer());
        String phys = inputDeviceDescriptor.getPhys();
        InputManagerGlobal.getInstance().removeUniqueIdAssociation(phys);
        if (inputDeviceDescriptor.getType() == 5) {
            this.mInputManagerInternal.unsetTypeAssociation(phys);
        }
        if (inputDeviceDescriptor.getType() == 1) {
            this.mInputManagerInternal.removeKeyboardLayoutAssociation(phys);
        }
        if (inputDeviceDescriptor.isMouse() && this.mInputManagerInternal.getVirtualMousePointerDisplayId() == inputDeviceDescriptor.getDisplayId()) {
            updateActivePointerDisplayIdLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInputDeviceId(IBinder iBinder) {
        int inputDeviceId;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not get device id for given token");
            }
            inputDeviceId = inputDeviceDescriptor.getInputDeviceId();
        }
        return inputDeviceId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShowPointerIcon(boolean z, int i) {
        this.mInputManagerInternal.setPointerIconVisible(z, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPointerAcceleration(float f, int i) {
        this.mInputManagerInternal.setPointerAcceleration(f, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDisplayEligibilityForPointerCapture(boolean z, int i) {
        this.mInputManagerInternal.setDisplayEligibilityForPointerCapture(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLocalIme(int i) {
        if ((this.mDisplayManagerInternal.getDisplayInfo(i).flags & 128) == 128) {
            this.mWindowManager.setDisplayImePolicy(i, 0);
        }
    }

    @GuardedBy({"mLock"})
    private void updateActivePointerDisplayIdLocked() {
        InputDeviceDescriptor inputDeviceDescriptor = null;
        for (int i = 0; i < this.mInputDeviceDescriptors.size(); i++) {
            InputDeviceDescriptor valueAt = this.mInputDeviceDescriptors.valueAt(i);
            if (valueAt.isMouse() && (inputDeviceDescriptor == null || valueAt.getCreationOrderNumber() > inputDeviceDescriptor.getCreationOrderNumber())) {
                inputDeviceDescriptor = valueAt;
            }
        }
        if (inputDeviceDescriptor != null) {
            this.mInputManagerInternal.setVirtualMousePointerDisplayId(inputDeviceDescriptor.getDisplayId());
        } else {
            this.mInputManagerInternal.setVirtualMousePointerDisplayId(-1);
        }
    }

    private void validateDeviceName(String str) throws DeviceCreationException {
        if (str.getBytes(StandardCharsets.UTF_8).length >= 80) {
            throw new DeviceCreationException("Input device name exceeds maximum length of 80bytes: " + str);
        }
        synchronized (this.mLock) {
            for (int i = 0; i < this.mInputDeviceDescriptors.size(); i++) {
                if (this.mInputDeviceDescriptors.valueAt(i).mName.equals(str)) {
                    throw new DeviceCreationException("Input device name already in use: " + str);
                }
            }
        }
    }

    private static String createPhys(String str) {
        return String.format("virtual%s:%d", str, Long.valueOf(sNextPhysId.getAndIncrement()));
    }

    private void setUniqueIdAssociation(int i, String str) {
        InputManagerGlobal.getInstance().addUniqueIdAssociation(str, this.mDisplayManagerInternal.getDisplayInfo(i).uniqueId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendDpadKeyEvent(IBinder iBinder, VirtualKeyEvent virtualKeyEvent) {
        boolean writeDpadKeyEvent;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not send key event to input device for given token");
            }
            writeDpadKeyEvent = this.mNativeWrapper.writeDpadKeyEvent(inputDeviceDescriptor.getNativePointer(), virtualKeyEvent.getKeyCode(), virtualKeyEvent.getAction(), virtualKeyEvent.getEventTimeNanos());
        }
        return writeDpadKeyEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendKeyEvent(IBinder iBinder, VirtualKeyEvent virtualKeyEvent) {
        boolean writeKeyEvent;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not send key event to input device for given token");
            }
            writeKeyEvent = this.mNativeWrapper.writeKeyEvent(inputDeviceDescriptor.getNativePointer(), virtualKeyEvent.getKeyCode(), virtualKeyEvent.getAction(), virtualKeyEvent.getEventTimeNanos());
        }
        return writeKeyEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendButtonEvent(IBinder iBinder, VirtualMouseButtonEvent virtualMouseButtonEvent) {
        boolean writeButtonEvent;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not send button event to input device for given token");
            }
            if (inputDeviceDescriptor.getDisplayId() != this.mInputManagerInternal.getVirtualMousePointerDisplayId()) {
                throw new IllegalStateException("Display id associated with this mouse is not currently targetable");
            }
            writeButtonEvent = this.mNativeWrapper.writeButtonEvent(inputDeviceDescriptor.getNativePointer(), virtualMouseButtonEvent.getButtonCode(), virtualMouseButtonEvent.getAction(), virtualMouseButtonEvent.getEventTimeNanos());
        }
        return writeButtonEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendTouchEvent(IBinder iBinder, VirtualTouchEvent virtualTouchEvent) {
        boolean writeTouchEvent;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not send touch event to input device for given token");
            }
            writeTouchEvent = this.mNativeWrapper.writeTouchEvent(inputDeviceDescriptor.getNativePointer(), virtualTouchEvent.getPointerId(), virtualTouchEvent.getToolType(), virtualTouchEvent.getAction(), virtualTouchEvent.getX(), virtualTouchEvent.getY(), virtualTouchEvent.getPressure(), virtualTouchEvent.getMajorAxisSize(), virtualTouchEvent.getEventTimeNanos());
        }
        return writeTouchEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendRelativeEvent(IBinder iBinder, VirtualMouseRelativeEvent virtualMouseRelativeEvent) {
        boolean writeRelativeEvent;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not send relative event to input device for given token");
            }
            if (inputDeviceDescriptor.getDisplayId() != this.mInputManagerInternal.getVirtualMousePointerDisplayId()) {
                throw new IllegalStateException("Display id associated with this mouse is not currently targetable");
            }
            writeRelativeEvent = this.mNativeWrapper.writeRelativeEvent(inputDeviceDescriptor.getNativePointer(), virtualMouseRelativeEvent.getRelativeX(), virtualMouseRelativeEvent.getRelativeY(), virtualMouseRelativeEvent.getEventTimeNanos());
        }
        return writeRelativeEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendScrollEvent(IBinder iBinder, VirtualMouseScrollEvent virtualMouseScrollEvent) {
        boolean writeScrollEvent;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not send scroll event to input device for given token");
            }
            if (inputDeviceDescriptor.getDisplayId() != this.mInputManagerInternal.getVirtualMousePointerDisplayId()) {
                throw new IllegalStateException("Display id associated with this mouse is not currently targetable");
            }
            writeScrollEvent = this.mNativeWrapper.writeScrollEvent(inputDeviceDescriptor.getNativePointer(), virtualMouseScrollEvent.getXAxisMovement(), virtualMouseScrollEvent.getYAxisMovement(), virtualMouseScrollEvent.getEventTimeNanos());
        }
        return writeScrollEvent;
    }

    public PointF getCursorPosition(IBinder iBinder) {
        PointF cursorPosition;
        synchronized (this.mLock) {
            InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(iBinder);
            if (inputDeviceDescriptor == null) {
                throw new IllegalArgumentException("Could not get cursor position for input device for given token");
            }
            if (inputDeviceDescriptor.getDisplayId() != this.mInputManagerInternal.getVirtualMousePointerDisplayId()) {
                throw new IllegalStateException("Display id associated with this mouse is not currently targetable");
            }
            cursorPosition = ((InputManagerInternal) LocalServices.getService(InputManagerInternal.class)).getCursorPosition();
        }
        return cursorPosition;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("    InputController: ");
        synchronized (this.mLock) {
            printWriter.println("      Active descriptors: ");
            for (int i = 0; i < this.mInputDeviceDescriptors.size(); i++) {
                InputDeviceDescriptor valueAt = this.mInputDeviceDescriptors.valueAt(i);
                printWriter.println("        ptr: " + valueAt.getNativePointer());
                printWriter.println("          displayId: " + valueAt.getDisplayId());
                printWriter.println("          creationOrder: " + valueAt.getCreationOrderNumber());
                printWriter.println("          type: " + valueAt.getType());
                printWriter.println("          phys: " + valueAt.getPhys());
                printWriter.println("          inputDeviceId: " + valueAt.getInputDeviceId());
            }
        }
    }

    @VisibleForTesting
    void addDeviceForTesting(IBinder iBinder, long j, int i, int i2, String str, String str2, int i3) {
        synchronized (this.mLock) {
            this.mInputDeviceDescriptors.put(iBinder, new InputDeviceDescriptor(j, new IBinder.DeathRecipient() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda5
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    InputController.lambda$addDeviceForTesting$6();
                }
            }, i, i2, str, str2, i3));
        }
    }

    @VisibleForTesting
    Map<IBinder, InputDeviceDescriptor> getInputDeviceDescriptors() {
        ArrayMap arrayMap = new ArrayMap();
        synchronized (this.mLock) {
            arrayMap.putAll((Map) this.mInputDeviceDescriptors);
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class NativeWrapper {
        protected NativeWrapper() {
        }

        public long openUinputDpad(String str, int i, int i2, String str2) {
            return InputController.nativeOpenUinputDpad(str, i, i2, str2);
        }

        public long openUinputKeyboard(String str, int i, int i2, String str2) {
            return InputController.nativeOpenUinputKeyboard(str, i, i2, str2);
        }

        public long openUinputMouse(String str, int i, int i2, String str2) {
            return InputController.nativeOpenUinputMouse(str, i, i2, str2);
        }

        public long openUinputTouchscreen(String str, int i, int i2, String str2, int i3, int i4) {
            return InputController.nativeOpenUinputTouchscreen(str, i, i2, str2, i3, i4);
        }

        public void closeUinput(long j) {
            InputController.nativeCloseUinput(j);
        }

        public boolean writeDpadKeyEvent(long j, int i, int i2, long j2) {
            return InputController.nativeWriteDpadKeyEvent(j, i, i2, j2);
        }

        public boolean writeKeyEvent(long j, int i, int i2, long j2) {
            return InputController.nativeWriteKeyEvent(j, i, i2, j2);
        }

        public boolean writeButtonEvent(long j, int i, int i2, long j2) {
            return InputController.nativeWriteButtonEvent(j, i, i2, j2);
        }

        public boolean writeTouchEvent(long j, int i, int i2, int i3, float f, float f2, float f3, float f4, long j2) {
            return InputController.nativeWriteTouchEvent(j, i, i2, i3, f, f2, f3, f4, j2);
        }

        public boolean writeRelativeEvent(long j, float f, float f2, long j2) {
            return InputController.nativeWriteRelativeEvent(j, f, f2, j2);
        }

        public boolean writeScrollEvent(long j, float f, float f2, long j2) {
            return InputController.nativeWriteScrollEvent(j, f, f2, j2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class InputDeviceDescriptor {
        static final int TYPE_DPAD = 4;
        static final int TYPE_KEYBOARD = 1;
        static final int TYPE_MOUSE = 2;
        static final int TYPE_NAVIGATION_TOUCHPAD = 5;
        static final int TYPE_TOUCHSCREEN = 3;
        private static final AtomicLong sNextCreationOrderNumber = new AtomicLong(1);
        private final long mCreationOrderNumber = sNextCreationOrderNumber.getAndIncrement();
        private final IBinder.DeathRecipient mDeathRecipient;
        private final int mDisplayId;
        private final int mInputDeviceId;
        private final String mName;
        private final String mPhys;
        private final long mPtr;
        private final int mType;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        @interface Type {
        }

        InputDeviceDescriptor(long j, IBinder.DeathRecipient deathRecipient, int i, int i2, String str, String str2, int i3) {
            this.mPtr = j;
            this.mDeathRecipient = deathRecipient;
            this.mType = i;
            this.mDisplayId = i2;
            this.mPhys = str;
            this.mName = str2;
            this.mInputDeviceId = i3;
        }

        public long getNativePointer() {
            return this.mPtr;
        }

        public int getType() {
            return this.mType;
        }

        public boolean isMouse() {
            return this.mType == 2;
        }

        public IBinder.DeathRecipient getDeathRecipient() {
            return this.mDeathRecipient;
        }

        public int getDisplayId() {
            return this.mDisplayId;
        }

        public long getCreationOrderNumber() {
            return this.mCreationOrderNumber;
        }

        public String getPhys() {
            return this.mPhys;
        }

        public int getInputDeviceId() {
            return this.mInputDeviceId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BinderDeathRecipient implements IBinder.DeathRecipient {
        private final IBinder mDeviceToken;

        BinderDeathRecipient(IBinder iBinder) {
            this.mDeviceToken = iBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Slog.e(InputController.TAG, "Virtual input controller binder died");
            InputController.this.unregisterInputDevice(this.mDeviceToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class WaitForDevice implements AutoCloseable {
        private final CountDownLatch mDeviceAddedLatch = new CountDownLatch(1);
        private int mInputDeviceId = -2;
        private final InputManager.InputDeviceListener mListener;

        WaitForDevice(final String str, final int i, final int i2) {
            InputManager.InputDeviceListener inputDeviceListener = new InputManager.InputDeviceListener() { // from class: com.android.server.companion.virtual.InputController.WaitForDevice.1
                @Override // android.hardware.input.InputManager.InputDeviceListener
                public void onInputDeviceChanged(int i3) {
                }

                @Override // android.hardware.input.InputManager.InputDeviceListener
                public void onInputDeviceRemoved(int i3) {
                }

                @Override // android.hardware.input.InputManager.InputDeviceListener
                public void onInputDeviceAdded(int i3) {
                    InputDevice inputDevice = InputManagerGlobal.getInstance().getInputDevice(i3);
                    Objects.requireNonNull(inputDevice, "Newly added input device was null.");
                    if (inputDevice.getName().equals(str)) {
                        InputDeviceIdentifier identifier = inputDevice.getIdentifier();
                        if (identifier.getVendorId() == i && identifier.getProductId() == i2) {
                            WaitForDevice.this.mInputDeviceId = i3;
                            WaitForDevice.this.mDeviceAddedLatch.countDown();
                        }
                    }
                }
            };
            this.mListener = inputDeviceListener;
            InputManagerGlobal.getInstance().registerInputDeviceListener(inputDeviceListener, InputController.this.mHandler);
        }

        int waitForDeviceCreation() throws DeviceCreationException {
            try {
                if (!this.mDeviceAddedLatch.await(1L, TimeUnit.MINUTES)) {
                    throw new DeviceCreationException("Timed out waiting for virtual device to be created.");
                }
                int i = this.mInputDeviceId;
                if (i != -2) {
                    return i;
                }
                throw new IllegalStateException("Virtual input device was created with an invalid id=" + this.mInputDeviceId);
            } catch (InterruptedException e) {
                throw new DeviceCreationException("Interrupted while waiting for virtual device to be created.", e);
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            InputManagerGlobal.getInstance().unregisterInputDeviceListener(this.mListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DeviceCreationException extends Exception {
        DeviceCreationException(String str) {
            super(str);
        }

        DeviceCreationException(String str, Exception exc) {
            super(str, exc);
        }
    }

    private void createDeviceInternal(int i, String str, int i2, int i3, IBinder iBinder, int i4, String str2, Supplier<Long> supplier) throws DeviceCreationException {
        if (!this.mThreadVerifier.isValidThread()) {
            throw new IllegalStateException("Virtual device creation should happen on an auxiliary thread (e.g. binder thread) and not from the handler's thread.");
        }
        validateDeviceName(str);
        setUniqueIdAssociation(i4, str2);
        try {
            WaitForDevice waitForDevice = new WaitForDevice(str, i2, i3);
            try {
                long longValue = supplier.get().longValue();
                if (longValue == 0) {
                    throw new DeviceCreationException("A native error occurred when creating virtual input device: " + str);
                }
                try {
                    int waitForDeviceCreation = waitForDevice.waitForDeviceCreation();
                    BinderDeathRecipient binderDeathRecipient = new BinderDeathRecipient(iBinder);
                    try {
                        iBinder.linkToDeath(binderDeathRecipient, 0);
                        waitForDevice.close();
                        synchronized (this.mLock) {
                            this.mInputDeviceDescriptors.put(iBinder, new InputDeviceDescriptor(longValue, binderDeathRecipient, i, i4, str2, str, waitForDeviceCreation));
                        }
                    } catch (RemoteException e) {
                        throw new DeviceCreationException("Client died before virtual device could be created.", e);
                    }
                } catch (DeviceCreationException e2) {
                    this.mNativeWrapper.closeUinput(longValue);
                    throw e2;
                }
            } finally {
            }
        } catch (DeviceCreationException e3) {
            InputManagerGlobal.getInstance().removeUniqueIdAssociation(str2);
            throw e3;
        }
    }
}
