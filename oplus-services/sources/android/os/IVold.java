package android.os;

import android.os.IVoldListener;
import android.os.IVoldMountCallback;
import android.os.IVoldTaskListener;
import android.os.incremental.IncrementalFileSystemControlParcel;
import java.io.FileDescriptor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IVold extends IInterface {
    public static final int FSTRIM_FLAG_DEEP_TRIM = 1;
    public static final int MOUNT_FLAG_PRIMARY = 1;
    public static final int MOUNT_FLAG_VISIBLE_FOR_READ = 2;
    public static final int MOUNT_FLAG_VISIBLE_FOR_WRITE = 4;
    public static final int PARTITION_TYPE_MIXED = 2;
    public static final int PARTITION_TYPE_PRIVATE = 1;
    public static final int PARTITION_TYPE_PUBLIC = 0;
    public static final int REMOUNT_MODE_ANDROID_WRITABLE = 4;
    public static final int REMOUNT_MODE_DEFAULT = 1;
    public static final int REMOUNT_MODE_INSTALLER = 2;
    public static final int REMOUNT_MODE_NONE = 0;
    public static final int REMOUNT_MODE_OPLUS_ANDROID_WRITABLE = 5;
    public static final int REMOUNT_MODE_PASS_THROUGH = 3;
    public static final int STORAGE_FLAG_CE = 2;
    public static final int STORAGE_FLAG_DE = 1;
    public static final int VOLUME_STATE_BAD_REMOVAL = 8;
    public static final int VOLUME_STATE_CHECKING = 1;
    public static final int VOLUME_STATE_EJECTING = 5;
    public static final int VOLUME_STATE_FORMATTING = 4;
    public static final int VOLUME_STATE_MOUNTED = 2;
    public static final int VOLUME_STATE_MOUNTED_READ_ONLY = 3;
    public static final int VOLUME_STATE_REMOVED = 7;
    public static final int VOLUME_STATE_UNMOUNTABLE = 6;
    public static final int VOLUME_STATE_UNMOUNTED = 0;
    public static final int VOLUME_TYPE_ASEC = 3;
    public static final int VOLUME_TYPE_EMULATED = 2;
    public static final int VOLUME_TYPE_OBB = 4;
    public static final int VOLUME_TYPE_PRIVATE = 1;
    public static final int VOLUME_TYPE_PUBLIC = 0;
    public static final int VOLUME_TYPE_STUB = 5;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Default implements IVold {
        @Override // android.os.IVold
        public void abortChanges(String str, boolean z) throws RemoteException {
        }

        @Override // android.os.IVold
        public void abortFuse() throws RemoteException {
        }

        @Override // android.os.IVold
        public void abortIdleMaint(IVoldTaskListener iVoldTaskListener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void addAppIds(String[] strArr, int[] iArr) throws RemoteException {
        }

        @Override // android.os.IVold
        public void addSandboxIds(int[] iArr, String[] strArr) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // android.os.IVold
        public void benchmark(String str, IVoldTaskListener iVoldTaskListener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void bindMount(String str, String str2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void checkBeforeMount(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public int clearCache(int i) throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public void commitChanges() throws RemoteException {
        }

        @Override // android.os.IVold
        public void configDfsFuse(String str, int i, int i2) throws RemoteException {
        }

        @Override // android.os.IVold
        public String createObb(String str, int i) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public String createStubVolume(String str, String str2, String str3, String str4, String str5, int i) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void createUserKey(int i, int i2, boolean z) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyDsuMetadataKey(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyObb(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroySandboxForApp(String str, String str2, int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyStubVolume(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyUserKey(int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyUserStorage(String str, int i, int i2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void earlyBootEnded() throws RemoteException {
        }

        @Override // android.os.IVold
        public void encryptFstab(String str, String str2, boolean z, String str3, String str4) throws RemoteException {
        }

        @Override // android.os.IVold
        public void ensureAppDirsCreated(String[] strArr, int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public byte[] exportSensitiveBePublicKey(int i, int i2) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public byte[] exportSensitiveKey(int i, int i2, boolean z) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void fbeEnable() throws RemoteException {
        }

        @Override // android.os.IVold
        public void fixupAppDir(String str, int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void forgetPartition(String str, String str2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void format(String str, String str2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fstrim(int i, IVoldTaskListener iVoldTaskListener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fsyncCtrl(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public int getStorageLifeTime() throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public int[] getUnlockedUsers() throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public int getWriteAmount() throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public boolean incFsEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void initUser0() throws RemoteException {
        }

        @Override // android.os.IVold
        public boolean isCheckpointing() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void lockUserKey(int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void markBootAttempt() throws RemoteException {
        }

        @Override // android.os.IVold
        public void monitor() throws RemoteException {
        }

        @Override // android.os.IVold
        public void mount(String str, int i, int i2, IVoldMountCallback iVoldMountCallback) throws RemoteException {
        }

        @Override // android.os.IVold
        public FileDescriptor mountAppFuse(int i, int i2) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public FileDescriptor mountDfsFuse(String str, String str2) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void mountFstab(String str, String str2, String str3) throws RemoteException {
        }

        @Override // android.os.IVold
        public IncrementalFileSystemControlParcel mountIncFs(String str, String str2, int i, String str3) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void moveStorage(String str, String str2, IVoldTaskListener iVoldTaskListener) throws RemoteException {
        }

        @Override // android.os.IVold
        public boolean needsCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean needsRollback() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void onSecureKeyguardStateChanged(boolean z) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onSecureKeyguardStateChangedForSensitiveFile(boolean z, int i, int i2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserAdded(int i, int i2, int i3) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserRemoved(int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserStarted(int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserStopped(int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public FileDescriptor openAppFuseFile(int i, int i2, int i3, int i4) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void partition(String str, int i, int i2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void prepareCheckpoint() throws RemoteException {
        }

        @Override // android.os.IVold
        public void prepareSandboxForApp(String str, int i, String str2, int i2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void prepareUserStorage(String str, int i, int i2, int i3) throws RemoteException {
        }

        @Override // android.os.IVold
        public void refreshLatestWrite() throws RemoteException {
        }

        @Override // android.os.IVold
        public void remountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException {
        }

        @Override // android.os.IVold
        public void remountUid(int i, int i2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void reset() throws RemoteException {
        }

        @Override // android.os.IVold
        public void resetCheckpoint() throws RemoteException {
        }

        @Override // android.os.IVold
        public void restoreCheckpoint(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void restoreCheckpointPart(String str, int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void runIdleMaint(boolean z, IVoldTaskListener iVoldTaskListener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void sdlockClearPassword(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void sdlockErase() throws RemoteException {
        }

        @Override // android.os.IVold
        public String sdlockGetCid() throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public String sdlockPoll() throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void sdlockSetPassword(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void sdlockUnlock(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setGCUrgentPace(int i, int i2, float f, float f2, int i3, int i4, int i5) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setIncFsMountOptions(IncrementalFileSystemControlParcel incrementalFileSystemControlParcel, boolean z, boolean z2, String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setListener(IVoldListener iVoldListener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setStorageBindingSeed(byte[] bArr) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setUserKeyProtection(int i, String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setupAppDir(String str, int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public void shutdown() throws RemoteException {
        }

        @Override // android.os.IVold
        public void startCheckpoint(int i) throws RemoteException {
        }

        @Override // android.os.IVold
        public int startserviceAppFuse() throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public int stopserviceAppFuse() throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public boolean supportsBlockCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean supportsCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean supportsFileCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void ufsHid() throws RemoteException {
        }

        @Override // android.os.IVold
        public int umountDfsFuse(String str) throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public void unlockSensitiveKey(int i, int i2, String str, String str2, int i3) throws RemoteException {
        }

        @Override // android.os.IVold
        public void unlockUserKey(int i, int i2, String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void unmount(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void unmountAppFuse(int i, int i2) throws RemoteException {
        }

        @Override // android.os.IVold
        public void unmountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException {
        }

        @Override // android.os.IVold
        public void unmountIncFs(String str) throws RemoteException {
        }

        @Override // android.os.IVold
        public void voldTBExt() throws RemoteException {
        }
    }

    void abortChanges(String str, boolean z) throws RemoteException;

    void abortFuse() throws RemoteException;

    void abortIdleMaint(IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void addAppIds(String[] strArr, int[] iArr) throws RemoteException;

    void addSandboxIds(int[] iArr, String[] strArr) throws RemoteException;

    void benchmark(String str, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void bindMount(String str, String str2) throws RemoteException;

    void checkBeforeMount(String str) throws RemoteException;

    int clearCache(int i) throws RemoteException;

    void commitChanges() throws RemoteException;

    void configDfsFuse(String str, int i, int i2) throws RemoteException;

    String createObb(String str, int i) throws RemoteException;

    String createStubVolume(String str, String str2, String str3, String str4, String str5, int i) throws RemoteException;

    void createUserKey(int i, int i2, boolean z) throws RemoteException;

    void destroyDsuMetadataKey(String str) throws RemoteException;

    void destroyObb(String str) throws RemoteException;

    void destroySandboxForApp(String str, String str2, int i) throws RemoteException;

    void destroyStubVolume(String str) throws RemoteException;

    void destroyUserKey(int i) throws RemoteException;

    void destroyUserStorage(String str, int i, int i2) throws RemoteException;

    void earlyBootEnded() throws RemoteException;

    void encryptFstab(String str, String str2, boolean z, String str3, String str4) throws RemoteException;

    void ensureAppDirsCreated(String[] strArr, int i) throws RemoteException;

    byte[] exportSensitiveBePublicKey(int i, int i2) throws RemoteException;

    byte[] exportSensitiveKey(int i, int i2, boolean z) throws RemoteException;

    void fbeEnable() throws RemoteException;

    void fixupAppDir(String str, int i) throws RemoteException;

    void forgetPartition(String str, String str2) throws RemoteException;

    void format(String str, String str2) throws RemoteException;

    void fstrim(int i, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void fsyncCtrl(String str) throws RemoteException;

    int getStorageLifeTime() throws RemoteException;

    int[] getUnlockedUsers() throws RemoteException;

    int getWriteAmount() throws RemoteException;

    boolean incFsEnabled() throws RemoteException;

    void initUser0() throws RemoteException;

    boolean isCheckpointing() throws RemoteException;

    void lockUserKey(int i) throws RemoteException;

    void markBootAttempt() throws RemoteException;

    void monitor() throws RemoteException;

    void mount(String str, int i, int i2, IVoldMountCallback iVoldMountCallback) throws RemoteException;

    FileDescriptor mountAppFuse(int i, int i2) throws RemoteException;

    FileDescriptor mountDfsFuse(String str, String str2) throws RemoteException;

    void mountFstab(String str, String str2, String str3) throws RemoteException;

    IncrementalFileSystemControlParcel mountIncFs(String str, String str2, int i, String str3) throws RemoteException;

    void moveStorage(String str, String str2, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    boolean needsCheckpoint() throws RemoteException;

    boolean needsRollback() throws RemoteException;

    void onSecureKeyguardStateChanged(boolean z) throws RemoteException;

    void onSecureKeyguardStateChangedForSensitiveFile(boolean z, int i, int i2) throws RemoteException;

    void onUserAdded(int i, int i2, int i3) throws RemoteException;

    void onUserRemoved(int i) throws RemoteException;

    void onUserStarted(int i) throws RemoteException;

    void onUserStopped(int i) throws RemoteException;

    FileDescriptor openAppFuseFile(int i, int i2, int i3, int i4) throws RemoteException;

    void partition(String str, int i, int i2) throws RemoteException;

    void prepareCheckpoint() throws RemoteException;

    void prepareSandboxForApp(String str, int i, String str2, int i2) throws RemoteException;

    void prepareUserStorage(String str, int i, int i2, int i3) throws RemoteException;

    void refreshLatestWrite() throws RemoteException;

    void remountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException;

    void remountUid(int i, int i2) throws RemoteException;

    void reset() throws RemoteException;

    void resetCheckpoint() throws RemoteException;

    void restoreCheckpoint(String str) throws RemoteException;

    void restoreCheckpointPart(String str, int i) throws RemoteException;

    void runIdleMaint(boolean z, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void sdlockClearPassword(String str) throws RemoteException;

    void sdlockErase() throws RemoteException;

    String sdlockGetCid() throws RemoteException;

    String sdlockPoll() throws RemoteException;

    void sdlockSetPassword(String str) throws RemoteException;

    void sdlockUnlock(String str) throws RemoteException;

    void setGCUrgentPace(int i, int i2, float f, float f2, int i3, int i4, int i5) throws RemoteException;

    void setIncFsMountOptions(IncrementalFileSystemControlParcel incrementalFileSystemControlParcel, boolean z, boolean z2, String str) throws RemoteException;

    void setListener(IVoldListener iVoldListener) throws RemoteException;

    void setStorageBindingSeed(byte[] bArr) throws RemoteException;

    void setUserKeyProtection(int i, String str) throws RemoteException;

    void setupAppDir(String str, int i) throws RemoteException;

    void shutdown() throws RemoteException;

    void startCheckpoint(int i) throws RemoteException;

    int startserviceAppFuse() throws RemoteException;

    int stopserviceAppFuse() throws RemoteException;

    boolean supportsBlockCheckpoint() throws RemoteException;

    boolean supportsCheckpoint() throws RemoteException;

    boolean supportsFileCheckpoint() throws RemoteException;

    void ufsHid() throws RemoteException;

    int umountDfsFuse(String str) throws RemoteException;

    void unlockSensitiveKey(int i, int i2, String str, String str2, int i3) throws RemoteException;

    void unlockUserKey(int i, int i2, String str) throws RemoteException;

    void unmount(String str) throws RemoteException;

    void unmountAppFuse(int i, int i2) throws RemoteException;

    void unmountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException;

    void unmountIncFs(String str) throws RemoteException;

    void voldTBExt() throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class Stub extends Binder implements IVold {
        public static final String DESCRIPTOR = "android.os.IVold";
        static final int TRANSACTION_abortChanges = 65;
        static final int TRANSACTION_abortFuse = 2;
        static final int TRANSACTION_abortIdleMaint = 36;
        static final int TRANSACTION_addAppIds = 10;
        static final int TRANSACTION_addSandboxIds = 11;
        static final int TRANSACTION_benchmark = 18;
        static final int TRANSACTION_bindMount = 83;
        static final int TRANSACTION_checkBeforeMount = 19;
        static final int TRANSACTION_clearCache = 45;
        static final int TRANSACTION_commitChanges = 66;
        static final int TRANSACTION_configDfsFuse = 93;
        static final int TRANSACTION_createObb = 32;
        static final int TRANSACTION_createStubVolume = 76;
        static final int TRANSACTION_createUserKey = 51;
        static final int TRANSACTION_destroyDsuMetadataKey = 94;
        static final int TRANSACTION_destroyObb = 33;
        static final int TRANSACTION_destroySandboxForApp = 60;
        static final int TRANSACTION_destroyStubVolume = 77;
        static final int TRANSACTION_destroyUserKey = 52;
        static final int TRANSACTION_destroyUserStorage = 58;
        static final int TRANSACTION_earlyBootEnded = 75;
        static final int TRANSACTION_encryptFstab = 49;
        static final int TRANSACTION_ensureAppDirsCreated = 31;
        static final int TRANSACTION_exportSensitiveBePublicKey = 24;
        static final int TRANSACTION_exportSensitiveKey = 23;
        static final int TRANSACTION_fbeEnable = 46;
        static final int TRANSACTION_fixupAppDir = 30;
        static final int TRANSACTION_forgetPartition = 14;
        static final int TRANSACTION_format = 17;
        static final int TRANSACTION_fstrim = 34;
        static final int TRANSACTION_fsyncCtrl = 20;
        static final int TRANSACTION_getStorageLifeTime = 37;
        static final int TRANSACTION_getUnlockedUsers = 54;
        static final int TRANSACTION_getWriteAmount = 40;
        static final int TRANSACTION_incFsEnabled = 79;
        static final int TRANSACTION_initUser0 = 47;
        static final int TRANSACTION_isCheckpointing = 64;
        static final int TRANSACTION_lockUserKey = 56;
        static final int TRANSACTION_markBootAttempt = 70;
        static final int TRANSACTION_monitor = 3;
        static final int TRANSACTION_mount = 15;
        static final int TRANSACTION_mountAppFuse = 41;
        static final int TRANSACTION_mountDfsFuse = 91;
        static final int TRANSACTION_mountFstab = 48;
        static final int TRANSACTION_mountIncFs = 80;
        static final int TRANSACTION_moveStorage = 25;
        static final int TRANSACTION_needsCheckpoint = 62;
        static final int TRANSACTION_needsRollback = 63;
        static final int TRANSACTION_onSecureKeyguardStateChanged = 12;
        static final int TRANSACTION_onSecureKeyguardStateChangedForSensitiveFile = 22;
        static final int TRANSACTION_onUserAdded = 6;
        static final int TRANSACTION_onUserRemoved = 7;
        static final int TRANSACTION_onUserStarted = 8;
        static final int TRANSACTION_onUserStopped = 9;
        static final int TRANSACTION_openAppFuseFile = 78;
        static final int TRANSACTION_partition = 13;
        static final int TRANSACTION_prepareCheckpoint = 67;
        static final int TRANSACTION_prepareSandboxForApp = 59;
        static final int TRANSACTION_prepareUserStorage = 57;
        static final int TRANSACTION_refreshLatestWrite = 39;
        static final int TRANSACTION_remountAppStorageDirs = 27;
        static final int TRANSACTION_remountUid = 26;
        static final int TRANSACTION_reset = 4;
        static final int TRANSACTION_resetCheckpoint = 74;
        static final int TRANSACTION_restoreCheckpoint = 68;
        static final int TRANSACTION_restoreCheckpointPart = 69;
        static final int TRANSACTION_runIdleMaint = 35;
        static final int TRANSACTION_sdlockClearPassword = 85;
        static final int TRANSACTION_sdlockErase = 89;
        static final int TRANSACTION_sdlockGetCid = 87;
        static final int TRANSACTION_sdlockPoll = 88;
        static final int TRANSACTION_sdlockSetPassword = 84;
        static final int TRANSACTION_sdlockUnlock = 86;
        static final int TRANSACTION_setGCUrgentPace = 38;
        static final int TRANSACTION_setIncFsMountOptions = 82;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_setStorageBindingSeed = 50;
        static final int TRANSACTION_setUserKeyProtection = 53;
        static final int TRANSACTION_setupAppDir = 29;
        static final int TRANSACTION_shutdown = 5;
        static final int TRANSACTION_startCheckpoint = 61;
        static final int TRANSACTION_startserviceAppFuse = 43;
        static final int TRANSACTION_stopserviceAppFuse = 44;
        static final int TRANSACTION_supportsBlockCheckpoint = 72;
        static final int TRANSACTION_supportsCheckpoint = 71;
        static final int TRANSACTION_supportsFileCheckpoint = 73;
        static final int TRANSACTION_ufsHid = 95;
        static final int TRANSACTION_umountDfsFuse = 92;
        static final int TRANSACTION_unlockSensitiveKey = 21;
        static final int TRANSACTION_unlockUserKey = 55;
        static final int TRANSACTION_unmount = 16;
        static final int TRANSACTION_unmountAppFuse = 42;
        static final int TRANSACTION_unmountAppStorageDirs = 28;
        static final int TRANSACTION_unmountIncFs = 81;
        static final int TRANSACTION_voldTBExt = 90;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVold asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IVold)) {
                return (IVold) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    IVoldListener asInterface = IVoldListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    setListener(asInterface);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    abortFuse();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    monitor();
                    parcel2.writeNoException();
                    return true;
                case 4:
                    reset();
                    parcel2.writeNoException();
                    return true;
                case 5:
                    shutdown();
                    parcel2.writeNoException();
                    return true;
                case 6:
                    int readInt = parcel.readInt();
                    int readInt2 = parcel.readInt();
                    int readInt3 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onUserAdded(readInt, readInt2, readInt3);
                    parcel2.writeNoException();
                    return true;
                case 7:
                    int readInt4 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onUserRemoved(readInt4);
                    parcel2.writeNoException();
                    return true;
                case 8:
                    int readInt5 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onUserStarted(readInt5);
                    parcel2.writeNoException();
                    return true;
                case 9:
                    int readInt6 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onUserStopped(readInt6);
                    parcel2.writeNoException();
                    return true;
                case 10:
                    String[] createStringArray = parcel.createStringArray();
                    int[] createIntArray = parcel.createIntArray();
                    parcel.enforceNoDataAvail();
                    addAppIds(createStringArray, createIntArray);
                    parcel2.writeNoException();
                    return true;
                case 11:
                    int[] createIntArray2 = parcel.createIntArray();
                    String[] createStringArray2 = parcel.createStringArray();
                    parcel.enforceNoDataAvail();
                    addSandboxIds(createIntArray2, createStringArray2);
                    parcel2.writeNoException();
                    return true;
                case 12:
                    boolean readBoolean = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    onSecureKeyguardStateChanged(readBoolean);
                    parcel2.writeNoException();
                    return true;
                case 13:
                    String readString = parcel.readString();
                    int readInt7 = parcel.readInt();
                    int readInt8 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    partition(readString, readInt7, readInt8);
                    parcel2.writeNoException();
                    return true;
                case 14:
                    String readString2 = parcel.readString();
                    String readString3 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    forgetPartition(readString2, readString3);
                    parcel2.writeNoException();
                    return true;
                case 15:
                    String readString4 = parcel.readString();
                    int readInt9 = parcel.readInt();
                    int readInt10 = parcel.readInt();
                    IVoldMountCallback asInterface2 = IVoldMountCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    mount(readString4, readInt9, readInt10, asInterface2);
                    parcel2.writeNoException();
                    return true;
                case 16:
                    String readString5 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    unmount(readString5);
                    parcel2.writeNoException();
                    return true;
                case 17:
                    String readString6 = parcel.readString();
                    String readString7 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    format(readString6, readString7);
                    parcel2.writeNoException();
                    return true;
                case 18:
                    String readString8 = parcel.readString();
                    IVoldTaskListener asInterface3 = IVoldTaskListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    benchmark(readString8, asInterface3);
                    parcel2.writeNoException();
                    return true;
                case 19:
                    String readString9 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    checkBeforeMount(readString9);
                    parcel2.writeNoException();
                    return true;
                case 20:
                    String readString10 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    fsyncCtrl(readString10);
                    parcel2.writeNoException();
                    return true;
                case 21:
                    int readInt11 = parcel.readInt();
                    int readInt12 = parcel.readInt();
                    String readString11 = parcel.readString();
                    String readString12 = parcel.readString();
                    int readInt13 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    unlockSensitiveKey(readInt11, readInt12, readString11, readString12, readInt13);
                    parcel2.writeNoException();
                    return true;
                case 22:
                    boolean readBoolean2 = parcel.readBoolean();
                    int readInt14 = parcel.readInt();
                    int readInt15 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onSecureKeyguardStateChangedForSensitiveFile(readBoolean2, readInt14, readInt15);
                    parcel2.writeNoException();
                    return true;
                case 23:
                    int readInt16 = parcel.readInt();
                    int readInt17 = parcel.readInt();
                    boolean readBoolean3 = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    byte[] exportSensitiveKey = exportSensitiveKey(readInt16, readInt17, readBoolean3);
                    parcel2.writeNoException();
                    parcel2.writeByteArray(exportSensitiveKey);
                    return true;
                case 24:
                    int readInt18 = parcel.readInt();
                    int readInt19 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    byte[] exportSensitiveBePublicKey = exportSensitiveBePublicKey(readInt18, readInt19);
                    parcel2.writeNoException();
                    parcel2.writeByteArray(exportSensitiveBePublicKey);
                    return true;
                case 25:
                    String readString13 = parcel.readString();
                    String readString14 = parcel.readString();
                    IVoldTaskListener asInterface4 = IVoldTaskListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    moveStorage(readString13, readString14, asInterface4);
                    parcel2.writeNoException();
                    return true;
                case 26:
                    int readInt20 = parcel.readInt();
                    int readInt21 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    remountUid(readInt20, readInt21);
                    parcel2.writeNoException();
                    return true;
                case 27:
                    int readInt22 = parcel.readInt();
                    int readInt23 = parcel.readInt();
                    String[] createStringArray3 = parcel.createStringArray();
                    parcel.enforceNoDataAvail();
                    remountAppStorageDirs(readInt22, readInt23, createStringArray3);
                    parcel2.writeNoException();
                    return true;
                case 28:
                    int readInt24 = parcel.readInt();
                    int readInt25 = parcel.readInt();
                    String[] createStringArray4 = parcel.createStringArray();
                    parcel.enforceNoDataAvail();
                    unmountAppStorageDirs(readInt24, readInt25, createStringArray4);
                    parcel2.writeNoException();
                    return true;
                case 29:
                    String readString15 = parcel.readString();
                    int readInt26 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    setupAppDir(readString15, readInt26);
                    parcel2.writeNoException();
                    return true;
                case 30:
                    String readString16 = parcel.readString();
                    int readInt27 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    fixupAppDir(readString16, readInt27);
                    parcel2.writeNoException();
                    return true;
                case 31:
                    String[] createStringArray5 = parcel.createStringArray();
                    int readInt28 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    ensureAppDirsCreated(createStringArray5, readInt28);
                    parcel2.writeNoException();
                    return true;
                case 32:
                    String readString17 = parcel.readString();
                    int readInt29 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    String createObb = createObb(readString17, readInt29);
                    parcel2.writeNoException();
                    parcel2.writeString(createObb);
                    return true;
                case 33:
                    String readString18 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    destroyObb(readString18);
                    parcel2.writeNoException();
                    return true;
                case 34:
                    int readInt30 = parcel.readInt();
                    IVoldTaskListener asInterface5 = IVoldTaskListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    fstrim(readInt30, asInterface5);
                    parcel2.writeNoException();
                    return true;
                case 35:
                    boolean readBoolean4 = parcel.readBoolean();
                    IVoldTaskListener asInterface6 = IVoldTaskListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    runIdleMaint(readBoolean4, asInterface6);
                    parcel2.writeNoException();
                    return true;
                case 36:
                    IVoldTaskListener asInterface7 = IVoldTaskListener.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    abortIdleMaint(asInterface7);
                    parcel2.writeNoException();
                    return true;
                case 37:
                    int storageLifeTime = getStorageLifeTime();
                    parcel2.writeNoException();
                    parcel2.writeInt(storageLifeTime);
                    return true;
                case 38:
                    int readInt31 = parcel.readInt();
                    int readInt32 = parcel.readInt();
                    float readFloat = parcel.readFloat();
                    float readFloat2 = parcel.readFloat();
                    int readInt33 = parcel.readInt();
                    int readInt34 = parcel.readInt();
                    int readInt35 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    setGCUrgentPace(readInt31, readInt32, readFloat, readFloat2, readInt33, readInt34, readInt35);
                    parcel2.writeNoException();
                    return true;
                case 39:
                    refreshLatestWrite();
                    parcel2.writeNoException();
                    return true;
                case 40:
                    int writeAmount = getWriteAmount();
                    parcel2.writeNoException();
                    parcel2.writeInt(writeAmount);
                    return true;
                case 41:
                    int readInt36 = parcel.readInt();
                    int readInt37 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    FileDescriptor mountAppFuse = mountAppFuse(readInt36, readInt37);
                    parcel2.writeNoException();
                    parcel2.writeRawFileDescriptor(mountAppFuse);
                    return true;
                case 42:
                    int readInt38 = parcel.readInt();
                    int readInt39 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    unmountAppFuse(readInt38, readInt39);
                    parcel2.writeNoException();
                    return true;
                case 43:
                    int startserviceAppFuse = startserviceAppFuse();
                    parcel2.writeNoException();
                    parcel2.writeInt(startserviceAppFuse);
                    return true;
                case 44:
                    int stopserviceAppFuse = stopserviceAppFuse();
                    parcel2.writeNoException();
                    parcel2.writeInt(stopserviceAppFuse);
                    return true;
                case 45:
                    int readInt40 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    int clearCache = clearCache(readInt40);
                    parcel2.writeNoException();
                    parcel2.writeInt(clearCache);
                    return true;
                case 46:
                    fbeEnable();
                    parcel2.writeNoException();
                    return true;
                case 47:
                    initUser0();
                    parcel2.writeNoException();
                    return true;
                case 48:
                    String readString19 = parcel.readString();
                    String readString20 = parcel.readString();
                    String readString21 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    mountFstab(readString19, readString20, readString21);
                    parcel2.writeNoException();
                    return true;
                case 49:
                    String readString22 = parcel.readString();
                    String readString23 = parcel.readString();
                    boolean readBoolean5 = parcel.readBoolean();
                    String readString24 = parcel.readString();
                    String readString25 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    encryptFstab(readString22, readString23, readBoolean5, readString24, readString25);
                    parcel2.writeNoException();
                    return true;
                case 50:
                    byte[] createByteArray = parcel.createByteArray();
                    parcel.enforceNoDataAvail();
                    setStorageBindingSeed(createByteArray);
                    parcel2.writeNoException();
                    return true;
                case 51:
                    int readInt41 = parcel.readInt();
                    int readInt42 = parcel.readInt();
                    boolean readBoolean6 = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    createUserKey(readInt41, readInt42, readBoolean6);
                    parcel2.writeNoException();
                    return true;
                case 52:
                    int readInt43 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    destroyUserKey(readInt43);
                    parcel2.writeNoException();
                    return true;
                case 53:
                    int readInt44 = parcel.readInt();
                    String readString26 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    setUserKeyProtection(readInt44, readString26);
                    parcel2.writeNoException();
                    return true;
                case 54:
                    int[] unlockedUsers = getUnlockedUsers();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(unlockedUsers);
                    return true;
                case 55:
                    int readInt45 = parcel.readInt();
                    int readInt46 = parcel.readInt();
                    String readString27 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    unlockUserKey(readInt45, readInt46, readString27);
                    parcel2.writeNoException();
                    return true;
                case 56:
                    int readInt47 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    lockUserKey(readInt47);
                    parcel2.writeNoException();
                    return true;
                case 57:
                    String readString28 = parcel.readString();
                    int readInt48 = parcel.readInt();
                    int readInt49 = parcel.readInt();
                    int readInt50 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    prepareUserStorage(readString28, readInt48, readInt49, readInt50);
                    parcel2.writeNoException();
                    return true;
                case 58:
                    String readString29 = parcel.readString();
                    int readInt51 = parcel.readInt();
                    int readInt52 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    destroyUserStorage(readString29, readInt51, readInt52);
                    parcel2.writeNoException();
                    return true;
                case 59:
                    String readString30 = parcel.readString();
                    int readInt53 = parcel.readInt();
                    String readString31 = parcel.readString();
                    int readInt54 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    prepareSandboxForApp(readString30, readInt53, readString31, readInt54);
                    parcel2.writeNoException();
                    return true;
                case 60:
                    String readString32 = parcel.readString();
                    String readString33 = parcel.readString();
                    int readInt55 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    destroySandboxForApp(readString32, readString33, readInt55);
                    parcel2.writeNoException();
                    return true;
                case 61:
                    int readInt56 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    startCheckpoint(readInt56);
                    parcel2.writeNoException();
                    return true;
                case 62:
                    boolean needsCheckpoint = needsCheckpoint();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(needsCheckpoint);
                    return true;
                case 63:
                    boolean needsRollback = needsRollback();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(needsRollback);
                    return true;
                case 64:
                    boolean isCheckpointing = isCheckpointing();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(isCheckpointing);
                    return true;
                case 65:
                    String readString34 = parcel.readString();
                    boolean readBoolean7 = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    abortChanges(readString34, readBoolean7);
                    parcel2.writeNoException();
                    return true;
                case 66:
                    commitChanges();
                    parcel2.writeNoException();
                    return true;
                case 67:
                    prepareCheckpoint();
                    parcel2.writeNoException();
                    return true;
                case 68:
                    String readString35 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    restoreCheckpoint(readString35);
                    parcel2.writeNoException();
                    return true;
                case 69:
                    String readString36 = parcel.readString();
                    int readInt57 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    restoreCheckpointPart(readString36, readInt57);
                    parcel2.writeNoException();
                    return true;
                case 70:
                    markBootAttempt();
                    parcel2.writeNoException();
                    return true;
                case 71:
                    boolean supportsCheckpoint = supportsCheckpoint();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(supportsCheckpoint);
                    return true;
                case 72:
                    boolean supportsBlockCheckpoint = supportsBlockCheckpoint();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(supportsBlockCheckpoint);
                    return true;
                case 73:
                    boolean supportsFileCheckpoint = supportsFileCheckpoint();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(supportsFileCheckpoint);
                    return true;
                case 74:
                    resetCheckpoint();
                    parcel2.writeNoException();
                    return true;
                case 75:
                    earlyBootEnded();
                    parcel2.writeNoException();
                    return true;
                case 76:
                    String readString37 = parcel.readString();
                    String readString38 = parcel.readString();
                    String readString39 = parcel.readString();
                    String readString40 = parcel.readString();
                    String readString41 = parcel.readString();
                    int readInt58 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    String createStubVolume = createStubVolume(readString37, readString38, readString39, readString40, readString41, readInt58);
                    parcel2.writeNoException();
                    parcel2.writeString(createStubVolume);
                    return true;
                case 77:
                    String readString42 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    destroyStubVolume(readString42);
                    parcel2.writeNoException();
                    return true;
                case 78:
                    int readInt59 = parcel.readInt();
                    int readInt60 = parcel.readInt();
                    int readInt61 = parcel.readInt();
                    int readInt62 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    FileDescriptor openAppFuseFile = openAppFuseFile(readInt59, readInt60, readInt61, readInt62);
                    parcel2.writeNoException();
                    parcel2.writeRawFileDescriptor(openAppFuseFile);
                    return true;
                case 79:
                    boolean incFsEnabled = incFsEnabled();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(incFsEnabled);
                    return true;
                case 80:
                    String readString43 = parcel.readString();
                    String readString44 = parcel.readString();
                    int readInt63 = parcel.readInt();
                    String readString45 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    IncrementalFileSystemControlParcel mountIncFs = mountIncFs(readString43, readString44, readInt63, readString45);
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(mountIncFs, 1);
                    return true;
                case 81:
                    String readString46 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    unmountIncFs(readString46);
                    parcel2.writeNoException();
                    return true;
                case 82:
                    IncrementalFileSystemControlParcel incrementalFileSystemControlParcel = (IncrementalFileSystemControlParcel) parcel.readTypedObject(IncrementalFileSystemControlParcel.CREATOR);
                    boolean readBoolean8 = parcel.readBoolean();
                    boolean readBoolean9 = parcel.readBoolean();
                    String readString47 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    setIncFsMountOptions(incrementalFileSystemControlParcel, readBoolean8, readBoolean9, readString47);
                    parcel2.writeNoException();
                    return true;
                case 83:
                    String readString48 = parcel.readString();
                    String readString49 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    bindMount(readString48, readString49);
                    parcel2.writeNoException();
                    return true;
                case 84:
                    String readString50 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    sdlockSetPassword(readString50);
                    parcel2.writeNoException();
                    return true;
                case 85:
                    String readString51 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    sdlockClearPassword(readString51);
                    parcel2.writeNoException();
                    return true;
                case 86:
                    String readString52 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    sdlockUnlock(readString52);
                    parcel2.writeNoException();
                    return true;
                case 87:
                    String sdlockGetCid = sdlockGetCid();
                    parcel2.writeNoException();
                    parcel2.writeString(sdlockGetCid);
                    return true;
                case 88:
                    String sdlockPoll = sdlockPoll();
                    parcel2.writeNoException();
                    parcel2.writeString(sdlockPoll);
                    return true;
                case 89:
                    sdlockErase();
                    parcel2.writeNoException();
                    return true;
                case 90:
                    voldTBExt();
                    parcel2.writeNoException();
                    return true;
                case 91:
                    String readString53 = parcel.readString();
                    String readString54 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    FileDescriptor mountDfsFuse = mountDfsFuse(readString53, readString54);
                    parcel2.writeNoException();
                    parcel2.writeRawFileDescriptor(mountDfsFuse);
                    return true;
                case 92:
                    String readString55 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    int umountDfsFuse = umountDfsFuse(readString55);
                    parcel2.writeNoException();
                    parcel2.writeInt(umountDfsFuse);
                    return true;
                case 93:
                    String readString56 = parcel.readString();
                    int readInt64 = parcel.readInt();
                    int readInt65 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    configDfsFuse(readString56, readInt64, readInt65);
                    parcel2.writeNoException();
                    return true;
                case 94:
                    String readString57 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    destroyDsuMetadataKey(readString57);
                    parcel2.writeNoException();
                    return true;
                case 95:
                    ufsHid();
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private static class Proxy implements IVold {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // android.os.IVold
            public void setListener(IVoldListener iVoldListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iVoldListener);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortFuse() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void monitor() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void reset() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void shutdown() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserAdded(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserRemoved(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserStarted(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserStopped(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void addAppIds(String[] strArr, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStringArray(strArr);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void addSandboxIds(int[] iArr, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    obtain.writeStringArray(strArr);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void onSecureKeyguardStateChanged(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void partition(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void forgetPartition(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void mount(String str, int i, int i2, IVoldMountCallback iVoldMountCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeStrongInterface(iVoldMountCallback);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmount(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void format(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void benchmark(String str, IVoldTaskListener iVoldTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongInterface(iVoldTaskListener);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void checkBeforeMount(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void fsyncCtrl(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void unlockSensitiveKey(int i, int i2, String str, String str2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void onSecureKeyguardStateChangedForSensitiveFile(boolean z, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public byte[] exportSensitiveKey(int i, int i2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public byte[] exportSensitiveBePublicKey(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void moveStorage(String str, String str2, IVoldTaskListener iVoldTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongInterface(iVoldTaskListener);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void remountUid(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void remountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeStringArray(strArr);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeStringArray(strArr);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void setupAppDir(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void fixupAppDir(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void ensureAppDirsCreated(String[] strArr, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStringArray(strArr);
                    obtain.writeInt(i);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public String createObb(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyObb(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void fstrim(int i, IVoldTaskListener iVoldTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iVoldTaskListener);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void runIdleMaint(boolean z, IVoldTaskListener iVoldTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    obtain.writeStrongInterface(iVoldTaskListener);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortIdleMaint(IVoldTaskListener iVoldTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iVoldTaskListener);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public int getStorageLifeTime() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void setGCUrgentPace(int i, int i2, float f, float f2, int i3, int i4, int i5) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeFloat(f);
                    obtain.writeFloat(f2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeInt(i5);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void refreshLatestWrite() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public int getWriteAmount() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public FileDescriptor mountAppFuse(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readRawFileDescriptor();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountAppFuse(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public int startserviceAppFuse() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public int stopserviceAppFuse() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public int clearCache(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void fbeEnable() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void initUser0() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void mountFstab(String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void encryptFstab(String str, String str2, boolean z, String str3, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeBoolean(z);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void setStorageBindingSeed(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void createUserKey(int i, int i2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyUserKey(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void setUserKeyProtection(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public int[] getUnlockedUsers() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createIntArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void unlockUserKey(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void lockUserKey(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(56, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareUserStorage(String str, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyUserStorage(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(58, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareSandboxForApp(String str, int i, String str2, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeString(str2);
                    obtain.writeInt(i2);
                    this.mRemote.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroySandboxForApp(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(60, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void startCheckpoint(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean needsCheckpoint() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(62, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean needsRollback() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(63, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean isCheckpointing() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(64, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortChanges(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(65, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void commitChanges() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(66, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareCheckpoint() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(67, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void restoreCheckpoint(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(68, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void restoreCheckpointPart(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(69, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void markBootAttempt() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(70, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean supportsCheckpoint() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(71, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean supportsBlockCheckpoint() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(72, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean supportsFileCheckpoint() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(73, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void resetCheckpoint() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(74, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void earlyBootEnded() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(75, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public String createStubVolume(String str, String str2, String str3, String str4, String str5, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeString(str5);
                    obtain.writeInt(i);
                    this.mRemote.transact(76, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyStubVolume(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(77, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public FileDescriptor openAppFuseFile(int i, int i2, int i3, int i4) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    this.mRemote.transact(78, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readRawFileDescriptor();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean incFsEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(79, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public IncrementalFileSystemControlParcel mountIncFs(String str, String str2, int i, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeString(str3);
                    this.mRemote.transact(80, obtain, obtain2, 0);
                    obtain2.readException();
                    return (IncrementalFileSystemControlParcel) obtain2.readTypedObject(IncrementalFileSystemControlParcel.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountIncFs(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(81, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void setIncFsMountOptions(IncrementalFileSystemControlParcel incrementalFileSystemControlParcel, boolean z, boolean z2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(incrementalFileSystemControlParcel, 0);
                    obtain.writeBoolean(z);
                    obtain.writeBoolean(z2);
                    obtain.writeString(str);
                    this.mRemote.transact(82, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void bindMount(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(83, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockSetPassword(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(84, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockClearPassword(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(85, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockUnlock(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(86, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public String sdlockGetCid() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(87, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public String sdlockPoll() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(88, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockErase() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(89, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void voldTBExt() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(90, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public FileDescriptor mountDfsFuse(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(91, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readRawFileDescriptor();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public int umountDfsFuse(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(92, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void configDfsFuse(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(93, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyDsuMetadataKey(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(94, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IVold
            public void ufsHid() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(95, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
