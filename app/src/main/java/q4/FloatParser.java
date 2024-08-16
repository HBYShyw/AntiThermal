package q4;

/* compiled from: FloatParser.java */
/* renamed from: q4.m, reason: use source file name */
/* loaded from: classes.dex */
public class FloatParser implements ValueParser<Float> {

    /* renamed from: a, reason: collision with root package name */
    public static final FloatParser f16875a = new FloatParser();

    private FloatParser() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public Float a(r4.c cVar, float f10) {
        return Float.valueOf(JsonUtils.g(cVar) * f10);
    }
}
