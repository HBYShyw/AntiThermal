package com.oplus.hiddenapi;

import android.content.Context;
import android.content.res.OplusThemeResources;
import android.database.ContentObserver;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class OplusHiddenApiManager implements IOplusHiddenApiManager {
    private static final int LENGTH_THRESHOLD = 10000;
    public static final String TAG = "OplusHiddenApiManager";
    private static volatile OplusHiddenApiManager sInstance;
    private Map<String, Set<String>> mExemptions = new ConcurrentHashMap();
    private Map<String, Integer> mExemptionsCount = new ArrayMap();
    private volatile boolean mNeedSkipSetExemptions = false;
    private boolean mInitialed = false;
    private final OplusHiddenApiParser mParser = new OplusHiddenApiParser();

    /* loaded from: classes.dex */
    private class HiddenApiBlackListExemptionsObserver extends ContentObserver {
        private final Context mContext;

        HiddenApiBlackListExemptionsObserver(Handler handler, Context context) {
            super(handler);
            this.mContext = context;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            String exemptions = Settings.Global.getString(this.mContext.getContentResolver(), "hidden_api_blacklist_exemptions");
            OplusHiddenApiManager.this.mNeedSkipSetExemptions = exemptions != null;
        }
    }

    public static OplusHiddenApiManager getInstance() {
        if (!isSystemProcess()) {
            Slog.wtf(TAG, "OplusHiddenApiManager is being accessed by a process other than system_server.");
        }
        if (sInstance == null) {
            synchronized (OplusHiddenApiManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusHiddenApiManager();
                }
            }
        }
        return sInstance;
    }

    private static boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }

    private OplusHiddenApiManager() {
        parseExemptions();
        verifyExemptions();
    }

    @Override // com.oplus.hiddenapi.IOplusHiddenApiManager
    public void initAndRegisterSettingsListener(Context context, Handler handler) {
        if (IOplusHiddenApiManager.DEBUG) {
            Log.d(TAG, "initAndRegisterSettingsListener");
        }
        if (context == null || handler == null) {
            Slog.e(TAG, "bad args in initAndRegisterSettingsListener");
            return;
        }
        synchronized (OplusHiddenApiManager.class) {
            if (this.mInitialed) {
                return;
            }
            boolean z = true;
            this.mInitialed = true;
            context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("hidden_api_blacklist_exemptions"), false, new HiddenApiBlackListExemptionsObserver(handler, context));
            String exemptions = Settings.Global.getString(context.getContentResolver(), "hidden_api_blacklist_exemptions");
            if (exemptions == null) {
                z = false;
            }
            this.mNeedSkipSetExemptions = z;
        }
    }

    private void parseExemptions() {
        if (!isSystemProcess()) {
            return;
        }
        try {
            parseBaseConfig();
            parsePatches();
        } catch (Exception e) {
        }
    }

    private void verifyExemptions() {
        for (Map.Entry<String, Set<String>> entry : this.mExemptions.entrySet()) {
            String packageName = entry.getKey();
            Set<String> exemptions = entry.getValue();
            int totalLength = computeLength(exemptions);
            this.mExemptionsCount.put(packageName, Integer.valueOf(totalLength));
        }
    }

    private int computeLength(Set<String> exemptions) {
        int length = 0;
        for (String str : exemptions) {
            length += str.length();
        }
        return length;
    }

    private void parseBaseConfig() throws Exception {
        File baseConfigFile = Environment.buildPath(Environment.getSystemExtDirectory(), new String[]{"etc", "hidden_api_exemptions"});
        if (baseConfigFile.exists()) {
            this.mExemptions.putAll(this.mParser.parse(baseConfigFile, false));
        }
    }

    private void parsePatches() {
        File patchDir = Environment.buildPath(Environment.getDataDirectory(), new String[]{OplusThemeResources.OPLUS_PACKAGE, "os", "hidden_api_config", "patch"});
        if (patchDir.isDirectory() && patchDir.exists()) {
            applyPatches(patchDir);
        }
    }

    private void applyPatches(File patchDir) {
        File[] patchFiles = patchDir.listFiles();
        if (patchFiles == null) {
            return;
        }
        for (File patchFile : patchFiles) {
            try {
                Map<String, Set<String>> patchExemptions = this.mParser.parse(patchFile, true);
                applyPatch(patchExemptions);
            } catch (Exception e) {
                Slog.e(TAG, "parsing patch " + patchFile + " failed", e);
            }
        }
    }

    private void applyPatch(Map<String, Set<String>> patchExemptions) {
        if (patchExemptions.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Set<String>> entry : patchExemptions.entrySet()) {
            String packageName = entry.getKey();
            Set<String> exemptions = this.mExemptions.get(packageName);
            if (exemptions == null) {
                this.mExemptions.put(packageName, entry.getValue());
            } else {
                exemptions.addAll(entry.getValue());
            }
        }
    }

    @Override // com.oplus.hiddenapi.IOplusHiddenApiManager
    public List<String> getExemptions(String packageName) {
        int myUid = UserHandle.getAppId(Process.myUid());
        if ((myUid == 1000 || myUid == 0) && !this.mNeedSkipSetExemptions) {
            return getExemptionsInternal(packageName);
        }
        Slog.d(TAG, "skip get hidden api exemptions from manager");
        return Collections.emptyList();
    }

    private List<String> getExemptionsInternal(String packageName) {
        Integer totalLength = this.mExemptionsCount.get(packageName);
        if (totalLength == null) {
            return Collections.emptyList();
        }
        if (totalLength.intValue() > 10000) {
            Slog.e(TAG, "set hidden-api exemption failed for " + packageName + "!!! Total length of hidden-api exemptions is " + totalLength + " over limit:10000");
            return Collections.emptyList();
        }
        Set<String> exemptions = this.mExemptions.get(packageName);
        if (exemptions == null) {
            return Collections.emptyList();
        }
        return new ArrayList(exemptions);
    }

    @Override // com.oplus.hiddenapi.IOplusHiddenApiManager
    public void dump(PrintWriter writer, String[] args) {
        if (args != null && args.length == 1) {
            dumpAll(writer);
            return;
        }
        if (args != null && args.length == 2) {
            String packageName = args[1];
            Set<String> exemptions = this.mExemptions.get(packageName);
            dumpPackage(writer, packageName, exemptions);
            return;
        }
        writer.write("nothing to dump");
    }

    private void dumpAll(PrintWriter writer) {
        if (this.mExemptions.isEmpty()) {
            writer.write("exemptions is empty\n");
            return;
        }
        for (Map.Entry<String, Set<String>> entry : this.mExemptions.entrySet()) {
            String packageName = entry.getKey();
            Set<String> exemptions = entry.getValue();
            dumpPackage(writer, packageName, exemptions);
        }
    }

    private void dumpPackage(PrintWriter writer, String packageName, Set<String> exemptions) {
        writer.write("hidden api exemptions for " + packageName + ":\n");
        Integer totalLength = this.mExemptionsCount.get(packageName);
        if (totalLength != null && totalLength.intValue() > 10000) {
            writer.write("warning!!! Total length of hidden-api exemptions is " + totalLength + " over limit:10000 set hidden-api exemptions for " + packageName + " will fail.\n");
        }
        if (exemptions == null || exemptions.isEmpty()) {
            writer.write("        ");
            writer.write("empty");
            return;
        }
        for (String api : exemptions) {
            writer.write("        ");
            writer.write(api + "\n");
        }
    }
}
