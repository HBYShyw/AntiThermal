package android.os;

import android.os.IPowerManagerExt;
import android.util.Log;

/* loaded from: classes.dex */
public class PowerManagerExtImpl implements IPowerManagerExt {
    public PowerManagerExtImpl(Object base) {
    }

    public void printStackTraceInfo(String methodName) {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        if (stack != null) {
            for (int i = 0; i < 5 && i < stack.length; i++) {
                Log.i("PowerManager", methodName + "    |----" + stack[i].toString());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements IPowerManagerExt.IStaticExt {
        private StaticExtImpl() {
        }

        public static StaticExtImpl getInstance(Object obj) {
            return LazyHolder.INSTANCE;
        }

        /* loaded from: classes.dex */
        private static class LazyHolder {
            private static final StaticExtImpl INSTANCE = new StaticExtImpl();

            private LazyHolder() {
            }
        }

        public String wakeReasonToStringExtend(int wakeReason) {
            switch (wakeReason) {
                case 97:
                    return OplusBasePowerManager.WAKE_UP_DUE_TO_PROXIMITY;
                case 98:
                    return OplusBasePowerManager.WAKE_UP_DUE_TO_FINGERPRINT;
                case 99:
                    return OplusBasePowerManager.WAKE_UP_DUE_TO_DOUBLE_HOME;
                case 100:
                    return OplusBasePowerManager.WAKE_UP_DUE_TO_DOUBLE_TAP_SCREEN;
                case 101:
                    return OplusBasePowerManager.WAKE_UP_DUE_TO_LIFT_HAND;
                case 102:
                    return OplusBasePowerManager.WAKE_UP_DUE_TO_WINDOWMANAGER_TURN_SCREENON;
                case 103:
                    return OplusBasePowerManager.WAKE_UP_DUE_TO_SYSTEM_UI_CLEAN_UP;
                default:
                    return null;
            }
        }
    }
}
