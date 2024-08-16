package android.net.util;

import android.system.OsConstants;
import com.android.server.network.StructInetDiagSockId;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class StructInetDiagReqV2Oppo {
    public static final int INET_DIAG_REQ_V2_ALL_STATES = -1;
    public static final int STRUCT_SIZE = 56;
    private final StructInetDiagSockId mId;
    private final byte mIdiagExt;
    private final byte mPad;
    private final byte mSdiagFamily;
    private final byte mSdiagProtocol;
    private final int mState;

    public StructInetDiagReqV2Oppo(int i, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, int i2) {
        this(i, inetSocketAddress, inetSocketAddress2, i2, 0, 0, -1);
    }

    public StructInetDiagReqV2Oppo(int i, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, int i2, int i3, int i4, int i5) throws NullPointerException {
        this.mSdiagFamily = (byte) i2;
        this.mSdiagProtocol = (byte) i;
        if ((inetSocketAddress == null) != (inetSocketAddress2 == null)) {
            throw new NullPointerException("Local and remote must be both null or both non-null");
        }
        this.mId = (inetSocketAddress == null || inetSocketAddress2 == null) ? null : new StructInetDiagSockId(inetSocketAddress, inetSocketAddress2);
        this.mPad = (byte) i3;
        this.mIdiagExt = (byte) i4;
        this.mState = i5;
    }

    public void pack(ByteBuffer byteBuffer) {
        byteBuffer.put(this.mSdiagFamily);
        byteBuffer.put(this.mSdiagProtocol);
        byteBuffer.put(this.mIdiagExt);
        byteBuffer.put(this.mPad);
        byteBuffer.putInt(this.mState);
        StructInetDiagSockId structInetDiagSockId = this.mId;
        if (structInetDiagSockId != null) {
            structInetDiagSockId.pack(byteBuffer);
        }
    }

    public static String stringForAddressFamily(int i) {
        return i == OsConstants.AF_INET ? "AF_INET" : i == OsConstants.AF_INET6 ? "AF_INET6" : i == OsConstants.AF_NETLINK ? "AF_NETLINK" : i == OsConstants.AF_UNSPEC ? "AF_UNSPEC" : String.valueOf(i);
    }

    public String toString() {
        String stringForAddressFamily = stringForAddressFamily(this.mSdiagFamily);
        String stringForAddressFamily2 = stringForAddressFamily(this.mSdiagProtocol);
        StringBuilder sb = new StringBuilder();
        sb.append("StructInetDiagReqV2Oppo{ sdiag_family{");
        sb.append(stringForAddressFamily);
        sb.append("}, sdiag_protocol{");
        sb.append(stringForAddressFamily2);
        sb.append("}, idiag_ext{");
        sb.append((int) this.mIdiagExt);
        sb.append(")}, pad{");
        sb.append((int) this.mPad);
        sb.append("}, idiag_states{");
        sb.append(Integer.toHexString(this.mState));
        sb.append("}, ");
        StructInetDiagSockId structInetDiagSockId = this.mId;
        sb.append(structInetDiagSockId != null ? structInetDiagSockId.toString() : "inet_diag_sockid=null");
        sb.append("}");
        return sb.toString();
    }
}
