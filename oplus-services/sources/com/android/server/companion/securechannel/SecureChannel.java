package com.android.server.companion.securechannel;

import android.content.Context;
import android.os.Build;
import android.util.Slog;
import com.google.security.cryptauth.lib.securegcm.BadHandleException;
import com.google.security.cryptauth.lib.securegcm.CryptoException;
import com.google.security.cryptauth.lib.securegcm.D2DConnectionContextV1;
import com.google.security.cryptauth.lib.securegcm.D2DHandshakeContext;
import com.google.security.cryptauth.lib.securegcm.HandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;
import libcore.io.IoUtils;
import libcore.io.Streams;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SecureChannel {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private static final int HEADER_LENGTH = 6;
    private static final String TAG = "CDM_SecureChannel";
    private static final int VERSION = 1;
    private String mAlias;
    private final Callback mCallback;
    private byte[] mClientInit;
    private D2DConnectionContextV1 mConnectionContext;
    private D2DHandshakeContext mHandshakeContext;
    private volatile boolean mInProgress;
    private final InputStream mInput;
    private final OutputStream mOutput;
    private final byte[] mPreSharedKey;
    private boolean mPskVerified;
    private D2DHandshakeContext.Role mRole;
    private volatile boolean mStopped;
    private int mVerificationResult;
    private final AttestationVerifier mVerifier;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Callback {
        void onError(Throwable th);

        void onSecureConnection();

        void onSecureMessageReceived(byte[] bArr);
    }

    public SecureChannel(InputStream inputStream, OutputStream outputStream, Callback callback, byte[] bArr) {
        this(inputStream, outputStream, callback, bArr, null);
    }

    public SecureChannel(InputStream inputStream, OutputStream outputStream, Callback callback, Context context) {
        this(inputStream, outputStream, callback, null, new AttestationVerifier(context));
    }

    public SecureChannel(InputStream inputStream, OutputStream outputStream, Callback callback, byte[] bArr, AttestationVerifier attestationVerifier) {
        this.mInput = inputStream;
        this.mOutput = outputStream;
        this.mCallback = callback;
        this.mPreSharedKey = bArr;
        this.mVerifier = attestationVerifier;
    }

    public void start() {
        if (DEBUG) {
            Slog.d(TAG, "Starting secure channel.");
        }
        this.mStopped = false;
        new Thread(new Runnable() { // from class: com.android.server.companion.securechannel.SecureChannel$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SecureChannel.this.lambda$start$0();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        try {
            exchangeHandshake();
            exchangeAuthentication();
            this.mInProgress = false;
            this.mCallback.onSecureConnection();
            while (!this.mStopped) {
                receiveSecureMessage();
            }
        } catch (Exception e) {
            if (this.mStopped) {
                return;
            }
            Slog.e(TAG, "Secure channel encountered an error.", e);
            close();
            this.mCallback.onError(e);
        }
    }

    public void stop() {
        if (DEBUG) {
            Slog.d(TAG, "Stopping secure channel.");
        }
        this.mStopped = true;
        this.mInProgress = false;
    }

    public void close() {
        stop();
        if (DEBUG) {
            Slog.d(TAG, "Closing secure channel.");
        }
        IoUtils.closeQuietly(this.mInput);
        IoUtils.closeQuietly(this.mOutput);
        KeyStoreUtils.cleanUp(this.mAlias);
    }

    public boolean isStopped() {
        return this.mStopped;
    }

    public void establishSecureConnection() throws IOException, SecureChannelException {
        if (isSecured()) {
            Slog.d(TAG, "Channel is already secure.");
            return;
        }
        if (this.mInProgress) {
            Slog.w(TAG, "Channel has already started establishing secure connection.");
            return;
        }
        try {
            this.mInProgress = true;
            initiateHandshake();
        } catch (BadHandleException e) {
            throw new SecureChannelException("Failed to initiate handshake protocol.", e);
        }
    }

    public void sendSecureMessage(byte[] bArr) throws IOException {
        if (!isSecured()) {
            Slog.d(TAG, "Cannot send a message without a secure connection.");
            throw new IllegalStateException("Channel is not secured yet.");
        }
        try {
            sendMessage(MessageType.SECURE_MESSAGE, bArr);
        } catch (BadHandleException e) {
            throw new SecureChannelException("Failed to encrypt data.", e);
        }
    }

    private void receiveSecureMessage() throws IOException, CryptoException {
        if (!isSecured()) {
            Slog.d(TAG, "Received a message without a secure connection. Message will be ignored.");
            this.mCallback.onError(new IllegalStateException("Connection is not secure."));
            return;
        }
        try {
            this.mCallback.onSecureMessageReceived(readMessage(MessageType.SECURE_MESSAGE));
        } catch (SecureChannelException e) {
            Slog.w(TAG, "Ignoring received message.", e);
        }
    }

    private byte[] readMessage(MessageType messageType) throws IOException, SecureChannelException, CryptoException {
        if (DEBUG) {
            if (isSecured()) {
                Slog.d(TAG, "Waiting to receive next secure message.");
            } else {
                Slog.d(TAG, "Waiting to receive next " + messageType + " message.");
            }
        }
        synchronized (this.mInput) {
            byte[] bArr = new byte[6];
            Streams.readFully(this.mInput, bArr);
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            int i = wrap.getInt();
            short s = wrap.getShort();
            if (i != 1) {
                Streams.skipByReading(this.mInput, Long.MAX_VALUE);
                throw new SecureChannelException("Secure channel version mismatch. Currently on version 1. Skipping rest of data.");
            }
            if (s != messageType.mValue) {
                Streams.skipByReading(this.mInput, Long.MAX_VALUE);
                throw new SecureChannelException("Unexpected message type. Expected " + messageType.name() + "; Found " + MessageType.from(s).name() + ". Skipping rest of data.");
            }
            byte[] bArr2 = new byte[4];
            Streams.readFully(this.mInput, bArr2);
            try {
                byte[] bArr3 = new byte[ByteBuffer.wrap(bArr2).getInt()];
                Streams.readFully(this.mInput, bArr3);
                if (!MessageType.shouldEncrypt(messageType)) {
                    return bArr3;
                }
                return this.mConnectionContext.decodeMessageFromPeer(bArr3, bArr);
            } catch (OutOfMemoryError e) {
                Streams.skipByReading(this.mInput, Long.MAX_VALUE);
                throw new SecureChannelException("Payload is too large.", e);
            }
        }
    }

    private void sendMessage(MessageType messageType, byte[] bArr) throws IOException, BadHandleException {
        synchronized (this.mOutput) {
            byte[] array = ByteBuffer.allocate(6).putInt(1).putShort(messageType.mValue).array();
            if (MessageType.shouldEncrypt(messageType)) {
                bArr = this.mConnectionContext.encodeMessageToPeer(bArr, array);
            }
            this.mOutput.write(array);
            this.mOutput.write(ByteBuffer.allocate(4).putInt(bArr.length).array());
            this.mOutput.write(bArr);
            this.mOutput.flush();
        }
    }

    private void initiateHandshake() throws IOException, BadHandleException {
        if (this.mConnectionContext != null) {
            Slog.d(TAG, "Ukey2 handshake is already completed.");
            return;
        }
        this.mRole = D2DHandshakeContext.Role.Initiator;
        D2DHandshakeContext forInitiator = D2DHandshakeContext.forInitiator();
        this.mHandshakeContext = forInitiator;
        this.mClientInit = forInitiator.getNextHandshakeMessage();
        if (DEBUG) {
            Slog.d(TAG, "Sending Ukey2 Client Init message");
        }
        sendMessage(MessageType.HANDSHAKE_INIT, constructHandshakeInitMessage(this.mClientInit));
    }

    private byte[] handleHandshakeCollision(byte[] bArr) throws IOException, HandshakeException, BadHandleException, CryptoException {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        boolean z = wrap.get() == 0;
        byte[] bArr2 = new byte[wrap.remaining()];
        wrap.get(bArr2);
        if (this.mHandshakeContext == null || !z) {
            return bArr2;
        }
        Slog.w(TAG, "Detected a Ukey2 handshake role collision. Negotiating a role.");
        if (compareByteArray(this.mClientInit, bArr2) < 0) {
            Slog.d(TAG, "Assigned: Responder");
            this.mHandshakeContext = null;
            return bArr2;
        }
        Slog.d(TAG, "Assigned: Initiator; Discarding received Client Init");
        ByteBuffer wrap2 = ByteBuffer.wrap(readMessage(MessageType.HANDSHAKE_INIT));
        if (wrap2.get() == 0) {
            throw new HandshakeException("Failed to resolve Ukey2 handshake role collision.");
        }
        byte[] bArr3 = new byte[wrap2.remaining()];
        wrap2.get(bArr3);
        return bArr3;
    }

    private void exchangeHandshake() throws IOException, HandshakeException, BadHandleException, CryptoException {
        if (this.mConnectionContext != null) {
            Slog.d(TAG, "Ukey2 handshake is already completed.");
            return;
        }
        MessageType messageType = MessageType.HANDSHAKE_INIT;
        byte[] readMessage = readMessage(messageType);
        this.mInProgress = true;
        byte[] handleHandshakeCollision = handleHandshakeCollision(readMessage);
        if (this.mHandshakeContext == null) {
            this.mRole = D2DHandshakeContext.Role.Responder;
            this.mHandshakeContext = D2DHandshakeContext.forResponder();
            boolean z = DEBUG;
            if (z) {
                Slog.d(TAG, "Receiving Ukey2 Client Init message");
            }
            this.mHandshakeContext.parseHandshakeMessage(handleHandshakeCollision);
            if (z) {
                Slog.d(TAG, "Sending Ukey2 Server Init message");
            }
            sendMessage(messageType, constructHandshakeInitMessage(this.mHandshakeContext.getNextHandshakeMessage()));
            if (z) {
                Slog.d(TAG, "Receiving Ukey2 Client Finish message");
            }
            this.mHandshakeContext.parseHandshakeMessage(readMessage(MessageType.HANDSHAKE_FINISH));
        } else {
            boolean z2 = DEBUG;
            if (z2) {
                Slog.d(TAG, "Receiving Ukey2 Server Init message");
            }
            this.mHandshakeContext.parseHandshakeMessage(handleHandshakeCollision);
            if (z2) {
                Slog.d(TAG, "Sending Ukey2 Client Finish message");
            }
            sendMessage(MessageType.HANDSHAKE_FINISH, this.mHandshakeContext.getNextHandshakeMessage());
        }
        if (this.mHandshakeContext.isHandshakeComplete()) {
            if (DEBUG) {
                Slog.d(TAG, "Ukey2 Handshake completed successfully");
            }
            this.mConnectionContext = this.mHandshakeContext.toConnectionContext();
            return;
        }
        Slog.e(TAG, "Failed to complete Ukey2 Handshake");
        throw new IllegalStateException("Ukey2 Handshake did not complete as expected.");
    }

    private void exchangeAuthentication() throws IOException, GeneralSecurityException, BadHandleException, CryptoException {
        if (this.mPreSharedKey != null) {
            exchangePreSharedKey();
        }
        if (this.mVerifier != null) {
            exchangeAttestation();
        }
    }

    private void exchangePreSharedKey() throws IOException, GeneralSecurityException, BadHandleException, CryptoException {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "Exchanging pre-shared keys.");
        }
        MessageType messageType = MessageType.PRE_SHARED_KEY;
        sendMessage(messageType, constructToken(this.mRole, this.mPreSharedKey));
        byte[] readMessage = readMessage(messageType);
        D2DHandshakeContext.Role role = this.mRole;
        D2DHandshakeContext.Role role2 = D2DHandshakeContext.Role.Initiator;
        if (role == role2) {
            role2 = D2DHandshakeContext.Role.Responder;
        }
        boolean equals = Arrays.equals(readMessage, constructToken(role2, this.mPreSharedKey));
        this.mPskVerified = equals;
        if (!equals) {
            throw new SecureChannelException("Failed to verify the hash of pre-shared key.");
        }
        if (z) {
            Slog.d(TAG, "The pre-shared key was successfully authenticated.");
        }
    }

    private void exchangeAttestation() throws IOException, GeneralSecurityException, BadHandleException, CryptoException {
        if (this.mVerificationResult == 1) {
            Slog.d(TAG, "Remote attestation was already verified.");
            return;
        }
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "Exchanging device attestation.");
        }
        if (this.mAlias == null) {
            this.mAlias = generateAlias();
        }
        KeyStoreUtils.generateAttestationKeyPair(this.mAlias, constructToken(this.mRole, this.mConnectionContext.getSessionUnique()));
        byte[] encodedCertificateChain = KeyStoreUtils.getEncodedCertificateChain(this.mAlias);
        MessageType messageType = MessageType.ATTESTATION;
        sendMessage(messageType, encodedCertificateChain);
        byte[] readMessage = readMessage(messageType);
        D2DHandshakeContext.Role role = this.mRole;
        D2DHandshakeContext.Role role2 = D2DHandshakeContext.Role.Initiator;
        if (role == role2) {
            role2 = D2DHandshakeContext.Role.Responder;
        }
        this.mVerificationResult = this.mVerifier.verifyAttestation(readMessage, constructToken(role2, this.mConnectionContext.getSessionUnique()));
        byte[] array = ByteBuffer.allocate(4).putInt(this.mVerificationResult).array();
        MessageType messageType2 = MessageType.AVF_RESULT;
        sendMessage(messageType2, array);
        if (ByteBuffer.wrap(readMessage(messageType2)).getInt() != 1) {
            throw new SecureChannelException("Remote device failed to verify local attestation.");
        }
        if (this.mVerificationResult != 1) {
            throw new SecureChannelException("Failed to verify remote attestation.");
        }
        if (z) {
            Slog.d(TAG, "Remote attestation was successfully verified.");
        }
    }

    private boolean isSecured() {
        if (this.mConnectionContext == null) {
            return false;
        }
        return this.mPskVerified || this.mVerificationResult == 1;
    }

    private byte[] constructHandshakeInitMessage(byte[] bArr) {
        return ByteBuffer.allocate(bArr.length + 1).put((byte) (!D2DHandshakeContext.Role.Initiator.equals(this.mRole) ? 1 : 0)).put(bArr).array();
    }

    private byte[] constructToken(D2DHandshakeContext.Role role, byte[] bArr) throws GeneralSecurityException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = role.name().getBytes(StandardCharsets.UTF_8);
        return messageDigest.digest(ByteBuffer.allocate(bytes.length + bArr.length).put(bytes).put(bArr).array());
    }

    private int compareByteArray(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return 0;
        }
        if (bArr.length != bArr2.length) {
            return bArr.length - bArr2.length;
        }
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i];
            byte b2 = bArr2[i];
            if (b != b2) {
                return b - b2;
            }
        }
        return 0;
    }

    private String generateAlias() {
        String str;
        do {
            str = "secure-channel-" + UUID.randomUUID();
        } while (KeyStoreUtils.aliasExists(str));
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum MessageType {
        HANDSHAKE_INIT(18505),
        HANDSHAKE_FINISH(18502),
        PRE_SHARED_KEY(20555),
        ATTESTATION(16724),
        AVF_RESULT(22098),
        SECURE_MESSAGE(21325),
        UNKNOWN(0);

        private final short mValue;

        MessageType(int i) {
            this.mValue = (short) i;
        }

        static MessageType from(short s) {
            for (MessageType messageType : values()) {
                if (s == messageType.mValue) {
                    return messageType;
                }
            }
            return UNKNOWN;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean shouldEncrypt(MessageType messageType) {
            return (messageType == HANDSHAKE_INIT || messageType == HANDSHAKE_FINISH) ? false : true;
        }
    }
}
