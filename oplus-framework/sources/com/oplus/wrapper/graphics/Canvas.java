package com.oplus.wrapper.graphics;

/* loaded from: classes.dex */
public class Canvas {
    private Canvas() {
    }

    public static void freeCaches() {
        android.graphics.Canvas.freeCaches();
    }

    public static void freeTextLayoutCaches() {
        android.graphics.Canvas.freeTextLayoutCaches();
    }
}
