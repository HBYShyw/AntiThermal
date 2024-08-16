package com.android.server.locksettings;

import android.app.admin.PasswordMetrics;
import android.content.Context;
import android.content.pm.UserInfo;
import android.hardware.weaver.IWeaver;
import android.hardware.weaver.WeaverConfig;
import android.hardware.weaver.WeaverReadResponse;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.UserManager;
import android.provider.Settings;
import android.security.Scrypt;
import android.service.gatekeeper.GateKeeperResponse;
import android.service.gatekeeper.IGateKeeperService;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.widget.ICheckCredentialProgressCallback;
import com.android.internal.widget.IWeakEscrowTokenRemovedListener;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.server.locksettings.LockSettingsStorage;
import com.android.server.usage.IntervalStats;
import com.android.server.utils.Slogf;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import libcore.util.HexEncoding;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SyntheticPasswordManager {
    private static final int INVALID_WEAVER_SLOT = -1;
    public static final long NULL_PROTECTOR_ID = 0;
    private static final String PASSWORD_DATA_NAME = "pwd";
    private static final String PASSWORD_METRICS_NAME = "metrics";
    private static final int PASSWORD_SALT_LENGTH = 16;
    private static final int PASSWORD_SCRYPT_LOG_N = 11;
    private static final int PASSWORD_SCRYPT_LOG_P = 1;
    private static final int PASSWORD_SCRYPT_LOG_R = 3;
    private static final String PROTECTOR_KEY_ALIAS_PREFIX = "synthetic_password_";
    private static final byte PROTECTOR_TYPE_LSKF_BASED = 0;
    private static final byte PROTECTOR_TYPE_STRONG_TOKEN_BASED = 1;
    private static final byte PROTECTOR_TYPE_WEAK_TOKEN_BASED = 2;
    private static final int SECDISCARDABLE_LENGTH = 16384;
    private static final String SECDISCARDABLE_NAME = "secdis";
    private static final String SP_BLOB_NAME = "spblob";
    private static final String SP_E0_NAME = "e0";
    private static final String SP_HANDLE_NAME = "handle";
    private static final String SP_P1_NAME = "p1";
    private static final int STRETCHED_LSKF_LENGTH = 32;
    private static final int SYNTHETIC_PASSWORD_SECURITY_STRENGTH = 32;
    private static final byte SYNTHETIC_PASSWORD_VERSION_V1 = 1;
    private static final byte SYNTHETIC_PASSWORD_VERSION_V2 = 2;
    private static final byte SYNTHETIC_PASSWORD_VERSION_V3 = 3;
    private static final String TAG = "SyntheticPasswordManager";
    static final int TOKEN_TYPE_STRONG = 0;
    static final int TOKEN_TYPE_WEAK = 1;
    private static final String VENDOR_AUTH_SECRET_NAME = "vendor_auth_secret";
    private static final String WEAVER_SLOT_NAME = "weaver";
    private static final byte WEAVER_VERSION = 1;
    private final Context mContext;
    private PasswordSlotManager mPasswordSlotManager;
    private LockSettingsStorage mStorage;
    private final UserManager mUserManager;
    private IWeaver mWeaver;
    private WeaverConfig mWeaverConfig;
    private static final byte[] DEFAULT_PASSWORD = "default-password".getBytes();
    private static final byte[] PERSONALIZATION_SECDISCARDABLE = "secdiscardable-transform".getBytes();
    private static final byte[] PERSONALIZATION_KEY_STORE_PASSWORD = "keystore-password".getBytes();
    private static final byte[] PERSONALIZATION_USER_GK_AUTH = "user-gk-authentication".getBytes();
    private static final byte[] PERSONALIZATION_SP_GK_AUTH = "sp-gk-authentication".getBytes();
    private static final byte[] PERSONALIZATION_FBE_KEY = "fbe-key".getBytes();
    private static final byte[] PERSONALIZATION_AUTHSECRET_KEY = "authsecret-hal".getBytes();
    private static final byte[] PERSONALIZATION_AUTHSECRET_ENCRYPTION_KEY = "vendor-authsecret-encryption-key".getBytes();
    private static final byte[] PERSONALIZATION_SP_SPLIT = "sp-split".getBytes();
    private static final byte[] PERSONALIZATION_PASSWORD_HASH = "pw-hash".getBytes();
    private static final byte[] PERSONALIZATION_E0 = "e0-encryption".getBytes();
    private static final byte[] PERSONALIZATION_WEAVER_PASSWORD = "weaver-pwd".getBytes();
    private static final byte[] PERSONALIZATION_WEAVER_KEY = "weaver-key".getBytes();
    private static final byte[] PERSONALIZATION_WEAVER_TOKEN = "weaver-token".getBytes();
    private static final byte[] PERSONALIZATION_PASSWORD_METRICS = "password-metrics".getBytes();
    private static final byte[] PERSONALIZATION_CONTEXT = "android-synthetic-password-personalization-context".getBytes();
    private ISyntheticPasswordManagerExt mSyntheticPasswordManagerExt = (ISyntheticPasswordManagerExt) ExtLoader.type(ISyntheticPasswordManagerExt.class).base(this).create();
    private final RemoteCallbackList<IWeakEscrowTokenRemovedListener> mListeners = new RemoteCallbackList<>();
    private ArrayMap<Integer, ArrayMap<Long, TokenData>> tokenMap = new ArrayMap<>();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface TokenType {
    }

    @VisibleForTesting
    static int fakeUserId(int i) {
        return i + IntervalStats.MAX_EVENTS;
    }

    private byte getTokenBasedProtectorType(int i) {
        return i != 1 ? (byte) 1 : (byte) 2;
    }

    private native long nativeSidFromPasswordHandle(byte[] bArr);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class AuthenticationResult {
        public VerifyCredentialResponse gkResponse;
        public SyntheticPassword syntheticPassword;

        AuthenticationResult() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SyntheticPassword {
        private byte[] mEncryptedEscrowSplit0;
        private byte[] mEscrowSplit1;
        private byte[] mSyntheticPassword;
        private final byte mVersion;

        /* JADX INFO: Access modifiers changed from: package-private */
        public SyntheticPassword(byte b) {
            this.mVersion = b;
        }

        private byte[] deriveSubkey(byte[] bArr) {
            if (this.mVersion == 3) {
                return new SP800Derive(this.mSyntheticPassword).withContext(bArr, SyntheticPasswordManager.PERSONALIZATION_CONTEXT);
            }
            return SyntheticPasswordCrypto.personalizedHash(bArr, this.mSyntheticPassword);
        }

        public byte[] deriveKeyStorePassword() {
            return SyntheticPasswordManager.bytesToHex(deriveSubkey(SyntheticPasswordManager.PERSONALIZATION_KEY_STORE_PASSWORD));
        }

        public byte[] deriveGkPassword() {
            return deriveSubkey(SyntheticPasswordManager.PERSONALIZATION_SP_GK_AUTH);
        }

        public byte[] deriveFileBasedEncryptionKey() {
            return deriveSubkey(SyntheticPasswordManager.PERSONALIZATION_FBE_KEY);
        }

        public byte[] deriveVendorAuthSecret() {
            return deriveSubkey(SyntheticPasswordManager.PERSONALIZATION_AUTHSECRET_KEY);
        }

        public byte[] derivePasswordHashFactor() {
            return deriveSubkey(SyntheticPasswordManager.PERSONALIZATION_PASSWORD_HASH);
        }

        public byte[] deriveMetricsKey() {
            return deriveSubkey(SyntheticPasswordManager.PERSONALIZATION_PASSWORD_METRICS);
        }

        public byte[] deriveVendorAuthSecretEncryptionKey() {
            return deriveSubkey(SyntheticPasswordManager.PERSONALIZATION_AUTHSECRET_ENCRYPTION_KEY);
        }

        public void setEscrowData(byte[] bArr, byte[] bArr2) {
            this.mEncryptedEscrowSplit0 = bArr;
            this.mEscrowSplit1 = bArr2;
        }

        public void recreateFromEscrow(byte[] bArr) {
            Objects.requireNonNull(this.mEscrowSplit1);
            Objects.requireNonNull(this.mEncryptedEscrowSplit0);
            recreate(bArr, this.mEscrowSplit1);
        }

        public void recreateDirectly(byte[] bArr) {
            this.mSyntheticPassword = Arrays.copyOf(bArr, bArr.length);
        }

        static SyntheticPassword create() {
            SyntheticPassword syntheticPassword = new SyntheticPassword((byte) 3);
            byte[] randomBytes = SecureRandomUtils.randomBytes(32);
            byte[] randomBytes2 = SecureRandomUtils.randomBytes(32);
            syntheticPassword.recreate(randomBytes, randomBytes2);
            syntheticPassword.setEscrowData(SyntheticPasswordCrypto.encrypt(syntheticPassword.mSyntheticPassword, SyntheticPasswordManager.PERSONALIZATION_E0, randomBytes), randomBytes2);
            return syntheticPassword;
        }

        private void recreate(byte[] bArr, byte[] bArr2) {
            this.mSyntheticPassword = SyntheticPasswordManager.bytesToHex(SyntheticPasswordCrypto.personalizedHash(SyntheticPasswordManager.PERSONALIZATION_SP_SPLIT, bArr, bArr2));
        }

        public byte[] getEscrowSecret() {
            if (this.mEncryptedEscrowSplit0 == null) {
                return null;
            }
            return SyntheticPasswordCrypto.decrypt(this.mSyntheticPassword, SyntheticPasswordManager.PERSONALIZATION_E0, this.mEncryptedEscrowSplit0);
        }

        public byte[] getSyntheticPassword() {
            return this.mSyntheticPassword;
        }

        public byte getVersion() {
            return this.mVersion;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PasswordData {
        public int credentialType;
        public byte[] passwordHandle;
        public int pinLength;
        byte[] salt;
        byte scryptLogN;
        byte scryptLogP;
        byte scryptLogR;

        PasswordData() {
        }

        public static PasswordData create(int i, int i2) {
            PasswordData passwordData = new PasswordData();
            passwordData.scryptLogN = (byte) 11;
            passwordData.scryptLogR = (byte) 3;
            passwordData.scryptLogP = (byte) 1;
            passwordData.credentialType = i;
            passwordData.pinLength = i2;
            passwordData.salt = SecureRandomUtils.randomBytes(16);
            return passwordData;
        }

        public static boolean isBadFormatFromAndroid14Beta(byte[] bArr) {
            return bArr != null && bArr.length >= 2 && bArr[0] == 0 && bArr[1] == 2;
        }

        public static PasswordData fromBytes(byte[] bArr) {
            PasswordData passwordData = new PasswordData();
            ByteBuffer allocate = ByteBuffer.allocate(bArr.length);
            allocate.put(bArr, 0, bArr.length);
            allocate.flip();
            passwordData.credentialType = (short) allocate.getInt();
            passwordData.scryptLogN = allocate.get();
            passwordData.scryptLogR = allocate.get();
            passwordData.scryptLogP = allocate.get();
            byte[] bArr2 = new byte[allocate.getInt()];
            passwordData.salt = bArr2;
            allocate.get(bArr2);
            int i = allocate.getInt();
            if (i > 0) {
                byte[] bArr3 = new byte[i];
                passwordData.passwordHandle = bArr3;
                allocate.get(bArr3);
            } else {
                passwordData.passwordHandle = null;
            }
            if (allocate.remaining() >= 4) {
                passwordData.pinLength = allocate.getInt();
            } else {
                passwordData.pinLength = -1;
            }
            return passwordData;
        }

        public byte[] toBytes() {
            int length = this.salt.length + 11 + 4;
            byte[] bArr = this.passwordHandle;
            ByteBuffer allocate = ByteBuffer.allocate(length + (bArr != null ? bArr.length : 0) + 4);
            int i = this.credentialType;
            if (i < -32768 || i > 32767) {
                throw new IllegalArgumentException("Unknown credential type: " + this.credentialType);
            }
            allocate.putInt(i);
            allocate.put(this.scryptLogN);
            allocate.put(this.scryptLogR);
            allocate.put(this.scryptLogP);
            allocate.putInt(this.salt.length);
            allocate.put(this.salt);
            byte[] bArr2 = this.passwordHandle;
            if (bArr2 != null && bArr2.length > 0) {
                allocate.putInt(bArr2.length);
                allocate.put(this.passwordHandle);
            } else {
                allocate.putInt(0);
            }
            allocate.putInt(this.pinLength);
            return allocate.array();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SyntheticPasswordBlob {
        byte[] mContent;
        byte mProtectorType;
        byte mVersion;

        private SyntheticPasswordBlob() {
        }

        public static SyntheticPasswordBlob create(byte b, byte b2, byte[] bArr) {
            SyntheticPasswordBlob syntheticPasswordBlob = new SyntheticPasswordBlob();
            syntheticPasswordBlob.mVersion = b;
            syntheticPasswordBlob.mProtectorType = b2;
            syntheticPasswordBlob.mContent = bArr;
            return syntheticPasswordBlob;
        }

        public static SyntheticPasswordBlob fromBytes(byte[] bArr) {
            SyntheticPasswordBlob syntheticPasswordBlob = new SyntheticPasswordBlob();
            syntheticPasswordBlob.mVersion = bArr[0];
            syntheticPasswordBlob.mProtectorType = bArr[1];
            syntheticPasswordBlob.mContent = Arrays.copyOfRange(bArr, 2, bArr.length);
            return syntheticPasswordBlob;
        }

        public byte[] toByte() {
            byte[] bArr = this.mContent;
            byte[] bArr2 = new byte[bArr.length + 1 + 1];
            bArr2[0] = this.mVersion;
            bArr2[1] = this.mProtectorType;
            System.arraycopy(bArr, 0, bArr2, 2, bArr.length);
            return bArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TokenData {
        byte[] aggregatedSecret;
        LockPatternUtils.EscrowTokenStateChangeCallback mCallback;
        int mType;
        byte[] secdiscardableOnDisk;
        byte[] weaverSecret;

        private TokenData() {
        }
    }

    public SyntheticPasswordManager(Context context, LockSettingsStorage lockSettingsStorage, UserManager userManager, PasswordSlotManager passwordSlotManager) {
        this.mContext = context;
        this.mStorage = lockSettingsStorage;
        this.mUserManager = userManager;
        this.mPasswordSlotManager = passwordSlotManager;
    }

    private boolean isDeviceProvisioned() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
    }

    @VisibleForTesting
    protected android.hardware.weaver.V1_0.IWeaver getWeaverHidlService() throws RemoteException {
        try {
            return android.hardware.weaver.V1_0.IWeaver.getService(true);
        } catch (NoSuchElementException unused) {
            return null;
        }
    }

    private IWeaver getWeaverService() {
        try {
            IWeaver asInterface = IWeaver.Stub.asInterface(ServiceManager.waitForDeclaredService(IWeaver.DESCRIPTOR + "/default"));
            if (asInterface != null) {
                Slog.i(TAG, "Using AIDL weaver service");
                return asInterface;
            }
        } catch (SecurityException unused) {
            Slog.w(TAG, "Does not have permissions to get AIDL weaver service");
        }
        try {
            android.hardware.weaver.V1_0.IWeaver weaverHidlService = getWeaverHidlService();
            if (weaverHidlService != null) {
                Slog.i(TAG, "Using HIDL weaver service");
                return new WeaverHidlAdapter(weaverHidlService);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to get HIDL weaver service.", e);
        }
        Slog.w(TAG, "Device does not support weaver");
        return null;
    }

    @VisibleForTesting
    public boolean isAutoPinConfirmationFeatureAvailable() {
        return LockPatternUtils.isAutoPinConfirmFeatureAvailable();
    }

    private synchronized boolean isWeaverAvailable() {
        if (this.mWeaver != null) {
            return true;
        }
        IWeaver weaverService = getWeaverService();
        if (weaverService == null) {
            return false;
        }
        try {
            WeaverConfig config = weaverService.getConfig();
            if (config != null && config.slots > 0) {
                this.mWeaver = weaverService;
                this.mWeaverConfig = config;
                this.mPasswordSlotManager.refreshActiveSlots(getUsedWeaverSlots());
                Slog.i(TAG, "Weaver service initialized");
                return true;
            }
            Slog.e(TAG, "Invalid weaver config");
            return false;
        } catch (RemoteException | ServiceSpecificException e) {
            Slog.e(TAG, "Failed to get weaver config", e);
            return false;
        }
    }

    private byte[] weaverEnroll(int i, byte[] bArr, byte[] bArr2) {
        if (i != -1) {
            WeaverConfig weaverConfig = this.mWeaverConfig;
            if (i < weaverConfig.slots) {
                if (bArr == null) {
                    bArr = new byte[weaverConfig.keySize];
                } else if (bArr.length != weaverConfig.keySize) {
                    throw new IllegalArgumentException("Invalid key size for weaver");
                }
                if (bArr2 == null) {
                    bArr2 = SecureRandomUtils.randomBytes(weaverConfig.valueSize);
                }
                try {
                    this.mWeaver.write(i, bArr, bArr2);
                    return bArr2;
                } catch (ServiceSpecificException e) {
                    Slog.e(TAG, "weaver write failed, slot: " + i, e);
                    return null;
                } catch (RemoteException e2) {
                    Slog.e(TAG, "weaver write binder call failed, slot: " + i, e2);
                    return null;
                }
            }
        }
        throw new IllegalArgumentException("Invalid slot for weaver");
    }

    private static VerifyCredentialResponse responseFromTimeout(WeaverReadResponse weaverReadResponse) {
        long j = weaverReadResponse.timeout;
        return VerifyCredentialResponse.fromTimeout((j > 2147483647L || j < 0) ? Integer.MAX_VALUE : (int) j);
    }

    private VerifyCredentialResponse weaverVerify(int i, byte[] bArr) {
        if (i != -1) {
            WeaverConfig weaverConfig = this.mWeaverConfig;
            if (i < weaverConfig.slots) {
                if (bArr == null) {
                    bArr = new byte[weaverConfig.keySize];
                } else if (bArr.length != weaverConfig.keySize) {
                    throw new IllegalArgumentException("Invalid key size for weaver");
                }
                try {
                    WeaverReadResponse read = this.mWeaver.read(i, bArr);
                    int i2 = read.status;
                    if (i2 == 0) {
                        return new VerifyCredentialResponse.Builder().setGatekeeperHAT(read.value).build();
                    }
                    if (i2 == 1) {
                        Slog.e(TAG, "weaver read failed (FAILED), slot: " + i);
                        return VerifyCredentialResponse.ERROR;
                    }
                    if (i2 == 2) {
                        if (read.timeout == 0) {
                            Slog.e(TAG, "weaver read failed (INCORRECT_KEY), slot: " + i);
                            return VerifyCredentialResponse.ERROR;
                        }
                        Slog.e(TAG, "weaver read failed (INCORRECT_KEY/THROTTLE), slot: " + i);
                        return responseFromTimeout(read);
                    }
                    if (i2 == 3) {
                        Slog.e(TAG, "weaver read failed (THROTTLE), slot: " + i);
                        return responseFromTimeout(read);
                    }
                    Slog.e(TAG, "weaver read unknown status " + read.status + ", slot: " + i);
                    return VerifyCredentialResponse.ERROR;
                } catch (RemoteException e) {
                    Slog.e(TAG, "weaver read failed, slot: " + i, e);
                    return VerifyCredentialResponse.ERROR;
                }
            }
        }
        throw new IllegalArgumentException("Invalid slot for weaver");
    }

    public void removeUser(IGateKeeperService iGateKeeperService, int i) {
        Iterator<Long> it = this.mStorage.listSyntheticPasswordProtectorsForUser(SP_BLOB_NAME, i).iterator();
        while (it.hasNext()) {
            long longValue = it.next().longValue();
            destroyWeaverSlot(longValue, i);
            destroyProtectorKey(getProtectorKeyAlias(longValue));
        }
        try {
            iGateKeeperService.clearSecureUserId(fakeUserId(i));
        } catch (RemoteException unused) {
            Slog.w(TAG, "Failed to clear SID from gatekeeper");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPinLength(long j, int i) {
        byte[] loadState = loadState(PASSWORD_DATA_NAME, j, i);
        if (loadState == null) {
            return -1;
        }
        return PasswordData.fromBytes(loadState).pinLength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCredentialType(long j, int i) {
        byte[] loadState = loadState(PASSWORD_DATA_NAME, j, i);
        if (loadState == null) {
            return -1;
        }
        return PasswordData.fromBytes(loadState).credentialType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getFrpCredentialType(byte[] bArr) {
        if (bArr == null) {
            return -1;
        }
        return PasswordData.fromBytes(bArr).credentialType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SyntheticPassword newSyntheticPassword(int i) {
        clearSidForUser(i);
        SyntheticPassword create = SyntheticPassword.create();
        saveEscrowData(create, i);
        return create;
    }

    public void newSidForUser(IGateKeeperService iGateKeeperService, SyntheticPassword syntheticPassword, int i) {
        try {
            GateKeeperResponse enroll = iGateKeeperService.enroll(i, (byte[]) null, (byte[]) null, syntheticPassword.deriveGkPassword());
            Slog.d(TAG, "[newSidForUser]  userId = " + i);
            if (enroll.getResponseCode() != 0) {
                throw new IllegalStateException("Fail to create new SID for user " + i + " response: " + enroll.getResponseCode());
            }
            saveSyntheticPasswordHandle(enroll.getPayload(), i);
        } catch (RemoteException e) {
            throw new IllegalStateException("Failed to create new SID for user", e);
        }
    }

    public void clearSidForUser(int i) {
        destroyState(SP_HANDLE_NAME, 0L, i);
    }

    public boolean hasSidForUser(int i) {
        return hasState(SP_HANDLE_NAME, 0L, i);
    }

    private byte[] loadSyntheticPasswordHandle(int i) {
        return loadState(SP_HANDLE_NAME, 0L, i);
    }

    private void saveSyntheticPasswordHandle(byte[] bArr, int i) {
        saveState(SP_HANDLE_NAME, bArr, 0L, i);
        syncState(i);
    }

    private boolean loadEscrowData(SyntheticPassword syntheticPassword, int i) {
        byte[] loadState = loadState(SP_E0_NAME, 0L, i);
        byte[] loadState2 = loadState(SP_P1_NAME, 0L, i);
        syntheticPassword.setEscrowData(loadState, loadState2);
        return (loadState == null || loadState2 == null) ? false : true;
    }

    private void saveEscrowData(SyntheticPassword syntheticPassword, int i) {
        saveState(SP_E0_NAME, syntheticPassword.mEncryptedEscrowSplit0, 0L, i);
        saveState(SP_P1_NAME, syntheticPassword.mEscrowSplit1, 0L, i);
    }

    public boolean hasEscrowData(int i) {
        return hasState(SP_E0_NAME, 0L, i) && hasState(SP_P1_NAME, 0L, i);
    }

    public boolean hasAnyEscrowData(int i) {
        return hasState(SP_E0_NAME, 0L, i) || hasState(SP_P1_NAME, 0L, i);
    }

    public void destroyEscrowData(int i) {
        destroyState(SP_E0_NAME, 0L, i);
        destroyState(SP_P1_NAME, 0L, i);
    }

    private int loadWeaverSlot(long j, int i) {
        byte[] loadState = loadState(WEAVER_SLOT_NAME, j, i);
        if (loadState == null || loadState.length != 5) {
            return -1;
        }
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put(loadState, 0, loadState.length);
        allocate.flip();
        if (allocate.get() != 1) {
            Slog.e(TAG, "Invalid weaver slot version for protector " + j);
            return -1;
        }
        return allocate.getInt();
    }

    private void saveWeaverSlot(int i, long j, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate(5);
        allocate.put((byte) 1);
        allocate.putInt(i);
        saveState(WEAVER_SLOT_NAME, allocate.array(), j, i2);
    }

    private void destroyWeaverSlot(long j, int i) {
        int loadWeaverSlot = loadWeaverSlot(j, i);
        destroyState(WEAVER_SLOT_NAME, j, i);
        if (loadWeaverSlot != -1) {
            if (!isWeaverAvailable()) {
                Slog.e(TAG, "Cannot erase Weaver slot because Weaver is unavailable");
            } else {
                if (!getUsedWeaverSlots().contains(Integer.valueOf(loadWeaverSlot))) {
                    Slogf.i(TAG, "Erasing Weaver slot %d", Integer.valueOf(loadWeaverSlot));
                    weaverEnroll(loadWeaverSlot, null, null);
                    this.mPasswordSlotManager.markSlotDeleted(loadWeaverSlot);
                    return;
                }
                Slogf.i(TAG, "Weaver slot %d was already reused; not erasing it", Integer.valueOf(loadWeaverSlot));
            }
        }
    }

    private Set<Integer> getUsedWeaverSlots() {
        Map<Integer, List<Long>> listSyntheticPasswordProtectorsForAllUsers = this.mStorage.listSyntheticPasswordProtectorsForAllUsers(WEAVER_SLOT_NAME);
        HashSet hashSet = new HashSet();
        for (Map.Entry<Integer, List<Long>> entry : listSyntheticPasswordProtectorsForAllUsers.entrySet()) {
            Iterator<Long> it = entry.getValue().iterator();
            while (it.hasNext()) {
                hashSet.add(Integer.valueOf(loadWeaverSlot(it.next().longValue(), entry.getKey().intValue())));
            }
        }
        return hashSet;
    }

    private int getNextAvailableWeaverSlot() {
        LockSettingsStorage.PersistentData readPersistentDataBlock;
        Set<Integer> usedWeaverSlots = getUsedWeaverSlots();
        usedWeaverSlots.addAll(this.mPasswordSlotManager.getUsedSlots());
        if (!isDeviceProvisioned() && (readPersistentDataBlock = this.mStorage.readPersistentDataBlock()) != null && readPersistentDataBlock.type == 2) {
            usedWeaverSlots.add(Integer.valueOf(readPersistentDataBlock.userId));
        }
        for (int i = 0; i < this.mWeaverConfig.slots; i++) {
            if (!usedWeaverSlots.contains(Integer.valueOf(i))) {
                return i;
            }
        }
        throw new IllegalStateException("Run out of weaver slots.");
    }

    public long createLskfBasedProtector(IGateKeeperService iGateKeeperService, LockscreenCredential lockscreenCredential, SyntheticPassword syntheticPassword, int i) {
        LockscreenCredential lockscreenCredential2;
        PasswordData create;
        byte[] transformUnderSecdiscardable;
        long generateProtectorId = generateProtectorId();
        int derivePinLength = isAutoPinConfirmationFeatureAvailable() ? derivePinLength(lockscreenCredential.size(), lockscreenCredential.isPin(), i) : -1;
        if (lockscreenCredential.isNone()) {
            lockscreenCredential2 = lockscreenCredential;
            create = null;
        } else {
            lockscreenCredential2 = lockscreenCredential;
            create = PasswordData.create(lockscreenCredential.getType(), derivePinLength);
        }
        byte[] stretchLskf = stretchLskf(lockscreenCredential2, create);
        Slogf.i(TAG, "Creating LSKF-based protector %016x for user %d", Long.valueOf(generateProtectorId), Integer.valueOf(i));
        long j = 0;
        if (isWeaverAvailable()) {
            int nextAvailableWeaverSlot = getNextAvailableWeaverSlot();
            Slogf.i(TAG, "Enrolling LSKF for user %d into Weaver slot %d", Integer.valueOf(i), Integer.valueOf(nextAvailableWeaverSlot));
            byte[] weaverEnroll = weaverEnroll(nextAvailableWeaverSlot, stretchedLskfToWeaverKey(stretchLskf), null);
            if (weaverEnroll == null) {
                throw new IllegalStateException("Fail to enroll user password under weaver " + i);
            }
            saveWeaverSlot(nextAvailableWeaverSlot, generateProtectorId, i);
            this.mPasswordSlotManager.markSlotInUse(nextAvailableWeaverSlot);
            synchronizeWeaverFrpPassword(create, 0, i, nextAvailableWeaverSlot);
            transformUnderSecdiscardable = transformUnderWeaverSecret(stretchLskf, weaverEnroll);
        } else {
            if (!lockscreenCredential.isNone()) {
                try {
                    iGateKeeperService.clearSecureUserId(fakeUserId(i));
                } catch (RemoteException unused) {
                    Slog.w(TAG, "Failed to clear SID from gatekeeper");
                }
                Slogf.i(TAG, "Enrolling LSKF for user %d into Gatekeeper", Integer.valueOf(i));
                try {
                    GateKeeperResponse enroll = iGateKeeperService.enroll(fakeUserId(i), (byte[]) null, (byte[]) null, stretchedLskfToGkPassword(stretchLskf));
                    if (enroll.getResponseCode() != 0) {
                        throw new IllegalStateException("Failed to enroll LSKF for new SP protector for user " + i);
                    }
                    byte[] payload = enroll.getPayload();
                    create.passwordHandle = payload;
                    j = sidFromPasswordHandle(payload);
                } catch (RemoteException e) {
                    throw new IllegalStateException("Failed to enroll LSKF for new SP protector for user " + i, e);
                }
            }
            transformUnderSecdiscardable = transformUnderSecdiscardable(stretchLskf, createSecdiscardable(generateProtectorId, i));
            synchronizeGatekeeperFrpPassword(create, 0, i);
        }
        byte[] bArr = transformUnderSecdiscardable;
        if (!lockscreenCredential.isNone()) {
            saveState(PASSWORD_DATA_NAME, create.toBytes(), generateProtectorId, i);
            savePasswordMetrics(lockscreenCredential, syntheticPassword, generateProtectorId, i);
        }
        createSyntheticPasswordBlob(generateProtectorId, (byte) 0, syntheticPassword, bArr, j, i);
        this.mSyntheticPasswordManagerExt.updateCreateParam(this.mContext, lockscreenCredential.getCredential(), create, i, generateProtectorId, lockscreenCredential.size());
        syncState(i);
        return generateProtectorId;
    }

    private int derivePinLength(int i, boolean z, int i2) {
        if (z && this.mStorage.isAutoPinConfirmSettingEnabled(i2) && i >= 6) {
            return i;
        }
        return -1;
    }

    public VerifyCredentialResponse verifyFrpCredential(IGateKeeperService iGateKeeperService, LockscreenCredential lockscreenCredential, ICheckCredentialProgressCallback iCheckCredentialProgressCallback) {
        LockSettingsStorage.PersistentData readPersistentDataBlock = this.mStorage.readPersistentDataBlock();
        int i = readPersistentDataBlock.type;
        if (i == 1) {
            PasswordData fromBytes = PasswordData.fromBytes(readPersistentDataBlock.payload);
            try {
                return VerifyCredentialResponse.fromGateKeeperResponse(iGateKeeperService.verifyChallenge(fakeUserId(readPersistentDataBlock.userId), 0L, fromBytes.passwordHandle, stretchedLskfToGkPassword(stretchLskf(lockscreenCredential, fromBytes))));
            } catch (RemoteException e) {
                Slog.e(TAG, "FRP verifyChallenge failed", e);
                return VerifyCredentialResponse.ERROR;
            }
        }
        if (i == 2) {
            if (!isWeaverAvailable()) {
                Slog.e(TAG, "No weaver service to verify SP-based FRP credential");
                return VerifyCredentialResponse.ERROR;
            }
            return weaverVerify(readPersistentDataBlock.userId, stretchedLskfToWeaverKey(stretchLskf(lockscreenCredential, PasswordData.fromBytes(readPersistentDataBlock.payload)))).stripPayload();
        }
        Slog.e(TAG, "persistentData.type must be TYPE_SP_GATEKEEPER or TYPE_SP_WEAVER, but is " + readPersistentDataBlock.type);
        return VerifyCredentialResponse.ERROR;
    }

    public void migrateFrpPasswordLocked(long j, UserInfo userInfo, int i) {
        if (this.mStorage.getPersistentDataBlockManager() == null || !LockPatternUtils.userOwnsFrpCredential(this.mContext, userInfo) || getCredentialType(j, userInfo.id) == -1) {
            return;
        }
        Slog.i(TAG, "Migrating FRP credential to persistent data block");
        PasswordData fromBytes = PasswordData.fromBytes(loadState(PASSWORD_DATA_NAME, j, userInfo.id));
        int loadWeaverSlot = loadWeaverSlot(j, userInfo.id);
        if (loadWeaverSlot != -1) {
            synchronizeWeaverFrpPassword(fromBytes, i, userInfo.id, loadWeaverSlot);
        } else {
            synchronizeGatekeeperFrpPassword(fromBytes, i, userInfo.id);
        }
    }

    private static boolean isNoneCredential(PasswordData passwordData) {
        return passwordData == null || passwordData.credentialType == -1;
    }

    private boolean shouldSynchronizeFrpCredential(PasswordData passwordData, int i) {
        if (this.mStorage.getPersistentDataBlockManager() == null) {
            return false;
        }
        if (!LockPatternUtils.userOwnsFrpCredential(this.mContext, this.mUserManager.getUserInfo(i))) {
            return false;
        }
        if (!isNoneCredential(passwordData) || isDeviceProvisioned()) {
            return true;
        }
        Slog.d(TAG, "Not clearing FRP credential yet because device is not yet provisioned");
        return false;
    }

    private void synchronizeGatekeeperFrpPassword(PasswordData passwordData, int i, int i2) {
        if (shouldSynchronizeFrpCredential(passwordData, i2)) {
            Slogf.d(TAG, "Syncing Gatekeeper-based FRP credential tied to user %d", Integer.valueOf(i2));
            if (!isNoneCredential(passwordData)) {
                this.mStorage.writePersistentDataBlock(1, i2, i, passwordData.toBytes());
            } else {
                this.mStorage.writePersistentDataBlock(0, i2, 0, null);
            }
        }
    }

    private void synchronizeWeaverFrpPassword(PasswordData passwordData, int i, int i2, int i3) {
        if (shouldSynchronizeFrpCredential(passwordData, i2)) {
            Slogf.d(TAG, "Syncing Weaver-based FRP credential tied to user %d", Integer.valueOf(i2));
            if (!isNoneCredential(passwordData)) {
                this.mStorage.writePersistentDataBlock(2, i3, i, passwordData.toBytes());
            } else {
                this.mStorage.writePersistentDataBlock(0, 0, 0, null);
            }
        }
    }

    public long addPendingToken(byte[] bArr, int i, int i2, LockPatternUtils.EscrowTokenStateChangeCallback escrowTokenStateChangeCallback) {
        long generateProtectorId = generateProtectorId();
        if (!this.tokenMap.containsKey(Integer.valueOf(i2))) {
            this.tokenMap.put(Integer.valueOf(i2), new ArrayMap<>());
        }
        TokenData tokenData = new TokenData();
        tokenData.mType = i;
        byte[] randomBytes = SecureRandomUtils.randomBytes(16384);
        if (isWeaverAvailable()) {
            byte[] randomBytes2 = SecureRandomUtils.randomBytes(this.mWeaverConfig.valueSize);
            tokenData.weaverSecret = randomBytes2;
            tokenData.secdiscardableOnDisk = SyntheticPasswordCrypto.encrypt(randomBytes2, PERSONALIZATION_WEAVER_TOKEN, randomBytes);
        } else {
            tokenData.secdiscardableOnDisk = randomBytes;
            tokenData.weaverSecret = null;
        }
        tokenData.aggregatedSecret = transformUnderSecdiscardable(bArr, randomBytes);
        tokenData.mCallback = escrowTokenStateChangeCallback;
        this.tokenMap.get(Integer.valueOf(i2)).put(Long.valueOf(generateProtectorId), tokenData);
        return generateProtectorId;
    }

    public Set<Long> getPendingTokensForUser(int i) {
        if (!this.tokenMap.containsKey(Integer.valueOf(i))) {
            return Collections.emptySet();
        }
        Slog.d(TAG, "[getPendingTokensForUser]  userId = " + i);
        return new ArraySet(this.tokenMap.get(Integer.valueOf(i)).keySet());
    }

    public boolean removePendingToken(long j, int i) {
        return this.tokenMap.containsKey(Integer.valueOf(i)) && this.tokenMap.get(Integer.valueOf(i)).remove(Long.valueOf(j)) != null;
    }

    public boolean createTokenBasedProtector(long j, SyntheticPassword syntheticPassword, int i) {
        TokenData tokenData;
        if (!this.tokenMap.containsKey(Integer.valueOf(i)) || (tokenData = this.tokenMap.get(Integer.valueOf(i)).get(Long.valueOf(j))) == null) {
            return false;
        }
        if (!loadEscrowData(syntheticPassword, i)) {
            Slog.w(TAG, "User is not escrowable");
            return false;
        }
        Slogf.i(TAG, "Creating token-based protector %016x for user %d", Long.valueOf(j), Integer.valueOf(i));
        if (isWeaverAvailable()) {
            int nextAvailableWeaverSlot = getNextAvailableWeaverSlot();
            Slogf.i(TAG, "Using Weaver slot %d for new token-based protector", Integer.valueOf(nextAvailableWeaverSlot));
            if (weaverEnroll(nextAvailableWeaverSlot, null, tokenData.weaverSecret) == null) {
                Slog.e(TAG, "Failed to enroll weaver secret when activating token");
                return false;
            }
            saveWeaverSlot(nextAvailableWeaverSlot, j, i);
            this.mPasswordSlotManager.markSlotInUse(nextAvailableWeaverSlot);
        }
        saveSecdiscardable(j, tokenData.secdiscardableOnDisk, i);
        createSyntheticPasswordBlob(j, getTokenBasedProtectorType(tokenData.mType), syntheticPassword, tokenData.aggregatedSecret, 0L, i);
        syncState(i);
        this.tokenMap.get(Integer.valueOf(i)).remove(Long.valueOf(j));
        LockPatternUtils.EscrowTokenStateChangeCallback escrowTokenStateChangeCallback = tokenData.mCallback;
        if (escrowTokenStateChangeCallback != null) {
            escrowTokenStateChangeCallback.onEscrowTokenActivated(j, i);
        }
        Slog.d(TAG, "[activateTokenBasedSyntheticPassword]  userId = " + i + ", tokenHandle = " + j);
        return true;
    }

    private void createSyntheticPasswordBlob(long j, byte b, SyntheticPassword syntheticPassword, byte[] bArr, long j2, int i) {
        byte[] escrowSecret;
        if (b == 1 || b == 2) {
            escrowSecret = syntheticPassword.getEscrowSecret();
        } else {
            escrowSecret = syntheticPassword.getSyntheticPassword();
        }
        saveState(SP_BLOB_NAME, SyntheticPasswordBlob.create(syntheticPassword.mVersion == 3 ? (byte) 3 : (byte) 2, b, createSpBlob(getProtectorKeyAlias(j), escrowSecret, bArr, j2)).toByte(), j, i);
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0138  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AuthenticationResult unlockLskfBasedProtector(IGateKeeperService iGateKeeperService, long j, LockscreenCredential lockscreenCredential, int i, ICheckCredentialProgressCallback iCheckCredentialProgressCallback) {
        PasswordData passwordData;
        int i2;
        byte[] bArr;
        AuthenticationResult authenticationResult;
        byte[] loadSecdiscardable;
        long j2;
        byte[] transformUnderSecdiscardable;
        GateKeeperResponse gateKeeperResponse;
        Slog.d(TAG, "[unwrapPasswordBasedSyntheticPassword] progressCallback = " + iCheckCredentialProgressCallback);
        AuthenticationResult authenticationResult2 = new AuthenticationResult();
        long j3 = 0;
        if (j == 0) {
            Slogf.wtf(TAG, "Synthetic password not found for user %d", Integer.valueOf(i));
            authenticationResult2.gkResponse = VerifyCredentialResponse.ERROR;
            return authenticationResult2;
        }
        byte[] loadState = loadState(PASSWORD_DATA_NAME, j, i);
        if (loadState != null) {
            PasswordData fromBytes = PasswordData.fromBytes(loadState);
            i2 = fromBytes.credentialType;
            passwordData = fromBytes;
        } else {
            passwordData = null;
            i2 = -1;
        }
        if (!lockscreenCredential.checkAgainstStoredType(i2)) {
            Slogf.e(TAG, "Credential type mismatch: stored type is %s but provided type is %s", LockPatternUtils.credentialTypeToString(i2), LockPatternUtils.credentialTypeToString(lockscreenCredential.getType()));
            authenticationResult2.gkResponse = VerifyCredentialResponse.ERROR;
            return authenticationResult2;
        }
        byte[] stretchLskf = stretchLskf(lockscreenCredential, passwordData);
        int loadWeaverSlot = loadWeaverSlot(j, i);
        if (loadWeaverSlot != -1) {
            if (!isWeaverAvailable()) {
                Slog.e(TAG, "Protector uses Weaver, but Weaver is unavailable");
                authenticationResult2.gkResponse = VerifyCredentialResponse.ERROR;
                return authenticationResult2;
            }
            VerifyCredentialResponse weaverVerify = weaverVerify(loadWeaverSlot, stretchedLskfToWeaverKey(stretchLskf));
            authenticationResult2.gkResponse = weaverVerify;
            if (weaverVerify.getResponseCode() != 0) {
                return authenticationResult2;
            }
            j2 = 0;
            transformUnderSecdiscardable = transformUnderWeaverSecret(stretchLskf, authenticationResult2.gkResponse.getGatekeeperHAT());
            authenticationResult = authenticationResult2;
        } else {
            if (passwordData == null || passwordData.passwordHandle == null) {
                bArr = stretchLskf;
                authenticationResult = authenticationResult2;
                if (!lockscreenCredential.isNone()) {
                    Slog.e(TAG, "Missing Gatekeeper password handle for nonempty LSKF");
                    authenticationResult.gkResponse = VerifyCredentialResponse.ERROR;
                    return authenticationResult;
                }
            } else {
                byte[] stretchedLskfToGkPassword = stretchedLskfToGkPassword(stretchLskf);
                try {
                    GateKeeperResponse verifyChallenge = iGateKeeperService.verifyChallenge(fakeUserId(i), 0L, passwordData.passwordHandle, stretchedLskfToGkPassword);
                    PasswordData passwordData2 = passwordData;
                    bArr = stretchLskf;
                    authenticationResult = authenticationResult2;
                    this.mSyntheticPasswordManagerExt.updateVerifyParam(this.mContext, lockscreenCredential, passwordData, i, j, this.mStorage);
                    int responseCode = verifyChallenge.getResponseCode();
                    if (responseCode != 0) {
                        if (responseCode == 1) {
                            authenticationResult.gkResponse = VerifyCredentialResponse.fromTimeout(verifyChallenge.getTimeout());
                            return authenticationResult;
                        }
                        authenticationResult.gkResponse = VerifyCredentialResponse.ERROR;
                        return authenticationResult;
                    }
                    authenticationResult.gkResponse = VerifyCredentialResponse.OK;
                    if (verifyChallenge.getShouldReEnroll()) {
                        try {
                        } catch (RemoteException e) {
                            e = e;
                        }
                        try {
                            gateKeeperResponse = iGateKeeperService.enroll(fakeUserId(i), passwordData2.passwordHandle, stretchedLskfToGkPassword, stretchedLskfToGkPassword);
                        } catch (RemoteException e2) {
                            e = e2;
                            Slog.w(TAG, "Fail to invoke gatekeeper.enroll", e);
                            gateKeeperResponse = GateKeeperResponse.ERROR;
                            if (gateKeeperResponse.getResponseCode() != 0) {
                            }
                            j3 = sidFromPasswordHandle(passwordData2.passwordHandle);
                            loadSecdiscardable = loadSecdiscardable(j, i);
                            if (loadSecdiscardable != null) {
                            }
                        }
                        if (gateKeeperResponse.getResponseCode() != 0) {
                            passwordData2.passwordHandle = gateKeeperResponse.getPayload();
                            passwordData2.credentialType = lockscreenCredential.getType();
                            saveState(PASSWORD_DATA_NAME, passwordData2.toBytes(), j, i);
                            syncState(i);
                            synchronizeGatekeeperFrpPassword(passwordData2, 0, i);
                        } else {
                            Slog.w(TAG, "Fail to re-enroll user password for user " + i);
                        }
                    }
                    j3 = sidFromPasswordHandle(passwordData2.passwordHandle);
                } catch (RemoteException e3) {
                    Slog.e(TAG, "gatekeeper verify failed", e3);
                    authenticationResult2.gkResponse = VerifyCredentialResponse.ERROR;
                    return authenticationResult2;
                }
            }
            loadSecdiscardable = loadSecdiscardable(j, i);
            if (loadSecdiscardable != null) {
                Slog.e(TAG, "secdiscardable file not found");
                authenticationResult.gkResponse = VerifyCredentialResponse.ERROR;
                return authenticationResult;
            }
            j2 = j3;
            transformUnderSecdiscardable = transformUnderSecdiscardable(bArr, loadSecdiscardable);
        }
        if (iCheckCredentialProgressCallback != null && !((ISyntheticPasswordManagerExt) ExtLoader.type(ISyntheticPasswordManagerExt.class).create()).isBootFromOTA()) {
            try {
                iCheckCredentialProgressCallback.onCredentialVerified();
                Slog.d(TAG, "[unwrapPasswordBasedSyntheticPassword] onCredentialVerified ");
            } catch (RemoteException e4) {
                Slog.w(TAG, "progressCallback throws exception", e4);
            }
        }
        SyntheticPassword unwrapSyntheticPasswordBlob = unwrapSyntheticPasswordBlob(j, (byte) 0, transformUnderSecdiscardable, j2, i);
        authenticationResult.syntheticPassword = unwrapSyntheticPasswordBlob;
        authenticationResult.gkResponse = verifyChallenge(iGateKeeperService, unwrapSyntheticPasswordBlob, 0L, i);
        if (authenticationResult.syntheticPassword != null && !lockscreenCredential.isNone() && !hasPasswordMetrics(j, i)) {
            savePasswordMetrics(lockscreenCredential, authenticationResult.syntheticPassword, j, i);
            syncState(i);
        }
        Slog.d(TAG, "[unwrapPasswordBasedSyntheticPassword]  userId = " + i + ", protectorId = " + j);
        return authenticationResult;
    }

    public boolean refreshPinLengthOnDisk(PasswordMetrics passwordMetrics, long j, int i) {
        byte[] loadState;
        if (!isAutoPinConfirmationFeatureAvailable() || (loadState = loadState(PASSWORD_DATA_NAME, j, i)) == null) {
            return false;
        }
        PasswordData fromBytes = PasswordData.fromBytes(loadState);
        int derivePinLength = derivePinLength(passwordMetrics.length, passwordMetrics.credType == 3, i);
        if (fromBytes.pinLength != derivePinLength) {
            fromBytes.pinLength = derivePinLength;
            saveState(PASSWORD_DATA_NAME, fromBytes.toBytes(), j, i);
            syncState(i);
        }
        return true;
    }

    public AuthenticationResult unlockTokenBasedProtector(IGateKeeperService iGateKeeperService, long j, byte[] bArr, int i) {
        return unlockTokenBasedProtectorInternal(iGateKeeperService, j, SyntheticPasswordBlob.fromBytes(loadState(SP_BLOB_NAME, j, i)).mProtectorType, bArr, i);
    }

    public AuthenticationResult unlockStrongTokenBasedProtector(IGateKeeperService iGateKeeperService, long j, byte[] bArr, int i) {
        return unlockTokenBasedProtectorInternal(iGateKeeperService, j, (byte) 1, bArr, i);
    }

    public AuthenticationResult unlockWeakTokenBasedProtector(IGateKeeperService iGateKeeperService, long j, byte[] bArr, int i) {
        return unlockTokenBasedProtectorInternal(iGateKeeperService, j, (byte) 2, bArr, i);
    }

    private AuthenticationResult unlockTokenBasedProtectorInternal(IGateKeeperService iGateKeeperService, long j, byte b, byte[] bArr, int i) {
        AuthenticationResult authenticationResult = new AuthenticationResult();
        byte[] loadSecdiscardable = loadSecdiscardable(j, i);
        if (loadSecdiscardable == null) {
            Slog.e(TAG, "secdiscardable file not found");
            authenticationResult.gkResponse = VerifyCredentialResponse.ERROR;
            return authenticationResult;
        }
        int loadWeaverSlot = loadWeaverSlot(j, i);
        if (loadWeaverSlot != -1) {
            if (!isWeaverAvailable()) {
                Slog.e(TAG, "Protector uses Weaver, but Weaver is unavailable");
                authenticationResult.gkResponse = VerifyCredentialResponse.ERROR;
                return authenticationResult;
            }
            VerifyCredentialResponse weaverVerify = weaverVerify(loadWeaverSlot, null);
            if (weaverVerify.getResponseCode() != 0 || weaverVerify.getGatekeeperHAT() == null) {
                Slog.e(TAG, "Failed to retrieve Weaver secret when unlocking token-based protector");
                authenticationResult.gkResponse = VerifyCredentialResponse.ERROR;
                return authenticationResult;
            }
            loadSecdiscardable = SyntheticPasswordCrypto.decrypt(weaverVerify.getGatekeeperHAT(), PERSONALIZATION_WEAVER_TOKEN, loadSecdiscardable);
        }
        SyntheticPassword unwrapSyntheticPasswordBlob = unwrapSyntheticPasswordBlob(j, b, transformUnderSecdiscardable(bArr, loadSecdiscardable), 0L, i);
        authenticationResult.syntheticPassword = unwrapSyntheticPasswordBlob;
        if (unwrapSyntheticPasswordBlob != null) {
            VerifyCredentialResponse verifyChallenge = verifyChallenge(iGateKeeperService, unwrapSyntheticPasswordBlob, 0L, i);
            authenticationResult.gkResponse = verifyChallenge;
            if (verifyChallenge == null) {
                authenticationResult.gkResponse = VerifyCredentialResponse.OK;
            }
        } else {
            authenticationResult.gkResponse = VerifyCredentialResponse.ERROR;
        }
        return authenticationResult;
    }

    private SyntheticPassword unwrapSyntheticPasswordBlob(long j, byte b, byte[] bArr, long j2, int i) {
        byte[] decryptSpBlob;
        byte[] loadState = loadState(SP_BLOB_NAME, j, i);
        if (loadState == null) {
            return null;
        }
        SyntheticPasswordBlob fromBytes = SyntheticPasswordBlob.fromBytes(loadState);
        byte b2 = fromBytes.mVersion;
        if (b2 != 3 && b2 != 2 && b2 != 1) {
            throw new IllegalArgumentException("Unknown blob version: " + ((int) fromBytes.mVersion));
        }
        if (fromBytes.mProtectorType != b) {
            throw new IllegalArgumentException("Invalid protector type: " + ((int) fromBytes.mProtectorType));
        }
        if (b2 == 1) {
            decryptSpBlob = SyntheticPasswordCrypto.decryptBlobV1(getProtectorKeyAlias(j), fromBytes.mContent, bArr);
        } else {
            decryptSpBlob = decryptSpBlob(getProtectorKeyAlias(j), fromBytes.mContent, bArr);
        }
        if (decryptSpBlob == null) {
            Slog.e(TAG, "Fail to decrypt SP for user " + i);
            return null;
        }
        SyntheticPassword syntheticPassword = new SyntheticPassword(fromBytes.mVersion);
        byte b3 = fromBytes.mProtectorType;
        if (b3 == 1 || b3 == 2) {
            if (!loadEscrowData(syntheticPassword, i)) {
                Slog.e(TAG, "User is not escrowable: " + i);
                return null;
            }
            syntheticPassword.recreateFromEscrow(decryptSpBlob);
        } else {
            syntheticPassword.recreateDirectly(decryptSpBlob);
        }
        if (fromBytes.mVersion == 1) {
            Slog.i(TAG, "Upgrading v1 SP blob for user " + i + ", protectorType = " + ((int) fromBytes.mProtectorType));
            createSyntheticPasswordBlob(j, fromBytes.mProtectorType, syntheticPassword, bArr, j2, i);
            syncState(i);
        }
        return syntheticPassword;
    }

    public VerifyCredentialResponse verifyChallenge(IGateKeeperService iGateKeeperService, SyntheticPassword syntheticPassword, long j, int i) {
        return verifyChallengeInternal(iGateKeeperService, syntheticPassword.deriveGkPassword(), j, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public VerifyCredentialResponse verifyChallengeInternal(IGateKeeperService iGateKeeperService, byte[] bArr, long j, int i) {
        GateKeeperResponse gateKeeperResponse;
        byte[] loadSyntheticPasswordHandle = loadSyntheticPasswordHandle(i);
        if (loadSyntheticPasswordHandle == null) {
            return null;
        }
        try {
            GateKeeperResponse verifyChallenge = iGateKeeperService.verifyChallenge(i, j, loadSyntheticPasswordHandle, bArr);
            int responseCode = verifyChallenge.getResponseCode();
            if (responseCode != 0) {
                if (responseCode == 1) {
                    return VerifyCredentialResponse.fromTimeout(verifyChallenge.getTimeout());
                }
                return VerifyCredentialResponse.ERROR;
            }
            VerifyCredentialResponse build = new VerifyCredentialResponse.Builder().setGatekeeperHAT(verifyChallenge.getPayload()).build();
            if (verifyChallenge.getShouldReEnroll()) {
                try {
                    gateKeeperResponse = iGateKeeperService.enroll(i, loadSyntheticPasswordHandle, loadSyntheticPasswordHandle, bArr);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Failed to invoke gatekeeper.enroll", e);
                    gateKeeperResponse = GateKeeperResponse.ERROR;
                }
                if (gateKeeperResponse.getResponseCode() == 0) {
                    saveSyntheticPasswordHandle(gateKeeperResponse.getPayload(), i);
                    return verifyChallengeInternal(iGateKeeperService, bArr, j, i);
                }
                Slog.w(TAG, "Fail to re-enroll SP handle for user " + i);
            }
            return build;
        } catch (RemoteException e2) {
            Slog.e(TAG, "Fail to verify with gatekeeper " + i, e2);
            return VerifyCredentialResponse.ERROR;
        }
    }

    public boolean protectorExists(long j, int i) {
        return hasState(SP_BLOB_NAME, j, i);
    }

    public void destroyTokenBasedProtector(long j, int i) {
        Slogf.i(TAG, "Destroying token-based protector %016x for user %d", Long.valueOf(j), Integer.valueOf(i));
        SyntheticPasswordBlob fromBytes = SyntheticPasswordBlob.fromBytes(loadState(SP_BLOB_NAME, j, i));
        destroyProtectorCommon(j, i);
        if (fromBytes.mProtectorType == 2) {
            notifyWeakEscrowTokenRemovedListeners(j, i);
        }
    }

    public void destroyAllWeakTokenBasedProtectors(int i) {
        Iterator<Long> it = this.mStorage.listSyntheticPasswordProtectorsForUser(SECDISCARDABLE_NAME, i).iterator();
        while (it.hasNext()) {
            long longValue = it.next().longValue();
            if (SyntheticPasswordBlob.fromBytes(loadState(SP_BLOB_NAME, longValue, i)).mProtectorType == 2) {
                destroyTokenBasedProtector(longValue, i);
            }
        }
    }

    public void destroyLskfBasedProtector(long j, int i) {
        Slogf.i(TAG, "Destroying LSKF-based protector %016x for user %d", Long.valueOf(j), Integer.valueOf(i));
        destroyProtectorCommon(j, i);
        destroyState(PASSWORD_DATA_NAME, j, i);
        destroyState(PASSWORD_METRICS_NAME, j, i);
    }

    private void destroyProtectorCommon(long j, int i) {
        destroyState(SP_BLOB_NAME, j, i);
        destroyProtectorKey(getProtectorKeyAlias(j));
        destroyState(SECDISCARDABLE_NAME, j, i);
        if (hasState(WEAVER_SLOT_NAME, j, i)) {
            destroyWeaverSlot(j, i);
        }
    }

    private byte[] transformUnderWeaverSecret(byte[] bArr, byte[] bArr2) {
        return ArrayUtils.concat(new byte[][]{bArr, SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_WEAVER_PASSWORD, bArr2)});
    }

    private byte[] transformUnderSecdiscardable(byte[] bArr, byte[] bArr2) {
        return ArrayUtils.concat(new byte[][]{bArr, SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_SECDISCARDABLE, bArr2)});
    }

    private byte[] createSecdiscardable(long j, int i) {
        byte[] randomBytes = SecureRandomUtils.randomBytes(16384);
        saveSecdiscardable(j, randomBytes, i);
        return randomBytes;
    }

    private void saveSecdiscardable(long j, byte[] bArr, int i) {
        saveState(SECDISCARDABLE_NAME, bArr, j, i);
    }

    private byte[] loadSecdiscardable(long j, int i) {
        return loadState(SECDISCARDABLE_NAME, j, i);
    }

    @VisibleForTesting
    boolean hasPasswordData(long j, int i) {
        return hasState(PASSWORD_DATA_NAME, j, i);
    }

    public PasswordMetrics getPasswordMetrics(SyntheticPassword syntheticPassword, long j, int i) {
        byte[] loadState = loadState(PASSWORD_METRICS_NAME, j, i);
        if (loadState == null) {
            Slogf.e(TAG, "Failed to read password metrics file for user %d", Integer.valueOf(i));
            return null;
        }
        byte[] decrypt = SyntheticPasswordCrypto.decrypt(syntheticPassword.deriveMetricsKey(), new byte[0], loadState);
        if (decrypt == null) {
            Slogf.e(TAG, "Failed to decrypt password metrics file for user %d", Integer.valueOf(i));
            return null;
        }
        return VersionedPasswordMetrics.deserialize(decrypt).getMetrics();
    }

    private void savePasswordMetrics(LockscreenCredential lockscreenCredential, SyntheticPassword syntheticPassword, long j, int i) {
        saveState(PASSWORD_METRICS_NAME, SyntheticPasswordCrypto.encrypt(syntheticPassword.deriveMetricsKey(), new byte[0], new VersionedPasswordMetrics(lockscreenCredential).serialize()), j, i);
    }

    @VisibleForTesting
    boolean hasPasswordMetrics(long j, int i) {
        return hasState(PASSWORD_METRICS_NAME, j, i);
    }

    private boolean hasState(String str, long j, int i) {
        return !ArrayUtils.isEmpty(loadState(str, j, i));
    }

    private byte[] loadState(String str, long j, int i) {
        return this.mStorage.readSyntheticPasswordState(i, j, str);
    }

    private void saveState(String str, byte[] bArr, long j, int i) {
        this.mStorage.writeSyntheticPasswordState(i, j, str, bArr);
    }

    private void syncState(int i) {
        this.mStorage.syncSyntheticPasswordState(i);
    }

    private void destroyState(String str, long j, int i) {
        this.mStorage.deleteSyntheticPasswordState(i, j, str);
    }

    @VisibleForTesting
    protected byte[] decryptSpBlob(String str, byte[] bArr, byte[] bArr2) {
        return SyntheticPasswordCrypto.decryptBlob(str, bArr, bArr2);
    }

    @VisibleForTesting
    protected byte[] createSpBlob(String str, byte[] bArr, byte[] bArr2, long j) {
        return SyntheticPasswordCrypto.createBlob(str, bArr, bArr2, j);
    }

    @VisibleForTesting
    protected void destroyProtectorKey(String str) {
        SyntheticPasswordCrypto.destroyProtectorKey(str);
    }

    private static long generateProtectorId() {
        long randomLong;
        do {
            randomLong = SecureRandomUtils.randomLong();
        } while (randomLong == 0);
        return randomLong;
    }

    private String getProtectorKeyAlias(long j) {
        return TextUtils.formatSimple("%s%x", new Object[]{PROTECTOR_KEY_ALIAS_PREFIX, Long.valueOf(j)});
    }

    @VisibleForTesting
    byte[] stretchLskf(LockscreenCredential lockscreenCredential, PasswordData passwordData) {
        byte[] credential = lockscreenCredential.isNone() ? DEFAULT_PASSWORD : lockscreenCredential.getCredential();
        if (passwordData == null) {
            Slog.d(TAG, "[stretchLskf] PasswordData is null");
            Preconditions.checkArgument(lockscreenCredential.isNone());
            return Arrays.copyOf(credential, 32);
        }
        return scrypt(credential, passwordData.salt, 1 << passwordData.scryptLogN, 1 << passwordData.scryptLogR, 1 << passwordData.scryptLogP, 32);
    }

    private byte[] stretchedLskfToGkPassword(byte[] bArr) {
        return SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_USER_GK_AUTH, bArr);
    }

    private byte[] stretchedLskfToWeaverKey(byte[] bArr) {
        byte[] personalizedHash = SyntheticPasswordCrypto.personalizedHash(PERSONALIZATION_WEAVER_KEY, bArr);
        int length = personalizedHash.length;
        int i = this.mWeaverConfig.keySize;
        if (length < i) {
            throw new IllegalArgumentException("weaver key length too small");
        }
        return Arrays.copyOf(personalizedHash, i);
    }

    @VisibleForTesting
    protected long sidFromPasswordHandle(byte[] bArr) {
        return nativeSidFromPasswordHandle(bArr);
    }

    @VisibleForTesting
    protected byte[] scrypt(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4) {
        return new Scrypt().scrypt(bArr, bArr2, i, i2, i3, i4);
    }

    @VisibleForTesting
    static byte[] bytesToHex(byte[] bArr) {
        return HexEncoding.encodeToString(bArr).getBytes();
    }

    public boolean migrateKeyNamespace() {
        Iterator<List<Long>> it = this.mStorage.listSyntheticPasswordProtectorsForAllUsers(SP_BLOB_NAME).values().iterator();
        boolean z = true;
        while (it.hasNext()) {
            Iterator<Long> it2 = it.next().iterator();
            while (it2.hasNext()) {
                z &= SyntheticPasswordCrypto.migrateLockSettingsKey(getProtectorKeyAlias(it2.next().longValue()));
            }
        }
        return z;
    }

    public boolean registerWeakEscrowTokenRemovedListener(IWeakEscrowTokenRemovedListener iWeakEscrowTokenRemovedListener) {
        return this.mListeners.register(iWeakEscrowTokenRemovedListener);
    }

    public boolean unregisterWeakEscrowTokenRemovedListener(IWeakEscrowTokenRemovedListener iWeakEscrowTokenRemovedListener) {
        return this.mListeners.unregister(iWeakEscrowTokenRemovedListener);
    }

    private void notifyWeakEscrowTokenRemovedListeners(long j, int i) {
        int beginBroadcast = this.mListeners.beginBroadcast();
        while (beginBroadcast > 0) {
            beginBroadcast--;
            try {
                try {
                    this.mListeners.getBroadcastItem(beginBroadcast).onWeakEscrowTokenRemoved(j, i);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Exception while notifying WeakEscrowTokenRemovedListener.", e);
                }
            } finally {
                this.mListeners.finishBroadcast();
            }
        }
    }

    public void writeVendorAuthSecret(byte[] bArr, SyntheticPassword syntheticPassword, int i) {
        saveState(VENDOR_AUTH_SECRET_NAME, SyntheticPasswordCrypto.encrypt(syntheticPassword.deriveVendorAuthSecretEncryptionKey(), new byte[0], bArr), 0L, i);
        syncState(i);
    }

    public byte[] readVendorAuthSecret(SyntheticPassword syntheticPassword, int i) {
        byte[] loadState = loadState(VENDOR_AUTH_SECRET_NAME, 0L, i);
        if (loadState == null) {
            return null;
        }
        return SyntheticPasswordCrypto.decrypt(syntheticPassword.deriveVendorAuthSecretEncryptionKey(), new byte[0], loadState);
    }
}
