package com.oplus.wrapper.app;

import java.util.Locale;

/* loaded from: classes.dex */
public class LocalePicker {
    private LocalePicker() {
    }

    public static void updateLocale(Locale locale) {
        com.android.internal.app.LocalePicker.updateLocale(locale);
    }
}
