package o5;

import com.oplus.cardwidget.proto.UIDataProto;
import kotlin.Metadata;
import ma.o;
import s5.StringCompressor;
import za.k;

/* compiled from: DataPackCompressor.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u000b\u0010\fJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u001c\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\r"}, d2 = {"Lo5/a;", "Lo5/b;", "", "size", "Lma/f0;", "b", "", "source", "Lma/o;", "", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: o5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class DataPackCompressor implements IDataCompress {

    /* renamed from: a, reason: collision with root package name */
    private final String f16257a = "DataPackCompressor";

    /* renamed from: b, reason: collision with root package name */
    private final int f16258b = 20000;

    /* renamed from: c, reason: collision with root package name */
    private final int f16259c = 2000;

    private final void b(long j10) {
        if (j10 > this.f16258b) {
            s5.b.f18066c.e(this.f16257a, "not allow to post data of size over " + this.f16258b + " Bytes");
        }
    }

    @Override // o5.IDataCompress
    public o<String, Integer> a(String source) {
        k.e(source, "source");
        int length = source.length();
        if (length >= this.f16259c) {
            b(length);
            return new o<>(StringCompressor.f18069a.a(source), Integer.valueOf(UIDataProto.DataCompress.FLATER.getNumber()));
        }
        s5.b.f18066c.c(this.f16257a, "no need to compress origin source size is " + source.length());
        return new o<>(source, Integer.valueOf(UIDataProto.DataCompress.NONE.getNumber()));
    }
}
