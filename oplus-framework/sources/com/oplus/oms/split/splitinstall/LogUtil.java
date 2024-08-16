package com.oplus.oms.split.splitinstall;

import com.oplus.oms.split.common.Identifiable;
import java.util.Collection;

/* loaded from: classes.dex */
public class LogUtil {
    private LogUtil() {
    }

    public static String toString(Collection<? extends Identifiable> collection) {
        if (collection == null || collection.isEmpty()) {
            return "empty";
        }
        StringBuilder sb = new StringBuilder("[");
        for (Identifiable id : collection) {
            sb.append(toString(id)).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String toString(Identifiable identifiable) {
        if (identifiable instanceof SplitVersionInfo) {
            SplitVersionInfo svi = (SplitVersionInfo) identifiable;
            String str = svi.getSplitInfo().getSplitName() + "@" + svi.getInstallVersionName() + "@" + svi.getInstallVersionCode() + "@" + svi.getFrom();
            if (svi.getDefaultVersionInfo() != null) {
                SplitVersionInfo def = svi.getDefaultVersionInfo();
                return str + "@Def[" + def.getInstallVersionName() + "@" + def.getInstallVersionCode() + "@" + def.getFrom() + "]";
            }
            return str;
        }
        return identifiable.getIdentify();
    }

    public static String hexHash(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getSimpleName() + "@0x" + Long.toHexString(obj.hashCode());
    }
}
