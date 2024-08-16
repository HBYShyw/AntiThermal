package android.app;

import android.os.OplusPropertyList;
import android.os.Process;
import android.os.SystemProperties;
import android.util.Log;
import dalvik.system.VMRuntime;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public class OplusAppHeapManager implements IOplusAppHeapManager {
    private static final long DEFAULT_AVAIL_MEM = 3500;
    private static final long GB_IN_B = 1073741824;
    private static final long MB_IN_KB = 1024;
    private static final String PROC_MEMINFO_PATH = "/proc/meminfo";
    private static final int SIZE_RAM_LEVEL = 12;
    private static final int VM_PROCESS_STATE_GT_TOP = 1;
    private static final int VM_PROCESS_STATE_LE_TOP = 0;
    private static OplusAppHeapManager sInstance;
    private boolean isFocusVer;
    private int sTotalMemory;
    private static final String TAG = OplusAppHeapManager.class.getSimpleName();
    private static Class vmRuntime = null;
    private static Method getRuntime = null;
    private static Object obj = null;
    private static Method UpdateProcess = null;
    private static int sIsInWhiteList = -1;
    private static final List<Integer> DEF_RAM_SIZE = Arrays.asList(2, 3, 4, 6, 8, 12, 16);
    private static int IS_FEATURE_APP_HEAP = SystemProperties.getInt("sys.heap.optimize.enable", 0);
    private String sPackageName = null;
    private boolean isBenchMark = false;

    protected OplusAppHeapManager() {
        this.sTotalMemory = 1;
        this.isFocusVer = false;
        initUpdateProcessMethods();
        this.sTotalMemory = getTotalProcMemInfoGB();
        this.isFocusVer = isFocusVersion();
    }

    public static OplusAppHeapManager getInstance() {
        synchronized (OplusAppHeapManager.class) {
            if (sInstance == null) {
                sInstance = new OplusAppHeapManager();
            }
        }
        return sInstance;
    }

    private long getMemInfoAvailMB() {
        return getMemInfoAvailKB() / 1024;
    }

    private long getMemInfoAvailKB() {
        String[] labels = {"MemAvailable:"};
        long[] values = new long[1];
        Process.readProcLines(PROC_MEMINFO_PATH, labels, values);
        return values[0];
    }

    private int getTotalProcMemInfoGB() {
        final double tempValue = (Process.getTotalMemory() * 1.0d) / 1.073741824E9d;
        List<Integer> list = DEF_RAM_SIZE;
        return list.stream().filter(new Predicate() { // from class: android.app.OplusAppHeapManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj2) {
                return OplusAppHeapManager.lambda$getTotalProcMemInfoGB$0(tempValue, (Integer) obj2);
            }
        }).min(new Comparator() { // from class: android.app.OplusAppHeapManager$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj2, Object obj3) {
                return ((Integer) obj2).compareTo((Integer) obj3);
            }
        }).orElse(list.get(list.size() - 1)).intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getTotalProcMemInfoGB$0(double tempValue, Integer i) {
        return ((double) i.intValue()) >= tempValue;
    }

    private boolean isFocusVersion() {
        return "V14.0.0".equals(SystemProperties.get(OplusPropertyList.PROPERTY_BUILD_VERSION_OPLUSROM_CONFIDENTIAL, OplusPropertyList.UNKNOWN));
    }

    private boolean isBenchMarkApp() {
        return "com.antutu.ABenchMark".equals(this.sPackageName);
    }

    private int isInWhiteList() {
        return sIsInWhiteList;
    }

    @Override // android.app.IOplusAppHeapManager
    public void setIsInWhiteList(int isInWhiteList, String pckName) {
        sIsInWhiteList = isInWhiteList;
        this.sPackageName = pckName;
        this.isBenchMark = isBenchMarkApp();
    }

    private void initUpdateProcessMethods() {
        try {
            if (vmRuntime == null) {
                Class<?> cls = Class.forName("dalvik.system.VMRuntime");
                vmRuntime = cls;
                Method method = cls.getMethod("getRuntime", null);
                getRuntime = method;
                obj = method.invoke(null, null);
                UpdateProcess = vmRuntime.getMethod("updateProcessValue", Integer.TYPE, Integer.TYPE, Integer.TYPE);
            }
        } catch (NoSuchMethodException e) {
            Log.d(TAG, e.toString());
        } catch (Exception e2) {
            Log.d(TAG, e2.toString());
        }
    }

    private void updateProcessValue(int state, int topProcessState, int isListPck) {
        Object obj2;
        try {
            Method method = UpdateProcess;
            if (method != null && (obj2 = obj) != null) {
                method.invoke(obj2, Integer.valueOf(state), Integer.valueOf(topProcessState), Integer.valueOf(isListPck));
            } else {
                VMRuntime.getRuntime().updateProcessState(state);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override // android.app.IOplusAppHeapManager
    public void updateProcessState(int state, int stateVaule) {
        int isListPck = isInWhiteList();
        int topProcessState = stateVaule == 2 ? 0 : 1;
        if (IS_FEATURE_APP_HEAP == 1 && this.sTotalMemory >= 12 && getMemInfoAvailMB() > DEFAULT_AVAIL_MEM) {
            if (this.isFocusVer) {
                if (this.isBenchMark) {
                    updateProcessValue(state, topProcessState, isListPck);
                    return;
                } else {
                    updateProcessValue(state, 1, -1);
                    return;
                }
            }
            updateProcessValue(state, topProcessState, isListPck);
            return;
        }
        updateProcessValue(state, 1, -1);
    }
}
