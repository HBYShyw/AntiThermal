package android.app;

/* loaded from: classes.dex */
public class ActivityThreadWrapper {
    public static Application currentApplication() {
        return ActivityThread.currentApplication();
    }

    public static String currentPackageName() {
        return ActivityThread.currentPackageName();
    }

    public static String currentProcessName() {
        return ActivityThread.currentProcessName();
    }
}
