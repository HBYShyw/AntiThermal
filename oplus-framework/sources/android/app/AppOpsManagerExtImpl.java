package android.app;

import android.app.AppOpsManager;
import android.util.Slog;
import java.util.Map;

/* loaded from: classes.dex */
public class AppOpsManagerExtImpl implements IAppOpsManagerExt {

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final AppOpsManagerExtImpl INSTANCE = new AppOpsManagerExtImpl();

        private LazyHolder() {
        }
    }

    private AppOpsManagerExtImpl() {
    }

    public static AppOpsManagerExtImpl getInstance(Object obj) {
        return LazyHolder.INSTANCE;
    }

    public void putCustomRuntimeOps(Map<String, Integer> map) {
        for (int op : sCustomRuntimeOps) {
            if (sCustomOpPerms[(op - 10000) - 1] != null) {
                map.put(sCustomOpPerms[op - 10001], Integer.valueOf(op));
            }
        }
    }

    public void putCustomOpStrToOp(Map<String, Integer> map) {
        if (sCustomOpStrToOp.size() > 0) {
            map.putAll(sCustomOpStrToOp);
        }
    }

    public Integer getCustomOpToSwitch(int op) {
        if (op > 10000) {
            return new Integer(sCustomOpToSwitch[op - 10001]);
        }
        return null;
    }

    public Integer getCustomOpToDefaultMode(int op) {
        if (op > 10000) {
            return Integer.valueOf(sCustomOpDefaultMode[op - 10001]);
        }
        return null;
    }

    public String getCustomOpToName(int op) {
        if (op > 10000) {
            return op + (-10001) < sCustomOpNames.length ? sCustomOpNames[op - 10001] : "Unknown(" + op + ")";
        }
        return null;
    }

    public String getCustomOpToPublicName(int op) {
        if (op > 10000) {
            return sCustomOpToString[op - 10001];
        }
        return null;
    }

    public String getCustomOpToRestriction(int op) {
        if (op > 10000) {
            return sCustomOpRestrictions[op - 10001];
        }
        return null;
    }

    public Integer getCustomStrDebugOpToOp(String op) {
        for (int i = 0; i < sCustomOpNames.length; i++) {
            if (sCustomOpNames[i].equals(op)) {
                return Integer.valueOf(i + 10001);
            }
        }
        return null;
    }

    public String getCustomOpToPermission(int op) {
        if (op > 10000) {
            return sCustomOpPerms[op - 10001];
        }
        return null;
    }

    public boolean getCustomOpAllowReset(int op) {
        if (op > 10000) {
            return !sCustomOpDisableReset[op - 10001];
        }
        return false;
    }

    public String getCustomPermissionToOp(Integer opCode) {
        if (opCode != null && opCode.intValue() > 10000) {
            return sCustomOpToString[(opCode.intValue() - 10000) - 1];
        }
        return null;
    }

    public String[] getAllOpStrs(String[] strs) {
        String[] allOpStrs = new String[strs.length + sCustomOpToString.length];
        System.arraycopy(strs, 0, allOpStrs, 0, strs.length);
        System.arraycopy(sCustomOpToString, 0, allOpStrs, strs.length, sCustomOpToString.length);
        return allOpStrs;
    }

    public AppOpsManager.RestrictionBypass getCustomOpAllowSystemBypassRestriction(int op) {
        if (op > 10000) {
            return sCustomOpAllowSystemRestrictionBypass[op - 10001];
        }
        return null;
    }

    public boolean onCustomOpChanged(AppOpsManager.OnOpChangedListener callback, int op, String packageName) {
        if (op < 10000) {
            return false;
        }
        try {
            String cusOpName = sCustomOpToString[op - 10001];
            if (cusOpName != null) {
                callback.onOpChanged(cusOpName, packageName);
                return true;
            }
        } catch (Exception e) {
            Slog.w("AppOpsManager", "opChanged Exception " + e.getMessage() + ", Cus op = " + op);
        }
        return true;
    }
}
