package com.google.security.cryptauth.lib.securegcm;

import javax.annotation.Nonnull;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class D2DHandshakeContext {
    private final long context_ptr;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public enum Role {
        Initiator,
        Responder
    }

    private static native long create_context(boolean z);

    private static native byte[] get_next_handshake_message(long j) throws BadHandleException;

    private static native byte[] get_verification_string(long j, int i) throws BadHandleException, HandshakeException;

    private static native boolean is_handshake_complete(long j) throws BadHandleException;

    private static native void parse_handshake_message(long j, byte[] bArr) throws BadHandleException, HandshakeException;

    private static native long to_connection_context(long j) throws HandshakeException;

    static {
        System.loadLibrary("ukey2_jni");
    }

    public D2DHandshakeContext(@Nonnull Role role) {
        this.context_ptr = create_context(role == Role.Initiator);
    }

    public static D2DHandshakeContext forInitiator() {
        return new D2DHandshakeContext(Role.Initiator);
    }

    public static D2DHandshakeContext forResponder() {
        return new D2DHandshakeContext(Role.Responder);
    }

    public boolean isHandshakeComplete() throws BadHandleException {
        return is_handshake_complete(this.context_ptr);
    }

    @Nonnull
    public byte[] getNextHandshakeMessage() throws BadHandleException {
        return get_next_handshake_message(this.context_ptr);
    }

    @Nonnull
    public void parseHandshakeMessage(@Nonnull byte[] bArr) throws BadHandleException, HandshakeException {
        parse_handshake_message(this.context_ptr, bArr);
    }

    @Nonnull
    public byte[] getVerificationString(int i) throws BadHandleException, HandshakeException {
        return get_verification_string(this.context_ptr, i);
    }

    public D2DConnectionContextV1 toConnectionContext() throws HandshakeException {
        return new D2DConnectionContextV1(to_connection_context(this.context_ptr));
    }
}
