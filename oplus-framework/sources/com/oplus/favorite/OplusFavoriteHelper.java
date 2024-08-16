package com.oplus.favorite;

import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.telephony.OplusTelephonyManager;
import android.view.OplusWindowManager;
import com.oplus.direct.IOplusDirectFindCallback;
import com.oplus.direct.OplusDirectFindCmd;
import com.oplus.direct.OplusDirectFindCmds;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public class OplusFavoriteHelper implements IOplusFavoriteConstans {
    public static final String TAG = "OplusFavoriteHelper";
    private static final String SETTING_KEY_ALL = "favorite_all";
    private static final String[] SELECTION_ARGS = {SETTING_KEY_ALL};
    private static final String SETTINGS_AUTHORITY = "com.coloros.favorite.settings.provider";
    private static final String SETTINGS_PATH = "settings";
    private static final Uri SETTINGS_URI = new Uri.Builder().scheme(OplusTelephonyManager.BUNDLE_CONTENT).authority(SETTINGS_AUTHORITY).path(SETTINGS_PATH).build();

    public static void startCrawl(IOplusDirectFindCallback callback) {
        startCrawl(callback, "");
    }

    public static void startCrawl(IOplusDirectFindCallback callback, String param) {
        try {
            OplusLog.d(TAG, "startCrawl");
            OplusWindowManager windowManager = new OplusWindowManager();
            OplusDirectFindCmd cmd = new OplusDirectFindCmd(callback, param);
            windowManager.directFindCmd(cmd);
        } catch (RemoteException e) {
            OplusLog.wtf(TAG, e);
        } catch (Exception e2) {
            OplusLog.wtf(TAG, e2);
        }
    }

    public static void startSave(IOplusDirectFindCallback callback) {
        try {
            OplusLog.d(TAG, "startSave");
            OplusWindowManager windowManager = new OplusWindowManager();
            OplusDirectFindCmd cmd = new OplusDirectFindCmd();
            cmd.putCommand(OplusDirectFindCmds.SAVE_FAVORITE.name());
            cmd.setCallback(callback);
            windowManager.directFindCmd(cmd);
        } catch (RemoteException e) {
            OplusLog.wtf(TAG, e);
        } catch (Exception e2) {
            OplusLog.wtf(TAG, e2);
        }
    }

    public static boolean isSettingOn(Context context) {
        return true;
    }
}
