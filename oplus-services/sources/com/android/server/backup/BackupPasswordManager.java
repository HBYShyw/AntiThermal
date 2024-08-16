package com.android.server.backup;

import android.content.Context;
import android.util.Slog;
import com.android.server.backup.utils.DataStreamCodec;
import com.android.server.backup.utils.DataStreamFileCodec;
import com.android.server.backup.utils.PasswordUtils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BackupPasswordManager {
    private static final int BACKUP_PW_FILE_VERSION = 2;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_PW_FILE_VERSION = 1;
    private static final String PASSWORD_HASH_FILE_NAME = "pwhash";
    private static final String PASSWORD_VERSION_FILE_NAME = "pwversion";
    public static final String PBKDF_CURRENT = "PBKDF2WithHmacSHA1";
    public static final String PBKDF_FALLBACK = "PBKDF2WithHmacSHA1And8bit";
    private static final String TAG = "BackupPasswordManager";
    private final File mBaseStateDir;
    private final Context mContext;
    private String mPasswordHash;
    private byte[] mPasswordSalt;
    private int mPasswordVersion;
    private final SecureRandom mRng;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackupPasswordManager(Context context, File file, SecureRandom secureRandom) {
        this.mContext = context;
        this.mRng = secureRandom;
        this.mBaseStateDir = file;
        loadStateFromFilesystem();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasBackupPassword() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "hasBackupPassword");
        String str = this.mPasswordHash;
        return str != null && str.length() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean backupPasswordMatches(String str) {
        return !hasBackupPassword() || passwordMatchesSaved(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setBackupPassword(String str, String str2) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "setBackupPassword");
        if (!passwordMatchesSaved(str)) {
            return false;
        }
        try {
            getPasswordVersionFileCodec().serialize(2);
            this.mPasswordVersion = 2;
            if (str2 == null || str2.isEmpty()) {
                return clearPassword();
            }
            try {
                byte[] randomSalt = randomSalt();
                String buildPasswordHash = PasswordUtils.buildPasswordHash(PBKDF_CURRENT, str2, randomSalt, 10000);
                getPasswordHashFileCodec().serialize(new BackupPasswordHash(buildPasswordHash, randomSalt));
                this.mPasswordHash = buildPasswordHash;
                this.mPasswordSalt = randomSalt;
                return true;
            } catch (IOException unused) {
                Slog.e(TAG, "Unable to set backup password");
                return false;
            }
        } catch (IOException unused2) {
            Slog.e(TAG, "Unable to write backup pw version; password not changed");
            return false;
        }
    }

    private boolean usePbkdf2Fallback() {
        return this.mPasswordVersion < 2;
    }

    private boolean clearPassword() {
        File passwordHashFile = getPasswordHashFile();
        if (passwordHashFile.exists() && !passwordHashFile.delete()) {
            Slog.e(TAG, "Unable to clear backup password");
            return false;
        }
        this.mPasswordHash = null;
        this.mPasswordSalt = null;
        return true;
    }

    private void loadStateFromFilesystem() {
        try {
            this.mPasswordVersion = getPasswordVersionFileCodec().deserialize().intValue();
        } catch (IOException unused) {
            Slog.e(TAG, "Unable to read backup pw version");
            this.mPasswordVersion = 1;
        }
        try {
            BackupPasswordHash deserialize = getPasswordHashFileCodec().deserialize();
            this.mPasswordHash = deserialize.hash;
            this.mPasswordSalt = deserialize.salt;
        } catch (IOException unused2) {
            Slog.e(TAG, "Unable to read saved backup pw hash");
        }
    }

    private boolean passwordMatchesSaved(String str) {
        return passwordMatchesSaved(PBKDF_CURRENT, str) || (usePbkdf2Fallback() && passwordMatchesSaved(PBKDF_FALLBACK, str));
    }

    private boolean passwordMatchesSaved(String str, String str2) {
        if (this.mPasswordHash == null) {
            return str2 == null || str2.equals("");
        }
        if (str2 == null || str2.length() == 0) {
            return false;
        }
        return this.mPasswordHash.equalsIgnoreCase(PasswordUtils.buildPasswordHash(str, str2, this.mPasswordSalt, 10000));
    }

    private byte[] randomSalt() {
        byte[] bArr = new byte[64];
        this.mRng.nextBytes(bArr);
        return bArr;
    }

    private DataStreamFileCodec<Integer> getPasswordVersionFileCodec() {
        return new DataStreamFileCodec<>(new File(this.mBaseStateDir, PASSWORD_VERSION_FILE_NAME), new PasswordVersionFileCodec());
    }

    private DataStreamFileCodec<BackupPasswordHash> getPasswordHashFileCodec() {
        return new DataStreamFileCodec<>(getPasswordHashFile(), new PasswordHashFileCodec());
    }

    private File getPasswordHashFile() {
        return new File(this.mBaseStateDir, PASSWORD_HASH_FILE_NAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class BackupPasswordHash {
        public String hash;
        public byte[] salt;

        BackupPasswordHash(String str, byte[] bArr) {
            this.hash = str;
            this.salt = bArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PasswordVersionFileCodec implements DataStreamCodec<Integer> {
        private PasswordVersionFileCodec() {
        }

        @Override // com.android.server.backup.utils.DataStreamCodec
        public void serialize(Integer num, DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.write(num.intValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.backup.utils.DataStreamCodec
        public Integer deserialize(DataInputStream dataInputStream) throws IOException {
            return Integer.valueOf(dataInputStream.readInt());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PasswordHashFileCodec implements DataStreamCodec<BackupPasswordHash> {
        private PasswordHashFileCodec() {
        }

        @Override // com.android.server.backup.utils.DataStreamCodec
        public void serialize(BackupPasswordHash backupPasswordHash, DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeInt(backupPasswordHash.salt.length);
            dataOutputStream.write(backupPasswordHash.salt);
            dataOutputStream.writeUTF(backupPasswordHash.hash);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.backup.utils.DataStreamCodec
        public BackupPasswordHash deserialize(DataInputStream dataInputStream) throws IOException {
            byte[] bArr = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(bArr);
            return new BackupPasswordHash(dataInputStream.readUTF(), bArr);
        }
    }
}
