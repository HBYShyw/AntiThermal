package com.android.server.lights;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class LightsManager {
    public static final int LIGHT_ID_ATTENTION = 5;
    public static final int LIGHT_ID_BACKLIGHT = 0;
    public static final int LIGHT_ID_BATTERY = 3;
    public static final int LIGHT_ID_BLUETOOTH = 6;
    public static final int LIGHT_ID_BUTTONS = 2;
    public static final int LIGHT_ID_CAMERA = 7;
    public static final int LIGHT_ID_COUNT = 8;
    public static final int LIGHT_ID_GAME = 5;
    public static final int LIGHT_ID_INCALL = 6;
    public static final int LIGHT_ID_KEYBOARD = 1;
    public static final int LIGHT_ID_MUSIC = 1;
    public static final int LIGHT_ID_NOTIFICATIONS = 4;
    public static final int LIGHT_ID_WIFI = 7;

    public abstract LogicalLight getLight(int i);
}
