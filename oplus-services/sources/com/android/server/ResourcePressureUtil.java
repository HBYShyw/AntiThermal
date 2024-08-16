package com.android.server;

import android.os.StrictMode;
import android.util.Slog;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ResourcePressureUtil {
    private static final List<String> PSI_FILES = Arrays.asList("/proc/pressure/memory", "/proc/pressure/cpu", "/proc/pressure/io");
    private static final String PSI_ROOT = "/proc/pressure";
    private static final String TAG = "ResourcePressureUtil";

    /* JADX INFO: Access modifiers changed from: private */
    public static String readResourcePsiState(String str) {
        StringWriter stringWriter = new StringWriter();
        try {
            if (new File(str).exists()) {
                stringWriter.append((CharSequence) ("----- Output from " + str + " -----\n"));
                stringWriter.append((CharSequence) IoUtils.readFileAsString(str));
                stringWriter.append((CharSequence) ("----- End output from " + str + " -----\n"));
            }
        } catch (IOException e) {
            Slog.e(TAG, " could not read " + str, e);
        }
        return stringWriter.toString();
    }

    public static String currentPsiState() {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        final StringWriter stringWriter = new StringWriter();
        try {
            PSI_FILES.stream().map(new Function() { // from class: com.android.server.ResourcePressureUtil$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String readResourcePsiState;
                    readResourcePsiState = ResourcePressureUtil.readResourcePsiState((String) obj);
                    return readResourcePsiState;
                }
            }).forEach(new Consumer() { // from class: com.android.server.ResourcePressureUtil$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    stringWriter.append((CharSequence) obj);
                }
            });
            StrictMode.setThreadPolicy(allowThreadDiskReads);
            String stringWriter2 = stringWriter.toString();
            if (stringWriter2.length() <= 0) {
                return stringWriter2;
            }
            return stringWriter2 + "\n";
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
            throw th;
        }
    }

    private ResourcePressureUtil() {
    }
}
