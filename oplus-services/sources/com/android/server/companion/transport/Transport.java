package com.android.server.companion.transport;

import android.companion.IOnMessageReceivedListener;
import android.content.Context;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.util.EmptyArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class Transport {
    protected static final boolean DEBUG = Build.IS_DEBUGGABLE;
    protected static final int HEADER_LENGTH = 12;
    public static final int MESSAGE_REQUEST_CONTEXT_SYNC = 1667729539;
    public static final int MESSAGE_REQUEST_PERMISSION_RESTORE = 1669491075;
    static final int MESSAGE_REQUEST_PING = 1669362552;
    static final int MESSAGE_RESPONSE_FAILURE = 863004019;
    static final int MESSAGE_RESPONSE_SUCCESS = 864257383;
    protected static final String TAG = "CDM_CompanionTransport";
    protected final int mAssociationId;
    protected final Context mContext;
    protected final ParcelFileDescriptor mFd;
    private OnTransportClosedListener mOnTransportClosed;
    protected final InputStream mRemoteIn;
    protected final OutputStream mRemoteOut;

    @GuardedBy({"mPendingRequests"})
    protected final SparseArray<CompletableFuture<byte[]>> mPendingRequests = new SparseArray<>();
    protected final AtomicInteger mNextSequence = new AtomicInteger();
    private final Map<Integer, IOnMessageReceivedListener> mListeners = new HashMap();

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface OnTransportClosedListener {
        void onClosed(Transport transport);
    }

    private static boolean isRequest(int i) {
        return (i & AudioFormat.MAIN_MASK) == 1660944384;
    }

    private static boolean isResponse(int i) {
        return (i & AudioFormat.MAIN_MASK) == 855638016;
    }

    protected abstract void sendMessage(int i, int i2, byte[] bArr) throws IOException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void start();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void stop();

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transport(int i, ParcelFileDescriptor parcelFileDescriptor, Context context) {
        this.mAssociationId = i;
        this.mFd = parcelFileDescriptor;
        this.mRemoteIn = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);
        this.mRemoteOut = new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor);
        this.mContext = context;
    }

    public void addListener(int i, IOnMessageReceivedListener iOnMessageReceivedListener) {
        this.mListeners.put(Integer.valueOf(i), iOnMessageReceivedListener);
    }

    public int getAssociationId() {
        return this.mAssociationId;
    }

    protected ParcelFileDescriptor getFd() {
        return this.mFd;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        OnTransportClosedListener onTransportClosedListener = this.mOnTransportClosed;
        if (onTransportClosedListener != null) {
            onTransportClosedListener.onClosed(this);
        }
    }

    public Future<byte[]> requestForResponse(int i, byte[] bArr) {
        if (DEBUG) {
            Slog.d(TAG, "Requesting for response");
        }
        int incrementAndGet = this.mNextSequence.incrementAndGet();
        CompletableFuture<byte[]> completableFuture = new CompletableFuture<>();
        synchronized (this.mPendingRequests) {
            this.mPendingRequests.put(incrementAndGet, completableFuture);
        }
        try {
            sendMessage(i, incrementAndGet, bArr);
        } catch (IOException e) {
            synchronized (this.mPendingRequests) {
                this.mPendingRequests.remove(incrementAndGet);
                completableFuture.completeExceptionally(e);
            }
        }
        return completableFuture;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void handleMessage(int i, int i2, byte[] bArr) throws IOException {
        if (DEBUG) {
            Slog.d(TAG, "Received message 0x" + Integer.toHexString(i) + " sequence " + i2 + " length " + bArr.length + " from association " + this.mAssociationId);
        }
        if (isRequest(i)) {
            try {
                processRequest(i, i2, bArr);
                return;
            } catch (IOException e) {
                Slog.w(TAG, "Failed to respond to 0x" + Integer.toHexString(i), e);
                return;
            }
        }
        if (isResponse(i)) {
            processResponse(i, i2, bArr);
            return;
        }
        Slog.w(TAG, "Unknown message 0x" + Integer.toHexString(i));
    }

    private void processRequest(int i, int i2, byte[] bArr) throws IOException {
        if (i == 1667729539) {
            callback(i, bArr);
            sendMessage(MESSAGE_RESPONSE_SUCCESS, i2, EmptyArray.BYTE);
            return;
        }
        if (i == MESSAGE_REQUEST_PING) {
            sendMessage(MESSAGE_RESPONSE_SUCCESS, i2, bArr);
            return;
        }
        if (i == 1669491075) {
            try {
                callback(i, bArr);
                sendMessage(MESSAGE_RESPONSE_SUCCESS, i2, EmptyArray.BYTE);
                return;
            } catch (Exception unused) {
                Slog.w(TAG, "Failed to restore permissions");
                sendMessage(MESSAGE_RESPONSE_FAILURE, i2, EmptyArray.BYTE);
                return;
            }
        }
        Slog.w(TAG, "Unknown request 0x" + Integer.toHexString(i));
        sendMessage(MESSAGE_RESPONSE_FAILURE, i2, EmptyArray.BYTE);
    }

    private void callback(int i, byte[] bArr) {
        if (this.mListeners.containsKey(Integer.valueOf(i))) {
            try {
                this.mListeners.get(Integer.valueOf(i)).onMessageReceived(getAssociationId(), bArr);
                Slog.i(TAG, "Message 0x" + Integer.toHexString(i) + " is received from associationId " + this.mAssociationId + ", sending data length " + bArr.length + " to the listener.");
            } catch (RemoteException unused) {
            }
        }
    }

    private void processResponse(int i, int i2, byte[] bArr) {
        CompletableFuture completableFuture;
        synchronized (this.mPendingRequests) {
            completableFuture = (CompletableFuture) this.mPendingRequests.removeReturnOld(i2);
        }
        if (completableFuture == null) {
            Slog.w(TAG, "Ignoring unknown sequence " + i2);
            return;
        }
        if (i == MESSAGE_RESPONSE_FAILURE) {
            completableFuture.completeExceptionally(new RuntimeException("Remote failure"));
            return;
        }
        if (i == MESSAGE_RESPONSE_SUCCESS) {
            completableFuture.complete(bArr);
            return;
        }
        Slog.w(TAG, "Ignoring unknown response 0x" + Integer.toHexString(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnTransportClosedListener(OnTransportClosedListener onTransportClosedListener) {
        this.mOnTransportClosed = onTransportClosedListener;
    }
}
