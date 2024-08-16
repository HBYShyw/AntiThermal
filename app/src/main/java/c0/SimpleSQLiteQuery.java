package c0;

/* compiled from: SimpleSQLiteQuery.java */
/* renamed from: c0.a, reason: use source file name */
/* loaded from: classes.dex */
public final class SimpleSQLiteQuery implements SupportSQLiteQuery {

    /* renamed from: e, reason: collision with root package name */
    private final String f4722e;

    /* renamed from: f, reason: collision with root package name */
    private final Object[] f4723f;

    public SimpleSQLiteQuery(String str, Object[] objArr) {
        this.f4722e = str;
        this.f4723f = objArr;
    }

    private static void a(SupportSQLiteProgram supportSQLiteProgram, int i10, Object obj) {
        if (obj == null) {
            supportSQLiteProgram.Y(i10);
            return;
        }
        if (obj instanceof byte[]) {
            supportSQLiteProgram.C(i10, (byte[]) obj);
            return;
        }
        if (obj instanceof Float) {
            supportSQLiteProgram.r(i10, ((Float) obj).floatValue());
            return;
        }
        if (obj instanceof Double) {
            supportSQLiteProgram.r(i10, ((Double) obj).doubleValue());
            return;
        }
        if (obj instanceof Long) {
            supportSQLiteProgram.y(i10, ((Long) obj).longValue());
            return;
        }
        if (obj instanceof Integer) {
            supportSQLiteProgram.y(i10, ((Integer) obj).intValue());
            return;
        }
        if (obj instanceof Short) {
            supportSQLiteProgram.y(i10, ((Short) obj).shortValue());
            return;
        }
        if (obj instanceof Byte) {
            supportSQLiteProgram.y(i10, ((Byte) obj).byteValue());
            return;
        }
        if (obj instanceof String) {
            supportSQLiteProgram.j(i10, (String) obj);
            return;
        }
        if (obj instanceof Boolean) {
            supportSQLiteProgram.y(i10, ((Boolean) obj).booleanValue() ? 1L : 0L);
            return;
        }
        throw new IllegalArgumentException("Cannot bind " + obj + " at index " + i10 + " Supported types: null, byte[], float, double, long, int, short, byte, string");
    }

    public static void d(SupportSQLiteProgram supportSQLiteProgram, Object[] objArr) {
        if (objArr == null) {
            return;
        }
        int length = objArr.length;
        int i10 = 0;
        while (i10 < length) {
            Object obj = objArr[i10];
            i10++;
            a(supportSQLiteProgram, i10, obj);
        }
    }

    @Override // c0.SupportSQLiteQuery
    public String b() {
        return this.f4722e;
    }

    @Override // c0.SupportSQLiteQuery
    public void c(SupportSQLiteProgram supportSQLiteProgram) {
        d(supportSQLiteProgram, this.f4723f);
    }

    public SimpleSQLiteQuery(String str) {
        this(str, null);
    }
}
