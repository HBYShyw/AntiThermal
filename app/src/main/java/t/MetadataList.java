package t;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* compiled from: MetadataList.java */
/* renamed from: t.b, reason: use source file name */
/* loaded from: classes.dex */
public final class MetadataList extends Table {
    public static MetadataList h(ByteBuffer byteBuffer) {
        return i(byteBuffer, new MetadataList());
    }

    public static MetadataList i(ByteBuffer byteBuffer, MetadataList metadataList) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return metadataList.f(byteBuffer.getInt(byteBuffer.position()) + byteBuffer.position(), byteBuffer);
    }

    public MetadataList f(int i10, ByteBuffer byteBuffer) {
        g(i10, byteBuffer);
        return this;
    }

    public void g(int i10, ByteBuffer byteBuffer) {
        c(i10, byteBuffer);
    }

    public MetadataItem j(MetadataItem metadataItem, int i10) {
        int b10 = b(6);
        if (b10 != 0) {
            return metadataItem.f(a(d(b10) + (i10 * 4)), this.f18510b);
        }
        return null;
    }

    public int k() {
        int b10 = b(6);
        if (b10 != 0) {
            return e(b10);
        }
        return 0;
    }

    public int l() {
        int b10 = b(4);
        if (b10 != 0) {
            return this.f18510b.getInt(b10 + this.f18509a);
        }
        return 0;
    }
}
