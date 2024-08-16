package android.app;

import com.oplus.reflect.RefBoolean;
import com.oplus.reflect.RefClass;

/* loaded from: classes.dex */
public class OplusMirrorActivityThread {
    public static RefBoolean DEBUG_BROADCAST;
    public static RefBoolean DEBUG_BROADCAST_LIGHT;
    public static RefBoolean DEBUG_CONFIGURATION;
    public static RefBoolean DEBUG_MEMORY_TRIM;
    public static RefBoolean DEBUG_MESSAGES;
    public static RefBoolean DEBUG_PROVIDER;
    public static RefBoolean DEBUG_SERVICE;
    public static Class<?> TYPE = RefClass.load(OplusMirrorActivityThread.class, ActivityThread.class);
    public static RefBoolean localLOGV;

    public static void setBooleanValue(RefBoolean refBoolean, boolean value) {
        if (refBoolean != null) {
            refBoolean.set((Object) null, value);
        }
    }
}
