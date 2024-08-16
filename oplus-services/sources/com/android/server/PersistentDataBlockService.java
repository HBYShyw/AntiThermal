package com.android.server;

import android.R;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.persistentdata.IPersistentDataBlockService;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.Preconditions;
import com.android.server.pm.UserManagerInternal;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PersistentDataBlockService extends SystemService {
    public static final int DIGEST_SIZE_BYTES = 32;
    private static final String FLASH_LOCK_LOCKED = "1";
    private static final String FLASH_LOCK_PROP = "ro.boot.flash.locked";
    private static final String FLASH_LOCK_UNLOCKED = "0";
    private static final int FRP_CREDENTIAL_RESERVED_SIZE = 1000;
    private static final String GSI_RUNNING_PROP = "ro.gsid.image_running";
    private static final String GSI_SANDBOX = "/data/gsi_persistent_data";
    private static final int HEADER_SIZE = 8;
    private static final int MAX_DATA_BLOCK_SIZE = 102400;
    private static final int MAX_FRP_CREDENTIAL_HANDLE_SIZE = 996;
    private static final int MAX_TEST_MODE_DATA_SIZE = 9996;
    private static final String OEM_UNLOCK_PROP = "sys.oem_unlock_allowed";
    private static final int PARTITION_TYPE_MARKER = 428873843;
    private static final String PERSISTENT_DATA_BLOCK_PROP = "ro.frp.pst";
    private static final String TAG = "PersistentDataBlockService";
    private static final int TEST_MODE_RESERVED_SIZE = 10000;
    private int mAllowedUid;
    private long mBlockDeviceSize;
    private final Context mContext;
    private final String mDataBlockFile;
    private final CountDownLatch mInitDoneSignal;
    private PersistentDataBlockManagerInternal mInternalService;
    private final boolean mIsRunningDSU;

    @GuardedBy({"mLock"})
    private boolean mIsWritable;
    private final Object mLock;
    private final IBinder mService;

    private native long nativeGetBlockDeviceSize(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeWipe(String str);

    public PersistentDataBlockService(Context context) {
        super(context);
        this.mLock = new Object();
        this.mInitDoneSignal = new CountDownLatch(1);
        this.mAllowedUid = -1;
        this.mIsWritable = true;
        this.mService = new IPersistentDataBlockService.Stub() { // from class: com.android.server.PersistentDataBlockService.1
            public int write(byte[] bArr) throws RemoteException {
                PersistentDataBlockService.this.enforceUid(Binder.getCallingUid());
                long doGetMaximumDataBlockSize = PersistentDataBlockService.this.doGetMaximumDataBlockSize();
                if (bArr.length > doGetMaximumDataBlockSize) {
                    return (int) (-doGetMaximumDataBlockSize);
                }
                try {
                    FileChannel blockOutputChannel = PersistentDataBlockService.this.getBlockOutputChannel();
                    ByteBuffer allocate = ByteBuffer.allocate(bArr.length + 8 + 32);
                    allocate.put(new byte[32]);
                    allocate.putInt(PersistentDataBlockService.PARTITION_TYPE_MARKER);
                    allocate.putInt(bArr.length);
                    allocate.put(bArr);
                    allocate.flip();
                    synchronized (PersistentDataBlockService.this.mLock) {
                        if (!PersistentDataBlockService.this.mIsWritable) {
                            return -1;
                        }
                        try {
                            blockOutputChannel.write(allocate);
                            blockOutputChannel.force(true);
                            if (!PersistentDataBlockService.this.computeAndWriteDigestLocked()) {
                                return -1;
                            }
                            return bArr.length;
                        } catch (IOException e) {
                            Slog.e(PersistentDataBlockService.TAG, "failed writing to the persistent data block", e);
                            return -1;
                        }
                    }
                } catch (IOException e2) {
                    Slog.e(PersistentDataBlockService.TAG, "partition not available?", e2);
                    return -1;
                }
            }

            public byte[] read() {
                PersistentDataBlockService.this.enforceUid(Binder.getCallingUid());
                if (!PersistentDataBlockService.this.enforceChecksumValidity()) {
                    return new byte[0];
                }
                try {
                    DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(PersistentDataBlockService.this.mDataBlockFile)));
                    try {
                        try {
                            synchronized (PersistentDataBlockService.this.mLock) {
                                int totalDataSizeLocked = PersistentDataBlockService.this.getTotalDataSizeLocked(dataInputStream);
                                if (totalDataSizeLocked == 0) {
                                    byte[] bArr = new byte[0];
                                    try {
                                        dataInputStream.close();
                                    } catch (IOException unused) {
                                        Slog.e(PersistentDataBlockService.TAG, "failed to close OutputStream");
                                    }
                                    return bArr;
                                }
                                byte[] bArr2 = new byte[totalDataSizeLocked];
                                int read = dataInputStream.read(bArr2, 0, totalDataSizeLocked);
                                if (read >= totalDataSizeLocked) {
                                    try {
                                        dataInputStream.close();
                                    } catch (IOException unused2) {
                                        Slog.e(PersistentDataBlockService.TAG, "failed to close OutputStream");
                                    }
                                    return bArr2;
                                }
                                Slog.e(PersistentDataBlockService.TAG, "failed to read entire data block. bytes read: " + read + "/" + totalDataSizeLocked);
                                try {
                                    dataInputStream.close();
                                } catch (IOException unused3) {
                                    Slog.e(PersistentDataBlockService.TAG, "failed to close OutputStream");
                                }
                                return null;
                            }
                        } catch (IOException e) {
                            Slog.e(PersistentDataBlockService.TAG, "failed to read data", e);
                            try {
                                dataInputStream.close();
                            } catch (IOException unused4) {
                                Slog.e(PersistentDataBlockService.TAG, "failed to close OutputStream");
                            }
                            return null;
                        }
                    } catch (Throwable th) {
                        try {
                            dataInputStream.close();
                        } catch (IOException unused5) {
                            Slog.e(PersistentDataBlockService.TAG, "failed to close OutputStream");
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e2) {
                    Slog.e(PersistentDataBlockService.TAG, "partition not available?", e2);
                    return null;
                }
            }

            public void wipe() {
                PersistentDataBlockService.this.enforceOemUnlockWritePermission();
                synchronized (PersistentDataBlockService.this.mLock) {
                    PersistentDataBlockService persistentDataBlockService = PersistentDataBlockService.this;
                    if (persistentDataBlockService.nativeWipe(persistentDataBlockService.mDataBlockFile) < 0) {
                        Slog.e(PersistentDataBlockService.TAG, "failed to wipe persistent partition");
                    } else {
                        PersistentDataBlockService.this.mIsWritable = false;
                        Slog.i(PersistentDataBlockService.TAG, "persistent partition now wiped and unwritable");
                    }
                }
            }

            public void setOemUnlockEnabled(boolean z) throws SecurityException {
                if (ActivityManager.isUserAMonkey()) {
                    return;
                }
                PersistentDataBlockService.this.enforceOemUnlockWritePermission();
                PersistentDataBlockService.this.enforceIsAdmin();
                if (z) {
                    PersistentDataBlockService.this.enforceUserRestriction("no_oem_unlock");
                    PersistentDataBlockService.this.enforceUserRestriction("no_factory_reset");
                }
                synchronized (PersistentDataBlockService.this.mLock) {
                    PersistentDataBlockService.this.doSetOemUnlockEnabledLocked(z);
                    PersistentDataBlockService.this.computeAndWriteDigestLocked();
                }
            }

            public boolean getOemUnlockEnabled() {
                PersistentDataBlockService.this.enforceOemUnlockReadPermission();
                return PersistentDataBlockService.this.doGetOemUnlockEnabled();
            }

            public int getFlashLockState() {
                PersistentDataBlockService.this.enforceOemUnlockReadPermission();
                String str = SystemProperties.get(PersistentDataBlockService.FLASH_LOCK_PROP);
                str.hashCode();
                if (str.equals(PersistentDataBlockService.FLASH_LOCK_UNLOCKED)) {
                    return 0;
                }
                return !str.equals(PersistentDataBlockService.FLASH_LOCK_LOCKED) ? -1 : 1;
            }

            public int getDataBlockSize() {
                int totalDataSizeLocked;
                enforcePersistentDataBlockAccess();
                try {
                    DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(PersistentDataBlockService.this.mDataBlockFile)));
                    try {
                        synchronized (PersistentDataBlockService.this.mLock) {
                            totalDataSizeLocked = PersistentDataBlockService.this.getTotalDataSizeLocked(dataInputStream);
                        }
                        return totalDataSizeLocked;
                    } catch (IOException unused) {
                        Slog.e(PersistentDataBlockService.TAG, "error reading data block size");
                        return 0;
                    } finally {
                        IoUtils.closeQuietly(dataInputStream);
                    }
                } catch (FileNotFoundException unused2) {
                    Slog.e(PersistentDataBlockService.TAG, "partition not available");
                    return 0;
                }
            }

            private void enforcePersistentDataBlockAccess() {
                if (PersistentDataBlockService.this.mContext.checkCallingPermission("android.permission.ACCESS_PDB_STATE") != 0) {
                    PersistentDataBlockService.this.enforceUid(Binder.getCallingUid());
                }
            }

            public long getMaximumDataBlockSize() {
                PersistentDataBlockService.this.enforceUid(Binder.getCallingUid());
                return PersistentDataBlockService.this.doGetMaximumDataBlockSize();
            }

            public boolean hasFrpCredentialHandle() {
                enforcePersistentDataBlockAccess();
                try {
                    return PersistentDataBlockService.this.mInternalService.getFrpCredentialHandle() != null;
                } catch (IllegalStateException e) {
                    Slog.e(PersistentDataBlockService.TAG, "error reading frp handle", e);
                    throw new UnsupportedOperationException("cannot read frp credential");
                }
            }

            public String getPersistentDataPackageName() {
                enforcePersistentDataBlockAccess();
                return PersistentDataBlockService.this.mContext.getString(R.string.date_picker_mode);
            }

            protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
                if (DumpUtils.checkDumpPermission(PersistentDataBlockService.this.mContext, PersistentDataBlockService.TAG, printWriter)) {
                    printWriter.println("mDataBlockFile: " + PersistentDataBlockService.this.mDataBlockFile);
                    printWriter.println("mIsRunningDSU: " + PersistentDataBlockService.this.mIsRunningDSU);
                    printWriter.println("mInitDoneSignal: " + PersistentDataBlockService.this.mInitDoneSignal);
                    printWriter.println("mAllowedUid: " + PersistentDataBlockService.this.mAllowedUid);
                    printWriter.println("mBlockDeviceSize: " + PersistentDataBlockService.this.mBlockDeviceSize);
                    synchronized (PersistentDataBlockService.this.mLock) {
                        printWriter.println("mIsWritable: " + PersistentDataBlockService.this.mIsWritable);
                    }
                }
            }
        };
        this.mInternalService = new PersistentDataBlockManagerInternal() { // from class: com.android.server.PersistentDataBlockService.2
            @Override // com.android.server.PersistentDataBlockManagerInternal
            public void setFrpCredentialHandle(byte[] bArr) {
                writeInternal(bArr, PersistentDataBlockService.this.getFrpCredentialDataOffset(), PersistentDataBlockService.MAX_FRP_CREDENTIAL_HANDLE_SIZE);
            }

            @Override // com.android.server.PersistentDataBlockManagerInternal
            public byte[] getFrpCredentialHandle() {
                return readInternal(PersistentDataBlockService.this.getFrpCredentialDataOffset(), PersistentDataBlockService.MAX_FRP_CREDENTIAL_HANDLE_SIZE);
            }

            @Override // com.android.server.PersistentDataBlockManagerInternal
            public void setTestHarnessModeData(byte[] bArr) {
                writeInternal(bArr, PersistentDataBlockService.this.getTestHarnessModeDataOffset(), PersistentDataBlockService.MAX_TEST_MODE_DATA_SIZE);
            }

            @Override // com.android.server.PersistentDataBlockManagerInternal
            public byte[] getTestHarnessModeData() {
                byte[] readInternal = readInternal(PersistentDataBlockService.this.getTestHarnessModeDataOffset(), PersistentDataBlockService.MAX_TEST_MODE_DATA_SIZE);
                return readInternal == null ? new byte[0] : readInternal;
            }

            @Override // com.android.server.PersistentDataBlockManagerInternal
            public void clearTestHarnessModeData() {
                writeDataBuffer(PersistentDataBlockService.this.getTestHarnessModeDataOffset(), ByteBuffer.allocate(Math.min(PersistentDataBlockService.MAX_TEST_MODE_DATA_SIZE, getTestHarnessModeData().length) + 4));
            }

            @Override // com.android.server.PersistentDataBlockManagerInternal
            public int getAllowedUid() {
                return PersistentDataBlockService.this.mAllowedUid;
            }

            private void writeInternal(byte[] bArr, long j, int i) {
                boolean z = true;
                Preconditions.checkArgument(bArr == null || bArr.length > 0, "data must be null or non-empty");
                if (bArr != null && bArr.length > i) {
                    z = false;
                }
                Preconditions.checkArgument(z, "data must not be longer than " + i);
                ByteBuffer allocate = ByteBuffer.allocate(i + 4);
                allocate.putInt(bArr != null ? bArr.length : 0);
                if (bArr != null) {
                    allocate.put(bArr);
                }
                allocate.flip();
                writeDataBuffer(j, allocate);
            }

            private void writeDataBuffer(long j, ByteBuffer byteBuffer) {
                synchronized (PersistentDataBlockService.this.mLock) {
                    if (PersistentDataBlockService.this.mIsWritable) {
                        try {
                            FileChannel blockOutputChannel = PersistentDataBlockService.this.getBlockOutputChannel();
                            blockOutputChannel.position(j);
                            blockOutputChannel.write(byteBuffer);
                            blockOutputChannel.force(true);
                            PersistentDataBlockService.this.computeAndWriteDigestLocked();
                        } catch (IOException e) {
                            Slog.e(PersistentDataBlockService.TAG, "unable to access persistent partition", e);
                        }
                    }
                }
            }

            private byte[] readInternal(long j, int i) {
                if (!PersistentDataBlockService.this.enforceChecksumValidity()) {
                    throw new IllegalStateException("invalid checksum");
                }
                try {
                    DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(PersistentDataBlockService.this.mDataBlockFile)));
                    try {
                        try {
                            synchronized (PersistentDataBlockService.this.mLock) {
                                dataInputStream.skip(j);
                                int readInt = dataInputStream.readInt();
                                if (readInt > 0 && readInt <= i) {
                                    byte[] bArr = new byte[readInt];
                                    dataInputStream.readFully(bArr);
                                    return bArr;
                                }
                                IoUtils.closeQuietly(dataInputStream);
                                return null;
                            }
                        } catch (IOException e) {
                            throw new IllegalStateException("persistent partition not readable", e);
                        }
                    } finally {
                        IoUtils.closeQuietly(dataInputStream);
                    }
                } catch (FileNotFoundException unused) {
                    throw new IllegalStateException("persistent partition not available");
                }
            }

            @Override // com.android.server.PersistentDataBlockManagerInternal
            public void forceOemUnlockEnabled(boolean z) {
                synchronized (PersistentDataBlockService.this.mLock) {
                    PersistentDataBlockService.this.doSetOemUnlockEnabledLocked(z);
                    PersistentDataBlockService.this.computeAndWriteDigestLocked();
                }
            }
        };
        this.mContext = context;
        boolean z = SystemProperties.getBoolean(GSI_RUNNING_PROP, false);
        this.mIsRunningDSU = z;
        if (z) {
            this.mDataBlockFile = GSI_SANDBOX;
        } else {
            this.mDataBlockFile = SystemProperties.get(PERSISTENT_DATA_BLOCK_PROP);
        }
        this.mBlockDeviceSize = -1L;
    }

    private int getAllowedUid() {
        int mainUserId = ((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).getMainUserId();
        if (mainUserId < 0) {
            return -1;
        }
        String string = this.mContext.getResources().getString(R.string.date_picker_mode);
        if (TextUtils.isEmpty(string)) {
            return -1;
        }
        try {
            return this.mContext.getPackageManager().getPackageUidAsUser(string, AudioDevice.OUT_FM, mainUserId);
        } catch (PackageManager.NameNotFoundException e) {
            Slog.e(TAG, "not able to find package " + string, e);
            return -1;
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.PersistentDataBlockService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PersistentDataBlockService.this.lambda$onStart$0();
            }
        }, TAG + ".onStart");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0() {
        enforceChecksumValidity();
        formatIfOemUnlockEnabled();
        publishBinderService("persistent_data_block", this.mService);
        this.mInitDoneSignal.countDown();
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            try {
                if (!this.mInitDoneSignal.await(10L, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Service " + TAG + " init timeout");
                }
                this.mAllowedUid = getAllowedUid();
                LocalServices.addService(PersistentDataBlockManagerInternal.class, this.mInternalService);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Service " + TAG + " init interrupted", e);
            }
        }
        super.onBootPhase(i);
    }

    private void formatIfOemUnlockEnabled() {
        boolean doGetOemUnlockEnabled = doGetOemUnlockEnabled();
        if (doGetOemUnlockEnabled) {
            synchronized (this.mLock) {
                formatPartitionLocked(true);
            }
        }
        SystemProperties.set(OEM_UNLOCK_PROP, doGetOemUnlockEnabled ? FLASH_LOCK_LOCKED : FLASH_LOCK_UNLOCKED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceOemUnlockReadPermission() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_OEM_UNLOCK_STATE") == -1 && this.mContext.checkCallingOrSelfPermission("android.permission.OEM_UNLOCK_STATE") == -1) {
            throw new SecurityException("Can't access OEM unlock state. Requires READ_OEM_UNLOCK_STATE or OEM_UNLOCK_STATE permission.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceOemUnlockWritePermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.OEM_UNLOCK_STATE", "Can't modify OEM unlock state");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceUid(int i) {
        if (i == this.mAllowedUid) {
            return;
        }
        throw new SecurityException("uid " + i + " not allowed to access PST");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceIsAdmin() {
        if (!UserManager.get(this.mContext).isUserAdmin(UserHandle.getCallingUserId())) {
            throw new SecurityException("Only the Admin user is allowed to change OEM unlock state");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceUserRestriction(String str) {
        if (UserManager.get(this.mContext).hasUserRestriction(str)) {
            throw new SecurityException("OEM unlock is disallowed by user restriction: " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTotalDataSizeLocked(DataInputStream dataInputStream) throws IOException {
        dataInputStream.skipBytes(32);
        if (dataInputStream.readInt() == PARTITION_TYPE_MARKER) {
            return dataInputStream.readInt();
        }
        return 0;
    }

    private long getBlockDeviceSize() {
        synchronized (this.mLock) {
            if (this.mBlockDeviceSize == -1) {
                if (this.mIsRunningDSU) {
                    this.mBlockDeviceSize = 102400L;
                } else {
                    this.mBlockDeviceSize = nativeGetBlockDeviceSize(this.mDataBlockFile);
                }
            }
        }
        return this.mBlockDeviceSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getFrpCredentialDataOffset() {
        return (getBlockDeviceSize() - 1) - 1000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getTestHarnessModeDataOffset() {
        return getFrpCredentialDataOffset() - IDeviceIdleControllerExt.ADVANCE_TIME;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enforceChecksumValidity() {
        byte[] bArr = new byte[32];
        synchronized (this.mLock) {
            byte[] computeDigestLocked = computeDigestLocked(bArr);
            if (computeDigestLocked != null && Arrays.equals(bArr, computeDigestLocked)) {
                return true;
            }
            Slog.i(TAG, "Formatting FRP partition...");
            formatPartitionLocked(false);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FileChannel getBlockOutputChannel() throws IOException {
        return new RandomAccessFile(this.mDataBlockFile, "rw").getChannel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean computeAndWriteDigestLocked() {
        byte[] computeDigestLocked = computeDigestLocked(null);
        if (computeDigestLocked != null) {
            try {
                FileChannel blockOutputChannel = getBlockOutputChannel();
                try {
                    ByteBuffer allocate = ByteBuffer.allocate(32);
                    allocate.put(computeDigestLocked);
                    allocate.flip();
                    blockOutputChannel.write(allocate);
                    blockOutputChannel.force(true);
                    return true;
                } catch (IOException e) {
                    Slog.e(TAG, "failed to write block checksum", e);
                    return false;
                }
            } catch (IOException e2) {
                Slog.e(TAG, "partition not available?", e2);
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0035 A[Catch: all -> 0x0041, IOException -> 0x0043, LOOP:0: B:11:0x002e->B:13:0x0035, LOOP_END, Merged into TryCatch #0 {all -> 0x0041, IOException -> 0x0043, blocks: (B:24:0x001c, B:26:0x001f, B:10:0x0026, B:11:0x002e, B:13:0x0035, B:9:0x0023, B:20:0x0044), top: B:7:0x001a }, TRY_LEAVE] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0039 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private byte[] computeDigestLocked(byte[] bArr) {
        byte[] bArr2;
        int read;
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(this.mDataBlockFile)));
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                try {
                    if (bArr != null) {
                        if (bArr.length == 32) {
                            dataInputStream.read(bArr);
                            bArr2 = new byte[1024];
                            messageDigest.update(bArr2, 0, 32);
                            while (true) {
                                read = dataInputStream.read(bArr2);
                                if (read == -1) {
                                    messageDigest.update(bArr2, 0, read);
                                } else {
                                    IoUtils.closeQuietly(dataInputStream);
                                    return messageDigest.digest();
                                }
                            }
                        }
                    }
                    dataInputStream.skipBytes(32);
                    bArr2 = new byte[1024];
                    messageDigest.update(bArr2, 0, 32);
                    while (true) {
                        read = dataInputStream.read(bArr2);
                        if (read == -1) {
                        }
                        messageDigest.update(bArr2, 0, read);
                    }
                } catch (IOException e) {
                    Slog.e(TAG, "failed to read partition", e);
                    return null;
                } finally {
                    IoUtils.closeQuietly(dataInputStream);
                }
            } catch (NoSuchAlgorithmException e2) {
                Slog.e(TAG, "SHA-256 not supported?", e2);
                return null;
            }
        } catch (FileNotFoundException e3) {
            Slog.e(TAG, "partition not available?", e3);
            return null;
        }
    }

    private void formatPartitionLocked(boolean z) {
        try {
            FileChannel blockOutputChannel = getBlockOutputChannel();
            ByteBuffer allocate = ByteBuffer.allocate(40);
            allocate.put(new byte[32]);
            allocate.putInt(PARTITION_TYPE_MARKER);
            allocate.putInt(0);
            allocate.flip();
            blockOutputChannel.write(allocate);
            blockOutputChannel.force(true);
            blockOutputChannel.write(ByteBuffer.allocate((((((int) getBlockDeviceSize()) - 40) - 10000) - 1000) - 1));
            blockOutputChannel.force(true);
            blockOutputChannel.position(blockOutputChannel.position() + IDeviceIdleControllerExt.ADVANCE_TIME);
            blockOutputChannel.write(ByteBuffer.allocate(1000));
            blockOutputChannel.force(true);
            ByteBuffer allocate2 = ByteBuffer.allocate(1000);
            allocate2.put((byte) 0);
            allocate2.flip();
            blockOutputChannel.write(allocate2);
            blockOutputChannel.force(true);
            doSetOemUnlockEnabledLocked(z);
            computeAndWriteDigestLocked();
        } catch (IOException e) {
            Slog.e(TAG, "failed to format block", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSetOemUnlockEnabledLocked(boolean z) {
        String str = FLASH_LOCK_LOCKED;
        try {
            try {
                FileChannel blockOutputChannel = getBlockOutputChannel();
                blockOutputChannel.position(getBlockDeviceSize() - 1);
                ByteBuffer allocate = ByteBuffer.allocate(1);
                allocate.put(z ? (byte) 1 : (byte) 0);
                allocate.flip();
                blockOutputChannel.write(allocate);
                blockOutputChannel.force(true);
                if (!z) {
                    str = FLASH_LOCK_UNLOCKED;
                }
                SystemProperties.set(OEM_UNLOCK_PROP, str);
            } catch (IOException e) {
                Slog.e(TAG, "unable to access persistent partition", e);
                if (!z) {
                    str = FLASH_LOCK_UNLOCKED;
                }
                SystemProperties.set(OEM_UNLOCK_PROP, str);
            }
        } catch (Throwable th) {
            if (!z) {
                str = FLASH_LOCK_UNLOCKED;
            }
            SystemProperties.set(OEM_UNLOCK_PROP, str);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean doGetOemUnlockEnabled() {
        boolean z;
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(this.mDataBlockFile)));
            try {
                synchronized (this.mLock) {
                    dataInputStream.skip(getBlockDeviceSize() - 1);
                    z = dataInputStream.readByte() != 0;
                }
                return z;
            } catch (IOException e) {
                Slog.e(TAG, "unable to access persistent partition", e);
                return false;
            } finally {
                IoUtils.closeQuietly(dataInputStream);
            }
        } catch (FileNotFoundException unused) {
            Slog.e(TAG, "partition not available");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long doGetMaximumDataBlockSize() {
        long blockDeviceSize = ((((getBlockDeviceSize() - 8) - 32) - IDeviceIdleControllerExt.ADVANCE_TIME) - 1000) - 1;
        if (blockDeviceSize <= 102400) {
            return blockDeviceSize;
        }
        return 102400L;
    }
}
