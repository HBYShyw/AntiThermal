package com.android.server.rollback;

import android.content.pm.VersionedPackage;
import android.content.rollback.PackageRollbackInfo;
import android.content.rollback.RollbackInfo;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.system.ErrnoException;
import android.system.Os;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.SparseIntArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.net.watchlist.WatchlistLoggingHandler;
import com.android.server.pm.verify.domain.DomainVerificationLegacySettings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import libcore.io.IoUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RollbackStore {
    private static final String TAG = "RollbackManager";
    private final File mRollbackDataDir;
    private final File mRollbackHistoryDir;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RollbackStore(File file, File file2) {
        this.mRollbackDataDir = file;
        this.mRollbackHistoryDir = file2;
    }

    private static List<Rollback> loadRollbacks(File file) {
        ArrayList arrayList = new ArrayList();
        file.mkdirs();
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                try {
                    arrayList.add(loadRollback(file2));
                } catch (IOException e) {
                    Slog.e(TAG, "Unable to read rollback at " + file2, e);
                    removeFile(file2);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Rollback> loadRollbacks() {
        return loadRollbacks(this.mRollbackDataDir);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Rollback> loadHistorialRollbacks() {
        return loadRollbacks(this.mRollbackHistoryDir);
    }

    private static List<Integer> toIntList(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(Integer.valueOf(jSONArray.getInt(i)));
        }
        return arrayList;
    }

    private static JSONArray fromIntList(List<Integer> list) {
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            jSONArray.put(list.get(i));
        }
        return jSONArray;
    }

    private static JSONArray convertToJsonArray(List<PackageRollbackInfo.RestoreInfo> list) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (PackageRollbackInfo.RestoreInfo restoreInfo : list) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("userId", restoreInfo.userId);
            jSONObject.put("appId", restoreInfo.appId);
            jSONObject.put("seInfo", restoreInfo.seInfo);
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    private static ArrayList<PackageRollbackInfo.RestoreInfo> convertToRestoreInfoArray(JSONArray jSONArray) throws JSONException {
        ArrayList<PackageRollbackInfo.RestoreInfo> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            arrayList.add(new PackageRollbackInfo.RestoreInfo(jSONObject.getInt("userId"), jSONObject.getInt("appId"), jSONObject.getString("seInfo")));
        }
        return arrayList;
    }

    private static JSONArray extensionVersionsToJson(SparseIntArray sparseIntArray) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < sparseIntArray.size(); i++) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("sdkVersion", sparseIntArray.keyAt(i));
            jSONObject.put("extensionVersion", sparseIntArray.valueAt(i));
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    private static SparseIntArray extensionVersionsFromJson(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null) {
            return new SparseIntArray(0);
        }
        SparseIntArray sparseIntArray = new SparseIntArray(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            sparseIntArray.append(jSONObject.getInt("sdkVersion"), jSONObject.getInt("extensionVersion"));
        }
        return sparseIntArray;
    }

    private static JSONObject rollbackInfoToJson(RollbackInfo rollbackInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("rollbackId", rollbackInfo.getRollbackId());
        jSONObject.put("packages", toJson((List<PackageRollbackInfo>) rollbackInfo.getPackages()));
        jSONObject.put("isStaged", rollbackInfo.isStaged());
        jSONObject.put("causePackages", versionedPackagesToJson(rollbackInfo.getCausePackages()));
        jSONObject.put("committedSessionId", rollbackInfo.getCommittedSessionId());
        return jSONObject;
    }

    private static RollbackInfo rollbackInfoFromJson(JSONObject jSONObject) throws JSONException {
        return new RollbackInfo(jSONObject.getInt("rollbackId"), packageRollbackInfosFromJson(jSONObject.getJSONArray("packages")), jSONObject.getBoolean("isStaged"), versionedPackagesFromJson(jSONObject.getJSONArray("causePackages")), jSONObject.getInt("committedSessionId"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rollback createNonStagedRollback(int i, int i2, int i3, String str, int[] iArr, SparseIntArray sparseIntArray) {
        return new Rollback(i, new File(this.mRollbackDataDir, Integer.toString(i)), i2, false, i3, str, iArr, sparseIntArray);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rollback createStagedRollback(int i, int i2, int i3, String str, int[] iArr, SparseIntArray sparseIntArray) {
        return new Rollback(i, new File(this.mRollbackDataDir, Integer.toString(i)), i2, true, i3, str, iArr, sparseIntArray);
    }

    private static boolean isLinkPossible(File file, File file2) {
        try {
            return Os.stat(file.getAbsolutePath()).st_dev == Os.stat(file2.getAbsolutePath()).st_dev;
        } catch (ErrnoException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void backupPackageCodePath(Rollback rollback, String str, String str2) throws IOException {
        File file = new File(str2);
        File file2 = new File(rollback.getBackupDir(), str);
        file2.mkdirs();
        File file3 = new File(file2, file.getName());
        boolean isLinkPossible = isLinkPossible(file, file2);
        boolean z = true;
        boolean z2 = !isLinkPossible;
        if (!z2) {
            try {
                Os.link(file.getAbsolutePath(), file3.getAbsolutePath());
            } catch (ErrnoException e) {
                if (SystemProperties.getBoolean("persist.rollback.is_test", false)) {
                    throw new IOException(e);
                }
            }
        }
        z = z2;
        if (z) {
            Files.copy(file.toPath(), file3.toPath(), new CopyOption[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File[] getPackageCodePaths(Rollback rollback, String str) {
        File[] listFiles = new File(rollback.getBackupDir(), str).listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        return listFiles;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void deletePackageCodePaths(Rollback rollback) {
        Iterator it = rollback.info.getPackages().iterator();
        while (it.hasNext()) {
            removeFile(new File(rollback.getBackupDir(), ((PackageRollbackInfo) it.next()).getPackageName()));
        }
    }

    private static void saveRollback(Rollback rollback, File file) {
        AtomicFile atomicFile = new AtomicFile(new File(file, "rollback.json"));
        FileOutputStream fileOutputStream = null;
        try {
            file.mkdirs();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("info", rollbackInfoToJson(rollback.info));
            jSONObject.put(WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, rollback.getTimestamp().toString());
            jSONObject.put("originalSessionId", rollback.getOriginalSessionId());
            jSONObject.put("state", rollback.getStateAsString());
            jSONObject.put("stateDescription", rollback.getStateDescription());
            jSONObject.put("restoreUserDataInProgress", rollback.isRestoreUserDataInProgress());
            jSONObject.put("userId", rollback.getUserId());
            jSONObject.putOpt("installerPackageName", rollback.getInstallerPackageName());
            jSONObject.putOpt("extensionVersions", extensionVersionsToJson(rollback.getExtensionVersions()));
            fileOutputStream = atomicFile.startWrite();
            fileOutputStream.write(jSONObject.toString().getBytes());
            fileOutputStream.flush();
            atomicFile.finishWrite(fileOutputStream);
        } catch (IOException | JSONException e) {
            Slog.e(TAG, "Unable to save rollback for: " + rollback.info.getRollbackId(), e);
            if (fileOutputStream != null) {
                atomicFile.failWrite(fileOutputStream);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void saveRollback(Rollback rollback) {
        saveRollback(rollback, rollback.getBackupDir());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveRollbackToHistory(Rollback rollback) {
        String hexString = Long.toHexString(rollback.getTimestamp().getEpochSecond());
        String num = Integer.toString(rollback.info.getRollbackId());
        saveRollback(rollback, new File(this.mRollbackHistoryDir, num + "-" + hexString));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void deleteRollback(Rollback rollback) {
        removeFile(rollback.getBackupDir());
    }

    private static Rollback loadRollback(File file) throws IOException {
        try {
            return rollbackFromJson(new JSONObject(IoUtils.readFileAsString(new File(file, "rollback.json").getAbsolutePath())), file);
        } catch (ParseException | DateTimeParseException | JSONException e) {
            throw new IOException(e);
        }
    }

    @VisibleForTesting
    static Rollback rollbackFromJson(JSONObject jSONObject, File file) throws JSONException, ParseException {
        return new Rollback(rollbackInfoFromJson(jSONObject.getJSONObject("info")), file, Instant.parse(jSONObject.getString(WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP)), jSONObject.optInt("originalSessionId", jSONObject.optInt("stagedSessionId", -1)), Rollback.rollbackStateFromString(jSONObject.getString("state")), jSONObject.optString("stateDescription"), jSONObject.getBoolean("restoreUserDataInProgress"), jSONObject.optInt("userId", UserHandle.SYSTEM.getIdentifier()), jSONObject.optString("installerPackageName", ""), extensionVersionsFromJson(jSONObject.optJSONArray("extensionVersions")));
    }

    private static JSONObject toJson(VersionedPackage versionedPackage) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, versionedPackage.getPackageName());
        jSONObject.put("longVersionCode", versionedPackage.getLongVersionCode());
        return jSONObject;
    }

    private static VersionedPackage versionedPackageFromJson(JSONObject jSONObject) throws JSONException {
        return new VersionedPackage(jSONObject.getString(DomainVerificationLegacySettings.ATTR_PACKAGE_NAME), jSONObject.getLong("longVersionCode"));
    }

    private static JSONObject toJson(PackageRollbackInfo packageRollbackInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("versionRolledBackFrom", toJson(packageRollbackInfo.getVersionRolledBackFrom()));
        jSONObject.put("versionRolledBackTo", toJson(packageRollbackInfo.getVersionRolledBackTo()));
        List pendingBackups = packageRollbackInfo.getPendingBackups();
        ArrayList pendingRestores = packageRollbackInfo.getPendingRestores();
        List snapshottedUsers = packageRollbackInfo.getSnapshottedUsers();
        jSONObject.put("pendingBackups", fromIntList(pendingBackups));
        jSONObject.put("pendingRestores", convertToJsonArray(pendingRestores));
        jSONObject.put("isApex", packageRollbackInfo.isApex());
        jSONObject.put("isApkInApex", packageRollbackInfo.isApkInApex());
        jSONObject.put("installedUsers", fromIntList(snapshottedUsers));
        jSONObject.put("rollbackDataPolicy", packageRollbackInfo.getRollbackDataPolicy());
        return jSONObject;
    }

    private static PackageRollbackInfo packageRollbackInfoFromJson(JSONObject jSONObject) throws JSONException {
        return new PackageRollbackInfo(versionedPackageFromJson(jSONObject.getJSONObject("versionRolledBackFrom")), versionedPackageFromJson(jSONObject.getJSONObject("versionRolledBackTo")), toIntList(jSONObject.getJSONArray("pendingBackups")), convertToRestoreInfoArray(jSONObject.getJSONArray("pendingRestores")), jSONObject.getBoolean("isApex"), jSONObject.getBoolean("isApkInApex"), toIntList(jSONObject.getJSONArray("installedUsers")), jSONObject.optInt("rollbackDataPolicy", 0));
    }

    private static JSONArray versionedPackagesToJson(List<VersionedPackage> list) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        Iterator<VersionedPackage> it = list.iterator();
        while (it.hasNext()) {
            jSONArray.put(toJson(it.next()));
        }
        return jSONArray;
    }

    private static List<VersionedPackage> versionedPackagesFromJson(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(versionedPackageFromJson(jSONArray.getJSONObject(i)));
        }
        return arrayList;
    }

    private static JSONArray toJson(List<PackageRollbackInfo> list) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        Iterator<PackageRollbackInfo> it = list.iterator();
        while (it.hasNext()) {
            jSONArray.put(toJson(it.next()));
        }
        return jSONArray;
    }

    private static List<PackageRollbackInfo> packageRollbackInfosFromJson(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(packageRollbackInfoFromJson(jSONArray.getJSONObject(i)));
        }
        return arrayList;
    }

    private static void removeFile(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                removeFile(file2);
            }
        }
        if (file.exists()) {
            file.delete();
        }
    }
}
