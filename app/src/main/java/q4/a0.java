package q4;

import android.graphics.PointF;

/* compiled from: PathParser.java */
/* loaded from: classes.dex */
public class a0 implements ValueParser<PointF> {

    /* renamed from: a, reason: collision with root package name */
    public static final a0 f16834a = new a0();

    private a0() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public PointF a(r4.c cVar, float f10) {
        return JsonUtils.e(cVar, f10);
    }
}
