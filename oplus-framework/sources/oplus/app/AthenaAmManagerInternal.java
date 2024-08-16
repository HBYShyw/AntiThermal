package oplus.app;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.oplus.app.OplusAppSwitchManager;
import com.oplus.util.OplusPackageFreezeData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public abstract class AthenaAmManagerInternal {
    public abstract void addOSensePolicyInfo(Bundle bundle);

    public abstract void compactProcess(int i, int i2, int i3);

    public abstract void forceStopPackages(List<String> list, List<Integer> list2) throws Exception;

    public abstract SparseArray<String> getAudioFocusList();

    public abstract SparseArray<String> getAudioRecordList();

    public abstract ArrayList<Integer> getBluetoothList();

    public abstract SparseArray<ArrayList<String>> getCurrentInputMethod();

    public abstract List<OplusPackageFreezeData> getCustomRunningProcesses() throws Exception;

    public abstract SparseArray<ArrayList<String>> getDefaultDialerList();

    public abstract SparseArray<ArrayList<String>> getDefaultLauncherList();

    public abstract SparseArray<ArrayList<String>> getDefaultSmsList();

    public abstract SparseArray<ArrayList<String>> getForegroundServiceList();

    public abstract ArrayList<String> getNavigationList();

    public abstract ArrayMap<String, SparseArray<Integer>> getPssData(ArrayList<String> arrayList) throws Exception;

    public abstract List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws Exception;

    public abstract SparseArray<ArrayList<String>> getScreenRecordList();

    public abstract ArrayMap<Integer, ArrayList<String>> getTopApps();

    public abstract ArrayList<Integer> getTrafficAppList();

    public abstract ArrayList<Integer> getVisibleWindowList();

    public abstract SparseArray<ArrayList<String>> getVpnConnectionList();

    public abstract SparseArray<ArrayList<String>> getWallpaperList();

    public abstract SparseArray<ArrayList<String>> getWidgetsList();

    public abstract boolean isProcStatsFeatureEnable() throws Exception;

    public abstract void registerAppSwitch(List<String> list, List<String> list2, OplusAppSwitchManager.OnAppSwitchObserver onAppSwitchObserver);

    public abstract boolean transact(int i, Bundle bundle, Bundle bundle2, int i2) throws Exception;

    public abstract void unRegisterAppSwitch(OplusAppSwitchManager.OnAppSwitchObserver onAppSwitchObserver);

    public abstract void updateAppList(ArrayList<String> arrayList) throws Exception;
}
