package android.debug;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.network.OlkConstants;
import com.oplus.util.OplusResolverIntentUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class DebugLogManager extends IDebugLogManager {
    private static final String TAG = TAG_BASE + DebugLogManager.class.getSimpleName();
    private static final HashMap<String, Object> sDefaultMaps = new HashMap<>();
    private static volatile DebugLogManager sInstance = null;
    private ArrayList<String> mActiveList = new ArrayList<>();

    private DebugLogManager(Context context) {
        HashMap<String, Object> hashMap = sDefaultMaps;
        hashMap.put("thirdpart", ThirdPartLogManager.getInstance(context));
        hashMap.put("media", AudioLogManager.getInstance(context));
        hashMap.put("bluetooth", BluetoothLogManager.getInstance(context));
        hashMap.put("gps", GpsLogManager.getInstance(context));
        hashMap.put("wifi", WifiLogManager.getInstance(context));
        hashMap.put(OlkConstants.EXT_NETWORK, NetworkLogManager.getInstance(context));
        hashMap.put("sensor", SensorLogManager.getInstance(context));
        hashMap.put(OplusResolverIntentUtil.DEFAULT_APP_CAMERA, CameraLogManager.getInstance(context));
        hashMap.put("junk", OplusPerfLogkit.getInstance(context));
        hashMap.put("OneTrace", OneTraceLogkitManager.getInstance(context));
        hashMap.put("inputmethod", InputMethodLogManager.getInstance(context));
        hashMap.put("touch", TouchLogManager.getInstance(context));
    }

    public static DebugLogManager getInstance() {
        return getInstance(null);
    }

    public static DebugLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DebugLogManager.class) {
                if (sInstance == null) {
                    sInstance = new DebugLogManager(context);
                }
            }
        }
        return sInstance;
    }

    public void setActiveList(ArrayList<String> list) throws RemoteException {
        if (list != null && list.size() > 0) {
            Log.d(TAG, "setActiveList: " + list.size());
            this.mActiveList = list;
        }
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        Log.d(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
        ArrayList<String> arrayList = this.mActiveList;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<String> it = this.mActiveList.iterator();
            while (it.hasNext()) {
                String logList = it.next();
                IDebugLogManager activeManager = getActiveManager(logList);
                if (activeManager != null) {
                    activeManager.setLogOn(maxSize, param);
                }
            }
        }
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.d(TAG, "setLogOff: ");
        ArrayList<String> arrayList = this.mActiveList;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<String> it = this.mActiveList.iterator();
            while (it.hasNext()) {
                String logList = it.next();
                IDebugLogManager activeManager = getActiveManager(logList);
                if (activeManager != null) {
                    activeManager.setLogOff();
                }
            }
        }
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        Log.d(TAG, "setLogDump: ");
        ArrayList<String> arrayList = this.mActiveList;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<String> it = this.mActiveList.iterator();
            while (it.hasNext()) {
                String logList = it.next();
                IDebugLogManager activeManager = getActiveManager(logList);
                if (activeManager != null) {
                    activeManager.setLogDump();
                }
            }
        }
    }

    public IDebugLogManager getActiveManager(String type) {
        try {
            return (IDebugLogManager) sDefaultMaps.get(type);
        } catch (Exception e) {
            Log.e(TAG, "get active manager failed: " + type);
            return null;
        }
    }
}
