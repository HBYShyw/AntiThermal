package a9;

import android.content.Context;
import android.media.AudioManager;
import b6.LocalLog;
import java.lang.reflect.InvocationTargetException;

/* compiled from: SuperEnduranceUtils.java */
/* renamed from: a9.c, reason: use source file name */
/* loaded from: classes2.dex */
public class SuperEnduranceUtils {
    public static void a(Context context, boolean z10) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        LocalLog.a("SuperEnduranceUtils", "setAudioPAState: state = " + z10);
        audioManager.setParameters("super_endurance_mode=" + z10);
    }

    public static int b(boolean z10) {
        LocalLog.a("SuperEnduranceUtils", "setSuperEnduranceStatus: status = " + z10);
        int i10 = -1;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            i10 = ((Integer) cls.getMethod("setSuperEnduranceStatus", Boolean.TYPE).invoke(cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]), Boolean.valueOf(z10))).intValue();
            LocalLog.a("SuperEnduranceUtils", "setSuperEnduranceStatus: success: " + i10);
            return i10;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e10) {
            e10.printStackTrace();
            LocalLog.l("SuperEnduranceUtils", "setSuperEnduranceStatus fail!");
            return i10;
        }
    }
}
