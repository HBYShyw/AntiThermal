package com.android.server.backup;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.SigningInfo;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Slog;
import com.android.server.backup.utils.BackupEligibilityRules;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PackageManagerBackupAgent extends BackupAgent {
    private static final String ANCESTRAL_RECORD_KEY = "@ancestral_record@";
    private static final int ANCESTRAL_RECORD_VERSION = 1;
    private static final boolean DEBUG = false;
    private static final String DEFAULT_HOME_KEY = "@home@";
    private static final String GLOBAL_METADATA_KEY = "@meta@";
    private static final String STATE_FILE_HEADER = "=state=";
    private static final int STATE_FILE_VERSION = 2;
    private static final String TAG = "PMBA";
    private static final int UNDEFINED_ANCESTRAL_RECORD_VERSION = -1;
    private List<PackageInfo> mAllPackages;
    private boolean mHasMetadata;
    private PackageManager mPackageManager;
    private ComponentName mRestoredHome;
    private String mRestoredHomeInstaller;
    private ArrayList<byte[]> mRestoredHomeSigHashes;
    private long mRestoredHomeVersion;
    private HashMap<String, Metadata> mRestoredSignatures;
    private ComponentName mStoredHomeComponent;
    private ArrayList<byte[]> mStoredHomeSigHashes;
    private long mStoredHomeVersion;
    private String mStoredIncrementalVersion;
    private int mStoredSdkVersion;
    private int mUserId;
    private HashMap<String, Metadata> mStateVersions = new HashMap<>();
    private final HashSet<String> mExisting = new HashSet<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface RestoreDataConsumer {
        void consumeRestoreData(BackupDataInput backupDataInput) throws IOException;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class Metadata {
        public ArrayList<byte[]> sigHashes;
        public long versionCode;

        Metadata(long j, ArrayList<byte[]> arrayList) {
            this.versionCode = j;
            this.sigHashes = arrayList;
        }
    }

    public PackageManagerBackupAgent(PackageManager packageManager, List<PackageInfo> list, int i) {
        init(packageManager, list, i);
    }

    public PackageManagerBackupAgent(PackageManager packageManager, int i, BackupEligibilityRules backupEligibilityRules) {
        init(packageManager, null, i);
        evaluateStorablePackages(backupEligibilityRules);
    }

    private void init(PackageManager packageManager, List<PackageInfo> list, int i) {
        this.mPackageManager = packageManager;
        this.mAllPackages = list;
        this.mRestoredSignatures = null;
        this.mHasMetadata = false;
        this.mStoredSdkVersion = Build.VERSION.SDK_INT;
        this.mStoredIncrementalVersion = Build.VERSION.INCREMENTAL;
        this.mUserId = i;
    }

    public void evaluateStorablePackages(BackupEligibilityRules backupEligibilityRules) {
        this.mAllPackages = getStorableApplications(this.mPackageManager, this.mUserId, backupEligibilityRules);
    }

    public static List<PackageInfo> getStorableApplications(PackageManager packageManager, int i, BackupEligibilityRules backupEligibilityRules) {
        List<PackageInfo> installedPackagesAsUser = packageManager.getInstalledPackagesAsUser(AudioFormat.OPUS, i);
        for (int size = installedPackagesAsUser.size() - 1; size >= 0; size--) {
            if (!backupEligibilityRules.appIsEligibleForBackup(installedPackagesAsUser.get(size).applicationInfo)) {
                installedPackagesAsUser.remove(size);
            }
        }
        return installedPackagesAsUser;
    }

    public boolean hasMetadata() {
        return this.mHasMetadata;
    }

    public Metadata getRestoredMetadata(String str) {
        HashMap<String, Metadata> hashMap = this.mRestoredSignatures;
        if (hashMap == null) {
            Slog.w(TAG, "getRestoredMetadata() before metadata read!");
            return null;
        }
        return hashMap.get(str);
    }

    public Set<String> getRestoredPackages() {
        HashMap<String, Metadata> hashMap = this.mRestoredSignatures;
        if (hashMap == null) {
            Slog.w(TAG, "getRestoredPackages() before metadata read!");
            return null;
        }
        return hashMap.keySet();
    }

    @Override // android.app.backup.BackupAgent
    public void onBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        parseStateFile(parcelFileDescriptor);
        String str = this.mStoredIncrementalVersion;
        if (str == null || !str.equals(Build.VERSION.INCREMENTAL)) {
            Slog.i(TAG, "Previous metadata " + this.mStoredIncrementalVersion + " mismatch vs " + Build.VERSION.INCREMENTAL + " - rewriting");
            this.mExisting.clear();
        }
        try {
            dataOutputStream.writeInt(1);
            writeEntity(backupDataOutput, ANCESTRAL_RECORD_KEY, byteArrayOutputStream.toByteArray());
            try {
                byteArrayOutputStream.reset();
                if (!this.mExisting.contains(GLOBAL_METADATA_KEY)) {
                    dataOutputStream.writeInt(Build.VERSION.SDK_INT);
                    dataOutputStream.writeUTF(Build.VERSION.INCREMENTAL);
                    writeEntity(backupDataOutput, GLOBAL_METADATA_KEY, byteArrayOutputStream.toByteArray());
                } else {
                    this.mExisting.remove(GLOBAL_METADATA_KEY);
                }
                Iterator<PackageInfo> it = this.mAllPackages.iterator();
                while (it.hasNext()) {
                    String str2 = it.next().packageName;
                    if (!str2.equals(GLOBAL_METADATA_KEY)) {
                        try {
                            PackageInfo packageInfoAsUser = this.mPackageManager.getPackageInfoAsUser(str2, AudioFormat.OPUS, this.mUserId);
                            if (this.mExisting.contains(str2)) {
                                this.mExisting.remove(str2);
                                if (packageInfoAsUser.getLongVersionCode() == this.mStateVersions.get(str2).versionCode) {
                                }
                            }
                            SigningInfo signingInfo = packageInfoAsUser.signingInfo;
                            if (signingInfo == null) {
                                Slog.w(TAG, "Not backing up package " + str2 + " since it appears to have no signatures.");
                            } else {
                                byteArrayOutputStream.reset();
                                if (packageInfoAsUser.versionCodeMajor != 0) {
                                    dataOutputStream.writeInt(Integer.MIN_VALUE);
                                    dataOutputStream.writeLong(packageInfoAsUser.getLongVersionCode());
                                } else {
                                    dataOutputStream.writeInt(packageInfoAsUser.versionCode);
                                }
                                writeSignatureHashArray(dataOutputStream, BackupUtils.hashSignatureArray(signingInfo.getApkContentsSigners()));
                                writeEntity(backupDataOutput, str2, byteArrayOutputStream.toByteArray());
                            }
                        } catch (PackageManager.NameNotFoundException unused) {
                            this.mExisting.add(str2);
                        }
                    }
                }
                writeStateFile(this.mAllPackages, parcelFileDescriptor2);
            } catch (IOException unused2) {
                Slog.e(TAG, "Unable to write package backup data file!");
            }
        } catch (IOException unused3) {
            Slog.e(TAG, "Unable to write package backup data file!");
        }
    }

    private static void writeEntity(BackupDataOutput backupDataOutput, String str, byte[] bArr) throws IOException {
        backupDataOutput.writeEntityHeader(str, bArr.length);
        backupDataOutput.writeEntityData(bArr, bArr.length);
    }

    @Override // android.app.backup.BackupAgent
    public void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        RestoreDataConsumer restoreDataConsumer = getRestoreDataConsumer(getAncestralRecordVersionValue(backupDataInput));
        if (restoreDataConsumer == null) {
            Slog.w(TAG, "Ancestral restore set version is unknown to this Android version; not restoring");
        } else {
            restoreDataConsumer.consumeRestoreData(backupDataInput);
        }
    }

    private int getAncestralRecordVersionValue(BackupDataInput backupDataInput) throws IOException {
        if (backupDataInput.readNextHeader()) {
            String key = backupDataInput.getKey();
            int dataSize = backupDataInput.getDataSize();
            if (ANCESTRAL_RECORD_KEY.equals(key)) {
                byte[] bArr = new byte[dataSize];
                backupDataInput.readEntityData(bArr, 0, dataSize);
                return new DataInputStream(new ByteArrayInputStream(bArr)).readInt();
            }
        }
        return -1;
    }

    private RestoreDataConsumer getRestoreDataConsumer(int i) {
        byte b = 0;
        if (i == -1) {
            return new LegacyRestoreDataConsumer();
        }
        if (i == 1) {
            return new AncestralVersion1RestoreDataConsumer();
        }
        Slog.e(TAG, "Unrecognized ANCESTRAL_RECORD_VERSION: " + i);
        return null;
    }

    private static void writeSignatureHashArray(DataOutputStream dataOutputStream, ArrayList<byte[]> arrayList) throws IOException {
        dataOutputStream.writeInt(arrayList.size());
        Iterator<byte[]> it = arrayList.iterator();
        while (it.hasNext()) {
            byte[] next = it.next();
            dataOutputStream.writeInt(next.length);
            dataOutputStream.write(next);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList<byte[]> readSignatureHashArray(DataInputStream dataInputStream) {
        try {
            try {
                int readInt = dataInputStream.readInt();
                if (readInt > 20) {
                    Slog.e(TAG, "Suspiciously large sig count in restore data; aborting");
                    throw new IllegalStateException("Bad restore state");
                }
                ArrayList<byte[]> arrayList = new ArrayList<>(readInt);
                boolean z = false;
                for (int i = 0; i < readInt; i++) {
                    int readInt2 = dataInputStream.readInt();
                    byte[] bArr = new byte[readInt2];
                    dataInputStream.read(bArr);
                    arrayList.add(bArr);
                    if (readInt2 != 32) {
                        z = true;
                    }
                }
                return z ? BackupUtils.hashSignatureArray(arrayList) : arrayList;
            } catch (EOFException unused) {
                Slog.w(TAG, "Read empty signature block");
                return null;
            }
        } catch (IOException unused2) {
            Slog.e(TAG, "Unable to read signatures");
            return null;
        }
    }

    private void parseStateFile(ParcelFileDescriptor parcelFileDescriptor) {
        this.mExisting.clear();
        this.mStateVersions.clear();
        boolean z = false;
        this.mStoredSdkVersion = 0;
        this.mStoredIncrementalVersion = null;
        this.mStoredHomeComponent = null;
        this.mStoredHomeVersion = 0L;
        this.mStoredHomeSigHashes = null;
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(parcelFileDescriptor.getFileDescriptor())));
        try {
            String readUTF = dataInputStream.readUTF();
            if (readUTF.equals(STATE_FILE_HEADER)) {
                int readInt = dataInputStream.readInt();
                if (readInt > 2) {
                    Slog.w(TAG, "Unsupported state file version " + readInt + ", redoing from start");
                    return;
                }
                readUTF = dataInputStream.readUTF();
            } else {
                Slog.i(TAG, "Older version of saved state - rewriting");
                z = true;
            }
            if (readUTF.equals(DEFAULT_HOME_KEY)) {
                this.mStoredHomeComponent = ComponentName.unflattenFromString(dataInputStream.readUTF());
                this.mStoredHomeVersion = dataInputStream.readLong();
                this.mStoredHomeSigHashes = readSignatureHashArray(dataInputStream);
                readUTF = dataInputStream.readUTF();
            }
            if (readUTF.equals(GLOBAL_METADATA_KEY)) {
                this.mStoredSdkVersion = dataInputStream.readInt();
                this.mStoredIncrementalVersion = dataInputStream.readUTF();
                if (!z) {
                    this.mExisting.add(GLOBAL_METADATA_KEY);
                }
                while (true) {
                    String readUTF2 = dataInputStream.readUTF();
                    int readInt2 = dataInputStream.readInt();
                    long readLong = readInt2 == Integer.MIN_VALUE ? dataInputStream.readLong() : readInt2;
                    if (!z) {
                        this.mExisting.add(readUTF2);
                    }
                    this.mStateVersions.put(readUTF2, new Metadata(readLong, null));
                }
            } else {
                Slog.e(TAG, "No global metadata in state file!");
            }
        } catch (EOFException unused) {
        } catch (IOException e) {
            Slog.e(TAG, "Unable to read Package Manager state file: " + e);
        }
    }

    private ComponentName getPreferredHomeComponent() {
        return this.mPackageManager.getHomeActivities(new ArrayList());
    }

    private void writeStateFile(List<PackageInfo> list, ParcelFileDescriptor parcelFileDescriptor) {
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(parcelFileDescriptor.getFileDescriptor())));
        try {
            dataOutputStream.writeUTF(STATE_FILE_HEADER);
            dataOutputStream.writeInt(2);
            dataOutputStream.writeUTF(GLOBAL_METADATA_KEY);
            dataOutputStream.writeInt(Build.VERSION.SDK_INT);
            dataOutputStream.writeUTF(Build.VERSION.INCREMENTAL);
            for (PackageInfo packageInfo : list) {
                dataOutputStream.writeUTF(packageInfo.packageName);
                if (packageInfo.versionCodeMajor != 0) {
                    dataOutputStream.writeInt(Integer.MIN_VALUE);
                    dataOutputStream.writeLong(packageInfo.getLongVersionCode());
                } else {
                    dataOutputStream.writeInt(packageInfo.versionCode);
                }
            }
            dataOutputStream.flush();
        } catch (IOException unused) {
            Slog.e(TAG, "Unable to write package manager state file!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class LegacyRestoreDataConsumer implements RestoreDataConsumer {
        private LegacyRestoreDataConsumer() {
        }

        @Override // com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer
        public void consumeRestoreData(BackupDataInput backupDataInput) throws IOException {
            ArrayList arrayList = new ArrayList();
            HashMap hashMap = new HashMap();
            while (true) {
                String key = backupDataInput.getKey();
                int dataSize = backupDataInput.getDataSize();
                byte[] bArr = new byte[dataSize];
                backupDataInput.readEntityData(bArr, 0, dataSize);
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
                if (key.equals(PackageManagerBackupAgent.GLOBAL_METADATA_KEY)) {
                    PackageManagerBackupAgent.this.mStoredSdkVersion = dataInputStream.readInt();
                    PackageManagerBackupAgent.this.mStoredIncrementalVersion = dataInputStream.readUTF();
                    PackageManagerBackupAgent.this.mHasMetadata = true;
                } else if (key.equals(PackageManagerBackupAgent.DEFAULT_HOME_KEY)) {
                    PackageManagerBackupAgent.this.mRestoredHome = ComponentName.unflattenFromString(dataInputStream.readUTF());
                    PackageManagerBackupAgent.this.mRestoredHomeVersion = dataInputStream.readLong();
                    PackageManagerBackupAgent.this.mRestoredHomeInstaller = dataInputStream.readUTF();
                    PackageManagerBackupAgent.this.mRestoredHomeSigHashes = PackageManagerBackupAgent.readSignatureHashArray(dataInputStream);
                } else {
                    int readInt = dataInputStream.readInt();
                    long readLong = readInt == Integer.MIN_VALUE ? dataInputStream.readLong() : readInt;
                    ArrayList readSignatureHashArray = PackageManagerBackupAgent.readSignatureHashArray(dataInputStream);
                    if (readSignatureHashArray == null || readSignatureHashArray.size() == 0) {
                        Slog.w(PackageManagerBackupAgent.TAG, "Not restoring package " + key + " since it appears to have no signatures.");
                    } else {
                        ApplicationInfo applicationInfo = new ApplicationInfo();
                        applicationInfo.packageName = key;
                        arrayList.add(applicationInfo);
                        hashMap.put(key, new Metadata(readLong, readSignatureHashArray));
                    }
                }
                if (!backupDataInput.readNextHeader()) {
                    PackageManagerBackupAgent.this.mRestoredSignatures = hashMap;
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AncestralVersion1RestoreDataConsumer implements RestoreDataConsumer {
        private AncestralVersion1RestoreDataConsumer() {
        }

        @Override // com.android.server.backup.PackageManagerBackupAgent.RestoreDataConsumer
        public void consumeRestoreData(BackupDataInput backupDataInput) throws IOException {
            ArrayList arrayList = new ArrayList();
            HashMap hashMap = new HashMap();
            while (backupDataInput.readNextHeader()) {
                String key = backupDataInput.getKey();
                int dataSize = backupDataInput.getDataSize();
                byte[] bArr = new byte[dataSize];
                backupDataInput.readEntityData(bArr, 0, dataSize);
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
                if (key.equals(PackageManagerBackupAgent.GLOBAL_METADATA_KEY)) {
                    PackageManagerBackupAgent.this.mStoredSdkVersion = dataInputStream.readInt();
                    PackageManagerBackupAgent.this.mStoredIncrementalVersion = dataInputStream.readUTF();
                    PackageManagerBackupAgent.this.mHasMetadata = true;
                } else if (key.equals(PackageManagerBackupAgent.DEFAULT_HOME_KEY)) {
                    PackageManagerBackupAgent.this.mRestoredHome = ComponentName.unflattenFromString(dataInputStream.readUTF());
                    PackageManagerBackupAgent.this.mRestoredHomeVersion = dataInputStream.readLong();
                    PackageManagerBackupAgent.this.mRestoredHomeInstaller = dataInputStream.readUTF();
                    PackageManagerBackupAgent.this.mRestoredHomeSigHashes = PackageManagerBackupAgent.readSignatureHashArray(dataInputStream);
                } else {
                    int readInt = dataInputStream.readInt();
                    long readLong = readInt == Integer.MIN_VALUE ? dataInputStream.readLong() : readInt;
                    ArrayList readSignatureHashArray = PackageManagerBackupAgent.readSignatureHashArray(dataInputStream);
                    if (readSignatureHashArray == null || readSignatureHashArray.size() == 0) {
                        Slog.w(PackageManagerBackupAgent.TAG, "Not restoring package " + key + " since it appears to have no signatures.");
                    } else {
                        ApplicationInfo applicationInfo = new ApplicationInfo();
                        applicationInfo.packageName = key;
                        arrayList.add(applicationInfo);
                        hashMap.put(key, new Metadata(readLong, readSignatureHashArray));
                    }
                }
            }
            PackageManagerBackupAgent.this.mRestoredSignatures = hashMap;
        }
    }
}
