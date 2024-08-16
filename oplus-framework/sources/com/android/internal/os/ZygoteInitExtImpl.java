package com.android.internal.os;

import android.app.IOplusCommonInjector;
import android.common.OplusFeatureCache;
import android.content.res.Resources;
import android.util.Slog;
import com.oplus.phoenix.Phoenix;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class ZygoteInitExtImpl implements IZygoteInitExt {
    public ZygoteInitExtImpl(Object base) {
    }

    public void hookPreloadResources(Resources resources, String tag) {
        ((IOplusCommonInjector) OplusFeatureCache.getOrCreate(IOplusCommonInjector.DEFAULT, new Object[0])).hookPreloadResources(resources, tag);
    }

    public void beginHookPreload() {
        Phoenix.setBootstage(Phoenix.ANDROID_ZYGOTE_PRELOAD_START);
    }

    public void endHookPreload() {
        Phoenix.setBootstage(Phoenix.ANDROID_ZYGOTE_PRELOAD_END);
    }

    public void beginHookGcAndFinalize(boolean enableLazyPreload) {
        if (!enableLazyPreload) {
            Phoenix.setBootstage(Phoenix.ANDROID_ZYGOTE_GC_INIT_START);
        }
    }

    public void endHookGcAndFinalize(boolean enableLazyPreload) {
        if (!enableLazyPreload) {
            Phoenix.setBootstage(Phoenix.ANDROID_ZYGOTE_GC_INIT_END);
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:30:0x001d -> B:6:0x003d). Please report as a decompilation issue!!! */
    public void addBootEvent(String bootevent) {
        FileOutputStream fbp = null;
        try {
        } catch (IOException e) {
            Slog.e("BOOTPROF", "Failure close /proc/bootprof entry", e);
        }
        try {
            try {
                fbp = new FileOutputStream("/proc/bootprof");
                fbp.write(bootevent.getBytes());
                fbp.flush();
                fbp.close();
            } catch (FileNotFoundException e2) {
                Slog.e("BOOTPROF", "Failure open /proc/bootprof, not found!", e2);
                if (fbp != null) {
                    fbp.close();
                }
            } catch (IOException e3) {
                Slog.e("BOOTPROF", "Failure open /proc/bootprof entry", e3);
                if (fbp != null) {
                    fbp.close();
                }
            }
        } catch (Throwable th) {
            if (fbp != null) {
                try {
                    fbp.close();
                } catch (IOException e4) {
                    Slog.e("BOOTPROF", "Failure close /proc/bootprof entry", e4);
                }
            }
            throw th;
        }
    }
}
