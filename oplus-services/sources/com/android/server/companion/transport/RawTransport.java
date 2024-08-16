package com.android.server.companion.transport;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Slog;
import java.io.IOException;
import java.nio.ByteBuffer;
import libcore.io.IoUtils;
import libcore.io.Streams;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RawTransport extends Transport {
    private volatile boolean mStopped;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RawTransport(int i, ParcelFileDescriptor parcelFileDescriptor, Context context) {
        super(i, parcelFileDescriptor, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.companion.transport.Transport
    public void start() {
        if (Transport.DEBUG) {
            Slog.d("CDM_CompanionTransport", "Starting raw transport.");
        }
        new Thread(new Runnable() { // from class: com.android.server.companion.transport.RawTransport$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RawTransport.this.lambda$start$0();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        while (!this.mStopped) {
            try {
                receiveMessage();
            } catch (IOException e) {
                if (this.mStopped) {
                    return;
                }
                Slog.w("CDM_CompanionTransport", "Trouble during transport", e);
                close();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.companion.transport.Transport
    public void stop() {
        if (Transport.DEBUG) {
            Slog.d("CDM_CompanionTransport", "Stopping raw transport.");
        }
        this.mStopped = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.companion.transport.Transport
    public void close() {
        stop();
        if (Transport.DEBUG) {
            Slog.d("CDM_CompanionTransport", "Closing raw transport.");
        }
        IoUtils.closeQuietly(this.mRemoteIn);
        IoUtils.closeQuietly(this.mRemoteOut);
        super.close();
    }

    @Override // com.android.server.companion.transport.Transport
    protected void sendMessage(int i, int i2, byte[] bArr) throws IOException {
        if (Transport.DEBUG) {
            Slog.e("CDM_CompanionTransport", "Sending message 0x" + Integer.toHexString(i) + " sequence " + i2 + " length " + bArr.length + " to association " + this.mAssociationId);
        }
        synchronized (this.mRemoteOut) {
            this.mRemoteOut.write(ByteBuffer.allocate(12).putInt(i).putInt(i2).putInt(bArr.length).array());
            this.mRemoteOut.write(bArr);
            this.mRemoteOut.flush();
        }
    }

    private void receiveMessage() throws IOException {
        synchronized (this.mRemoteIn) {
            byte[] bArr = new byte[12];
            Streams.readFully(this.mRemoteIn, bArr);
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            int i = wrap.getInt();
            int i2 = wrap.getInt();
            byte[] bArr2 = new byte[wrap.getInt()];
            Streams.readFully(this.mRemoteIn, bArr2);
            handleMessage(i, i2, bArr2);
        }
    }
}
