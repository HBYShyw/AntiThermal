package com.android.server.locksettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import javax.crypto.SecretKey;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class RebootEscrowData {
    private static final int CURRENT_VERSION = 2;
    private static final int LEGACY_SINGLE_ENCRYPTED_VERSION = 1;
    private final byte[] mBlob;
    private final RebootEscrowKey mKey;
    private final byte mSpVersion;
    private final byte[] mSyntheticPassword;

    private RebootEscrowData(byte b, byte[] bArr, byte[] bArr2, RebootEscrowKey rebootEscrowKey) {
        this.mSpVersion = b;
        this.mSyntheticPassword = bArr;
        this.mBlob = bArr2;
        this.mKey = rebootEscrowKey;
    }

    public byte getSpVersion() {
        return this.mSpVersion;
    }

    public byte[] getSyntheticPassword() {
        return this.mSyntheticPassword;
    }

    public byte[] getBlob() {
        return this.mBlob;
    }

    public RebootEscrowKey getKey() {
        return this.mKey;
    }

    private static byte[] decryptBlobCurrentVersion(SecretKey secretKey, RebootEscrowKey rebootEscrowKey, DataInputStream dataInputStream) throws IOException {
        if (secretKey == null) {
            throw new IOException("Failed to find wrapper key in keystore, cannot decrypt the escrow data");
        }
        return AesEncryptionUtil.decrypt(rebootEscrowKey.getKey(), AesEncryptionUtil.decrypt(secretKey, dataInputStream));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RebootEscrowData fromEncryptedData(RebootEscrowKey rebootEscrowKey, byte[] bArr, SecretKey secretKey) throws IOException {
        Objects.requireNonNull(rebootEscrowKey);
        Objects.requireNonNull(bArr);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        int readInt = dataInputStream.readInt();
        byte readByte = dataInputStream.readByte();
        if (readInt == 1) {
            return new RebootEscrowData(readByte, AesEncryptionUtil.decrypt(rebootEscrowKey.getKey(), dataInputStream), bArr, rebootEscrowKey);
        }
        if (readInt == 2) {
            return new RebootEscrowData(readByte, decryptBlobCurrentVersion(secretKey, rebootEscrowKey, dataInputStream), bArr, rebootEscrowKey);
        }
        throw new IOException("Unsupported version " + readInt);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RebootEscrowData fromSyntheticPassword(RebootEscrowKey rebootEscrowKey, byte b, byte[] bArr, SecretKey secretKey) throws IOException {
        Objects.requireNonNull(bArr);
        byte[] encrypt = AesEncryptionUtil.encrypt(secretKey, AesEncryptionUtil.encrypt(rebootEscrowKey.getKey(), bArr));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeInt(2);
        dataOutputStream.writeByte(b);
        dataOutputStream.write(encrypt);
        return new RebootEscrowData(b, bArr, byteArrayOutputStream.toByteArray(), rebootEscrowKey);
    }
}
