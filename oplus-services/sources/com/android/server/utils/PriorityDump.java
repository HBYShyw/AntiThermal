package com.android.server.utils;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PriorityDump {
    public static final String PRIORITY_ARG = "--dump-priority";
    public static final String PRIORITY_ARG_CRITICAL = "CRITICAL";
    public static final String PRIORITY_ARG_HIGH = "HIGH";
    public static final String PRIORITY_ARG_NORMAL = "NORMAL";
    private static final int PRIORITY_TYPE_CRITICAL = 1;
    private static final int PRIORITY_TYPE_HIGH = 2;
    private static final int PRIORITY_TYPE_INVALID = 0;
    private static final int PRIORITY_TYPE_NORMAL = 3;
    public static final String PROTO_ARG = "--proto";

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface PriorityType {
    }

    private PriorityDump() {
        throw new UnsupportedOperationException();
    }

    public static void dump(PriorityDumper priorityDumper, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i = 0;
        if (strArr == null) {
            priorityDumper.dump(fileDescriptor, printWriter, strArr, false);
            return;
        }
        String[] strArr2 = new String[strArr.length];
        int i2 = 0;
        int i3 = 0;
        boolean z = false;
        while (i < strArr.length) {
            if (strArr[i].equals("--proto")) {
                z = true;
            } else if (strArr[i].equals(PRIORITY_ARG)) {
                int i4 = i + 1;
                if (i4 < strArr.length) {
                    i3 = getPriorityType(strArr[i4]);
                    i = i4;
                }
            } else {
                strArr2[i2] = strArr[i];
                i2++;
            }
            i++;
        }
        if (i2 < strArr.length) {
            strArr2 = (String[]) Arrays.copyOf(strArr2, i2);
        }
        if (i3 == 1) {
            priorityDumper.dumpCritical(fileDescriptor, printWriter, strArr2, z);
            return;
        }
        if (i3 == 2) {
            priorityDumper.dumpHigh(fileDescriptor, printWriter, strArr2, z);
        } else if (i3 == 3) {
            priorityDumper.dumpNormal(fileDescriptor, printWriter, strArr2, z);
        } else {
            priorityDumper.dump(fileDescriptor, printWriter, strArr2, z);
        }
    }

    private static int getPriorityType(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1986416409:
                if (str.equals(PRIORITY_ARG_NORMAL)) {
                    c = 0;
                    break;
                }
                break;
            case -1560189025:
                if (str.equals(PRIORITY_ARG_CRITICAL)) {
                    c = 1;
                    break;
                }
                break;
            case 2217378:
                if (str.equals(PRIORITY_ARG_HIGH)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 3;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PriorityDumper {
        default void dumpCritical(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
        }

        default void dumpHigh(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
        }

        default void dumpNormal(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
        }

        default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
            dumpCritical(fileDescriptor, printWriter, strArr, z);
            dumpHigh(fileDescriptor, printWriter, strArr, z);
            dumpNormal(fileDescriptor, printWriter, strArr, z);
        }
    }
}
