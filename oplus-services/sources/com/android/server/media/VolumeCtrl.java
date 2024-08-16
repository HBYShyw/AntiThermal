package com.android.server.media;

import android.media.AudioSystem;
import android.media.IAudioService;
import android.os.ServiceManager;
import android.util.AndroidException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class VolumeCtrl {
    private static final String ADJUST_LOWER = "lower";
    private static final String ADJUST_RAISE = "raise";
    private static final String ADJUST_SAME = "same";
    private static final String LOG_E = "[E]";
    private static final String LOG_V = "[V]";
    private static final String TAG = "VolumeCtrl";
    public static final String USAGE = new String("the options are as follows: \n\t\t--stream STREAM selects the stream to control, see AudioManager.STREAM_*\n\t\t                controls AudioManager.STREAM_MUSIC if no stream is specified\n\t\t--set INDEX     sets the volume index value\n\t\t--adj DIRECTION adjusts the volume, use raise|same|lower for the direction\n\t\t--get           outputs the current volume\n\t\t--show          shows the UI during the volume change\n\texamples:\n\t\tadb shell media volume --show --stream 3 --set 11\n\t\tadb shell media volume --stream 0 --adj lower\n\t\tadb shell media volume --stream 3 --get\n");
    private static final int VOLUME_CONTROL_MODE_ADJUST = 2;
    private static final int VOLUME_CONTROL_MODE_SET = 1;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0054 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x009f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00a2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00c6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00cf A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x006b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0154  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void run(MediaShellCommand mediaShellCommand) throws Exception {
        IAudioService asInterface;
        boolean z;
        int i = 5;
        int i2 = 0;
        String str = null;
        int i3 = 3;
        char c = 0;
        int i4 = 0;
        boolean z2 = false;
        while (true) {
            String nextOption = mediaShellCommand.getNextOption();
            char c2 = 65535;
            if (nextOption != null) {
                switch (nextOption.hashCode()) {
                    case 42995463:
                        if (nextOption.equals("--adj")) {
                            c2 = 0;
                        }
                        switch (c2) {
                            case 0:
                                str = mediaShellCommand.getNextArgRequired();
                                mediaShellCommand.log(LOG_V, "will adjust volume");
                                c = 2;
                                break;
                            case 1:
                                mediaShellCommand.log(LOG_V, "will get volume");
                                z2 = true;
                                break;
                            case 2:
                                i = Integer.decode(mediaShellCommand.getNextArgRequired()).intValue();
                                mediaShellCommand.log(LOG_V, "will set volume to index=" + i);
                                c = (char) 1;
                                break;
                            case 3:
                                i4 = 1;
                                break;
                            case 4:
                                i3 = Integer.decode(mediaShellCommand.getNextArgRequired()).intValue();
                                mediaShellCommand.log(LOG_V, "will control stream=" + i3 + " (" + streamName(i3) + ")");
                                break;
                            default:
                                throw new IllegalArgumentException("Unknown argument " + nextOption);
                        }
                    case 43001270:
                        if (nextOption.equals("--get")) {
                            c2 = 1;
                        }
                        switch (c2) {
                        }
                        break;
                    case 43012802:
                        if (nextOption.equals("--set")) {
                            c2 = 2;
                        }
                        switch (c2) {
                        }
                        break;
                    case 1333399709:
                        if (nextOption.equals("--show")) {
                            c2 = 3;
                        }
                        switch (c2) {
                        }
                        break;
                    case 1508023584:
                        if (nextOption.equals("--stream")) {
                            c2 = 4;
                        }
                        switch (c2) {
                        }
                        break;
                    default:
                        switch (c2) {
                        }
                        break;
                }
            } else {
                if (c == 2) {
                    if (str == null) {
                        mediaShellCommand.showError("Error: no valid volume adjustment (null)");
                        return;
                    }
                    switch (str.hashCode()) {
                        case 3522662:
                            if (str.equals(ADJUST_SAME)) {
                                z = false;
                                break;
                            }
                            z = -1;
                            break;
                        case 103164673:
                            if (str.equals(ADJUST_LOWER)) {
                                z = true;
                                break;
                            }
                            z = -1;
                            break;
                        case 108275692:
                            if (str.equals(ADJUST_RAISE)) {
                                z = 2;
                                break;
                            }
                            z = -1;
                            break;
                        default:
                            z = -1;
                            break;
                    }
                    switch (z) {
                        case false:
                            break;
                        case true:
                            i2 = -1;
                            break;
                        case true:
                            break;
                        default:
                            mediaShellCommand.showError("Error: no valid volume adjustment, was " + str + ", expected " + ADJUST_LOWER + "|" + ADJUST_SAME + "|" + ADJUST_RAISE);
                            return;
                    }
                    mediaShellCommand.log(LOG_V, "Connecting to AudioService");
                    asInterface = IAudioService.Stub.asInterface(ServiceManager.checkService("audio"));
                    if (asInterface != null) {
                        mediaShellCommand.log(LOG_E, "Error type 2");
                        throw new AndroidException("Can't connect to audio service; is the system running?");
                    }
                    if (c == 1 && (i > asInterface.getStreamMaxVolume(i3) || i < asInterface.getStreamMinVolume(i3))) {
                        mediaShellCommand.showError(String.format("Error: invalid volume index %d for stream %d (should be in [%d..%d])", Integer.valueOf(i), Integer.valueOf(i3), Integer.valueOf(asInterface.getStreamMinVolume(i3)), Integer.valueOf(asInterface.getStreamMaxVolume(i3))));
                        return;
                    }
                    String name = mediaShellCommand.getClass().getPackage().getName();
                    if (c == 1) {
                        asInterface.setStreamVolume(i3, i, i4, name);
                    } else if (c == 2) {
                        asInterface.adjustStreamVolume(i3, i2, i4, name);
                    }
                    if (z2) {
                        mediaShellCommand.log(LOG_V, "volume is " + asInterface.getStreamVolume(i3) + " in range [" + asInterface.getStreamMinVolume(i3) + ".." + asInterface.getStreamMaxVolume(i3) + "]");
                        return;
                    }
                    return;
                }
                i2 = 1;
                mediaShellCommand.log(LOG_V, "Connecting to AudioService");
                asInterface = IAudioService.Stub.asInterface(ServiceManager.checkService("audio"));
                if (asInterface != null) {
                }
            }
        }
    }

    static String streamName(int i) {
        try {
            return AudioSystem.STREAM_NAMES[i];
        } catch (ArrayIndexOutOfBoundsException unused) {
            return "invalid stream";
        }
    }
}
