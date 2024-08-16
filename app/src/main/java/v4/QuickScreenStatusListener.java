package v4;

import android.content.Intent;
import android.os.OplusPowerManager;
import b6.LocalLog;
import com.oplus.os.IOplusScreenStatusListener;
import com.oplus.os.OplusScreenStatusListener;
import java.lang.reflect.InvocationTargetException;
import w4.Affair;

/* compiled from: QuickScreenStatusListener.java */
/* renamed from: v4.h, reason: use source file name */
/* loaded from: classes.dex */
public class QuickScreenStatusListener extends OplusScreenStatusListener {
    public void onScreenOff() {
        LocalLog.a("QuickScreenStatusListener", "onScreenOff");
        Affair.f().d(311, new Intent());
    }

    public void onScreenOn() {
        LocalLog.a("QuickScreenStatusListener", "onScreenOn");
        Affair.f().d(310, new Intent());
    }

    public void z() {
        LocalLog.a("QuickScreenStatusListener", "register");
        OplusPowerManager oplusPowerManager = new OplusPowerManager();
        try {
            oplusPowerManager.getClass().getDeclaredMethod("registerScreenStatusListener", IOplusScreenStatusListener.class).invoke(oplusPowerManager, this);
            LocalLog.a("QuickScreenStatusListener", "register ok");
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e10) {
            LocalLog.a("QuickScreenStatusListener", "register fail e=" + e10);
        }
    }
}
