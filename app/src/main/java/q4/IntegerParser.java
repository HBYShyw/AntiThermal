package q4;

/* compiled from: IntegerParser.java */
/* renamed from: q4.s, reason: use source file name */
/* loaded from: classes.dex */
public class IntegerParser implements ValueParser<Integer> {

    /* renamed from: a, reason: collision with root package name */
    public static final IntegerParser f16885a = new IntegerParser();

    private IntegerParser() {
    }

    @Override // q4.ValueParser
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public Integer a(r4.c cVar, float f10) {
        return Integer.valueOf(Math.round(JsonUtils.g(cVar) * f10));
    }
}
