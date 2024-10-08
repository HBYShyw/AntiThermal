package com.android.server.network;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class StructInetDiagSockId {
    private static final byte[] INET_DIAG_NOCOOKIE = {-1, -1, -1, -1, -1, -1, -1, -1};
    private static final byte[] IPV4_PADDING = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int STRUCT_SIZE = 48;
    private final InetSocketAddress mLocSocketAddress;
    private final InetSocketAddress mRemSocketAddress;

    public StructInetDiagSockId(InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2) {
        this.mLocSocketAddress = inetSocketAddress;
        this.mRemSocketAddress = inetSocketAddress2;
    }

    public void pack(ByteBuffer byteBuffer) {
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putShort((short) this.mLocSocketAddress.getPort());
        byteBuffer.putShort((short) this.mRemSocketAddress.getPort());
        byteBuffer.put(this.mLocSocketAddress.getAddress().getAddress());
        if (this.mLocSocketAddress.getAddress() instanceof Inet4Address) {
            byteBuffer.put(IPV4_PADDING);
        }
        byteBuffer.put(this.mRemSocketAddress.getAddress().getAddress());
        if (this.mRemSocketAddress.getAddress() instanceof Inet4Address) {
            byteBuffer.put(IPV4_PADDING);
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.putInt(0);
        byteBuffer.put(INET_DIAG_NOCOOKIE);
    }

    public String toString() {
        return "StructInetDiagSockId{ idiag_sport{" + this.mLocSocketAddress.getPort() + "}, idiag_dport{" + this.mRemSocketAddress.getPort() + "}, idiag_src{" + this.mLocSocketAddress.getAddress().getHostAddress() + "}, idiag_dst{" + this.mRemSocketAddress.getAddress().getHostAddress() + "}, idiag_if{0} idiag_cookie{INET_DIAG_NOCOOKIE}}";
    }
}
