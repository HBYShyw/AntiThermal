package com.google.security.cryptauth.lib.securegcm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class D2DConnectionContextV1 {
    private final long contextPtr;

    private static native byte[] decode_message_from_peer(long j, byte[] bArr, byte[] bArr2) throws CryptoException;

    private static native byte[] encode_message_to_peer(long j, byte[] bArr, byte[] bArr2) throws BadHandleException;

    private static native long from_saved_session(byte[] bArr);

    private static native int get_sequence_number_for_decoding(long j) throws BadHandleException;

    private static native int get_sequence_number_for_encoding(long j) throws BadHandleException;

    private static native byte[] get_session_unique(long j) throws BadHandleException;

    private static native byte[] save_session(long j) throws BadHandleException;

    static {
        System.loadLibrary("ukey2_jni");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public D2DConnectionContextV1(@Nonnull long j) {
        this.contextPtr = j;
    }

    @Nonnull
    public byte[] encodeMessageToPeer(@Nonnull byte[] bArr, @Nullable byte[] bArr2) throws BadHandleException {
        return encode_message_to_peer(this.contextPtr, bArr, bArr2);
    }

    @Nonnull
    public byte[] decodeMessageFromPeer(@Nonnull byte[] bArr, @Nullable byte[] bArr2) throws CryptoException {
        return decode_message_from_peer(this.contextPtr, bArr, bArr2);
    }

    @Nonnull
    public byte[] getSessionUnique() throws BadHandleException {
        return get_session_unique(this.contextPtr);
    }

    public int getSequenceNumberForEncoding() throws BadHandleException {
        return get_sequence_number_for_encoding(this.contextPtr);
    }

    public int getSequenceNumberForDecoding() throws BadHandleException {
        return get_sequence_number_for_decoding(this.contextPtr);
    }

    @Nonnull
    public byte[] saveSession() throws BadHandleException {
        return save_session(this.contextPtr);
    }

    public static D2DConnectionContextV1 fromSavedSession(@Nonnull byte[] bArr) throws SessionRestoreException {
        return new D2DConnectionContextV1(from_saved_session(bArr));
    }
}
