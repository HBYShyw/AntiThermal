package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class Display {
    public static final int TYPE_WIFI = getTypeWifi();
    private final android.view.Display mDisplay;

    public Display(android.view.Display display) {
        this.mDisplay = display;
    }

    public android.view.Display getDisplay() {
        return this.mDisplay;
    }

    public int getType() {
        return this.mDisplay.getType();
    }

    public static String typeToString(int type) {
        return android.view.Display.typeToString(type);
    }

    private static int getTypeWifi() {
        return 3;
    }

    public boolean getDisplayInfo(DisplayInfo outDisplayInfo) {
        return this.mDisplay.getDisplayInfo(outDisplayInfo.getInnerDisplayInfo());
    }
}
