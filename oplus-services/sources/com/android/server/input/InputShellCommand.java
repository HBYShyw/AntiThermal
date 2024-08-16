package com.android.server.input;

import android.hardware.input.InputManagerGlobal;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.IntArray;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.pm.DumpState;
import com.android.server.usb.descriptors.UsbACInterface;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class InputShellCommand extends ShellCommand {
    private static final int DEFAULT_BUTTON_STATE = 0;
    private static final int DEFAULT_DEVICE_ID = 0;
    private static final int DEFAULT_EDGE_FLAGS = 0;
    private static final int DEFAULT_FLAGS = 0;
    private static final int DEFAULT_META_STATE = 0;
    private static final float DEFAULT_PRECISION_X = 1.0f;
    private static final float DEFAULT_PRECISION_Y = 1.0f;
    private static final float DEFAULT_PRESSURE = 1.0f;
    private static final float DEFAULT_SIZE = 1.0f;
    private static final String INVALID_ARGUMENTS = "Error: Invalid arguments for command: ";
    private static final String INVALID_DISPLAY_ARGUMENTS = "Error: Invalid arguments for display ID.";
    private static final Map<Integer, Integer> MODIFIER;
    private static final float NO_PRESSURE = 0.0f;
    private static final Map<String, Integer> SOURCES;

    private int getSource(int i, int i2) {
        return i == 0 ? i2 : i;
    }

    private int getToolType(int i) {
        switch (i) {
            case UsbACInterface.FORMAT_II_AC3 /* 4098 */:
            case 1048584:
            case DumpState.DUMP_COMPILER_STATS /* 2097152 */:
                return 1;
            case UsbACInterface.FORMAT_III_IEC1937_MPEG1_Layer1 /* 8194 */:
            case 65540:
            case 131076:
                return 3;
            case 16386:
            case 49154:
                return 2;
            default:
                return 0;
        }
    }

    private float lerp(float f, float f2, float f3) {
        return ((f2 - f) * f3) + f;
    }

    static {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(Integer.valueOf(HdmiCecKeycode.CEC_KEYCODE_F1_BLUE), 12288);
        arrayMap.put(Integer.valueOf(HdmiCecKeycode.CEC_KEYCODE_F2_RED), 20480);
        arrayMap.put(57, 18);
        arrayMap.put(58, 34);
        arrayMap.put(59, 65);
        arrayMap.put(60, 129);
        arrayMap.put(Integer.valueOf(HdmiCecKeycode.CEC_KEYCODE_F5), 196608);
        arrayMap.put(Integer.valueOf(HdmiCecKeycode.CEC_KEYCODE_DATA), 327680);
        MODIFIER = Collections.unmodifiableMap(arrayMap);
        ArrayMap arrayMap2 = new ArrayMap();
        arrayMap2.put("keyboard", Integer.valueOf(UsbTerminalTypes.TERMINAL_USB_STREAMING));
        arrayMap2.put("dpad", Integer.valueOf(UsbTerminalTypes.TERMINAL_IN_MIC));
        arrayMap2.put("gamepad", Integer.valueOf(UsbTerminalTypes.TERMINAL_BIDIR_HANDSET));
        arrayMap2.put("touchscreen", Integer.valueOf(UsbACInterface.FORMAT_II_AC3));
        arrayMap2.put("mouse", Integer.valueOf(UsbACInterface.FORMAT_III_IEC1937_MPEG1_Layer1));
        arrayMap2.put("stylus", 16386);
        arrayMap2.put("trackball", 65540);
        arrayMap2.put("touchpad", 1048584);
        arrayMap2.put("touchnavigation", Integer.valueOf(DumpState.DUMP_COMPILER_STATS));
        arrayMap2.put("joystick", 16777232);
        SOURCES = Collections.unmodifiableMap(arrayMap2);
    }

    private void injectKeyEvent(KeyEvent keyEvent) {
        InputManagerGlobal.getInstance().injectInputEvent(keyEvent, 2);
    }

    private int getInputDeviceId(int i) {
        for (int i2 : InputDevice.getDeviceIds()) {
            if (InputDevice.getDevice(i2).supportsSource(i)) {
                return i2;
            }
        }
        return 0;
    }

    private int getDisplayId() {
        String nextArgRequired = getNextArgRequired();
        if ("INVALID_DISPLAY".equalsIgnoreCase(nextArgRequired)) {
            return -1;
        }
        if ("DEFAULT_DISPLAY".equalsIgnoreCase(nextArgRequired)) {
            return 0;
        }
        try {
            int parseInt = Integer.parseInt(nextArgRequired);
            if (parseInt == -1) {
                return -1;
            }
            return Math.max(parseInt, 0);
        } catch (NumberFormatException unused) {
            throw new IllegalArgumentException(INVALID_DISPLAY_ARGUMENTS);
        }
    }

    private void injectMotionEvent(int i, int i2, long j, long j2, float f, float f2, float f3, int i3) {
        MotionEvent.PointerProperties[] pointerPropertiesArr = {new MotionEvent.PointerProperties()};
        MotionEvent.PointerProperties pointerProperties = pointerPropertiesArr[0];
        pointerProperties.id = 0;
        pointerProperties.toolType = getToolType(i);
        MotionEvent.PointerCoords[] pointerCoordsArr = {new MotionEvent.PointerCoords()};
        MotionEvent.PointerCoords pointerCoords = pointerCoordsArr[0];
        pointerCoords.x = f;
        pointerCoords.y = f2;
        pointerCoords.pressure = f3;
        pointerCoords.size = 1.0f;
        InputManagerGlobal.getInstance().injectInputEvent(MotionEvent.obtain(j, j2, i2, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 1.0f, 1.0f, getInputDeviceId(i), 0, i, (i3 != -1 || (i & 2) == 0) ? i3 : 0, 0), 2);
    }

    public final int onCommand(String str) {
        int i;
        int i2;
        Map<String, Integer> map = SOURCES;
        if (map.containsKey(str)) {
            i = map.get(str).intValue();
            str = getNextArgRequired();
        } else {
            i = 0;
        }
        if ("-d".equals(str)) {
            i2 = getDisplayId();
            str = getNextArgRequired();
        } else {
            i2 = -1;
        }
        try {
            if ("text".equals(str)) {
                runText(i, i2);
            } else if ("keyevent".equals(str)) {
                runKeyEvent(i, i2);
            } else if ("tap".equals(str)) {
                runTap(i, i2);
            } else if ("swipe".equals(str)) {
                runSwipe(i, i2);
            } else if ("draganddrop".equals(str)) {
                runDragAndDrop(i, i2);
            } else if ("press".equals(str)) {
                runPress(i, i2);
            } else if ("roll".equals(str)) {
                runRoll(i, i2);
            } else if ("motionevent".equals(str)) {
                runMotionEvent(i, i2);
            } else if ("keycombination".equals(str)) {
                runKeyCombination(i, i2);
            } else {
                handleDefaultCommands(str);
            }
            return 0;
        } catch (NumberFormatException unused) {
            throw new IllegalArgumentException(INVALID_ARGUMENTS + str);
        }
    }

    public final void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            outPrintWriter.println("Usage: input [<source>] [-d DISPLAY_ID] <command> [<arg>...]");
            outPrintWriter.println();
            outPrintWriter.println("The sources are: ");
            Iterator<String> it = SOURCES.keySet().iterator();
            while (it.hasNext()) {
                outPrintWriter.println("      " + it.next());
            }
            outPrintWriter.println();
            outPrintWriter.printf("-d: specify the display ID.\n      (Default: %d for key event, %d for motion event if not specified.)", -1, 0);
            outPrintWriter.println();
            outPrintWriter.println("The commands and default sources are:");
            outPrintWriter.println("      text <string> (Default: touchscreen)");
            outPrintWriter.println("      keyevent [--longpress|--doubletap] <key code number or name> ... (Default: keyboard)");
            outPrintWriter.println("      tap <x> <y> (Default: touchscreen)");
            outPrintWriter.println("      swipe <x1> <y1> <x2> <y2> [duration(ms)] (Default: touchscreen)");
            outPrintWriter.println("      draganddrop <x1> <y1> <x2> <y2> [duration(ms)] (Default: touchscreen)");
            outPrintWriter.println("      press (Default: trackball)");
            outPrintWriter.println("      roll <dx> <dy> (Default: trackball)");
            outPrintWriter.println("      motionevent <DOWN|UP|MOVE|CANCEL> <x> <y> (Default: touchscreen)");
            outPrintWriter.println("      keycombination [-t duration(ms)] <key code 1> <key code 2> ... (Default: keyboard, the key order is important here.)");
            outPrintWriter.close();
        } catch (Throwable th) {
            if (outPrintWriter != null) {
                try {
                    outPrintWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void runText(int i, int i2) {
        sendText(getSource(i, UsbTerminalTypes.TERMINAL_USB_STREAMING), getNextArgRequired(), i2);
    }

    private void sendText(int i, String str, int i2) {
        StringBuilder sb = new StringBuilder(str);
        int i3 = 0;
        boolean z = false;
        while (i3 < sb.length()) {
            if (z) {
                if (sb.charAt(i3) == 's') {
                    sb.setCharAt(i3, ' ');
                    i3--;
                    sb.deleteCharAt(i3);
                }
                z = false;
            }
            if (sb.charAt(i3) == '%') {
                z = true;
            }
            i3++;
        }
        for (KeyEvent keyEvent : KeyCharacterMap.load(-1).getEvents(sb.toString().toCharArray())) {
            if (i != keyEvent.getSource()) {
                keyEvent.setSource(i);
            }
            keyEvent.setDisplayId(i2);
            injectKeyEvent(keyEvent);
        }
    }

    private void runKeyEvent(int i, int i2) {
        String nextArgRequired = getNextArgRequired();
        boolean equals = "--longpress".equals(nextArgRequired);
        if (equals) {
            nextArgRequired = getNextArgRequired();
        } else if ("--doubletap".equals(nextArgRequired)) {
            sendKeyDoubleTap(i, KeyEvent.keyCodeFromString(getNextArgRequired()), i2);
            return;
        }
        do {
            sendKeyEvent(i, KeyEvent.keyCodeFromString(nextArgRequired), equals, i2);
            nextArgRequired = getNextArg();
        } while (nextArgRequired != null);
    }

    private void sendKeyEvent(int i, int i2, boolean z, int i3) {
        long uptimeMillis = SystemClock.uptimeMillis();
        KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, 0, i2, 0, 0, -1, 0, 0, i);
        keyEvent.setDisplayId(i3);
        injectKeyEvent(keyEvent);
        if (z) {
            sleep(ViewConfiguration.getLongPressTimeout());
            injectKeyEvent(KeyEvent.changeTimeRepeat(keyEvent, uptimeMillis + ViewConfiguration.getLongPressTimeout(), 1, 128));
        }
        injectKeyEvent(KeyEvent.changeAction(keyEvent, 1));
    }

    private void sendKeyDoubleTap(int i, int i2, int i3) {
        sendKeyEvent(i, i2, false, i3);
        sleep(ViewConfiguration.getDoubleTapMinTime());
        sendKeyEvent(i, i2, false, i3);
    }

    private void runTap(int i, int i2) {
        sendTap(getSource(i, UsbACInterface.FORMAT_II_AC3), Float.parseFloat(getNextArgRequired()), Float.parseFloat(getNextArgRequired()), i2);
    }

    private void sendTap(int i, float f, float f2, int i2) {
        long uptimeMillis = SystemClock.uptimeMillis();
        injectMotionEvent(i, 0, uptimeMillis, uptimeMillis, f, f2, 1.0f, i2);
        injectMotionEvent(i, 1, uptimeMillis, uptimeMillis, f, f2, 0.0f, i2);
    }

    private void runPress(int i, int i2) {
        sendTap(getSource(i, 65540), 0.0f, 0.0f, i2);
    }

    private void runSwipe(int i, int i2) {
        sendSwipe(getSource(i, UsbACInterface.FORMAT_II_AC3), i2, false);
    }

    private void sendSwipe(int i, int i2, boolean z) {
        float parseFloat = Float.parseFloat(getNextArgRequired());
        float parseFloat2 = Float.parseFloat(getNextArgRequired());
        float parseFloat3 = Float.parseFloat(getNextArgRequired());
        float parseFloat4 = Float.parseFloat(getNextArgRequired());
        String nextArg = getNextArg();
        int parseInt = nextArg != null ? Integer.parseInt(nextArg) : -1;
        if (parseInt < 0) {
            parseInt = 300;
        }
        int i3 = parseInt;
        long uptimeMillis = SystemClock.uptimeMillis();
        injectMotionEvent(i, 0, uptimeMillis, uptimeMillis, parseFloat, parseFloat2, 1.0f, i2);
        if (z) {
            sleep(ViewConfiguration.getLongPressTimeout());
        }
        long j = uptimeMillis + i3;
        long uptimeMillis2 = SystemClock.uptimeMillis();
        while (uptimeMillis2 < j) {
            float f = ((float) (uptimeMillis2 - uptimeMillis)) / i3;
            injectMotionEvent(i, 2, uptimeMillis, uptimeMillis2, lerp(parseFloat, parseFloat3, f), lerp(parseFloat2, parseFloat4, f), 1.0f, i2);
            uptimeMillis2 = SystemClock.uptimeMillis();
        }
        injectMotionEvent(i, 1, uptimeMillis, uptimeMillis2, parseFloat3, parseFloat4, 0.0f, i2);
    }

    private void runDragAndDrop(int i, int i2) {
        sendSwipe(getSource(i, UsbACInterface.FORMAT_II_AC3), i2, true);
    }

    private void runRoll(int i, int i2) {
        sendMove(getSource(i, 65540), Float.parseFloat(getNextArgRequired()), Float.parseFloat(getNextArgRequired()), i2);
    }

    private void sendMove(int i, float f, float f2, int i2) {
        long uptimeMillis = SystemClock.uptimeMillis();
        injectMotionEvent(i, 2, uptimeMillis, uptimeMillis, f, f2, 0.0f, i2);
    }

    private int getAction() {
        String nextArgRequired = getNextArgRequired();
        String upperCase = nextArgRequired.toUpperCase();
        upperCase.hashCode();
        char c = 65535;
        switch (upperCase.hashCode()) {
            case 2715:
                if (upperCase.equals("UP")) {
                    c = 0;
                    break;
                }
                break;
            case 2104482:
                if (upperCase.equals("DOWN")) {
                    c = 1;
                    break;
                }
                break;
            case 2372561:
                if (upperCase.equals("MOVE")) {
                    c = 2;
                    break;
                }
                break;
            case 1980572282:
                if (upperCase.equals("CANCEL")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 1;
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown action: " + nextArgRequired);
        }
    }

    private void runMotionEvent(int i, int i2) {
        float parseFloat;
        float parseFloat2;
        float f;
        float f2;
        int source = getSource(i, UsbACInterface.FORMAT_II_AC3);
        int action = getAction();
        if (action == 0 || action == 2 || action == 1) {
            parseFloat = Float.parseFloat(getNextArgRequired());
            parseFloat2 = Float.parseFloat(getNextArgRequired());
        } else {
            String nextArg = getNextArg();
            String nextArg2 = getNextArg();
            if (nextArg != null && nextArg2 != null) {
                parseFloat = Float.parseFloat(nextArg);
                parseFloat2 = Float.parseFloat(nextArg2);
            } else {
                f2 = 0.0f;
                f = 0.0f;
                sendMotionEvent(source, action, f2, f, i2);
            }
        }
        f2 = parseFloat;
        f = parseFloat2;
        sendMotionEvent(source, action, f2, f, i2);
    }

    private void sendMotionEvent(int i, int i2, float f, float f2, int i3) {
        float f3 = (i2 == 0 || i2 == 2) ? 1.0f : 0.0f;
        long uptimeMillis = SystemClock.uptimeMillis();
        injectMotionEvent(i, i2, uptimeMillis, uptimeMillis, f, f2, f3, i3);
    }

    private void runKeyCombination(int i, int i2) {
        long j;
        String nextArgRequired = getNextArgRequired();
        if ("-t".equals(nextArgRequired)) {
            j = Integer.parseInt(getNextArgRequired());
            nextArgRequired = getNextArgRequired();
        } else {
            j = 0;
        }
        IntArray intArray = new IntArray();
        while (nextArgRequired != null) {
            int keyCodeFromString = KeyEvent.keyCodeFromString(nextArgRequired);
            if (keyCodeFromString == 0) {
                throw new IllegalArgumentException("Unknown keycode: " + nextArgRequired);
            }
            intArray.add(keyCodeFromString);
            nextArgRequired = getNextArg();
        }
        if (intArray.size() < 2) {
            throw new IllegalArgumentException("keycombination requires at least 2 keycodes");
        }
        sendKeyCombination(i, intArray, i2, j);
    }

    private void injectKeyEventAsync(KeyEvent keyEvent) {
        InputManagerGlobal.getInstance().injectInputEvent(keyEvent, 0);
    }

    private void sendKeyCombination(int i, IntArray intArray, int i2, long j) {
        long uptimeMillis = SystemClock.uptimeMillis();
        int size = intArray.size();
        KeyEvent[] keyEventArr = new KeyEvent[size];
        Integer num = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < size) {
            int i5 = intArray.get(i3);
            int i6 = i3;
            Integer num2 = num;
            KeyEvent[] keyEventArr2 = keyEventArr;
            KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, 0, i5, 0, i4, -1, 0, 0, i);
            keyEvent.setDisplayId(i2);
            keyEventArr2[i6] = keyEvent;
            i4 |= MODIFIER.getOrDefault(Integer.valueOf(i5), num2).intValue();
            i3 = i6 + 1;
            size = size;
            num = num2;
            keyEventArr = keyEventArr2;
            uptimeMillis = uptimeMillis;
        }
        KeyEvent[] keyEventArr3 = keyEventArr;
        long j2 = uptimeMillis;
        Integer num3 = num;
        int i7 = size;
        for (int i8 = 0; i8 < i7; i8++) {
            injectKeyEventAsync(keyEventArr3[i8]);
        }
        sleep(j);
        for (int i9 = 0; i9 < i7; i9++) {
            int keyCode = keyEventArr3[i9].getKeyCode();
            injectKeyEventAsync(new KeyEvent(j2, j2, 1, keyCode, 0, i4, -1, 0, 0, i));
            i4 &= ~MODIFIER.getOrDefault(Integer.valueOf(keyCode), num3).intValue();
        }
    }

    private void sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
